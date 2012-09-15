package org.ictclas4j.bean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.gftech.util.GFNet;

/**
 * 汉字数字指纹词典
 * 
 * @author sinboy
 * @since 2008.7.5
 * 
 */
public class FingerprintDict {
	private String fileName;

	private int wordCount;

	private HashMap<String, WordFingerprint> fpMap;

	static Logger logger = Logger.getLogger(FingerprintDict.class);

	public FingerprintDict(String fileName) {
		this.fileName = fileName;
	}

	public void load() {
		if (fileName != null) {
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(fileName));
				// 词典库在写二进制数据时采用低位优先(小头在前)方式,需要转换一下
				wordCount = GFNet.readInt32(in);
				for (int i = 0; i < wordCount; i++) {
					WordFingerprint wfp = new WordFingerprint();
					wfp.read(in, 0);
					String word = wfp.getWord();
					if (word != null) {
						fpMap.put(word, wfp);
					}
				}

				in.close();
			} catch (FileNotFoundException e) {
				logger.fatal("load fingerprint dict " + fileName + ":", e);
			} catch (IOException e) {
				logger.fatal("load fingerprint dict " + fileName + ":", e);
			}

		}
	}

	public void save(ArrayList<WordFingerprint> wlist) {
		if (fileName != null && wlist != null) {
			try {
				DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
				// 词典库在写二进制数据时采用低位优先(小头在前)方式,需要转换一下
				int wordCount = wlist.size();
				GFNet.writeInt32(out, wordCount);
				for (int i = 0; i < wordCount; i++) {
					WordFingerprint wfp = wlist.get(i);
					wfp.write(out);
				}

				out.close();
			} catch (FileNotFoundException e) {
				logger.fatal("save fingerprint dict " + fileName + ":", e);
			} catch (IOException e) {
				logger.fatal("save fingerprint dict " + fileName + ":", e);
			}

		}
	}

	public WordFingerprint getWordFingerprint(String word) {
		if (word != null && fpMap != null) {
			return fpMap.get(word);
		}

		return null;
	}

}
