package org.ictclas4j.segment;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.ictclas4j.bean.Atom;
import org.ictclas4j.bean.DebugResult;
import org.ictclas4j.bean.DictLib;
import org.ictclas4j.bean.MidResult;
import org.ictclas4j.bean.POSTag;
import org.ictclas4j.bean.SegAtom;
import org.ictclas4j.bean.SegNode;
import org.ictclas4j.bean.SegResult;
import org.ictclas4j.bean.Sentence;
import org.ictclas4j.util.DebugUtil;
import org.ictclas4j.util.Utility;


public class Segment {
	private DictLib dictLib;

	private int segPathCount = 1;// 分词路径的数目

	private boolean isRecogniseUnknown;// 是否识别未登录词

	private boolean isOutputMidResult;// 是否输出中间过程

	static Logger logger = Logger.getLogger(Segment.class);

	public Segment(DictLib dictLib, int segPathCount) {
		this.dictLib = dictLib;
		this.segPathCount = segPathCount;
		this.isRecogniseUnknown = true;
	}

	public SegResult split(String src) {
		SegResult finalResult = new SegResult();// 分词结果
		DebugResult debugResult = new DebugResult(src);

		if (src != null) {
			int index = 0;
			SegResult midResult = null;
			finalResult.setRawContent(src);
			SentenceSeg ss = new SentenceSeg(src);
			ArrayList<Sentence> sens = ss.getSens();

			for (Sentence sen : sens) {
				logger.debug(sen);
				MidResult mr = new MidResult();
				mr.setIndex(index++);
				mr.setSource(sen.getContent());
				if (sen.isSeg()) {

					// 原子分词
					AtomSeg as = new AtomSeg(sen.getContent());
					ArrayList<Atom> atoms = as.getAtoms();
					mr.setAtoms(atoms);

					// 生成分词图表,先进行初步分词，然后进行优化，最后进行词性标记
					SegGraph segGraph = GraphGenerate.generate(atoms, dictLib);
					mr.setSegGraph(segGraph.getSnList());
					// 生成二叉分词图表
					SegGraph biSegGraph = GraphGenerate.biGenerate(segGraph, dictLib);
					mr.setBiSegGraph(biSegGraph.getSnList());

					// 求N最短路径
					NShortPath nsp = new NShortPath(biSegGraph, segPathCount);
					ArrayList<ArrayList<Integer>> bipath = nsp.getPaths();
					mr.setBipath(bipath);

					for (ArrayList<Integer> onePath : bipath) {
						// 得到初次分词路径
						ArrayList<SegNode> segPath = getSegPath(segGraph, onePath);
						ArrayList<SegNode> firstPath = AdjustSeg.firstAdjust(segPath);
						SegResult firstResult = outputResult(firstPath);
						mr.addFirstResult(firstResult.toString());

						if (isRecogniseUnknown)
							midResult = optinium(mr, firstPath);
						else {
							PosTagger lexTagger = new PosTagger(Utility.TAG_TYPE.TT_NORMAL, dictLib);
							lexTagger.recognise(firstPath);
							SegResult optResult = outputResult(firstPath);
							mr.addOptResult(optResult.toString());
							ArrayList<SegNode> adjResult = AdjustSeg.finalAdjust(firstPath, dictLib);

							midResult = outputResult(adjResult);
						}
						break;
					}
				} else {
					SegAtom atom = new SegAtom(sen.getContent());
					SegAtom[] atoms = new SegAtom[1];
					atoms[0] = atom;
					midResult = new SegResult();
					midResult.setRawContent(sen.getContent());
					midResult.setAtoms(atoms);
				}
				finalResult.merge(midResult);
				debugResult.addMidResult(mr);
			}
			logger.debug(finalResult.toString());
			if (this.isOutputMidResult) {
				DebugUtil.output2html(debugResult);
			}
		}

		return finalResult;
	}

