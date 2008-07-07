package org.ictclas4j.bean;

import java.util.HashMap;
/**
 * 词性标记，在北大词性标注的基础上进行了扩展
 * @author sinboy
 * @since 2007.8.1
 *
 */
public class POSTag {
	public static final int SEN_BEGIN = 1 ;// 句子的开始标记“始##始”

	public static final int SEN_END = 4 ;// 句子的结束标记“末##末”

	public static final int ADJ_GEN = str2int("ag");// 'A'<<8)+'g';//Ag 形语素

	// 形容词性语素。形容词代码为
	// a，语素代码ｇ前面置以A。

	public static final int ADJ = str2int("a");// a 形容词 取英语形容词 adjective 的第1

	// 个字母。

	public static final int ADJ_AD = str2int("ad");// ad 副形词 直接作状语的形容词。形容词代码 a

	// 和副词代码d 并在一起。

	public static final int ADJ_NOUN = str2int("an");// an 名形词

	// 具有名词功能的形容词。形容词代码 a
	// 和名词代码n 并在一起。

	public static final int BIE = str2int("b");// b 区别词 取汉字“别”的声母。

	public static final int CONJ = str2int("c");// c 连词 取英语连词 conjunction 的第1

	// 个字母。

	public static final int ADV_GEN = str2int("dg");// dg 副语素 副词性语素。副词代码为

	// d，语素代码ｇ前面置以D。

	public static final int ADV = str2int("d");// d 副词 取 adverb 的第2 个字母，因其第1

	// 个字母已用于形容词。

	public static final int EXC = str2int("e");// e 叹词 取英语叹词 exclamation 的第1

	// 个字母。

	public static final int FANG = str2int("f");// f 方位词 取汉字“方”

	public static final int GEN = str2int("g");// g 语素

	// 绝大多数语素都能作为合成词的“词根”，取汉字“根”的声母。

	public static final int HEAD = str2int("h");// h 前接成分 取英语 head 的第1 个字母。

	public static final int IDIOM = str2int("i");// i 成语 取英语成语 idiom 的第1 个字母。

	public static final int JIAN = str2int("j");// j 简称略语 取汉字“简”的声母。

	public static final int SUFFIX = str2int("k");// k 后接成分

	public static final int TEMP = str2int("l");// l 习用语

	// 习用语尚未成为成语，有点“临时性”，取“临”的声母。

	public static final int NUM = str2int("m");// m 数词 取英语 numeral 的第3 个字母，n，u已有他用。

	public static final int NOUN_GEN = str2int("ng");// Ng 名语素 名词性语素。名词代码为n，语素代码ｇ前面置以N。
	public static final int NOUN = str2int("n");// n 名词 取英语名词 noun 的第1 个字母。

	public static final int NOUN_AGENT = str2int("na");// 代理商，取agent的第一个字母

	public static final int NOUN_BUSS = str2int("nb");// 商家，取bussinesser的第一个字母
 

	public static final int NOUN_LOGO = str2int("nl");//  商标、品牌	取Logo的第一个字母
	public static final int NOUN_LOGO_AUTO=str2int("nla");//汽车品牌	取auto的第一个字母
	public static final int NOUN_LOGO_CLOTHING=str2int("nlc");//衣服相关品牌	取clothing的第一个字母 
	public static final int NOUN_LOGO_DELICATESSEN=str2int("nld");//食品相关品牌	取delicatessen的首字母
	public static final int NOUN_LOGO_ELEC=str2int("nld");//家电品牌	取electrical appliances的首字母
	public static final int NOUN_LOGO_FITMENT=str2int("nlf");//nlf	家具品牌	取fitment的首字母
	public static final int NOUN_LOGO_HOUSE=str2int("nlh");//nlh	房产品牌	取house的第一个字母
	public static final int NOUN_LOGO_IT=str2int("nli");//nli	IT相关品牌	取IT的首字母
	public static final int NOUN_LOGO_IT_COMPUTER=str2int("nlic");//nlic	电脑相关	取computer的首字母
	public static final int NOUN_LOGO_IT_DIGITAL=str2int("nlid");//nlid	数码品牌	取digital的第一个字母
	public static final int NOUN_LOGO_IT_MOBILE=str2int("nlim");//nlim	手机品牌	取mobile的第一个字母
	public static final int NOUN_LOGO_LEECHDOM=str2int("nll");//nll	医药品牌	取leechdom的首字母
	public static final int NOUN_LOGO_TOILETRY=str2int("nlt");//nlt	洗化品牌	取toiletry的首字母
	public static final int NOUN_LOGO_WATCH=str2int("nlw");//nlw	手表品牌	取watch的首字母

