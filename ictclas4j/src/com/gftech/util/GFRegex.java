package com.gftech.util;

/**
 * 常用正则表达式
 * 
 * @author sinboy
 * @since 2007.4.23
 * 
 */
public interface GFRegex {
	// 数字字符,包括中文
	String NUM_CN = "[0-9零一二三四五六七八九十壹贰叁肆伍陆柒捌玖拾]";

	// 不带0的数字字符
	String NUM_CN_NO_ZERO = "[1-9一二三四五六七八九十壹贰叁肆伍陆柒捌玖拾]";

	// 非数字字符
	String NUM_CN_EXCLUDE = "[^0-9零一二三四五六七八九十壹贰叁肆伍陆柒捌玖拾]";

	/**
	 * 表示范围以内的价格
	 */
	String RANGE_LOW_PREFIX = "((不高于)|(不大于)|(不超过)|(不多于)|(低于)|(最大)|(最多)|(至多))";

	String RANGE_LOW_SUFFIX = "((以内)|(以下)|(之内)|(之下))";

	/**
	 * 表示范围中间的价格
	 */
	String RANGE_EQUAL_PREFIX = "((大概)|(大约)|(大致)|约)";

	String RANGE_EQUAL_SUFFIX = "((左右)|(上下))";

	/**
	 * 表示范围以上的价格
	 */
	String RANGE_GREAT_PREFIX = "((不低)|(高于)|(大于)|(超过)|(最少)|(最低)|(至少))";

	String RANGE_GREAT_SUFFIX = "((以上)|(之上))";

	/**
	 * 表范围连接符
	 */
	String RANGE_MIDFIX = "到|至|－|-";

}
