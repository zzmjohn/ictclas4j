package org.ictclas4j.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.gftech.util.GFFile;
import com.gftech.util.GFString;

public class ImportPinyin {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<String, String> pinyinDict = new HashMap<String, String>();
		String[][] dict=new String[6821][3];
		try {
			String pinyin = GFFile.readTxtFile("data\\pronun.txt");

			String one = null;
			String[] ones = null;
			String[] cs2 = pinyin.split("\n");
			System.out.println("读取拼音文件成功,共" + cs2.length + "个汉字");

			for (int i = 0,j=0; i < cs2.length; i++) {
				one = GFString.removeSpace(cs2[i].trim());
				if (one != null) {
					ones = one.split("#");
					if (ones != null && ones.length == 2)
						if (ones[0] != null && ones[0].length() > 0 && ones[1] != null && ones[1].length() > 0) {
							pinyinDict.put(ones[0], ones[1].toUpperCase());
							dict[j][0]=ones[0];
							dict[j][1]=ones[1].toUpperCase();
							j++;
			
						}
				}
			}

			System.out.println("初始化拼音字典表成功");
			ArrayList<String> rslist=new ArrayList<String>();
			for(int i=0;i<dict.length;i++){
				for(int j=i+1;j<dict.length;j++){
					if(i==4204)
						System.out.println("tark");
					if(  dict[i][1].equals(dict[j][1])){
						if(dict[i][2]==null)
							dict[i][2]=dict[j][0];
						else
							dict[i][2]+=dict[j][0];
						if(dict[j][2]==null)
							dict[j][2]=dict[i][0];
						else
							dict[j][2]+=dict[i][0];
					}   	 
				}
				String rs=dict[i][0]+"#"+dict[i][1]+"#"+dict[i][2];
				System.out.println("dict["+i+"]:"+rs);
				rslist.add(rs);
			}
			GFFile.writeTxtFile("data\\pinyin.txt", rslist,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
