package org.ictclas4j.bean;

import java.util.ArrayList;

/**
 * GBK编码的汉字对应的拼音，并记录读音相同的汉字的索引列表
 * 
 * @author sinboy
 * 
 */
public class Pronun {
	private String word;//汉字

	private String pronun;//拼音

	private ArrayList<Integer> homophonyList;//相同读音的列表

	public String getGbkID() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public ArrayList<Integer> getHomophonyList() {
		return homophonyList;
	}

	public void setHomophonyList(ArrayList<Integer> homophonyList) {
		this.homophonyList = homophonyList;
	}

	public String getPronun() {
		return pronun;
	}

	public void setPronun(String pronun) {
		this.pronun = pronun;
	}
	
	public void addHomophony(int index){
		if(homophonyList==null)
			homophonyList=new ArrayList<Integer>();
		if(!homophonyList.contains(index))
			homophonyList.add(index);
	}

}