	public static final int NOUN_MYSELF = str2int("nm");// 自定义的名词

	public static final int NOUN_PERSON = str2int("nr");// nr 人名 名词代码 n

	// 和“人(ren)”的声母并在一起。

	public static final int NOUN_SPACE = str2int("ns");// ns 地名 名词代码 n 和处所词代码s
	 
	public static final int NOUN_SPACE_BUS_STATION = str2int("nsb");//nsb	公交站点	取bus的首字母
	public static final int NOUN_SPACE_COMMUNITY = str2int("nsc");//nsc	社区、小区	取community的首字母
	public static final int NOUN_SPACE_FENGJING = str2int("nsf");//nsf	风景名胜	取汉语拼音feng的首字母
	public static final int NOUN_SPACE_NATION = str2int("nsg");//nsg	国家	取汉语拼音guo的首字母
	public static final int NOUN_SPACE_BUILDING = str2int("nsj");//nsj	建筑物	取汉语拼音jian的首字母
	public static final int NOUN_SPACE_HOSPITAL = str2int("nsh");//nsh	医院	取hospital的首字母
	public static final int NOUN_SPACE_MARKETPLACE = str2int("nsm");//nsm	商场、商店	取marketplace的第一个字母
	public static final int NOUN_SPACE_ROAD = str2int("nsr");//nsr	街道	取road的首字母
	public static final int NOUN_SPACE_SCHOOL = str2int("nss");//nss	学校	取school的前两个字母
	public static final int NOUN_SPACE_SCHOOL_PRIMARY = str2int("nssp");//nssp	小学	取primary school的第一个字母
	public static final int NOUN_SPACE_SCHOOL_MIDDLE = str2int("nssm");//nssm	中学	取middle school的第一个字母
	public static final int NOUN_SPACE_SCHOOL_UNIVERSITY = str2int("nssu");//nssu	大学	取university的第一个字母
	public static final int NOUN_SPACE_RESTAURANT = str2int("nsu");//nsu	餐馆	
	public static final int NOUN_SPACE_PROVINCE = str2int("ns1");//ns1	省（自治区）
	public static final int NOUN_SPACE_CITY = str2int("ns2");//ns2	市
	public static final int NOUN_SPACE_COUNTY = str2int("ns2");//ns3	县（区）
	public static final int NOUN_SPACE_TOWN = str2int("ns4");//ns4	乡（镇）	 
	public static final int NOUN_SPACE_VILLAGE = str2int("ns5");//ns5	村	

	public static final int NOUN_ORG = str2int("nt");// nt 机构团体 “团”的声母为

	// t，名词代码n 和t 并在一起。

	public static final int NOUN_LETTER = str2int("nx");// 英文或英文数字字符串

	public static final int NOUN_ZHUAN = str2int("nz");// nz 其他专名 “专”的声母的第 1个字母为z，名词代码n 和z 并在一起。 
	public static final int NOUN_ZHUAN_AUTO = str2int("nza");// nza	汽车专名	取auto的首字母
	public static final int NOUN_ZHUAN_CLOTHING = str2int("nzc");// nzc	衣服相关	取chemistry的首字母
	public static final int NOUN_ZHUAN_DELICATESSEN = str2int("nzd");// nzd	食品相关专名	取delicatessen的首字母
	public static final int NOUN_ZHUAN_ELEC = str2int("nze");// nze	家电电器专名	取electric的首字母
	public static final int NOUN_ZHUAN_IT = str2int("nzi");// nzi	IT相关专名	取it的首字母
	public static final int NOUN_ZHUAN_FITMENT = str2int("nzf");// nzf	家具相关专名	取fitment的首字母
	public static final int NOUN_ZHUAN_HOUSE = str2int("nzh");// nzh	房产相关专名	取house的首字母
	public static final int NOUN_ZHUAN_LEECHDOM = str2int("nzl");// nzl	医药专名	取leechdom的第一个字母
	public static final int NOUN_ZHUAN_MACHINE = str2int("nzm");// nzm	机械设备专名	取machine首字母
	public static final int NOUN_ZHUAN_SPORT= str2int("nzs");// nzs	运动相关专名	取sport的首字母
	public static final int NOUN_ZHUAN_TIOLETRY = str2int("nzt");// nzt	洗化相关专名	取toiletry的首字母
	public static final int NOUN_ZHUAN_WORK = str2int("nzw");// nzw	职业专名	取work的首字母
	public static final int NOUN_ZHUAN_TOOL = str2int("nz");// nzy	仪器工具专名	取汉语拼音yiqi的首字母
	public static final int NOUN_ZHUAN_CHEMICAL = str2int("nz");// nzz	化工专名	

