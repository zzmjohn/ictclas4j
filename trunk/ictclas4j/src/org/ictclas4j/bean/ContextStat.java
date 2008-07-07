package org.ictclas4j.bean;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.ictclas4j.util.Utility;

import com.gftech.util.GFNet;

public class ContextStat {
	private int posCount;

	private int[] posTable;

	private ArrayList<TagContext> tcList;

	static Logger logger = Logger.getLogger(ContextStat.class);

	public ContextStat() {
		this(null);
	}

	public ContextStat(String fileName) {
		tcList = new ArrayList<TagContext>();
		load(fileName);
	}

	public boolean load(String fileName) {
		return load(fileName, false);
	}

	public boolean load(String fileName, boolean isReset) {
		if (fileName != null) {
			File file = new File(fileName);
			if (!file.canRead())
				return false;// fail while opening the file

			try {
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
				// 读取长度
				posCount = GFNet.readInt32(in);
				logger.debug("tableLen:" + posCount);

				// 读取符号标志
				posTable = new int[posCount];
				for (int i = 0; i < posCount; i++) {
					posTable[i] = GFNet.readInt32(in);
					logger.debug("symbol["+i+"]:"+  posTable[i]+","+Utility.int2pos(posTable[i]));
				}

				long fileLen = file.length();
				long curLen = 4 + posCount * 4;
				while (curLen < fileLen) { 
					TagContext tc = new TagContext();

					// 读取关键词
					int key = GFNet.readInt32(in);
					curLen += 4;
					logger.debug("  key:" + key);

					// 读取总词频
					curLen += 4;
					int totalFreq = GFNet.readInt32(in);
					logger.debug("  totalFreq:" + totalFreq);

					// 读取词频
					int[] tagFreq = new int[posCount];
					for (int i = 0; i < posCount; i++) {
						curLen += 4;
						tagFreq[i] = GFNet.readInt32(in);
						logger.debug("    freq:" + tagFreq[i]);
					}

					// 读取上下文数组
					int[][] contextArray = new int[posCount][posCount];
					for (int i = 0; i < posCount; i++) {
						StringBuffer pr = new StringBuffer(); 
						for (int j = 0; j < posCount; j++) {
							curLen += 4;
							contextArray[i][j] = GFNet.readInt32(in);
							pr.append(contextArray[i][j]).append(" ");
						}
						logger.debug("    " + pr);
					}

					tc.setTotalFreq(totalFreq);
					tc.setKey(key);
					tc.setTagFreq(tagFreq);
					tc.setContextArray(contextArray);
					tcList.add(tc);
				}
				in.close();
			} catch (FileNotFoundException e) {
				logger.debug(e);
			} catch (IOException e) {
				logger.debug(e);
			}
		}
		return true;
	}

	public int getFreq(int key, int symbol) {
		TagContext tc = getItem(key);
		if (tc == null)
			return 0;

		int index = Utility.binarySearch(symbol, posTable);
		if (index == -1)// error finding the symbol
			return 0;

		// Add the frequency
		int frequency = 0;
		if (tc.getTagFreq() != null)
			frequency = tc.getTagFreq()[index];
		return frequency;

	}

	public double getPossibility(int key, int prev, int cur) {
		double result = 0;

		int curIndex = Utility.binarySearch(cur, posTable);
		int prevIndex = Utility.binarySearch(prev, posTable);

		TagContext tc = getItem(key);

		// return a lower value, not 0 to prevent data sparse
		if (tc == null || curIndex == -1 || prevIndex == -1 || tc.getContextArray()[prevIndex][curIndex] == 0 || tc.getTagFreq()[prevIndex] == 0)
			return 0.000001;

		int prevCurConFreq = tc.getContextArray()[prevIndex][curIndex];
		int prevFreq = tc.getTagFreq()[prevIndex];

		// 0.9 and 0.1 is a value based experience
		result = 0.9 * (double) prevCurConFreq;
		result /= (double) prevFreq;
		result += 0.1 * (double) prevFreq / (double) tc.getTotalFreq();

		return result;
	}

	public TagContext getItem(int key) {
		TagContext result = null;

		if (tcList == null || tcList.size() == 0)
			return null;
		if (key == 0)
			result = tcList.get(0);
		else {
			int i = 0;
			for (; i < tcList.size() && tcList.get(i).getKey() < key; i++)
				;
			if (i < tcList.size() && tcList.get(i).getKey() == key)
				result = tcList.get(i);
			else if (i - 1 < tcList.size())
				result = tcList.get(i - 1);
		}

		return result;
	}

}
