package org.ictclas4j.segment;

import java.util.ArrayList;

import org.ictclas4j.bean.Atom;
import org.ictclas4j.bean.DictLib;
import org.ictclas4j.bean.Dictionary;
import org.ictclas4j.bean.POSTag;
import org.ictclas4j.bean.Pos;
import org.ictclas4j.bean.SegAtom;
import org.ictclas4j.bean.SegNode;
import org.ictclas4j.util.Utility;


public class GraphGenerate {

	/**
	 * 全切分,生成切分图.即找出所有可能的词组
	 * 
	 * @param atoms
	 * @return
	 */
	public static SegGraph generate(ArrayList<Atom> atoms, DictLib dictLib) {
		SegGraph segGraph = null;
		SegNode sn = null;
		Atom atom = null;

		if (atoms != null && atoms.size() > 0 && dictLib != null) {
			segGraph = new SegGraph();
			Dictionary dict = dictLib.getCoreDict();

			// 先把非汉字字符的词性识别出来
			for (int i = 0; i < atoms.size(); i++) {
				atom = atoms.get(i);
				String word = atom.getWord();
				if (atom.getPos() == Utility.CT_CHINESE)
					sn = new SegNode(i, i + 1, 0, 0, atom.getWord());
				else {
					int pos = 0;
					double value = Utility.MAX_FREQUENCE;

					switch (atom.getPos()) {
					case Utility.CT_INDEX:
					case Utility.CT_NUM:
						pos = -POSTag.NUM;// 'm'*256
						word = Utility.UNKNOWN_NUM;
						value = 0;
						break;
					case Utility.CT_DELIMITER:
						pos = POSTag.PUNC;// 'w'*256;
						break;
					case Utility.CT_LETTER:
						pos = -POSTag.NOUN_LETTER;//
						value = 0;
						word = Utility.UNKNOWN_LETTER;
						break;
					case Utility.CT_SINGLE:// 12021-2129-3121
						if (Utility.getCharCount("+-1234567890", atom.getWord()) == atom.getLen()) {
							pos = -POSTag.NUM;// 'm'*256
							word = Utility.UNKNOWN_NUM;
						} else {
							pos = -POSTag.NOUN_LETTER;//
							word = Utility.UNKNOWN_LETTER;
						}
						value = 0;
						break;
					default:
						pos = atom.getPos();// '?'*256;
						break;
					}

					int gbkID = dictLib.getGBKID(word);
					sn = new SegNode(i, i + 1, pos, value, word);
					sn.setGbkID(gbkID);
				}

				sn.setSrcWord(atom.getWord());
				segGraph.insert(sn, true);
			}

			StringBuffer words = new StringBuffer();
			for (int i = 0; i < atoms.size(); i++) {
				int j = i + 1;
				words.delete(0, words.length());
				words.append(atoms.get(i).getWord());

				// 如果是“月份”，不要分割
				boolean flag = false;
				if (j < atoms.size()) {
					Atom a2 = atoms.get(j);
					if ("月".equals(words.toString()) && "份".equals(a2.getWord())) {
						segGraph.delete(i, j);
						segGraph.delete(i + 1, j + 1);
						words.append(a2.getWord());
						flag = true;
						j++;
					}
				}

				SegAtom sa = null;
				String word = words.toString();
				int gbkID = dictLib.getGBKID(word);
				int wordMaxLen = dict.getWordMaxLen(word, gbkID);
				for (; j <= atoms.size() && word.length() < wordMaxLen; j++) {
					word = words.toString();
					sa = dict.getSegAtom(word, gbkID);
					if (sa != null) {
						// 1年内，1999年末
						// if (word.length() == 2 && segGraph.getSize() > 0) {
						// SegNode g2 = segGraph.getLast();
						// if (Utility.isAllNum(g2.getWord()) ||
						// Utility.isAllChinese(g2.getWord())
						// && (g2.getWord().indexOf("年") == 0 ||
						// g2.getWord().indexOf("月") == 0)) {
						// if ("末内中底前间初".indexOf(words.substring(1)) != -1)
						// break;
						// }
						// }
						// 只有一个性词，存贮它
						SegNode sg = null;
						if (sa.getPosCount() == 1) {
							Pos pos = sa.getPos(0);
							sg = new SegNode(i, j, pos.getTag(), sa.getTotalFreq(), word);
						} else
							sg = new SegNode(i, j, 0, sa.getTotalFreq(), word);
						sg.setSrcWord(word);
						sg.setGbkID(gbkID);
						segGraph.insert(sg, true);
					}

					if (j < atoms.size()) {
						String word2 = atoms.get(j).getWord();
						words.append(word2);
					}
				}
				if (flag)
					i++;
			}

		}
		return segGraph;
	}

	/**
	 * 生成二叉图表,每个节点表示相邻两个词组的耦合关系,如:说@的确
	 * 
	 * @param sgs
	 */
	public static SegGraph biGenerate(SegGraph seg, DictLib dictLib) {
		double curFreq;
		SegGraph segGraph = null;
		final double smoothParam = 0.1;
		if (dictLib == null)
			return null;
		Dictionary dict = dictLib.getCoreDict();
		Dictionary biDict = dictLib.getBigramDict();

		if (seg != null && dict != null && biDict != null) {
			segGraph = new SegGraph();
			ArrayList<SegNode> sgs = seg.getSnList();

			for (int i = 0; sgs != null && i < sgs.size(); i++) {
				SegNode sg = sgs.get(i);
				if (sg.getPos() >= 0)
					curFreq = sg.getWeight();
				else {
					int gbkID = sg.getGbkID();// dictLib.getGBKID(sg.getWord());
					curFreq = dict.getFreq(sg.getWord(), 2, gbkID);
				}

				// 得到下面行值和该列值相等的所有元素
				ArrayList<SegNode> nextSgs = seg.getNextElements(i);
				for (SegNode graph : nextSgs) {
					String twoWords = sg.getWord();
					twoWords += Utility.WORD_SEGMENTER;
					twoWords += graph.getWord();
					int gbkID = sg.getGbkID();// dictLib.getGBKID(twoWords);

					// 计算相临两个词之间的平滑值
					// -log{a*P(Ci-1)+(1-a)P(Ci|Ci-1)} Note 0<a<1
					int twoFreq = biDict.getFreq(twoWords, 3, gbkID);
					double temp = (double) 1 / Utility.MAX_FREQUENCE;
					double value = smoothParam * (1 + curFreq) / (Utility.MAX_FREQUENCE + 80000);
					value += (1 - smoothParam) * ((1 - temp) * twoFreq / (1 + curFreq) + temp);
					value = -Math.log(value);

					if (value < 0) {
						value += sg.getFreq();
					}

					SegNode sg2 = new SegNode();
					// 分隔符@前的词在链表中的位置
					int wordIndex = getWordIndex(sgs, sg);
					sg2.setRow(wordIndex);

					// 分隔符@后的词在链表中的位置
					wordIndex = getWordIndex(sgs, graph);
					sg2.setCol(wordIndex);
					sg2.setWord(twoWords);
					sg2.setPos(sg.getPos());
					sg2.setWeight(value);
					sg2.setGbkID(gbkID);
					segGraph.insert(sg2, false);
				}
			}
		}
		return segGraph;
	}

	private static int getWordIndex(ArrayList<SegNode> sgs, SegNode graph) {
		if (sgs != null && graph != null) {
			for (int i = 0; i < sgs.size(); i++) {
				if (sgs.get(i) == graph)
					return i;
			}
		}

		return -1;
	}

}
