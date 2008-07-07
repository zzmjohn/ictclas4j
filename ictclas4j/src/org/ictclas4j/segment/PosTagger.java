package org.ictclas4j.segment;

import java.util.ArrayList;

import org.ictclas4j.bean.AdjoiningPos;
import org.ictclas4j.bean.DictLib;
import org.ictclas4j.bean.Dictionary;
import org.ictclas4j.bean.POSTag;
import org.ictclas4j.bean.Pos;
import org.ictclas4j.bean.PosContext;
import org.ictclas4j.bean.SegAtom;
import org.ictclas4j.bean.SegNode;
import org.ictclas4j.util.Utility;
import org.ictclas4j.util.Utility.TAG_TYPE;


/**
 * 未登录词的处理
 * 
 * @author sinboy
 * @since 2007.5.17 updated
 * 
 */
public class PosTagger {
	private DictLib dictLib;

	private Dictionary coreDict;

	private Dictionary unknownDict;

	private PosContext context;

	private int pos;

	private TAG_TYPE tagType;

	String unknownFlags;

	public PosTagger(TAG_TYPE type, DictLib dictLib) {
		if (dictLib != null) {
			this.tagType = type;
			this.dictLib = dictLib;
			this.coreDict = dictLib.getCoreDict();

			switch (type) {
			case TT_PERSON:
				unknownFlags = "未##人";
				pos = -POSTag.NOUN_PERSON;
				context = dictLib.getPersonContext();
				unknownDict = dictLib.getPersonUnknownDict();
				break;
			case TT_TRANS_PERSON:
				unknownFlags = "未##人";
				pos = -POSTag.NOUN_PERSON;
				context = dictLib.getTransPersonContext();
				unknownDict = dictLib.getTransPersonUnknownDict();
				break;
			case TT_PLACE:
				unknownFlags = "未##地";
				pos = -POSTag.NOUN_SPACE;
				context = dictLib.getPlaceContext();
				unknownDict = dictLib.getPlaceUnknownDict();
				break;
			default:
				pos = 0;
				context = dictLib.getLexContext();
				unknownDict = dictLib.getLexUnknownDict();
				break;
			}

		}
	}

	/**
	 * 从经过初分的结果中，找出构成人名、地名或其它词的未登陆词
	 * 
	 * @param segGraph
	 * @param coreDict
	 * @return
	 */
	public boolean recognise(SegGraph segGraph, ArrayList<SegNode> sns) {

		if (segGraph != null && sns != null && coreDict != null && unknownDict != null && context != null) {
			posTag(sns);
			getBestPos(sns);
			// DebugUtil.outputPostag(sns);
			switch (tagType) {
			case TT_PERSON:// Person recognition
				personRecognize(segGraph, sns);
				break;
			case TT_PLACE:// Place name recognition
			case TT_TRANS_PERSON:// Transliteration Person
				placeRecognize(segGraph, sns, coreDict);
				break;
			}
		}

		return true;
	}

	public boolean recognise(ArrayList<SegNode> sns) {

		if (sns != null && unknownDict != null && context != null) {
			posTag(sns);
			getBestPos(sns);
			// DebugUtil.outputPostag(sns);
			switch (tagType) {
			case TT_NORMAL:
				for (SegNode sn : sns) {
					if (sn.getPos() == 0) {
						sn.setPos(getBestTag(sn));
					}
				}
			}
		}

		return true;
	}

