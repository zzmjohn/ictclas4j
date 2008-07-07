package org.ictclas4j.bean;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.ictclas4j.util.Utility;

import com.gftech.util.GFString;

/**
 * 预处理后的词条.
 * 
 * @author sinboy
 * 
 */
public class Preword {
	// 词
	private String word;

	// 去掉第一个字后剩余的部分
	private String res;

	// 第一个字
	private String first;

	// 词在词典表中出现的位置，即词的首字在区位码表中对应的偏移位置.比如：啊－－0
	private int index;

	public Preword() {

	}

	public Preword(String word) {

		if (word != null && word.length() > 0) {
			word = word.toLowerCase();
			int type = Utility.charType(word);
			word = GFString.removeSpace(word);
			int len = word.length();
			int end = len - 1, begin = 0;

			if (begin <= end) {
				this.word = word;

				if (type == Utility.CT_CHINESE || type == Utility.CT_SINGLE) {// Chinese
					// word
					index = Utility.GBK_ID(word);
					if (word != null) {
						first = word.length() > 1 ? word.substring(0, 1) : word;
						res = word.length() > 1 ? word.substring(1) : "";
					}
				} else if (type == Utility.CT_DELIMITER) {// Delimiter
					index = 3755;
					res = word;
					first = word; 
				} else
					index = -1;
			}
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int pos) {
		this.index = pos;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String toString() {

		return ReflectionToStringBuilder.toString(this);

	}

}
