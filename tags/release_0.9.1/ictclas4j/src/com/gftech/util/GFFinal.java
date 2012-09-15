package com.gftech.util;

public interface GFFinal {
	// 文件分隔符
	String FILE_SEP = System.getProperty("file.separator");

	/**
	 * 数字,0-9
	 */
	String[] NUMBER = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	/**
	 * 字母,A-Z
	 */
	String[] UPPER_LETTER = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
			"Y", "Z" };

	/**
	 * 大写数字，比如：一，二等
	 */
	String[] CHINA_NUMBER = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "百", "千", "万", "两" };

	/**
	 * 做帐用的大写数字,比如:壹,伍
	 */
	String[] ACCOUNT_NUMBER = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾", "佰", "仟", "万" };

	/**
	 * 标点符号
	 */
	String[] INTERPUNCTION = { "、", "：", "，", "；", "。", "”", "“", "（", "）", "(", ")", "‘", "’", ",", ".", ";", "'", ";", "\"", "/", ":", "?", "!",
			"*" };

	/**
	 * 特殊符号,比如#,@,/,&
	 */
	String[] SPECIAL_SYMBOL = { "#", "&", "$", "%", "^", "*", "(", ")", "<", ">", "[", "]", "{", "}", "+", "=", "\\", "|", "/", "~", "`", " " };

	//短信最大长度
	int CN_SMS_MAX_LEN=70;
	int EN_SMS_MAX_LEN=140;
	int EN_7BIT_SMS_MAX_LEN=160;
}
