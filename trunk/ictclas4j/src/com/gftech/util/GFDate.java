/*
 * Created on 2004-5-31
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.gftech.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 和日期相关的常用操作
 * 
 * @author sinboy
 */
public class GFDate {
	final static String[] ft1 = { "前年", "去年", "今年", "明年", "后年", "上个月", "上一月", "上月", "本月", "下月", "下个月", "今晚", "明早",
			"明晚", "前天", "昨天", "今天", "明天", "后天", "凌晨", "早上", "早晨", "上午", "中午", "下午", "傍晚", "晚上", "白天", "周一", "周二", "周三",
			"周四", "周五", "周六", "周日", "周末", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日", "星期天", "五一", "十一", "本周",
			"下周", "下星期" };

	final static String[] ft2 = { "年", "月", "日", "号", "时", "点", "分", "秒", "-", "/", ":" };

	final static String[] ft3 = { "左右", "前后", "以前", "以后", "之后", "之前", "前", "后" };

	final static String[] ft4 = { "到", "至" };

	/**
	 * 把CALENDAR类型的日期按照指定的格式转换成字符串的表示形式。
	 * 
	 * @param date
	 * @param format
	 *            指定的日期格式，如yyyy-mm-dd,hh24:mi:ss,yyyy/mm/dd
	 * @return
	 */
	public static String cdate(Calendar date, String format) {
		StringBuffer l_strDate = new StringBuffer();
		// System.out.println("cdate:"+date.toString() );
		if (date != null && format != null) {
			
			String year = "" + date.get(Calendar.YEAR);
			String month = "" + (date.get(Calendar.MONTH) + 1);
			String day = "" + date.get(Calendar.DAY_OF_MONTH);
			String hour = "" + date.get(Calendar.HOUR_OF_DAY);
			String minute = "" + date.get(Calendar.MINUTE);
			String second = "" + date.get(Calendar.SECOND);

			month = GFString.getFixedLenStr(month, 2, '0');
			day = GFString.getFixedLenStr(day, 2, '0');
			hour = GFString.getFixedLenStr(hour, 2, '0');
			minute = GFString.getFixedLenStr(minute, 2, '0');
			second = GFString.getFixedLenStr(second, 2, '0');

			if (format.equalsIgnoreCase("yyyy-mm-dd hh24:mi:ss")){
				l_strDate.append(year);l_strDate.append( "-");
				l_strDate.append( month );
				l_strDate.append("-" );
				l_strDate.append(day );
				l_strDate.append(" " );
				l_strDate.append(hour );
				l_strDate.append(":" );
				l_strDate.append(minute );
				l_strDate.append(":" );
				l_strDate.append(second);
			}

			else if (format.equalsIgnoreCase("yyyy-mm-dd")){
				l_strDate.append(year);
				l_strDate.append( "-" );
				l_strDate.append(month );
				l_strDate.append("-" );
				l_strDate.append(day);
			}

			else if (format.equalsIgnoreCase("hh24:mi:ss")){
				l_strDate.append( hour );
				l_strDate.append(":" );
				l_strDate.append(minute );
				l_strDate.append(":" );
				l_strDate.append(second);
			}
			else if (format.equalsIgnoreCase("hh24:mi")){
				l_strDate.append( hour );
				l_strDate.append(":" );
				l_strDate.append(minute);
			}
			else if (format.equalsIgnoreCase("yyyy"))
				l_strDate.append( year);

			else if (format.equalsIgnoreCase("mm"))
				l_strDate.append( month);

			else if (format.equalsIgnoreCase("dd"))
				l_strDate.append( day);

			else if (format.equalsIgnoreCase("hh24"))
				l_strDate.append( hour);

			else if (format.equalsIgnoreCase("mi"))
				l_strDate.append( minute);

			else if (format.equalsIgnoreCase("ss"))
				l_strDate.append(second);
			else if (format.equalsIgnoreCase("yyyymmddhhmiss")){
				l_strDate.append(year );
				l_strDate.append(month );
				l_strDate.append(day );
				l_strDate.append(hour );
				l_strDate.append(minute );
				l_strDate.append(second);
			}
			else if (format.equalsIgnoreCase("yyyymmdd")){
				l_strDate.append( year );
				l_strDate.append(month );
				l_strDate.append(day);
			}
			else if (format.equalsIgnoreCase("hhmiss")){
				l_strDate.append( hour );
				l_strDate.append(minute );
				l_strDate.append(second);
			}
			else if (format.equalsIgnoreCase("yyyymm")){
				l_strDate.append( year );
				l_strDate.append(month);
			}
			else if (format.equalsIgnoreCase("mmdd")){
				l_strDate.append(month );
				l_strDate.append(day);
			}
			else if (format.equalsIgnoreCase("hhmi")){
				l_strDate.append( hour );
				l_strDate.append(minute);
			}
			else if (format.equalsIgnoreCase("miss")){
				l_strDate.append(minute );
				l_strDate.append(second);
			}

		}

		return l_strDate.length()>0?l_strDate.toString():null;
	}

