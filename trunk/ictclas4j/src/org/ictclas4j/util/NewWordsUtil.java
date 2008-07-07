package org.ictclas4j.util;

import java.io.File;

import org.apache.log4j.Logger;
import org.ictclas4j.bean.Thesaurus;


public class NewWordsUtil {
	static Logger logger = Logger.getLogger(NewWordsUtil.class);

//	/**
//	 * 加载新增词汇
//	 * @param seg
//	 * @param dhouse
//	 * @param fileName
//	 */
//	public static void loadNewWords(DictLib dictLib,  String fileName) {
//		// load new words from local file
//		File file = new File(fileName);
//		if (file.exists() && file.isFile()) {
//			try {
//				int count=0;
//				long start_time=System.currentTimeMillis();
//				logger.info("start load new words ..."); 
//				ArrayList<String> newList =GFFile.readTxtFile2(fileName);
//				for (String newWord : newList) {
//					if(newWord.startsWith("//"))
//						continue;
//					newWord = newWord.replaceAll("，", ",");
//					String[] strs = newWord.split(",");
//					if (strs.length == 3) {
//						SegAtom wi = new SegAtom();
//						wi.setWord(strs[0]);
//						//TODO:
////						wi.setHandle(POSTag.str2int(strs[1]));
////						wi.setFreq(GFString.cint(strs[2]));
//						dictLib.addWordItem(wi, false,false);  
//						count++;
//					}
//				}
//				 
//				
//				// save coreDict  
//				dictLib.getCoreDict().save("data"+GFFinal.FILE_SEP+"coreDict_ext.dct");
//				// backup the new words to  another file ,and delete this file
//				newList.add(0,"------"+GFDate.getCurrentDate("yyyy-mm-dd hh24:mi:ss")+"--------");
//				GFFile.writeTxtFile("data"+GFFinal.FILE_SEP+"new_word_backup.txt", newList, true);
//				file.delete();
//				logger.info("+++ load new words is over.count:"+count+",time:"+(System.currentTimeMillis()-start_time)/1000+"s");
//			} catch (IOException e) {
//				logger.error("add new words:",e);
//			}
//		}
//
//
//	}
// 
//	/**
//	 * 加载binary words
//	 * @param seg
//	 * @param dhouse
//	 * @param fileName
//	 */
//	public static void loadNewBiWords(DictLib dictLib,  String fileName) {
//		// load new words from local file
//		File file = new File(fileName);
//		if (file.exists() && file.isFile()) {
//			try {
//				int count=0;
//				long start_time=System.currentTimeMillis();
//				logger.info("start load new bigram words ...");
//				ArrayList<String> newList =GFFile.readTxtFile2(fileName);
//				for (String newWord : newList) {
//					if(newWord.startsWith("//"))
//						continue;
//					newWord = newWord.replaceAll("，", ",");
//					String[] strs = newWord.split(",");
//					if (strs.length == 3) {
//						SegAtom wi = new SegAtom();
//						wi.setWord(strs[0]);
//						//TODO:
//						//wi.setHandle(GFString.cint(strs[1]));
//						wi.setFreq(GFString.cint(strs[2]));
//						dictLib.addBigramWordItem(wi, false);
//						count++;
//					} 
//				}
//				
//				// save coreDict  
//				dictLib.getBigramDict().save("data"+GFFinal.FILE_SEP+"bigramDict_ext.dct");
//				// backup the new words to  another file ,and delete this file
//				newList.add(0,"------"+GFDate.getCurrentDate("yyyy-mm-dd hh24:mi:ss")+"--------");
//				GFFile.writeTxtFile("data"+GFFinal.FILE_SEP+"new_bi_word_backup.txt", newList, true);
//				file.delete();
//				logger.info("++ load new bigram words is over.count:"+count+",time:"+(System.currentTimeMillis()-start_time)/1000+"s");
//			} catch (IOException e) {
//				logger.error("add new words:",e);
//			}
//		}
//
//
//	}

	 
	//加载同义词库
	public static Thesaurus loadThesaurus(String fileName){
		Thesaurus thesaurus=null;
		if(fileName!=null){
			File file=new File(fileName);
			if(file.exists() && file.isFile()){
				logger.info("start load thesaurus ...");
				thesaurus = new Thesaurus( );
				thesaurus.load(fileName);
				logger.info("++ load thesaurus is over");
			}
		}
		
		return thesaurus;
	}

 

}