	public static final int ONOM = str2int("o");// o 拟声词 取英语拟声词 onomatopoeia 的第1 个字母。

	public static final int PREP = str2int("p");// p 介词 取英语介词 prepositional 的第1 个字母。

	public static final int QUAN = str2int("q");// q 量词 取英语 quantity 的第1 个字母。

	public static final int PRONOUN = str2int("r");// r 代词 取英语代词 pronoun 的第2 个字母,因p 已用于介词。

	public static final int SPACE = str2int("s");// s 处所词 取英语 space 的第1 个字母。

	public static final int TIME_GEN = str2int("tg");// g 时语素 时间词性语素。时间词代码为 t,在语素的代码g 前面置以T。

	public static final int TIME = str2int("t");// t 时间词 取英语 time 的第1 个字母。

	public static final int AUXI = str2int("u");// u 助词 取英语助词 auxiliary

	public static final int VERB_GEN = str2int("vg");// vg 动语素 动词性语素。动词代码为 v。在语素的代码g 前面置以V。

	public static final int VERB = str2int("v");// v 动词 取英语动词 verb 的第一个字母。

	public static final int VERB_AD = str2int("vd");// vd 副动词 直接作状语的动词。动词和副词的代码并在一起。

	public static final int VERB_NOUN = str2int("vn");// vn 名动词 指具有名词功能的动词。动词和名词的代码并在一起。

	public static final int PUNC = str2int("w");// w 标点符号

	public static final int NO_GEN = str2int("x");// x 非语素字 非语素字只是一个符号，字母 x 通常用于代表未知数、符 号。

	public static final int YUQI = str2int("y");// y 语气词 取汉字“语”的声母。

	public static final int STATUS = str2int("z");// z 状态词 取汉字“状”的声母的前一个字母。

	public static final int UNKNOWN = str2int("un");// un 未知词 不可识别词及用户自定义词组。取英文Unkonwn 首两个字母。(非北大标准，CSW 分词中定义)
	
	private static final HashMap<Integer,Double> weightMap=initWeightMap();
	
