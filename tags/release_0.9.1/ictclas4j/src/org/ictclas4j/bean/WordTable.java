package org.ictclas4j.bean;

import java.util.HashMap;


/**
 * 关键词相同开头的词组表.
 * 
 * @author sinboy
 * @since 2006.7
 * @update 2007.12.22
 * 
 */
public class WordTable implements Cloneable {
	// 该词组表中关键词的数目
	private int wordCount;

	// 该词表中关键词的最大长度
	private int wordMaxLen;

	private HashMap<String, SegAtom> wordMap;

	public WordTable() {
		wordMap = new HashMap<String, SegAtom>();
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int count) {
		this.wordCount = count;
	}

	public int getWordMaxLen() {
		return wordMaxLen;
	}

	public void setWordMaxLen(int wordMaxLen) {
		this.wordMaxLen = wordMaxLen;
	}

	public void addSegAtom(SegAtom atom) {
		if (atom != null) {
			String word = atom.getWord();
			if (word != null) {
				SegAtom sa=wordMap.get(word);
				atom.merge(sa);
				wordCount++;
				wordMap.put(word, atom);
				if (word.length() > wordMaxLen)
					wordMaxLen = word.length();
			}
		}
	}

	public SegAtom getSegAtom(String word) {
		if (wordMap != null) {
			return wordMap.get(word);
		}

		return null;
	}

	public HashMap<String, SegAtom> getWordMap() {
		return wordMap;
	}

	public void setWordMap(HashMap<String, SegAtom> wordMap) {
		this.wordMap = wordMap;
	}

	public WordTable clone() throws CloneNotSupportedException {
		return (WordTable) super.clone();
	}
 
}
