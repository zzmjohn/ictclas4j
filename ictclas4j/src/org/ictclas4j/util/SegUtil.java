package org.ictclas4j.util;


public class SegUtil {

	/**
	 * inverted document freq(逆文档频率）
	 * 
	 * @param d
	 *            文档总数
	 * @param dw
	 *            出现关键词的文档总数
	 * @return
	 */
	public static double IDF(long d, long dw) {
		double result = 0;
		if (dw <= 0)
			dw = 1;
		if (d > 0 && dw > 0) {
			result = Math.log((double) d / dw);
			result = result < 0 ? 0 : result;
		}
		return result;
	}

	/**
	 * Term freq(词频）
	 * 
	 * @param count
	 *            关键词在文档中出现的次数
	 * @param total
	 *            文档中关键词的总数
	 * @return
	 */
	public static double TF(int count, int total) {
		double result = 0;

		if (count >= 0 && total > 0) {
			result = (double) count / total;
		}
		return result;

	}

	/**
	 * 用来衡量信息的相关性的算法
	 * 
	 * @param tf
	 *            词频
	 * @param idf
	 *            逆文档频率
	 */
	public static double TF_IDF(double tf, double idf) {
		return tf * idf;
	}

	/**
	 * 得到一个关键词的权重，该词的长度*TF/IDF
	 * 
	 * @param word
	 *            关键词
	 * @param count
	 *            关键词在源文本出现的次数
	 * @param total
	 *            源文本的关键词总数
	 * @param d
	 *            文档总数
	 * @param dw
	 *            出现该关键词的文档数目
	 * @return
	 */
	public static double getWeight(String word, int count, int total, long d, long dw) {
		double result = 0;
		if (word != null) {
			result = Math.log(word.length()) / 3.0 + TF_IDF(TF(count, total), IDF(d, dw));
		}
		return result;
	}
 


}