	/**
	 * 对所有的词性进行标记
	 * 
	 * @param frs
	 *            初次切分的结果
	 * @pararm startIndex 开始进行词性标记的位置
	 * @param coreDict
	 *            核心词典库
	 * @param unknownDict
	 *            未登陆词典库
	 * @return 下一个需要开始的位置
	 */
	public void posTag(ArrayList<SegNode> sns) {

		if (sns != null && coreDict != null && unknownDict != null && context != null) {
			int i = 0;
			String curWord = null;

			for (; i < sns.size(); i++) {
				SegNode sn = sns.get(i);
				sn.setAllPos(null);
				curWord = sn.getSrcWord();
				int gbkID = sn.getGbkID();// dictLib.getGBKID(curWord);
				// if (tagType == Utility.TAG_TYPE.TT_NORMAL ||
				// !unknownDict.isExist(sn.getWord(), 44)) {
				//
				// }

				if (tagType != Utility.TAG_TYPE.TT_NORMAL) {

					// 把全角字符车成半角的字符
					if (tagType == Utility.TAG_TYPE.TT_TRANS_PERSON && i > 0) {
						String prevWord = sns.get(i - 1).getSrcWord();
						if (Utility.charType(prevWord) == Utility.CT_CHINESE) {
							if (".".equals(curWord))
								curWord = "．";
							else if ("-".equals(curWord))
								curWord = "－";
						}
					}

					if (sn.getPos() < 0) {
						AdjoiningPos pos = new AdjoiningPos( 0 , 0);
						sn.addPos(pos);
					} else {
						// 从unknownDict词典库中获取当前的所有词性
						SegAtom sa = unknownDict.getSegAtom(curWord, gbkID);
						for (int j = 0; sa != null && j < sa.getPosCount(); j++) {
							Pos pos = sa.getPos(j);
							double value = -Math.log((1 + pos.getFreq()));
							value += Math.log((context.getFreq(pos.getTag()) + sa.getPosCount() + 1));
							AdjoiningPos apos = new AdjoiningPos(pos , value);
							sn.addPos(apos);
						}

						if (Utility.SENTENCE_BEGIN.equals(curWord))
							sn.addPos(new AdjoiningPos( 100 , 0));

						else if (Utility.SENTENCE_END.equals(curWord))
							sn.addPos(new AdjoiningPos( 101 , 0));
						else {
							int freq = 0;
							sa = coreDict.getSegAtom(curWord, gbkID);
							if (sa != null) {
								double value = -Math.log((double) (1 + freq));
								value += Math.log((double) (context.getFreq(0) + sa.getPosCount()));
								sn.addPos(new AdjoiningPos( 0 , value));

							}
						}
					}
				} else {
					if (sn.getPos() > 0) {
						int tag = sn.getPos();
						double value = -Math.log(1 + sn.getFreq());
						value += Math.log(1 + context.getFreq(tag));
						if (value < 0)
							value = 0;
						sn.addPos(new AdjoiningPos( tag,  value));
					} else {
						if (sn.getPos() < 0) {
							sn.setPos(-sn.getPos());
							sn.addPos(new AdjoiningPos( -sn.getPos(),  sn.getFreq()));
						}
						SegAtom sa = coreDict.getSegAtom(curWord, gbkID);
						if (sa != null) {
							for (int j = 0; j < sa.getPosCount(); j++) {
								Pos pos = sa.getPos(j);
								double value = -Math.log(1 + pos.getFreq());
								value += Math.log(context.getFreq(pos.getTag()) + sa.getPosCount());
								sn.addPos(new AdjoiningPos(pos , value));
							}
						}
					}
				}

				if (sn.getAllPos() == null)
					guessPos(tagType, sn);

				// 如果一个词节点对应的allPos为null，则说明它无法单独成词
				// 它的词性随下一个词的词性,但是结束标识“末##末”除外
				if (i - 1 >= 0 && sns.get(i - 1).getPosSize() == -1) {
					if (sn.getPosSize() > 0) {
						Pos pos = sn.getAllPos().get(0).getPos();
						int ipos = pos.getTag() == POSTag.SEN_END ? POSTag.UNKNOWN : pos.getTag();
						AdjoiningPos apos = new AdjoiningPos( ipos , 0);
						sns.get(i - 1).addPos(apos);
					}
				}
			}

			// 添加一个结束点
			SegNode last = sns.get(i - 1);
			if (last != null) {
				SegNode sn = new SegNode();
				int tag = 0;
				if (tagType != Utility.TAG_TYPE.TT_NORMAL)
					tag = 101;
				else
					tag = 1;
				AdjoiningPos pos = new AdjoiningPos( tag, 0);
				sn.addPos(pos);
				sns.add(sn);
			}
		}
	}

