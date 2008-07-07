package org.ictclas4j.bean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 汉字数字指纹
 * 
 * @author sinboy
 * @since 2008.7.5
 * 
 */
public class WordFingerprint {
	private String word;

	// 汉字拼音
	private String[] pinyin;

	// 汉字结构数字指纹，按笔画生成，如：快->4425134
	private String fingerprint;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String[] getPinyin() {
		return pinyin;
	}

	public void setPinyin(String[] pinyin) {
		this.pinyin = pinyin;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(word).append(",");
		sb.append(fingerprint).append(",");
		for (int i = 0; i < pinyin.length; i++) {
			sb.append(pinyin[i]);
			if (i != pinyin.length - 1)
				sb.append("/");
		}
		return sb.toString();
	}

	public int read(DataInputStream in, long offset) throws IOException {
		int size = 0;

		if (in != null) {

			in.skip(offset);

			// read word
			int wordLen = in.readByte();
			byte[] bword = new byte[wordLen];
			in.read(bword);
			size += 1 + wordLen;
			word = new String(bword);

			// read fingerprint
			int fpLen = in.readByte();
			byte[] bfp = new byte[fpLen];
			in.read(bfp);
			size += 1 + fpLen;
			fingerprint = new String(bfp);

			// read pinyin
			int pinyinCount = in.readByte();
			size++;
			pinyin = new String[pinyinCount];
			for (int i = 0; i < pinyinCount; i++) {
				int pinyinLen = in.readByte();
				byte[] bpinyin = new byte[pinyinLen];
				in.read(bpinyin);
				pinyin[i] = new String(bpinyin);
				size += 1 + pinyinLen;
			}

		}
		return size;
	}

	public int write(DataOutputStream out) throws IOException {
		int size = 0;

		if (out != null) {
			// write word
			int wordLen = word != null ? word.getBytes().length : 0;
			out.writeByte((byte) wordLen);
			size++;
			if (wordLen > 0) {
				byte[] b = word.getBytes();
				out.write(b);
				size += wordLen;
			}

			// write fingerprint
			int fpLen = fingerprint != null ? fingerprint.getBytes().length : 0;
			out.writeByte((byte) fpLen);
			size++;
			if (fpLen > 0) {
				byte[] b = fingerprint.getBytes();
				out.write(b);
				size += fpLen;
			}

			// write pinyin
			int pinyinCount = pinyin != null ? pinyin.length : 0;
			out.writeByte((byte) pinyinCount);
			size++;
			if (pinyinCount > 0) {
				for (int i = 0; i < pinyinCount; i++) {
					int pinyinLen = pinyin[i] != null ? pinyin[i].getBytes().length : 0;
					out.writeByte(pinyinLen);
					size++;
					if (pinyinLen > 0) {
						byte[] b = pinyin[i].getBytes();
						out.write(b);
						size += pinyinLen;
					}
				}
			}

		}
		return size;

	}

}