	private static HashMap<Integer,Double> initWeightMap(){
		HashMap<Integer,Double> map=new HashMap<Integer,Double>();
		map.put(ADJ_GEN,0.1);
		map.put(ADJ, 0.7);
		map.put(ADJ_AD, 0.2);
		map.put(ADJ_NOUN,0.9);
		map.put( BIE, 0.1);
		map.put(CONJ, 0.3);
		map.put(ADV_GEN, 0.1);
		map.put(ADV, 0.4);
		map.put(EXC, 0.1);
		map.put(FANG, 0.2);
		map.put(GEN, 0.1);
		map.put( HEAD,0.2);
		map.put(IDIOM,1.0);
		map.put(JIAN,1.0);
		map.put(SUFFIX,0.2);
		map.put(TEMP, 1.0);
		map.put(NUM, 0.9);
		map.put(NOUN_GEN,1.05);
		map.put(NOUN, 1.0);
		map.put(NOUN_AGENT, 1.1);
		map.put(NOUN_BUSS,1.1);   
		map.put(NOUN_LOGO_AUTO, 1.4);
		map.put(NOUN_LOGO_CLOTHING, 1.4);
		map.put(NOUN_LOGO_DELICATESSEN, 1.4);
		map.put(NOUN_LOGO_ELEC, 1.4);
		map.put(NOUN_LOGO_FITMENT, 1.4);
		map.put(NOUN_LOGO_IT, 1.4);
		map.put(NOUN_LOGO_IT_COMPUTER, 1.41);
		map.put(NOUN_LOGO_IT_DIGITAL, 1.41);
		map.put(NOUN_LOGO_IT_MOBILE, 1.41);
		map.put(NOUN_LOGO_LEECHDOM, 1.4);
		map.put(NOUN_LOGO_TOILETRY, 1.4);
		map.put(NOUN_LOGO_WATCH, 1.4); 
		map.put(NOUN_MYSELF, 1.1);
		map.put(NOUN_PERSON, 1.12);
		map.put(NOUN_SPACE, 1.2);  
		map.put(NOUN_SPACE_BUS_STATION, 1.25);
		map.put(NOUN_SPACE_COMMUNITY, 1.25);
		map.put(NOUN_SPACE_FENGJING, 1.25);
		map.put(NOUN_SPACE_NATION, 1.25);
		map.put(NOUN_SPACE_BUILDING, 1.25);
		map.put(NOUN_SPACE_HOSPITAL, 1.25);
		map.put(NOUN_SPACE_MARKETPLACE, 1.25);
		map.put(NOUN_SPACE_ROAD, 1.25);
		map.put(NOUN_SPACE_SCHOOL, 1.25);
		map.put(NOUN_SPACE_SCHOOL_PRIMARY, 1.256);
		map.put(NOUN_SPACE_SCHOOL_MIDDLE, 1.256);
		map.put(NOUN_SPACE_SCHOOL_UNIVERSITY, 1.256);
		map.put(NOUN_SPACE_RESTAURANT, 1.25);
		map.put(NOUN_SPACE_PROVINCE, 1.25);
		map.put(NOUN_SPACE_CITY, 1.3);
		map.put(NOUN_SPACE_COUNTY, 1.35);
		map.put(NOUN_SPACE_TOWN, 1.4); 
		map.put(NOUN_SPACE_VILLAGE, 1.45);
		map.put( NOUN_ORG,1.3);
		map.put( NOUN_ZHUAN, 1.3); 
		map.put( NOUN_ZHUAN_AUTO, 1.35);
		map.put( NOUN_ZHUAN_CLOTHING, 1.35);
		map.put( NOUN_ZHUAN_DELICATESSEN, 1.35);
		map.put( NOUN_ZHUAN_ELEC, 1.35);
		map.put( NOUN_ZHUAN_IT, 1.35);
		map.put( NOUN_ZHUAN_FITMENT, 1.35);
		map.put( NOUN_ZHUAN_HOUSE, 1.35);
		map.put( NOUN_ZHUAN_LEECHDOM, 1.35);
		map.put( NOUN_ZHUAN_MACHINE, 1.35);
		map.put( NOUN_ZHUAN_SPORT, 1.35);
		map.put( NOUN_ZHUAN_TIOLETRY, 1.35);
		map.put( NOUN_ZHUAN_WORK, 1.35);
		map.put( NOUN_ZHUAN_TOOL, 1.35);
		map.put( NOUN_ZHUAN_CHEMICAL, 1.35);  
		map.put( ONOM, 0.1);
		map.put(PREP, 0.2);
		map.put( QUAN,0.3);
		map.put(PRONOUN,0.2);
		map.put(SPACE, 0.2);
		map.put(TIME_GEN,0.1);
		map.put( TIME,1.15);
		map.put( AUXI,0.1);
		map.put(VERB_GEN, 0.1);
		map.put( VERB,0.5);
		map.put(VERB_AD, 0.5);
		map.put(VERB_NOUN, 0.9);
		map.put(PUNC, 0.0);
		map.put( NO_GEN,0.1);
		map.put( YUQI,0.1);
		map.put(STATUS, 0.3);
		map.put(UNKNOWN,0.5);
		return map;
	}
	

	/**
	 * 权重系数。名词（n）=1，根据不同词性在句子中的重要程度来设置
	 * 
	 * @param pos
	 *            词性
	 * @return
	 */
	public static double getWeightCoefficient(int pos) {
		double result = 1;
		 if(weightMap!=null){
			 Double dl=weightMap.get(pos);
			 if(dl!=null)
				 result=dl;
		 }
		return result;
	}

	public static int str2int(String str) {
		int result = 0;
		if (str != null) {
			char[] cs = str.toCharArray();
			if (cs.length <= 4) {
				for (int i = 0; i < cs.length; i++) {
					result += cs[i] << ((3 - i) * 8);
				}
			}
		}
		return result;
	}

	/**
	 * 把整数表示的词性转成字符串表示的、可以直接查看的形式
	 * @param pos 词性
	 * @return
	 */
	public static String int2str(int pos) {
		StringBuffer bs = new StringBuffer();
		char[] cs = new char[4];
		cs[0] = (char) (pos >> 24);
		cs[1] = (char) ((pos >> 16) & 0xFF);
		cs[2] = (char) ((pos >> 8) & 0xFF);
		cs[3] = (char) (pos & 0xFF);
		for (char c : cs)
			if (c > 0)
				bs.append(c);

		return bs.toString();
	}
	
	public static int old2new(int old){
		return old<<16;
	}
}
