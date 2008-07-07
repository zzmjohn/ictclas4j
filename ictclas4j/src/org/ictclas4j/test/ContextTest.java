package org.ictclas4j.test;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ictclas4j.bean.ContextStat;
import org.ictclas4j.bean.Dictionary;
import org.ictclas4j.bean.PosContext;


public class ContextTest {
	static Logger logger = Logger.getLogger(ContextTest.class);

	public static void main(String[] args) { 
		BasicConfigurator.configure( );
		logger.setLevel(Level.DEBUG);
//		test1();
		test3();

	}

	public static void test1() {
		ContextStat cs = new ContextStat("E:\\document\\NLP\\corpus\\ictclas\\lexical.ctx");
		System.out.println(cs);
	}
	
	public static void test2() {
		PosContext cs = new PosContext("data\\lexical.ctx");
		System.out.println(cs);
	}
	
	public static void test3(){
		Dictionary cs = new Dictionary("E:\\document\\NLP\\corpus\\eve\\bigramDict.dct");
		System.out.println(cs);
	}

}