	/**
	 * 取得上一个词的N个词性虽和当前词的词性最匹配的那一个
	 */
	private void getBestPos(ArrayList<SegNode> sns) {
		ArrayList<AdjoiningPos> prevAllPos = null;
		ArrayList<AdjoiningPos> allPos = null;
		if (sns != null && context != null) {
			for (int i = 0; i < sns.size(); i++) {
				if (i == 0) {
					int pos = tagType != Utility.TAG_TYPE.TT_NORMAL ? 100 : 0;
					prevAllPos = new ArrayList<AdjoiningPos>();
					prevAllPos.add(new AdjoiningPos(pos, 0));
				} else {
					prevAllPos = sns.get(i - 1).getAllPos();
				}
				allPos = sns.get(i).getAllPos();
				if (allPos != null)
					for (AdjoiningPos pos : allPos) {
						// 找出前一个词性和当前词性最有可能邻接的词性
						int bestPrev = 0;
						double minValue = 10000000;
						for (int k = 0; prevAllPos != null && k < prevAllPos.size(); k++) {
							AdjoiningPos prevPos = prevAllPos.get(k);
							double temp = context.computePossibility(prevPos.getPos().getTag(), pos.getPos().getTag());
							temp = -Math.log(temp) + prevPos.getValue();
							if (temp < minValue) {
								minValue = temp;
								bestPrev = k;
							}
						}

						pos.setPrev(bestPrev);
						pos.setValue(pos.getValue() + minValue);
					}
			}

			tagBest(sns);

			// for(SegNode sn:sns){
			// String word=sn.getSrcWord();
			// System.out.println(word+":");
			// for(AdjoiningPos ap:sn.getAllPos()){
			// System.out.println("
			// "+POSTag.int2str(ap.getPos())+","+ap.getValue()+","+ap.getPrev()+","+ap.isBest());
			// }
			// }
		}
	}

