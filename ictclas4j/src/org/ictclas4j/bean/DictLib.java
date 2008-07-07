package org.ictclas4j.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.control.CompositeCacheManager;
import org.apache.log4j.Logger;
import org.ictclas4j.util.Utility;
import org.ictclas4j.util.Utility.TAG_TYPE;

import com.gftech.util.GFFile;
import com.gftech.util.GFFinal;
import com.gftech.util.GFString;
import com.gftech.util.GFUtil;

/**
 * Dictionary Library
 * 
 * @author sinboy
 * @since 2007.12.6
 * 
 */
public class DictLib {
	private Dictionary coreDict;

	private Dictionary bigramDict;

	private Dictionary personUnknownDict;

	private PosContext personContext;

	private Dictionary transPersonUnknownDict;

	private PosContext transPersonContext;

	private Dictionary placeUnknownDict;

	private PosContext placeContext;

	private Dictionary lexUnknownDict;

	private PosContext lexContext;

	private JCS segCache;// 分词结果Cache

	// GBK汉字+字母数字对的GBK_ID值
	private HashMap<String, Integer> idMap;

	static Logger logger = Logger.getLogger(DictLib.class);

	public DictLib() {
		boolean isGBKExtend = false;
		idMap = new HashMap<String, Integer>();
		for (int i = 0; i < Utility.GBK_NUM_EXT; i++) {
			idMap.put(Utility.getGBKWord(i), i);
		}

		logger.info("Load coreDict  ...");
		coreDict = new Dictionary("data" + GFFinal.FILE_SEP + "coreDict.dct", isGBKExtend);

		logger.info("Load bigramDict ...");
		bigramDict = new Dictionary("data" + GFFinal.FILE_SEP + "bigramDict.dct", isGBKExtend);

		logger.info("Load tagger dict ...");
		personUnknownDict = new Dictionary("data" + GFFinal.FILE_SEP + "nr.dct", isGBKExtend);
		personContext = new PosContext("data" + GFFinal.FILE_SEP + "nr.ctx");
		transPersonUnknownDict = new Dictionary("data" + GFFinal.FILE_SEP + "tr.dct", isGBKExtend);
		transPersonContext = new PosContext("data" + GFFinal.FILE_SEP + "tr.ctx");
		placeUnknownDict = new Dictionary("data" + GFFinal.FILE_SEP + "ns.dct", isGBKExtend);
		placeContext = new PosContext("data" + GFFinal.FILE_SEP + "ns.ctx");
		lexUnknownDict = coreDict;
		lexContext = new PosContext("data" + GFFinal.FILE_SEP + "lexical.ctx");

		loadMyDict("data"+ GFFinal.FILE_SEP +"myDict.txt");
		// personTagger = new PosTagger(Utility.TAG_TYPE.TT_PERSON, "data" +
		// GFFinal.FILE_SEP + "nr", coreDict);
		// transPersonTagger = new PosTagger(Utility.TAG_TYPE.TT_TRANS_PERSON,
		// "data" + GFFinal.FILE_SEP + "tr", coreDict);
		// placeTagger = new PosTagger(Utility.TAG_TYPE.TT_TRANS_PERSON, "data"
		// + GFFinal.FILE_SEP + "ns", coreDict);
		// lexTagger = new PosTagger(Utility.TAG_TYPE.TT_NORMAL, "data" +
		// GFFinal.FILE_SEP + "lexical", coreDict);

		// pronunDict = new PronunDict("data"+GFFinal.FILE_SEP+"pronun.txt");
		logger.info("Load dict is over");

		// init Segment Cache
		try {
			CompositeCacheManager ccm = CompositeCacheManager.getUnconfiguredInstance();
			StringBuffer sb = new StringBuffer();
			Properties props = new Properties();
			sb.append("conf").append(GFFinal.FILE_SEP).append("cache.ccf");
			props.load(new FileInputStream(new File(sb.toString())));
			ccm.configure(props);
			segCache = JCS.getInstance("segCache");
			logger.info("init index、info、seg cache");
		} catch (CacheException e) {
			logger.error("init segment cache is failed", e);
		} catch (IOException e) {
			logger.error("init segment cache is failed", e);
		}
	}

	public Dictionary getBigramDict() {
		return bigramDict;
	}

	public Dictionary getCoreDict() {
		return coreDict;
	}

	public Dictionary getPersonUnknownDict() {
		return personUnknownDict;
	}

