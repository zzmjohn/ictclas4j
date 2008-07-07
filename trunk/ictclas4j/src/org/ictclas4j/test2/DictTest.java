package org.ictclas4j.test2;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.PropertyConfigurator;
import org.ictclas4j.bean.Dictionary;
import org.ictclas4j.bean.Pos;
import org.ictclas4j.bean.SegAtom;
import org.ictclas4j.bean.WordTable;
import org.ictclas4j.run.Config;


public class DictTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		PropertyConfigurator.configure(Config.LOG4J_CONF); 
		//testRead();
		convertFormat();
	}
	
	public static  void testWrite(){
		Dictionary dict =new Dictionary();
	}
	
	public static void testRead(){
		Dictionary0 dict=new Dictionary0(true);
		dict.load("data\\nr.dct");
	}

	//把词典库从旧格式转成新格式，增加了wordMaxLen
	public static void convertFormat(){
		Dictionary0 dict=new Dictionary0(false);
		dict.load("data\\tr.dct");
		Dictionary dict2=new Dictionary(false);
		WordTable[] wts=dict2.getWts();
		
		long size=0;
		for(int i=0;i<dict.dict_count;i++){
			WordTable0 wt0=dict.wts[i];
			ArrayList<WordItem0> wis0=wt0.getWords();
			int count=0;
			int wordMaxLen=0;
			if(wis0!=null){
				System.out.println("size:"+i+","+wis0.size()+","+size);
				HashMap<String, SegAtom> wordMap = new HashMap<String, SegAtom>();
				for(int j=0;j<wis0.size();j++){ 
					SegAtom sa=new SegAtom();
					WordItem0 wi=wis0.get(j);
					sa.setWord(wi.getWord()); 
					sa.addPos(new Pos(wi.getHandle(),wi.getFreq(),false));
					count++;
					size+=8+wi.getWord().getBytes().length;
					
					//有相同的词，进行合并
					while(j<wis0.size()-1 && wis0.get(j).getWord()!=null && wis0.get(j).getWord().equals(wis0.get(j+1).getWord())){
						wi=wis0.get(j+1); 
						sa.addPos(new Pos(wi.getHandle(),wi.getFreq(),false));
						j++;
						size+=8;
					} 
					
					wordMap.put(sa.getWord(), sa);
					if(sa.getWord().length()>wordMaxLen)
						wordMaxLen=sa.getWord().length();
					
				}
				
				WordTable wt=new WordTable();
				wt.setWordCount(count);
				wt.setWordMaxLen(wordMaxLen);
				wt.setWordMap(wordMap);
				wts[i]=wt;
			}
		}
		
		dict2.save("data2\\tr.dct");
	}
}