	// 猜测该词的词性
	private int guessPos(TAG_TYPE tagType, SegNode sn) {
		int result = -1;
		if (sn != null && context != null) {
			int charType;
			double freq = 0;

			String word = sn.getWord();
			if (word == null)
				return result;

			switch (tagType) {
			case TT_NORMAL:
				break;
			case TT_PERSON:
				if (word.indexOf("××") != -1) {
					freq = (double) 1 / (double) (context.getFreq(6) + 1);
					sn.addPos(new AdjoiningPos(6, freq));
				} else {
					freq = (double) 1 / (double) (context.getFreq(0) + 1);
					sn.addPos(new AdjoiningPos(0, freq));

					if (sn.getLen() >= 4) {
						freq = (double) 1 / (double) (context.getFreq(0) + 1);
						sn.addPos(new AdjoiningPos(0, freq));
						freq = (double) 1 / (double) (context.getFreq(11) * 8);
						sn.addPos(new AdjoiningPos(11, freq));
						freq = (double) 1 / (double) (context.getFreq(12) * 8);
						sn.addPos(new AdjoiningPos(12, freq));
						freq = (double) 1 / (double) (context.getFreq(13) * 8);
						sn.addPos(new AdjoiningPos(13, freq));
					} else if (sn.getLen() == 2) {
						freq = (double) 1 / (double) (context.getFreq(0) + 1);
						sn.addPos(new AdjoiningPos(0, freq));
						charType = Utility.charType(word);
						if (charType == Utility.CT_OTHER || charType == Utility.CT_CHINESE) {
							freq = (double) 1 / (double) (context.getFreq(1) + 1);
							sn.addPos(new AdjoiningPos(1, freq));
							freq = (double) 1 / (double) (context.getFreq(2) + 1);
							sn.addPos(new AdjoiningPos(2, freq));
							freq = (double) 1 / (double) (context.getFreq(3) + 1);
							sn.addPos(new AdjoiningPos(3, freq));
							freq = (double) 1 / (double) (context.getFreq(4) + 1);
							sn.addPos(new AdjoiningPos(4, freq));
						}
						freq = (double) 1 / (double) (context.getFreq(11) * 8);
						sn.addPos(new AdjoiningPos(11, freq));
						freq = (double) 1 / (double) (context.getFreq(12) * 8);
						sn.addPos(new AdjoiningPos(12, freq));
						freq = (double) 1 / (double) (context.getFreq(13) * 8);
						sn.addPos(new AdjoiningPos(13, freq));
					}
				}
				break;
			case TT_PLACE:
				freq = (double) 1 / (double) (context.getFreq(0) + 1);
				sn.addPos(new AdjoiningPos(0, freq));

				if (sn.getLen() >= 4) {
					freq = (double) 1 / (double) (context.getFreq(11) * 8);
					sn.addPos(new AdjoiningPos(11, freq));
					freq = (double) 1 / (double) (context.getFreq(12) * 8);
					sn.addPos(new AdjoiningPos(12, freq));
					freq = (double) 1 / (double) (context.getFreq(13) * 8);
					sn.addPos(new AdjoiningPos(13, freq));
				} else if (sn.getLen() == 2) {
					freq = (double) 1 / (double) (context.getFreq(0) + 1);
					sn.addPos(new AdjoiningPos(0, freq));
					charType = Utility.charType(word);
					if (charType == Utility.CT_OTHER || charType == Utility.CT_CHINESE) {

						freq = (double) 1 / (double) (context.getFreq(1) + 1);
						sn.addPos(new AdjoiningPos(1, freq));
						freq = (double) 1 / (double) (context.getFreq(2) + 1);
						sn.addPos(new AdjoiningPos(2, freq));
						freq = (double) 1 / (double) (context.getFreq(3) + 1);
						sn.addPos(new AdjoiningPos(3, freq));
						freq = (double) 1 / (double) (context.getFreq(4) + 1);
						sn.addPos(new AdjoiningPos(4, freq));
					}
					freq = (double) 1 / (double) (context.getFreq(11) * 8);
					sn.addPos(new AdjoiningPos(11, freq));
					freq = (double) 1 / (double) (context.getFreq(12) * 8);
					sn.addPos(new AdjoiningPos(12, freq));
					freq = (double) 1 / (double) (context.getFreq(13) * 8);
					sn.addPos(new AdjoiningPos(13, freq));
				}
				break;
			case TT_TRANS_PERSON:
				freq = (double) 1 / (double) (context.getFreq(0) + 1);
				sn.addPos(new AdjoiningPos(0, freq));
				if (!Utility.isAllChinese(word)) {
					if (Utility.isAllLetter(word)) {
						freq = (double) 1 / (double) (context.getFreq(1) + 1);
						sn.addPos(new AdjoiningPos(1, freq));
						freq = (double) 1 / (double) (context.getFreq(11) + 1);
						sn.addPos(new AdjoiningPos(11, freq));
						freq = (double) 1 / (double) (context.getFreq(2) * 2 + 1);
						sn.addPos(new AdjoiningPos(2, freq));
						freq = (double) 1 / (double) (context.getFreq(3) * 2 + 1);
						sn.addPos(new AdjoiningPos(3, freq));
						freq = (double) 1 / (double) (context.getFreq(12) * 2 + 1);
						sn.addPos(new AdjoiningPos(12, freq));
						freq = (double) 1 / (double) (context.getFreq(13) * 2 + 1);
						sn.addPos(new AdjoiningPos(13, freq));
					}
					freq = (double) 1 / (double) (context.getFreq(41) * 8);
					sn.addPos(new AdjoiningPos(41, freq));
					freq = (double) 1 / (double) (context.getFreq(42) * 8);
					sn.addPos(new AdjoiningPos(42, freq));
					freq = (double) 1 / (double) (context.getFreq(43) * 8);
					sn.addPos(new AdjoiningPos(43, freq));
				} else if (sn.getLen() >= 4) {
					freq = (double) 1 / (double) (context.getFreq(41) * 8);
					sn.addPos(new AdjoiningPos(41, freq));
					freq = (double) 1 / (double) (context.getFreq(42) * 8);
					sn.addPos(new AdjoiningPos(42, freq));
					freq = (double) 1 / (double) (context.getFreq(43) * 8);
					sn.addPos(new AdjoiningPos(43, freq));
				} else if (sn.getLen() == 2) {
					charType = Utility.charType(word);
					if (charType == Utility.CT_OTHER || charType == Utility.CT_CHINESE) {
						freq = (double) 1 / (double) (context.getFreq(1) * 2 + 1);
						sn.addPos(new AdjoiningPos(1, freq));
						freq = (double) 1 / (double) (context.getFreq(2) * 2 + 1);
						sn.addPos(new AdjoiningPos(2, freq));
						freq = (double) 1 / (double) (context.getFreq(3) * 2 + 1);
						sn.addPos(new AdjoiningPos(3, freq));
						freq = (double) 1 / (double) (context.getFreq(30) * 8 + 1);
						sn.addPos(new AdjoiningPos(30, freq));
						freq = (double) 1 / (double) (context.getFreq(11) * 4 + 1);
						sn.addPos(new AdjoiningPos(11, freq));
						freq = (double) 1 / (double) (context.getFreq(12) * 4 + 1);
						sn.addPos(new AdjoiningPos(12, freq));
						freq = (double) 1 / (double) (context.getFreq(13) * 4 + 1);
						sn.addPos(new AdjoiningPos(13, freq));
						freq = (double) 1 / (double) (context.getFreq(21) * 2 + 1);
						sn.addPos(new AdjoiningPos(21, freq));
						freq = (double) 1 / (double) (context.getFreq(22) * 2 + 1);
						sn.addPos(new AdjoiningPos(22, freq));
						freq = (double) 1 / (double) (context.getFreq(23) * 2 + 1);
						sn.addPos(new AdjoiningPos(23, freq));
					}
					freq = (double) 1 / (double) (context.getFreq(41) * 8);
					sn.addPos(new AdjoiningPos(41, freq));
					freq = (double) 1 / (double) (context.getFreq(42) * 8);
					sn.addPos(new AdjoiningPos(42, freq));
					freq = (double) 1 / (double) (context.getFreq(43) * 8);
					sn.addPos(new AdjoiningPos(43, freq));
				}
				break;
			default:
				break;
			}
			if (sn.getAllPos() != null)
				result = sn.getAllPos().size();
		}
		return result;
	}