	public PosContext getPersonContext() {
		return personContext;
	}

	public Dictionary getTransPersonUnknownDict() {
		return transPersonUnknownDict;
	}

	public PosContext getTransPersonContext() {
		return transPersonContext;
	}

	public Dictionary getPlaceUnknownDict() {
		return placeUnknownDict;
	}

	public PosContext getPlaceContext() {
		return placeContext;
	}

	public Dictionary getLexUnknownDict() {
		return lexUnknownDict;
	}

	public PosContext getLexContext() {
		return lexContext;
	}

	public Dictionary getUnknownDict(TAG_TYPE type) {
		switch (type) {
		case TT_PERSON:
			return this.personUnknownDict;
		case TT_TRANS_PERSON:
			return this.transPersonUnknownDict;
		case TT_PLACE:
			return this.placeUnknownDict;
		default:
			return this.lexUnknownDict;
		}
	}

	public PosContext getContext(TAG_TYPE type) {
		switch (type) {
		case TT_PERSON:
			return this.personContext;
		case TT_TRANS_PERSON:
			return this.transPersonContext;
		case TT_PLACE:
			return this.placeContext;
		default:
			return this.lexContext;
		}
	}

	// TODO:
	public boolean addWordItem(SegAtom wi, boolean isOvercast, boolean isNotSave) {
		// if (wi != null && coreDict != null) {
		// int handle = wi.getHandle();
		// return coreDict.addItem(wi.getWord(), handle, wi.getFreq(), false,
		// isOvercast, isNotSave);
		// } else
		return false;
	}

	// TODO:
	public boolean addBigramWordItem(SegAtom wi, boolean isNotSave) {
		// if (wi != null && bigramDict != null) {
		// int handle = wi.getHandle();
		// return bigramDict.addItem(wi.getWord(), handle, wi.getFreq(), false,
		// false, isNotSave);
		// } else
		return false;
	}

	// TODO:
	public boolean delWordItem(String word, int pos) {
		// if (word != null && coreDict != null) {
		// return coreDict.delItem(word, pos);
		// } else
		return false;
	}

	// 取得Cache中的分词结果
	public SegResult getCachedSeg(String src) {
		SegResult result = null;

		if (segCache != null && src != null) {
			result = (SegResult) segCache.get(src);

		}
		return result;
	}

	public void delCachedSeg(String word) {
		if (segCache != null && word != null) {
			try {
				segCache.remove(word);
			} catch (CacheException e) {
				logger.error(e);
			}
		}
	}

	public void addCachedSeg(String src, SegResult result) {
		if (segCache != null && src != null && result != null) {
			try {
				GFUtil.putIntoCache(segCache, src, result);
			} catch (CacheException e) {
				logger.error(e);
			}
		}
	}

	public int getGBKID(String word) {
		if (word != null && word.length() > 0) {
			String first = GFString.getFirst(word);
			if (first != null) {
				Integer obj = idMap.get(first);
				return obj != null ? obj : -1;
			}
		}
		return -1;
	}

	// 加载用户自定义词组
	private void loadMyDict(String fileName) {
		if (fileName != null) {
			try {
				SegAtom sa = new SegAtom();
				ArrayList<String> list = GFFile.readTxtFile2(fileName);
				for (String line : list) {
					if (line.startsWith("#"))
						continue;
					line = line.replaceAll("，", ",");
					String[] strs = line.split(",");
					if (strs.length >= 4) {
						SegAtom saClone = sa.clone();
						saClone.setWord(strs[0]);
						Pos pos=new Pos();
						pos.setTag(POSTag.str2int(strs[1]));
						pos.setFreq(GFString.cint(strs[2]));
						pos.setVisible("1".equals(strs[3])?true:false);
						saClone.addPos(pos);
						int index=getGBKID(strs[0]);
						coreDict.addSegAtom(saClone,index);
						
						if(strs.length==5){
							String str=strs[4];
							String[] strs2=str.split(" ");
							for(String s:strs2){
								SegAtom saClone2=sa.clone();
								saClone2.setWord(s); 
								Pos pos2=new Pos();
								pos2.setTag(3);
								pos2.setFreq(1);
								saClone2.addPos(pos2);
								bigramDict.addSegAtom(saClone2,index);
							}
						}
					}

				}
			} catch (IOException e) {
				logger.error("load myDict is failed", e);
			} catch (CloneNotSupportedException e) {
				logger.error(e);
			}
		}
	}

}
