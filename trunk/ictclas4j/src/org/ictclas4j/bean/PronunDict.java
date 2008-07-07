package org.ictclas4j.bean;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.ictclas4j.util.Utility;

import com.gftech.util.GFFile;
import com.gftech.util.GFString;

public class PronunDict {
	private Pronun[] pronunDict;

	static Logger logger = Logger.getLogger(PronunDict.class);

	public PronunDict(String fileName) {
		pronunDict = new Pronun[Utility.GBK_NUM];
		load(fileName);
	}

	/**
	 * 加载拼音库
	 * 
	 * @param fileName
	 */
	private void load(String fileName) {

		if (fileName != null) {
			try {
				ArrayList<String> pinyin = GFFile.readTxtFile2(fileName);

				String one = null;
				String[] ones = null;
				long start = System.currentTimeMillis();
				for (int i = 0; i < pinyin.size(); i++) {
					one = pinyin.get(i);
					if (one != null) {
						ones = one.split("#");
//						System.out.println(one);
						if (ones != null && ones.length == 3) {
							Pronun pronun = new Pronun();
							int index = Utility.GBK_ID(ones[0]);
							pronun.setPronun(ones[1]);
							pronun.setWord(ones[0]);
							if (!"null".equals(ones[2])) {
								String[] atoms = GFString.atomSplit(ones[2]);
								for (String atom : atoms) {
									pronun.addHomophony(Utility.GBK_ID(atom));
								}
							}
							pronunDict[index] = pronun;
						}
					}
				}

				logger.info("Load pronunciation dict is ok " + (System.currentTimeMillis() - start));
				System.out.println("Load pronunciation dict is ok " + (System.currentTimeMillis() - start));
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	// 得到一个词的读音
	public String getPronun(String word) {
		String result = null;
		if (word != null && pronunDict != null) {
			String[] atoms = GFString.atomSplit(word);
			for (int i = 0; i < atoms.length; i++) {
				if (GFString.isAllChinese(atoms[i])) {
					int gbkID = Utility.GBK_ID(atoms[i]);
					if (gbkID >= 0)
						atoms[i] = pronunDict[gbkID].getPronun();
				}
			}
			result = GFString.array2string(atoms, "~");
		}
		return result;
	}

	/**
	 * 给到同音词的列表
	 * 
	 * @return 同音词的CCID索引值
	 */
	public ArrayList<Integer> getHomophonyList(String word) {
		ArrayList<Integer> result = null;

		if (word != null && pronunDict != null) {
			int gbkID = Utility.GBK_ID(word);
			if (gbkID >= 0) {
				result = pronunDict[gbkID].getHomophonyList();
			}
		}
		return result;
	}

}
