package org.ictclas4j.bean;

public class ThesaurusItem {
	private String word;

	private double value;

	public ThesaurusItem(String word, double value) {
		this.word = word;
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}