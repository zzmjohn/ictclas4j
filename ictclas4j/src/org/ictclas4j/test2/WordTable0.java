package org.ictclas4j.test2;

import java.util.ArrayList;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class WordTable0 {
	// 该词组表中关键词的数目
	private int count;

	private ArrayList<WordItem0> words;

	public WordTable0() {

	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void addWordItem(WordItem0 wi) {
		if (wi != null) {
			if (words == null)
				words = new ArrayList<WordItem0>();
			words.add(wi);
			count++;
		}
	}
	
	public void addWordItem(int index,WordItem0 wi) {
		if (wi != null) {
			if (words == null)
				words = new ArrayList<WordItem0>();
			words.add(index,wi);
			count++;
		}
	}
	
	public WordItem0 getWordItem(int index){
		if(words!=null){
			if(index>=0 && index<words.size())
				return words.get(index);
		}
		
		return null;
	}

	public ArrayList<WordItem0> getWords() {
		return words;
	}

	public void setWords(ArrayList<WordItem0> words) {
		this.words = words;
	}

	public void setWords(WordItem0[] wis) {
		if (wis != null) {
			if (words == null)
				words = new ArrayList<WordItem0>();
			for (WordItem0 wi : wis)
				words.add(wi);

		}
	}

	public String toString() {

		return ReflectionToStringBuilder.toString(this);

	}
}
