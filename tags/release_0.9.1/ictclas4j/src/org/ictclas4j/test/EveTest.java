package org.ictclas4j.test;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ictclas4j.bean.DictLib;
import org.ictclas4j.bean.SegResult;
import org.ictclas4j.run.Config;
import org.ictclas4j.segment.Segment;

import com.gftech.util.GFFile;
import com.gftech.util.GFFinal;

public class EveTest {

	 
	static Logger logger = Logger.getLogger(EveTest.class);
	public static void main(String[] args) {
		PropertyConfigurator.configure(Config.LOG4J_CONF); 
		try {
			long bytes = 0;
			int segPathCount = 1;
			int forCount = 1;
			DictLib dictLib = new DictLib();
			Segment seg = new Segment(dictLib, segPathCount);
			ArrayList<String> testCases = GFFile.readTxtFile2("test" + GFFinal.FILE_SEP + "case1.txt");

			long times = System.currentTimeMillis();
			for (int i = 0; i < forCount; i++) {
				for (String src : testCases) {
					SegResult sr = seg.split(src);
					bytes += src.getBytes().length;
					//打印输出会极大的影响速度
//					System.out.println(sr);
				}
			}

			times = System.currentTimeMillis() - times;
			System.out.println("times:" + times + "ms" + ",bytes:" + bytes + ",avg:" + (bytes / times) + "kb/s");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