	/**
	 * 人名模式匹配
	 * 
	 * <pre>
	 *           
	 *           BBCD 343 0.003606 
	 *           BBC 2 0.000021 
	 *           BBE 125 0.001314 
	 *           BBZ 30 0.000315 
	 *           BCD 62460 0.656624 
	 *           BEE 0 0.000000 
	 *           BE 13899 0.146116 
	 *           BG 869 0.009136 
	 *           BXD 4 0.000042 
	 *           BZ 3707 0.038971 
	 *           CD 8596 0.090367 
	 *           EE 26 0.000273 
	 *           FB 871 0.009157 
	 *           Y 3265 0.034324
	 *           XD 926 0.009735
	 *           
	 *           The person recognition patterns set
	 *           BBCD:姓+姓+名1+名2;
	 *           BBE: 姓+姓+单名;
	 *           BBZ: 姓+姓+双名成词;
	 *           BCD: 姓+名1+名2;
	 *           BE: 姓+单名;
	 *           BEE: 姓+单名+单名;韩磊磊
	 *           BG: 姓+后缀
	 *           BXD: 姓+姓双名首字成词+双名末字
	 *           BZ: 姓+双名成词;
	 *           B: 姓
	 *           CD: 名1+名2;
	 *           EE: 单名+单名;
	 *           FB: 前缀+姓
	 *           XD: 姓双名首字成词+双名末字
	 *           Y: 姓单名成词
	 * </pre>
	 */
	private void personRecognize(SegGraph segGraph, ArrayList<SegNode> sns) {
		String sPos = null;
		String personName = null;
		// 人名识别模式
		final String[] patterns = { "BBCD", "BBC", "BBE", "BBZ", "BCD", "BEE", "BE", "BG", "BXD", "BZ", "CDCD", "CD", "EE", "FB", "Y", "XD", "" };
		final double[] factor = { 0.003606, 0.000021, 0.001314, 0.000315, 0.656624, 0.000021, 0.146116, 0.009136, 0.000042, 0.038971, 0, 0.090367,
				0.000273, 0.009157, 0.034324, 0.009735, 0 };

		if (segGraph != null && sns != null) {
			int j = 1, k, nPos;
			boolean bMatched = false;

			sPos = word2pattern(sns);
			while (sPos != null && j < sPos.length()) {
				bMatched = false;
				for (k = 0; !bMatched && patterns[k].length() > 0; k++) {
					// 如果当前句子中有符合该模式的字串，并且该字串前后都不是圆点，则认为是匹配的
					if (sPos.substring(j).indexOf(patterns[k]) == 0 && !"·".equals(sns.get(j - 1).getWord())
							&& !"·".equals(sns.get(j + patterns[k].length()))) {// Find

						String temp = sPos.substring(j + 2);
						if (temp.length() > 1)
							temp = temp.substring(0, 1);

						// Rule 1 for exclusion:前缀+姓+名1(名2): 规则(前缀+姓)失效；
						if ("FB".equals(patterns[k]) && ("E".equals(temp) || "C".equals(temp) || "G".equals(temp))) {
							continue;
						}

						nPos = j;
						personName = "";
						// Get the possible person name
						while (nPos < j + patterns[k].length()) {
							SegNode sn = sns.get(nPos);
							int gbkID = sn.getGbkID();// dictLib.getGBKID(sn.getSrcWord());
							if (sn.getPos() < 4 && unknownDict.getFreq(sn.getSrcWord(), sn.getPos(), gbkID) < Utility.LITTLE_FREQUENCY)
								personName += sn.getSrcWord();
							nPos += 1;
						}
						if ("CDCD".equals(patterns[k])) {
							if (GetForeignCharCount(personName) > 0)
								j += patterns[k].length() - 1;
							continue;
						}

						SegNode usn = new SegNode();
						usn.setRow(sns.get(j).getRow());
						usn.setCol(sns.get(j + patterns[k].length() - 1).getCol());
						usn.setWord(unknownFlags);
						usn.setSrcWord(personName);
						double value = -Math.log(factor[k]) + computePossibility(j, patterns[k].length(), sns);
						usn.setPos(pos);
						usn.setWeight(value);
						segGraph.insert(usn, true);

						j += patterns[k].length();
						bMatched = true;
					}
				}
				if (!bMatched)// Not matched, add j by 1
					j += 1;
			}

		}
	}