	// 对初次分词结果进行优化
	private SegResult optinium(MidResult mr, ArrayList<SegNode> firstPath) {
		SegResult result = null;
		if (mr != null && firstPath != null) {
			// 处理未登陆词，进对初次分词结果进行优化
			SegGraph optSegGraph = new SegGraph(firstPath);
			ArrayList<SegNode> sns = clone(firstPath);
			PosTagger personTagger = new PosTagger(Utility.TAG_TYPE.TT_PERSON, dictLib);
			personTagger.recognise(optSegGraph, sns);
			PosTagger transPersonTagger = new PosTagger(Utility.TAG_TYPE.TT_TRANS_PERSON, dictLib);
			transPersonTagger.recognise(optSegGraph, sns);
			// PosTagger placeTagger=new
			// PosTagger(Utility.TAG_TYPE.TT_PLACE,dictLib);
			// placeTagger.recognise(optSegGraph, sns);
			mr.setOptSegGraph(optSegGraph.getSnList());

			// 根据优化后的结果，重新进行生成二叉分词图表
			SegGraph optBiSegGraph = GraphGenerate.biGenerate(optSegGraph, dictLib);
			mr.setOptBiSegGraph(optBiSegGraph.getSnList());

			// 重新求取N－最短路径
			NShortPath optNsp = new NShortPath(optBiSegGraph, segPathCount);
			ArrayList<ArrayList<Integer>> optBipath = optNsp.getPaths();
			mr.setOptBipath(optBipath);

			// 生成优化后的分词结果，并对结果进行词性标记和最后的优化调整处理
			ArrayList<SegNode> adjResult = null;
			PosTagger lexTagger = new PosTagger(Utility.TAG_TYPE.TT_NORMAL, dictLib);
			for (ArrayList<Integer> optOnePath : optBipath) {
				ArrayList<SegNode> optSegPath = getSegPath(optSegGraph, optOnePath);
				lexTagger.recognise(optSegPath);
				SegResult optResult = outputResult(optSegPath);
				mr.addOptResult(optResult.toString());
				adjResult = AdjustSeg.finalAdjust(optSegPath, dictLib);
				result = outputResult(adjResult);
				break;
			}
		}
		return result;
	}

	private ArrayList<SegNode> clone(ArrayList<SegNode> sns) {
		ArrayList<SegNode> result = null;
		if (sns != null && sns.size() > 0) {
			result = new ArrayList<SegNode>();
			for (SegNode sn : sns)
				try {
					result.add(sn.clone());
				} catch (CloneNotSupportedException e) {
					logger.error(e.getMessage(),e);
				}
		}

		return result;
	}

	// 根据二叉分词路径生成分词路径
	private ArrayList<SegNode> getSegPath(SegGraph sg, ArrayList<Integer> bipath) {

		ArrayList<SegNode> path = null;

		if (sg != null && bipath != null) {
			ArrayList<SegNode> sns = sg.getSnList();
			path = new ArrayList<SegNode>();

			for (int index : bipath)
				path.add(sns.get(index));

		}
		return path;
	}

	// 根据分词路径生成分词结果
	private SegResult outputResult(ArrayList<SegNode> wrList) {
		SegResult result = null;
		if (wrList != null && wrList.size() > 0) {
			result = new SegResult();
			ArrayList<SegAtom> saList = new ArrayList<SegAtom>();
			for (int i = 0; i < wrList.size(); i++) {

				SegNode sn = wrList.get(i);
				if (sn.getPos() != POSTag.SEN_BEGIN && sn.getPos() != POSTag.SEN_END) { 
					SegAtom sa =sn.toSegAtom();
					saList.add(sa);
				} 
			}

			SegAtom[] atoms = new SegAtom[saList.size() - 1];
			atoms = saList.toArray(atoms);
			result.setAtoms(atoms);
		}

		return result;
	}

	public void setSegPathCount(int segPathCount) {
		this.segPathCount = segPathCount;
	}

	public void setRecogniseUnknown(boolean isRecogniseUnknown) {
		this.isRecogniseUnknown = isRecogniseUnknown;
	}

	public void setOutputMidResult(boolean isOutputMidResult) {
		this.isOutputMidResult = isOutputMidResult;
	}

}
