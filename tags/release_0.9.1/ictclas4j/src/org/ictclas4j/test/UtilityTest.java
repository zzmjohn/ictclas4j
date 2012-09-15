package org.ictclas4j.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ictclas4j.run.Config;
import org.ictclas4j.util.Utility;

import com.gftech.util.GFString;

//test1
//test2
public class UtilityTest {

	static Logger logger = Logger.getLogger(UtilityTest.class);

	public static void main(String[] args) {

		PropertyConfigurator.configure(Config.LOG4J_CONF);
		test3();
	}

	private static void test1() {
		System.out.println(Utility.GBK_ID("ａ"));

		byte[] bs = new byte[2];
		// for(int i=0xa1;i<0xb0;i++)
		// for(int j=0xa1;j<0xFF;j++){
		// bs[0]=(byte)i;
		// bs[1]=(byte)j;
		// System.out.println(i+","+j+":"+new String(bs));
		// }
		//			
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 21998; i < 22034; i++) {
			String w = Utility.getGBKWord(i);
			String str = i + "," + GFString.int2hex(i, 4) + ":" + w;
			list.add(str);

			System.out.println(str);
			if (w != null && Utility.GBK_ID(w) != i) {
				System.err.println("\nerr:" + w + "," + Utility.GBK_ID(w));
				// break;
			}

		}
		//			
		// try {
		// GFFile.writeTxtFile("test\\gbk_test.txt",list,false);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//		
	}

	// test GBK_ID() profile
	public static void test2() {
		String s = "唐家山堰塞湖泄流槽主体工程基c本完成，可比原计划D提前实现分洪，预计6月1日至3日进行分洪。北京时间9点，新华网记者与正在唐家山堰塞湖现场的本网军事记者刘昕和王玉山进行电话连线，他们报道了今天首批施工人员撤离现场的相关情况。 问：在今天撤离之前，都做了一些什么样的准备工作？ 王玉山： 现在抢险队伍首先进a行了动员，昨天晚上大家都已经开始准备撤离，对战士进行安全培";
		int count = 0;
		HashMap<String, Integer> idMap = new HashMap<String, Integer>();
		for (int i = 0; i < Utility.GBK_NUM_EXT; i++) {
			idMap.put(Utility.getGBKWord(i), i);
		}
		String[] ss = new String[s.length()];
		for (int j = 0; j < s.length(); j++) {
			ss[j] = s.substring(j, j + 1);
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			for (String s0 : ss) {
				count++;
				// idMap.get(s0);
				Utility.GBK_ID(s0);
			}
		}

		long end = System.currentTimeMillis();
		long spend = end - start;
		System.out.println("total time:" + spend + ",avg time:" + ((double) spend / count));

	}

	// test substring's time of String and StringBuffer
	public static void test3() {
		int count = 0;
		String s = "唐家山堰塞湖泄流槽主体工程基c本完成，可比原计划D提前实现分洪，预计6月1日至3日进行分洪。北京时间9点，新华网记者与正在唐家山堰塞湖现场的本网军事记者刘昕和王玉山进行电话连线，他们报道了今天首批施工人员撤离现场的相关情况。 问：在今天撤离之前，都做了一些什么样的准备工作？ 王玉山： 现在抢险队伍首先进a行了动员，昨天晚上大家都已经开始准备撤离，对战士进行安全培";
		StringBuffer sb=new StringBuffer(s);
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			for (int j = 0; j < s.length(); j++) {
			  sb.substring(j, j + 1);
			  count++;
			}
		}
		
		long end = System.currentTimeMillis();
		long spend = end - start;
		System.out.println("total time:" + spend + ",avg time:" + ((double) spend / count));

	}

}