	// TODO:
	private int GetForeignCharCount(String personName) {
		return 0;
	}

	/**
	 * 地名模式匹配
	 * 
	 */
	private void placeRecognize(SegGraph segGraph, ArrayList<SegNode> sns, Dictionary coreDict) {
		if (segGraph != null && coreDict != null) {
			int start = 1;
			int end = 1;
			double dPanelty = 1;
			String srcWord = "";
			for (int i = 1; i < sns.size(); i++) {
				start = i;
				end = start;
				srcWord = sns.get(i).getSrcWord();
				if (getBestTag(sns, i) == 1) {
					for (end = i + 1; end < sns.size(); end++) {
						int bestTag = getBestTag(sns, end);
						if (bestTag == -1)
							continue;
						else if (bestTag == 1 || bestTag == 3) {
							if (end > i + 1)
								dPanelty += 1;
							srcWord += sns.get(end).getSrcWord();
						} else if (bestTag == 2)
							srcWord += sns.get(end).getSrcWord();
						else
							break;
					}

				} else if (getBestTag(sns, i) == 2) {
					dPanelty += 1;
					for (end = i + 1; end < sns.size(); end++) {
						int bestTag = getBestTag(sns, end);
						if (bestTag == -1)
							continue;
						else if (bestTag == 3) {
							if (end > i + 1)
								dPanelty += 1;
							srcWord += sns.get(end).getSrcWord();
						} else if (bestTag == 2)
							srcWord += sns.get(end).getSrcWord();
						else
							break;
					}
				}
				if (end > start) {
					SegNode newsn = new SegNode();
					newsn.setRow(sns.get(start).getRow());
					newsn.setCol(sns.get(end - 1).getCol());
					newsn.setPos(pos);
					newsn.setWord(unknownFlags);
					newsn.setSrcWord(srcWord);
					double value = computePossibility(start, end - start + 1, sns);
					newsn.setWeight(value);
					segGraph.insert(newsn, true);
				}
			}
		}
	}

	private int getBestTag(ArrayList<SegNode> sns, int index) {
		if (sns != null && index >= 0 && index < sns.size()) {
			SegNode sn = sns.get(index);
			return getBestTag(sn);

		}

		return -1;
	}

	private int getBestTag(SegNode sn) {
		if (sn != null) {
			ArrayList<AdjoiningPos> allPos = sn.getAllPos();
			if (allPos != null) {
				for (AdjoiningPos pos : allPos) {
					if (pos.isBest())
						return pos.getPos().getTag();
				}
			}
		}

		return -1;
	}