	/**
	 * 把表示的日期字符型数据转换成日期型的数据
	 * 
	 * @param strDate
	 *            字符型的数据,时间的格式必须是hh24:mi:ss 日期的格式为：yyyy-mm-dd 或者是yyyymmddhhmiss
	 * @return 日期型的数据
	 */
	public static Calendar cdate(String strDate) {
		if (strDate != null) {
			try {
				int yy = 0; // 年
				int mm = 0; // 月
				int dd = 0; // 日
				int hh = 0; // 小时
				int mi = 0; // 分
				int ss = 0; // 秒
				Calendar cal = Calendar.getInstance();
				int c1 = 0; // 记录“-”号的数目
				int c2 = 0; // 记录字符串中“：”的数目
				String temp = strDate + "0";

				for (int i = 0; i < strDate.length(); i++) {
					if (temp.substring(i, i + 1).equals("-"))
						c1++;
					else if (temp.substring(i, i + 1).equals(":"))
						c2++;
					else
						continue;
				}
				if (c1 == 2 && c2 == 2) { // 长日期型:yyyy-mm-dd hh24:mi:ss
					int index1 = 0;
					int index2 = 0;
					int index3 = 0;
					index1 = strDate.indexOf("-");
					index2 = strDate.lastIndexOf("-");
					index3 = strDate.indexOf(" ");
					yy = GFString.cint(strDate.substring(0, index1));
					mm = GFString.cint(strDate.substring(index1 + 1, index2));
					dd = GFString.cint(strDate.substring(index2 + 1, index3));

					index1 = 0;
					index2 = 0;
					index1 = strDate.indexOf(":");
					index2 = strDate.lastIndexOf(":");
					hh = GFString.cint(strDate.substring(index3 + 1, index1));
					mi = GFString.cint(strDate.substring(index1 + 1, index2));
					ss = GFString.cint(strDate.substring(index2 + 1));
					cal.set(yy, mm - 1, dd, hh, mi, ss);
					return cal;

				} else if (c1 == 2) {
					int index1 = 0;
					int index2 = 0;
					index1 = strDate.indexOf("-");
					index2 = strDate.lastIndexOf("-");
					yy = GFString.cint(strDate.substring(0, index1));
					mm = GFString.cint(strDate.substring(index1 + 1, index2));
					dd = GFString.cint(strDate.substring(index2 + 1));
					cal.set(yy, mm - 1, dd, 0, 0, 0);
					return cal;

				} else if (c2 == 2) {
					int index1 = 0;
					int index2 = 0;
					index1 = strDate.indexOf(":");
					index2 = strDate.lastIndexOf(":");
					hh = GFString.cint(strDate.substring(0, index1));
					mi = GFString.cint(strDate.substring(index1 + 1, index2));
					ss = GFString.cint(strDate.substring(index2 + 1));

					cal
							.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hh,
									mi, ss);
					return cal;
				} else if (c2 == 1) {
					int index1 = 0;
					index1 = strDate.indexOf(":");
					hh = GFString.cint(strDate.substring(0, index1));
					mi = GFString.cint(strDate.substring(index1 + 1));
					ss = 0;

					cal
							.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hh,
									mi, ss);
					return cal;
				} else {// yyyymmddhhmiss
					if (strDate.length() == 8) {// yyyymmdd
						yy = GFString.cint(strDate.substring(0, 4));
						mm = GFString.cint(strDate.substring(4, 6));
						dd = GFString.cint(strDate.substring(6));
						cal.set(yy, mm - 1, dd, 0, 0, 0);
						return cal;
					} else if (strDate.length() == 6) {// hhmiss
						hh = GFString.cint(strDate.substring(0, 2));
						mi = GFString.cint(strDate.substring(2, 4));
						ss = GFString.cint(strDate.substring(4));
						cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hh,
								mi, ss);
						return cal;
					} else if (strDate.length() == 14) {// yyyymmddhhmiss
						yy = GFString.cint(strDate.substring(0, 4));
						mm = GFString.cint(strDate.substring(4, 6));
						dd = GFString.cint(strDate.substring(6, 8));
						hh = GFString.cint(strDate.substring(8, 10));
						mi = GFString.cint(strDate.substring(10, 12));
						ss = GFString.cint(strDate.substring(12));
						cal.set(yy, mm - 1, dd, hh, mi, ss);
						return cal;

					}

				}
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	/**
	 * 得到当前日期
	 * 
	 * @param format
	 *            日期格式。如：YYYYMMDD HH24MISS，YYYY-MM-DD HH24：MI：SS等
	 * @return
	 */
	public static String getCurrentDate(String format) {
		String result = null;
		Calendar now = null;
		String year;
		String month;
		String day;
		String hour;
		String minute;
		String second;

		if (format != null) {
			now = Calendar.getInstance();
			year = Integer.toString(now.get(Calendar.YEAR));
			month = Integer.toString((now.get(Calendar.MONTH) + 1));
			day = Integer.toString( now.get(Calendar.DAY_OF_MONTH));
			hour = Integer.toString( now.get(Calendar.HOUR_OF_DAY));
			minute = Integer.toString( now.get(Calendar.MINUTE));
			second = Integer.toString( now.get(Calendar.SECOND));

			month = GFString.getFixedLenStr(month, 2, '0');
			day = GFString.getFixedLenStr(day, 2, '0');
			hour = GFString.getFixedLenStr(hour, 2, '0');
			minute = GFString.getFixedLenStr(minute, 2, '0');
			second = GFString.getFixedLenStr(second, 2, '0');

			if (format.equalsIgnoreCase("YYYYMMDD HH24MISS")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year);
				sb.append(month);
				sb.append(day);
				sb.append(" ");
				sb.append(hour);
				sb.append(minute);
				sb.append(second);
				result=sb.toString(); 

			} else if (format.equalsIgnoreCase("YYYYMMDDHH24MISS")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year);
				sb.append(month);
				sb.append(day); 
				sb.append(hour);
				sb.append(minute);
				sb.append(second);
				result=sb.toString();  

			} else if (format.equalsIgnoreCase("YYMMDDHH24MISS")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year.substring(2));
				sb.append(month);
				sb.append(day); 
				sb.append(hour);
				sb.append(minute);
				sb.append(second);
				result=sb.toString(); 
				result = year.substring(2) + month + day + hour + minute + second;

			} else if (format.equalsIgnoreCase("YYYYMMDD")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year);
				sb.append(month);
				sb.append(day);  
				result=sb.toString();  

			} else if (format.equalsIgnoreCase("HH24MISS")) {
				StringBuffer sb=new StringBuffer();  
				sb.append(hour);
				sb.append(minute);
				sb.append(second);
				result=sb.toString();  

			} else if (format.equalsIgnoreCase("YYYY")) {
				result = year;

			} else if (format.equalsIgnoreCase("MM")) {
				result = month;

			} else if (format.equalsIgnoreCase("DD")) {
				result = day;

			} else if (format.equalsIgnoreCase("HH24")) {
				result = hour;

			} else if (format.equalsIgnoreCase("MI")) {
				result = minute;

			} else if (format.equalsIgnoreCase("SS")) {
				result = second;

			} else if (format.equalsIgnoreCase("YYYYMM")) {
				result = year + month;

			}

			else if (format.equalsIgnoreCase("MMDD")) {
				result = month + day;

			} else if (format.equalsIgnoreCase("HH24MI")) {
				result = hour + minute;

			} else if (format.equalsIgnoreCase("MISS")) {
				result = minute + second;

			} else if (format.equalsIgnoreCase("YYYY-MM-DD HH24:MI:SS")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year);
				sb.append("-");
				sb.append(month);
				sb.append("-");
				sb.append(day);
				sb.append(" ");
				sb.append(hour);
				sb.append(":");
				sb.append(minute);
				sb.append(":");
				sb.append(second);
				result=sb.toString();  
			} else if (format.equalsIgnoreCase("YYYY-MM-DD")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year);
				sb.append("-");
				sb.append(month);
				sb.append("-");
				sb.append(day); 
				result=sb.toString();   

			} else if (format.equalsIgnoreCase("HH24:MI:SS")) {
				StringBuffer sb=new StringBuffer(); 
				sb.append(hour);
				sb.append(":");
				sb.append(minute);
				sb.append(":");
				sb.append(second);
				result=sb.toString();   
			} else if (format.equalsIgnoreCase("YYYY-MM")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year);
				sb.append("-");
				sb.append(month);
				 
				result=sb.toString();   

			} else if (format.equalsIgnoreCase("MM-DD")) {
				StringBuffer sb=new StringBuffer(); 
				sb.append(month);
				sb.append("-");
				sb.append(day); 
				result=sb.toString();   

			} else if (format.equalsIgnoreCase("HH24:MI")) {
				StringBuffer sb=new StringBuffer(); 
				sb.append(hour);
				sb.append(":");
				sb.append(minute); 
				result=sb.toString();  

			} else if (format.equalsIgnoreCase("MI:SS")) {
				StringBuffer sb=new StringBuffer(); 
				sb.append(minute);
				sb.append(":");
				sb.append(second);
				result=sb.toString();  

			} else if (format.equalsIgnoreCase("YYYY/MM/DD")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year);
				sb.append("/");
				sb.append(month);
				sb.append("/");
				sb.append(day); 
				result=sb.toString();   

			} else if (format.equalsIgnoreCase("YYYY/MM")) {
				StringBuffer sb=new StringBuffer();
				sb.append(year);
				sb.append("/");
				sb.append(month); 
				result=sb.toString();   

			} else if (format.equalsIgnoreCase("MM/DD")) {
				StringBuffer sb=new StringBuffer(); 
				sb.append(month);
				sb.append("/");
				sb.append(day); 
				result=sb.toString();   

			}

		}

		return result;
	}

	/**
	 * 把字符串表示的日期转化成Sql中对应的日期
	 * 
	 * @param date
	 *            yyyymmdd,yyyy-mm-dd,hh:mi:ss,hhmiss
	 * @return
	 */
	public static java.sql.Date convertToSqldate(String date) {
		if (date != null) {
			Calendar d1 = cdate(date);
			if (d1 != null)
				return new java.sql.Date(d1.getTimeInMillis());
		}
		return null;
	}

	public static Timestamp cal2timestamp(Calendar cal) {
		Timestamp ts = null;

		if (cal != null) {
			ts = new Timestamp(cal.getTimeInMillis());
		}
		return ts;

	}

	/**
	 * 是否是日期或时间格式的字符串
	 * 
	 * @param date
	 */
	public static boolean isDateTime(String date) {
		if (date != null) {
			int index = -1;
			if (date == null)
				return false;
			for (String s3 : ft3) {
				index = date.indexOf(s3);
				if (index > 1) {
					if (index == date.length() - 1 || index < date.length() - 1
							&& !"天".equals(GFString.getNextString(date, index)))
						break;
					else
						index = -1;
				}
			}

			if (index > 0)
				date = date.substring(0, index);

			// 类似明天/后天/明天下午3点以后等的时间
			String day1 = null;
			for (int i = 0; i < date.length(); i++) {
				for (String s : ft1) {
					if (date.indexOf(s) == 0) {
						if (date.length() > s.length()) {
							day1 = date.substring(0, s.length());
							date = date.substring(s.length());
							i = 0;
							break;
						} else {
							if (day1 == null
									|| date.equals("白天")
									|| day1.indexOf("天") > 0
									&& ((date.indexOf("天") == -1) && date.indexOf("月") == -1 && date.indexOf("年") == -1))
								return true;
						}
					}
				}
			}
			// 明天下午4点左右

			date = GFString.treatChineseNumber(date, ft2);
			String t = null;
			boolean flag = false;
			for (int i = 0; i < date.length(); i++) {
				if (i < date.length() - 1)
					t = date.substring(i, i + 1);
				else
					t = date.substring(i);
				if (GFString.isNumeric(t))
					continue;
				if ("年".equals(t)) {
					flag = true;
					for (int j = 1; j < ft2.length; j++) {
						if (date.indexOf(ft2[j]) != -1 && date.indexOf(ft2[j]) < i)
							return false;
					}
				} else if ("月".equals(t)) {
					flag = true;
					for (int j = 2; j < ft2.length; j++) {
						if (date.indexOf(ft2[j]) != -1 && date.indexOf(ft2[j]) < i)
							return false;
					}
				} else if ("日".equals(t)) {
					flag = true;
					for (int j = 4; j < ft2.length; j++) {
						if (date.indexOf(ft2[j]) != -1 && date.indexOf(ft2[j]) < i)
							return false;
					}
				} else if ("号".equals(t)) {
					flag = true;
					for (int j = 4; j < ft2.length; j++) {
						if (date.indexOf(ft2[j]) != -1 && date.indexOf(ft2[j]) < i)
							return false;
					}
				} else if ("时".equals(t)) {
					flag = true;
					for (int j = 6; j < ft2.length; j++) {
						if (date.indexOf(ft2[j]) != -1 && date.indexOf(ft2[j]) < i)
							return false;
					}
				} else if ("点".equals(t)) {
					flag = true;
					for (int j = 6; j < ft2.length; j++) {
						if (date.indexOf(ft2[j]) != -1 && date.indexOf(ft2[j]) < i)
							return false;
					}
				} else if ("分".equals(t)) {
					flag = true;
					for (int j = 7; j < ft2.length; j++) {
						if (date.indexOf(ft2[j]) != -1 && date.indexOf(ft2[j]) < i)
							return false;
					}
				} else if ("秒".equals(t)) {
					flag = true;
					for (int j = 8; j < ft2.length; j++) {
						if (date.indexOf(ft2[j]) != -1 && date.indexOf(ft2[j]) < i)
							return false;
					}
				} else if (GFString.isAlphanumeric(t))
					return false;
			}

			if (!flag) {
				try {
					DateFormat dateformat = DateFormat.getDateTimeInstance();
					Date d = dateformat.parse(date);
					if (d != null)
						return true;
				} catch (ParseException e) {
					// e.printStackTrace();
				}
				try {

					DateFormat dateformat2 = DateFormat.getDateInstance();
					Date d2 = dateformat2.parse(date);
					if (d2 != null)
						return true;
				} catch (ParseException e) {
					// e.printStackTrace();
				}

				try {

					DateFormat dateformat3 = DateFormat.getTimeInstance();
					Date d3 = dateformat3.parse(date);
					if (d3 != null)
						return true;
				} catch (ParseException e) {
					// e.printStackTrace();
				}

			} else
				return true;

		}
		return false;
	}

	/**
	 * 字符串中是否包含有日期或时间
	 * 
	 * @param msg
	 * @return
	 */
	public static boolean hasDateTimeKey(String msg) {
		if (msg != null) {
			msg = GFString.treatChineseNumber(msg, ft2);

			// 进行原子分词,把字符串分成一个个字
			String[] atoms = new String[msg.length()];
			for (int i = 0; i < msg.length(); i++) {
				if (i < msg.length() - 1)
					atoms[i] = msg.substring(i, i + 1);
				else
					atoms[i] = msg.substring(i);
			}

			String tk = "";
			String tk2 = null;
			for (int i = 0; i < atoms.length; i++) {
				for (int j = i; j < atoms.length; j++) {
					if (isTimeKey(atoms[j])) {
						tk += atoms[j];
						if (j < atoms.length - 1)
							tk2 = atoms[j + 1];
						else
							tk2 = null;

						if (!isTimeKey(tk2) && isDateTime(tk)) {
							return true;
						} else if (!isTimeKey(tk2))
							tk = "";
					}
				}

			}

		}

		return false;
	}

	/**
	 * 从一个字符串中解析出日期 比如: 1.今天,明天,后天,大后天 2.三月五号 3.2006年3月5号
	 * 
	 * @param msg
	 * @return
	 */
	public static Calendar parseDate(String msg) {
		Calendar cal = null;
		if (msg != null && msg.length() > 1) {
			cal = Calendar.getInstance();
			if (msg.indexOf("今天") != -1)
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			else if (msg.indexOf("明天") != -1)
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1);
			else if (msg.indexOf("大后天") != -1)
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 3);
			else if (msg.indexOf("后天") != -1)
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 2);
			else {
				int year = -1;
				int month = -1;
				int day = -1;
				String s = null;

				for (int i = 0, j = i; i < msg.length(); i++) {
					if (i != msg.length() - 1) {
						s = msg.substring(i, i + 1);
						if (GFString.isGeneralNumber(s))
							continue;
						else {
							if (s.equals("年") && i > j + 1)
								year = GFString.chinaNum2arebNum(msg.substring(j, i));
							else if (s.equals("月") && i > j)
								month = GFString.chinaNum2arebNum(msg.substring(j, i));
							else if ((s.equals("日") || s.equals("号")) && i > j)
								day = GFString.chinaNum2arebNum(msg.substring(j, i));

							j = i;
							j++;
						}
					}

				}

				if (year == -1)
					year = cal.get(Calendar.YEAR);
				if (month == -1)
					month = cal.get(Calendar.MONTH) + 1;
				if (day == -1)
					day = cal.get(Calendar.DAY_OF_MONTH);
				cal.set(year, month - 1, day);
			}

		}

		return cal;
	}

	/**
	 * <pre>
	 *            解析字符串当中的时间段
	 *                                                       
	 *           1.大前年/前年/去年/今年/明年/后年/大后年
	 *           2.上个月/上一月/上月/本月/下月/下一月/下个月
	 *           3.大前天/前天/昨天/今天/明天/后天/大后天
	 *           4.凌晨/早上/早晨/上午/中午/下午/傍晚/晚上/半夜/
	 *           5.2006年/二零零六年/2006年7月/2006年7月8号/2006年7月8日/二零零六年七月八号
	 *           6.7点54/7点54分/七点五十分/7点40分8秒
	 *           7.12:20/20:30:42
	 *           8.2006-4-5/2006/4/5/
	 *           9.8点左右/以前/以后
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static Calendar[] parseTimeSeg(String msg) {
		Calendar[] cals = null;

		if (msg != null && msg.length() > 1) {
			String stime = null;
			String etime = null;
			msg = GFString.treatChineseNumber(msg, ft2);

			// 进行原子分词,把字符串分成一个个字
			String[] atoms = new String[msg.length()];
			for (int i = 0; i < msg.length(); i++) {
				if (i < msg.length() - 1)
					atoms[i] = msg.substring(i, i + 1);
				else
					atoms[i] = msg.substring(i);
			}

			String tk = "";
			String tk2 = null;
			String tk3 = null;
			String tk4 = null;
			for (int i = 0; i < atoms.length; i++) {
				for (int j = i; j < atoms.length; j++) {
					if (isTimeKey(atoms[j])) {
						tk += atoms[j];

						tk2 = j < atoms.length - 1 ? atoms[j + 1] : null;
						tk4 = j < atoms.length - 2 ? atoms[j + 2] : null;

						if (isDateTime(tk) && (!isTimeKey(tk2) || !isDateTime(tk + tk2) && !isTimeKey(tk4))) {
							tk3 = splitTime(tk);
							if (tk3 != null) {
								if (stime == null)
									stime = tk3;
								else if (etime == null && stime.indexOf("/t3") == stime.lastIndexOf("/t3"))
									etime = tk3;

							}
							i = j + 1;
							tk = "";
						} else if (!isTimeKey(tk2))
							tk = "";
					}
				}
			}

			System.out.println("stime:" + stime);
			System.out.println("etime:" + etime);
			cals = new Calendar[2];
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			cal2.set(2100, 0, 0, 0, 0, 0);
			int[][] rs1 = null;
			int[][] rs2 = null;

			if (stime != null && stime.length()>1) {
				int index1 = stime.indexOf("/t3");
				int index2 = stime.lastIndexOf("/t3");
				int index3 = stime.indexOf("/t4");

				if (index1 > 1 && index1 == index2) {
					if ("左右".equals(stime.substring(index1 - 2, index1))
							|| "前后".equals(stime.substring(index1 - 2, index1))) {
						rs1 = parseTime(stime);
						if (rs1[0][0] == 0)
							rs1[0][0] = cal1.get(Calendar.YEAR);
						if (rs1[0][1] == 0)
							rs1[0][1] = cal1.get(Calendar.MONTH);
						if (rs1[0][3] == 0 && rs1[0][2] == 0)
							rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
						if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0)
							rs1[0][4] = cal1.get(Calendar.MINUTE);
						if (rs1[0][2] == 0)
							rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);
						if (rs1[1][0] == 0)
							rs1[1][0] = rs1[0][0];
						if (rs1[1][1] == 0)
							rs1[1][1] = rs1[0][1];
						if (rs1[1][2] == 0)
							rs1[1][2] = rs1[0][2];
						if (rs1[1][3] == 0)
							rs1[1][3] = rs1[0][3];
						if (rs1[1][4] == 0)
							rs1[1][4] = rs1[0][4];
						cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
						cal2.set(rs1[1][0], rs1[1][1], rs1[1][2], rs1[1][3], rs1[1][4], rs1[1][5]);
					} else if ("以前".equals(stime.substring(index1 - 2, index1))
							|| "之前".equals(stime.substring(index1 - 2, index1))
							|| "前".equals(stime.substring(index1 - 1, index1))) {
						if (stime.indexOf(" 前/t3") != -1)
							rs1 = parseTime(stime.substring(0, index1 - 2));
						else
							rs1 = parseTime(stime.substring(0, index1 - 3));
						if (rs1[0][0] == 0)
							rs1[0][0] = cal1.get(Calendar.YEAR);
						if (rs1[0][1] == 0)
							rs1[0][1] = cal1.get(Calendar.MONTH);
						if (rs1[0][3] == 0 && rs1[0][2] == 0)
							rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
						if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0)
							rs1[0][4] = cal1.get(Calendar.MINUTE);
						if (rs1[0][2] == 0)
							rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);
						cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH),
								cal1.get(Calendar.HOUR_OF_DAY), cal1.get(Calendar.MINUTE), cal1.get(Calendar.SECOND));
						cal2.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
					} else if ("以后".equals(stime.substring(index1 - 2, index1))
							|| "之后".equals(stime.substring(index1 - 2, index1))
							|| "后".equals(stime.substring(index1 - 1, index1))) {
						if (stime.indexOf(" 后/t3") != -1)
							rs1 = parseTime(stime.substring(0, index1 - 2));
						else
							rs1 = parseTime(stime.substring(0, index1 - 3));
						if (rs1[0][0] == 0)
							rs1[0][0] = cal1.get(Calendar.YEAR);
						if (rs1[0][1] == 0)
							rs1[0][1] = cal1.get(Calendar.MONTH);
						if (rs1[0][3] == 0 && rs1[0][2] == 0)
							rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
						if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0)
							rs1[0][4] = cal1.get(Calendar.MINUTE);
						if (rs1[0][2] == 0)
							rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);

						if (rs1[0][3] > 0 || rs1[0][4] > 0)
							rs1[1][2] = rs1[0][2];
						else {
							Calendar calt = Calendar.getInstance();
							calt.set(rs1[0][0], rs1[0][1], 0, 0, 0, 0);
							rs1[1][2] = getLastDayOfMonth(calt);
						}
						cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
						cal2.set(rs1[0][0], rs1[0][1], rs1[1][2], 23, 59, 0);
					}
				} else if (index1 > 1 && index2 > index1) {
					rs1 = parseTime(stime.substring(0, index1 - 3));
					rs2 = parseTime(stime.substring(index1 + 4, index2 - 3));
					if (rs1[0][0] == 0)
						rs1[0][0] = cal1.get(Calendar.YEAR);
					if (rs1[0][1] == 0)
						rs1[0][1] = cal1.get(Calendar.MONTH);
					if (rs2[0][0] == 0)
						rs2[0][0] = rs1[0][0];
					if (rs2[0][1] == 0)
						rs2[0][1] = rs1[0][1];
					if (rs2[0][2] == 0)
						rs2[0][2] = rs1[0][2];
					if (rs2[0][3] == 0)
						rs2[0][3] = rs1[0][3];
					if (rs1[0][3] > 12 && rs2[0][3] <= 12)
						rs2[0][3] += 12;
					cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
					cal2.set(rs2[0][0], rs2[0][1], rs2[0][2], rs2[0][3], rs2[0][4], rs2[0][5]);

				} else if (index3 > 1) {
					rs1 = parseTime(stime.substring(0, index3 - 2));
					rs2 = parseTime(stime.substring(index3 + 3));
					if (rs1[0][0] == 0)
						rs1[0][0] = cal1.get(Calendar.YEAR);
					if (rs1[0][1] == 0)
						rs1[0][1] = cal1.get(Calendar.MONTH);
					if (rs1[0][3] == 0 && rs1[0][2] == 0)
						rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
					if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0)
						rs1[0][4] = cal1.get(Calendar.MINUTE);
					if (rs1[0][2] == 0)
						rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);
					if (rs2[0][0] == 0)
						rs2[0][0] = rs1[0][0];
					if (rs2[0][1] == 0)
						rs2[0][1] = rs1[0][1];
					if (rs2[0][2] == 0)
						rs2[0][2] = rs1[0][2];
					if (rs2[0][3] == 0 && rs2[0][4] == 0)
						rs2[0][4] = 59;
					if (rs2[0][3] == 0)
						rs2[0][3] = 23;

					if (rs1[0][3] > 12 && rs2[0][3] < 12)
						rs2[0][3] += 12;
					cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
					cal2.set(rs2[0][0], rs2[0][1], rs2[0][2], rs2[0][3], rs2[0][4], rs2[0][5]);

				} else {
					rs1 = parseTime(stime);
					if (rs1[0][0] == 0)
						rs1[0][0] = cal1.get(Calendar.YEAR);
					if (rs1[0][1] == 0)
						rs1[0][1] = cal1.get(Calendar.MONTH);
					if (rs1[0][3] == 0 && rs1[0][2] == 0)
						rs1[0][3] = cal1.get(Calendar.HOUR_OF_DAY);
					if (rs1[0][4] == 0 && rs1[0][2] == 0 && rs1[0][3] == 0)
						rs1[0][4] = cal1.get(Calendar.MINUTE);
					if (rs1[0][2] == 0)
						rs1[0][2] = cal1.get(Calendar.DAY_OF_MONTH);

					if (etime != null && etime.length()>1) {
						rs2 = parseTime(etime);
						if (rs2[0][0] == 0)
							rs2[0][0] = rs1[0][0];
						if (rs2[0][1] == 0)
							rs2[0][1] = rs1[0][1];
						if (rs2[0][2] == 0)
							rs2[0][2] = rs1[0][2];
						if (rs2[0][3] == 0)
							rs2[0][3] = rs1[0][3];
						cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
						cal2.set(rs2[0][0], rs2[0][1], rs2[0][2], rs2[0][3], rs2[0][4], rs2[0][5]);
					} else {
						if (rs1[1][0] == 0)
							rs1[1][0] = rs1[0][0];
						if (rs1[1][1] == 0)
							rs1[1][1] = rs1[0][1];
						if (rs1[1][2] == 0)
							rs1[1][2] = rs1[0][2];
						if (rs1[1][3] == 0)
							rs1[1][3] = rs1[0][3];
						if (rs1[1][4] == 0)
							rs1[1][4] = rs1[0][4];
						cal1.set(rs1[0][0], rs1[0][1], rs1[0][2], rs1[0][3], rs1[0][4], rs1[0][5]);
						cal2.set(rs1[1][0], rs1[1][1], rs1[1][2], rs1[1][3], rs1[1][4], rs1[1][5]);
					}
				}
			}
			cals[0] = cal1;
			cals[1] = cal2;
			System.out.println("cal1:" + cdate(cal1, "yyyy-mm-dd hh24:mi:ss"));
			System.out.println("cal2:" + cdate(cal2, "yyyy-mm-dd hh24:mi:ss"));
		}

		return cals;
	}

	private static int[][] parseTime(String stime) {
		int[][] rs = null;
		int year1 = 0;
		int month1 = 0;
		int day1 = 0;
		int hour1 = 0;
		int minute1 = 0;
		int year2 = 0;
		int month2 = 0;
		int day2 = 0;
		int hour2 = 23;
		int minute2 = 59;
		String t = null;
		Calendar cal1 = Calendar.getInstance();
		if (stime != null) {
			rs = new int[2][6];
			String[] ts = stime.split(" ");
			for (int k = 0; k < ts.length; k++) {
				t = ts[k];
				if (t.indexOf("/t1") > 1) {

					if (t.indexOf("今天") == 0) {
						day1 = cal1.get(Calendar.DAY_OF_MONTH);
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("明天") == 0) {
						day1 = cal1.get(Calendar.DAY_OF_MONTH) + 1;
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("后天") == 0) {
						day1 = cal1.get(Calendar.DAY_OF_MONTH) + 2;
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("大后天") == 0) {
						month1 = cal1.get(Calendar.MONTH);
						day1 = cal1.get(Calendar.DAY_OF_MONTH) + 3;
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("周一") == 0 || t.indexOf("星期一") == 0) {

						day1 = getThisWeekDay(1);
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("周二") == 0 || t.indexOf("星期二") == 0) {

						day1 = getThisWeekDay(2);
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("周三") == 0 || t.indexOf("星期三") == 0) {
						day1 = getThisWeekDay(3);
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("周四") == 0 || t.indexOf("星期四") == 0) {
						day1 = getThisWeekDay(4);
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("周五") == 0 || t.indexOf("星期五") == 0) {
						day1 = getThisWeekDay(5);
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("周六") == 0 || t.indexOf("星期六") == 0) {
						day1 = getThisWeekDay(6);
						hour2 = 23;
						minute2 = 59;

					} else if (t.indexOf("周日") == 0 || t.indexOf("星期天") == 0 || t.indexOf("星期日") == 0) {
						day1 = getThisWeekDay(0);
						hour2 = 23;
						minute2 = 59;
					} else if (t.indexOf("周末") == 0) {
						day1 = getThisWeekDay(6);
						day2 = day1 + 1;
						hour2 = 23;
						minute2 = 59;
					} else if (t.indexOf("五一") == 0) {
						month1 = 5;
						day1 = 1;
						day2 = day1 + 6;
						hour2 = 23;
						minute2 = 59;
					} else if (t.indexOf("十一") == 0) {
						month1 = 10;
						day1 = 1;
						day2 = day1 + 6;
						hour2 = 23;
						minute2 = 59;
					} else if (t.indexOf("下周") == 0 || t.indexOf("下星期") == 0) {
						day1 = getThisWeekDay(6) + 2;
						day2 = day1 + 6;
						hour2 = 23;
						minute2 = 59;
					} else if (t.indexOf("本周") == 0) {
						day1 = getThisWeekDay(1);
						day2 = day1 + 6;
						hour2 = 23;
						minute2 = 59;
					}

					else if (t.indexOf("早上") == 0 || t.indexOf("早晨") == 0) {
						hour1 = 4;
						hour2 = 8;
					} else if (t.indexOf("早上") == 0 || t.indexOf("早晨") == 0) {
						hour1 = 4;
						hour2 = 8;
					} else if (t.indexOf("上午") == 0) {
						hour1 = 8;
						hour2 = 12;
					} else if (t.indexOf("中午") == 0) {
						hour1 = 11;
						hour2 = 13;
					} else if (t.indexOf("下午") == 0) {
						hour1 = 13;
						hour2 = 18;
					} else if (t.indexOf("晚上") == 0) {
						hour1 = 18;
						hour2 = 23;
						minute2 = 59;
					} else if (t.indexOf("傍晚") == 0) {
						hour1 = 18;
						hour2 = 20;
					} else if (t.indexOf("白天") == 0) {
						hour1 = 6;
						hour2 = 23;
						minute2 = 59;
					} else if (t.indexOf("今晚") == 0) {
						year1 = cal1.get(Calendar.YEAR);
						month1 = cal1.get(Calendar.MONTH);
						day1 = cal1.get(Calendar.DAY_OF_MONTH);
						day2 = cal1.get(Calendar.DAY_OF_MONTH);
						hour1 = 18;
						hour2 = 20;
					} else if (t.indexOf("明晚") == 0) {
						year1 = cal1.get(Calendar.YEAR);
						month1 = cal1.get(Calendar.MONTH);
						day1 = cal1.get(Calendar.DAY_OF_MONTH) + 1;
						day2 = cal1.get(Calendar.DAY_OF_MONTH) + 1;
						hour1 = 18;
						hour2 = 24;
					} else if (t.indexOf("明早") == 0) {
						year1 = cal1.get(Calendar.YEAR);
						month1 = cal1.get(Calendar.MONTH);
						day1 = cal1.get(Calendar.DAY_OF_MONTH) + 1;
						day2 = cal1.get(Calendar.DAY_OF_MONTH) + 1;
						hour1 = 4;
						hour2 = 8;
					}

				} else if (t.indexOf("/t3") > 1) {
					if (k > 0) {
						if (t.indexOf("左右") == 0 || t.indexOf("前后") == 0) {
							if (minute1 != 0) {
								minute2 = minute1 + 30;
								minute1 -= 30;
								hour2 = 0;
							} else if (hour1 != 0) {
								hour2 = hour1 + 1;
								hour1 -= 1;

							} else if (day1 != 0) {
								day2 = day1 + 1;
								day1 -= 1;
							}
						}
					}
				} else {
					for (int i = 0, j = i; i < t.length(); i++) {
						String s = null;
						if (i != t.length() - 1)
							s = t.substring(i, i + 1);
						else
							s = t.substring(i);
						if (GFString.isGeneralNumber(s))
							continue;
						else {
							if (s.equals("年") && i > j + 1)
								year1 = GFString.chinaNum2arebNum(t.substring(j, i));
							else if (s.equals("月") && i > j)
								month1 = GFString.chinaNum2arebNum(t.substring(j, i));
							else if ((s.equals("日") || s.equals("号")) && i > j)
								day1 = GFString.chinaNum2arebNum(t.substring(j, i));
							else if ((s.equals("时") || s.equals("点")) && i > j) {

								int th = GFString.chinaNum2arebNum(t.substring(j, i));
								int tm = 0;
								int m = i + 1;
								if (m < t.length()) {
									for (; m < t.length(); m++) {
										String s1 = null;
										if (m < t.length() - 1)
											s1 = t.substring(m, m + 1);
										else
											s1 = t.substring(m);
										if (!GFString.isGeneralNumber(s1))
											break;
									}

									if (m == t.length())
										tm = GFString.chinaNum2arebNum(t.substring(i + 1));

									else
										tm = GFString.chinaNum2arebNum(t.substring(i + 1, m));
								}

								hour1 = th;
								minute1 = tm;

								if (k > 0 && (ts[k - 1].indexOf("下午") == 0 || ts[k - 1].indexOf("晚上") == 0)) {
									if (hour1 != 0)
										hour1 += 12;
								}

							}

							else if (s.equals("分") && i > j)
								minute1 = GFString.chinaNum2arebNum(t.substring(j, i));

							j = i;
							j++;
						}
					}
				}
			}
			if (month1 > 0)
				month1 -= 1;
			if (month2 > 0)
				month2 -= 1;
			rs[0][0] = year1;
			rs[0][1] = month1;
			rs[0][2] = day1;
			rs[0][3] = hour1;
			rs[0][4] = minute1;
			rs[0][5] = 0;
			rs[1][0] = year2;
			rs[1][1] = month2;
			rs[1][2] = day2;
			rs[1][3] = hour2;
			rs[1][4] = minute2;
			rs[1][5] = 0;
		}

		return rs;
	}

	private static boolean isTimeKey(String str) {
		if (str != null) {
			if (GFString.isGeneralNumber(str))
				return true;
			for (String s : ft1) {
				if (s.indexOf(str) != -1)
					return true;
			}

			for (String s : ft2) {
				if (s.indexOf(str) != -1)
					return true;
			}

			for (String s : ft3) {
				if (s.indexOf(str) != -1)
					return true;
			}
			for (String s : ft4) {
				if (s.indexOf(str) != -1)
					return true;
			}
		}

		return false;
	}

	/**
	 * <pre>
	 *                                        把时间字符进行分隔.
	 *                                        
	 *                                        比如:明天下午3点以后8点之前
	 *                                        分隔后:明天/t1 下午/t1 3点 以后/t3 8点 之前/t3 
	 * </pre>
	 * 
	 * @param st
	 * @return
	 */
	private static String splitTime(String st) {
		if (st != null) {
			int index = -1;
			for (String s : ft1) {
				for (int i = 0; i < st.length(); i++) {
					index = st.indexOf(s, i);
					if (index != -1) {
						st = st.substring(0, index) + " " + s + "/t1 " + st.substring(index + s.length());
						i = index + s.length() - 1;
					}
				}
			}

			for (String s : ft3) {
				index = st.indexOf(s);
				int index2 = st.indexOf("/t", index);
				if (index != -1 && index2 != index + 1 && index2 != index + 2) {
					st = st.substring(0, index) + " " + s + "/t3 " + st.substring(index + s.length());
				}
			}

			for (String s : ft4) {
				index = st.indexOf(s);
				if (index != -1) {
					st = st.substring(0, index) + " " + s + "/t4 " + st.substring(index + s.length());
				}
			}

		}
		return GFString.formatSpace(st);
	}

	/**
	 * 取得星期X的日期
	 * 
	 * @param week
	 *            0:星期天 1:星期一 2:星期二 3:星期三 4:星期四 5:星期五 6:星期六
	 * 
	 * @return 日期
	 */
	public static int getThisWeekDay(int week) {

		int w = 0;
		int t = 0;
		Calendar cal = Calendar.getInstance();
		t = cal.get(Calendar.DAY_OF_WEEK);

		switch (week) {
		case 0:
			w = cal.get(Calendar.DAY_OF_MONTH) + (8 - t);
			break;
		case 1:
			w = cal.get(Calendar.DAY_OF_MONTH) + (2 - t);
			break;
		case 2:
			w = cal.get(Calendar.DAY_OF_MONTH) + (3 - t);
			break;
		case 3:
			w = cal.get(Calendar.DAY_OF_MONTH) + (4 - t);
			break;
		case 4:
			w = cal.get(Calendar.DAY_OF_MONTH) + (5 - t);
			break;
		case 5:
			w = cal.get(Calendar.DAY_OF_MONTH) + (6 - t);
			break;
		case 6:
			w = cal.get(Calendar.DAY_OF_MONTH) + (7 - t);
			break;
		}

		return w;
	}

	public static int getLastDayOfMonth(Calendar ca) {
		int intDay = 31;
		if (ca != null) {
			GregorianCalendar cca = new GregorianCalendar();
			intDay = cca.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		return intDay;
	}

	/**
	 * 5.1
	 * 
	 * @return
	 */
	public static boolean isHoliday51() {
		Calendar cal = Calendar.getInstance();
		return isHoliday51(cal);
	}

	public static boolean isHoliday51(Calendar cal) {
		if (cal != null) {
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			if (month == 5 && day > 0 && day < 8)
				return true;
		}

		return false;
	}

	/**
	 * 国庆节
	 * 
	 * @return
	 */
	public static boolean isNationalDay() {
		Calendar cal = Calendar.getInstance();
		return isHoliday51(cal);
	}

	public static boolean isNationalDay(Calendar cal) {
		if (cal != null) {
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			if (month == 10 && day > 0 && day < 8)
				return true;
		}

		return false;
	}

	/**
	 * 春节
	 * 
	 * @return
	 */
	public static boolean isSpringDay() {
		Calendar cal = Calendar.getInstance();
		return isHoliday51(cal);
	}

	public static boolean isSpringDay(Calendar cal) {
		if (cal != null) {
			Lunar lunar = new Lunar(cal);
			int month = lunar.getMonth();
			int day = lunar.getDay();
			if (month == 1 && day > 0 && day < 8)
				return true;
		}

		return false;
	}

	/**
	 * 节假日.周六、周日，五一、十一、春节
	 * 
	 * @return
	 */
	public static boolean isHolidayWeek() {
		Calendar now = Calendar.getInstance();
		return isHolidayWeek(now);
	}

	public static boolean isHolidayWeek(Calendar cal) {
		if (cal != null) { 
			if (isHoliday51(cal) || isNationalDay(cal) || isSpringDay(cal)||isWeekend(cal))
				return true;

		}
		return false;
	}
	
	/**
	 * 是否是周末
	 * @return
	 */
	public static boolean isWeekend(){
		Calendar now = Calendar.getInstance();
		return isWeekend(now);
	}
	
	public static boolean isWeekend(Calendar cal){
		if (cal != null) {
			int week = cal.get(Calendar.DAY_OF_WEEK);
			if (week == Calendar.SATURDAY || week == Calendar.SUNDAY)
				return true;
			 

		}
		return false;
	}

}
