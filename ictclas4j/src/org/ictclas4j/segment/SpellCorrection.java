package org.ictclas4j.segment;

import java.util.ArrayList;

import org.ictclas4j.bean.Dictionary;
import org.ictclas4j.bean.PronunDict;
import org.ictclas4j.bean.SegNode;

/**
 * 拼写纠错（只对自定义词库进行纠错）。 一是按拼音进行纠错，二是按字体相似度进行纠错
 * 
 * @author sinboy
 * @since 2007.6.12
 * 
 */
public class SpellCorrection {
	private Dictionary coreDict;

	private PronunDict pronunDict;

	public SpellCorrection(Dictionary coreDict, PronunDict pronunDict) {
		this.coreDict = coreDict;
		this.pronunDict = pronunDict;
	}

	public void correct(ArrayList<SegNode> sns) {
		correctByPronunciation(sns);
		correctByWordface(sns);
	}

	/**
	 * 根据拼音进行纠错，比如：泥泊尔-->尼泊尔
	 * 
	 * @param sns
	 * @param coreDict
	 */
	private void correctByPronunciation(ArrayList<SegNode> sns) {
		if (sns != null && sns.size() > 0 && coreDict != null) {

		}
	}

	/**
	 * 根据字型来进行纠错，比如：喜马立雅山-->喜马拉雅山
	 * 
	 * @param sns
	 */
	private void correctByWordface(ArrayList<SegNode> sns) {
		if (sns != null && sns.size() > 0 && coreDict != null) {

		}
	}

}