	// Judge whether the name is a given name
	public boolean isGivenName(String sName) {
		String firstChar;
		String secondChar;
		// given Name Possibility
		double gnp = 0;
		// singleNamePossibility
		double snp = 0;

		if (sName != null) {
			if (sName.getBytes().length != 4)
				return false;

			firstChar = sName.substring(0, 1);
			int gbkID1 = dictLib.getGBKID(firstChar);
			secondChar = sName.substring(1);
			int gbkID2 = dictLib.getGBKID(secondChar);

			// The possibility of P(Wi|Ti)
			gnp += Math.log((double) unknownDict.getFreq(firstChar, 2, gbkID1) + 1.0);
			gnp -= Math.log(context.getFreq(2) + 1.0);
			gnp += Math.log((double) unknownDict.getFreq(secondChar, 3, gbkID2) + 1.0);
			gnp -= Math.log(context.getFreq(3) + 1.0);
			// The possibility of conversion from 2 to 3
			gnp += Math.log(context.computePossibility(2, 3) + 1.0);
			gnp -= Math.log(context.getFreq(2) + 1.0);

			// The possibility of P(Wi|Ti)
			snp += Math.log((double) unknownDict.getFreq(firstChar, 1, gbkID1) + 1.0);
			snp -= Math.log(context.getFreq(1) + 1.0);
			snp += Math.log((double) unknownDict.getFreq(secondChar, 4, gbkID2) + 1.0);
			snp -= Math.log(context.getFreq(4) + 1.0);
			// The possibility of conversion from 1 to 4
			snp += Math.log(context.computePossibility(1, 4) + 1.0);
			snp -= Math.log(context.getFreq(1) + 1.0);

			// 张震||m_dict.getFrequency(sFirstChar,1)/m_dict.getFrequency(sFirstChar,2)>=10
			// The possibility being a single given name is more than being a
			// 2-char given name
			if (snp >= gnp)
				return false;
			return true;
		}

		return false;
	}

	// 把经过初次分词后的链表形式转成人名字符串模式
	private String word2pattern(ArrayList<SegNode> sns) {
		String result = null;

		if (sns != null) {
			result = "";
			for (SegNode sn : sns) {
				result += (char) (getBestTag(sn) + 'A');
			}

		}
		return result;
	}

	/**
	 * 标记出最佳词性
	 * 
	 * @param sns
	 */
	private void tagBest(ArrayList<SegNode> sns) {

		if (sns != null) {
			int size = sns.size();

			// 不考虑开始和结束标记
			for (int i = size - 1, j = 0; i >= 0; i--) {
				SegNode sn = sns.get(i);
				ArrayList<AdjoiningPos> allPos = sn.getAllPos();
				if (allPos != null && allPos.size() > j) {
					AdjoiningPos pos = allPos.get(j);
					pos.setBest(true);
					j = pos.getPrev();
				} else if (i + 1 < size - 1) {
					int tag = getBestTag(sns.get(i + 1));
					AdjoiningPos pos = new AdjoiningPos(tag, 0);
					pos.setBest(true);
					sns.get(i).addPos(pos);
				}
				// 如果该词是字母数字，并且是用户定义的企业实名，则重新设置该词的词性
				if (sn.getPos() == POSTag.NOUN_LETTER || sn.getPos() == POSTag.NUM) {
					for (AdjoiningPos pos : allPos) {
						if (pos.isBest() && pos.getPos().getTag() > 0) {
							sn.setPos(pos.getPos().getTag());
							break;
						}
					}
				}
			}
			// 把结束点去掉，用到它的目的仅仅是为了得到最后一个“末＃＃末”词的最优词性

			if (size > 1) {
				if (sns.get(size - 1).getWord() == null)
					sns.remove(size - 1);
			}
		}
	}

	private double computePossibility(int startPos, int length, ArrayList<SegNode> sns) {
		double retValue = 0, posPoss;

		if (sns != null && unknownDict != null && context != null) {
			for (int i = startPos; sns != null && i < startPos + length && i < sns.size(); i++) {
				SegNode sn = sns.get(i);
				int bestTag = getBestTag(sn);
				if (bestTag != -1) {
					int gbkID = sn.getGbkID();// dictLib.getGBKID(sn.getSrcWord());
					int freq = unknownDict.getFreq(sn.getSrcWord(), bestTag, gbkID);
					posPoss = Math.log((double) (context.getFreq(sn.getPos()) + 1));
					posPoss += -Math.log((double) (freq + 1));
					retValue += posPoss;
				}
			}
		}
		return retValue;
	}

	public Dictionary getUnknownDict() {
		return unknownDict;
	}

}
