package org.ictclas4j.test;

import java.io.IOException;

import com.gftech.util.GFFile;
import com.gftech.util.GFString;

public class ExportGBK {

	 
	public static void main(String[] args) { 
		String rs="";
		for(int i=0x8140,j=1;i<=0xfefe;i++,j++)
		{
			
			byte[] bs=new byte[2];
			bs[0]=(byte)((i&0xFF00)>>8);
			bs[1]=(byte)(i&0xFF);
			rs+="0x"+GFString.bytes2hex(bs)+":"+new String(bs)+" ";
			if(j%10==0){
				try {
					System.out.println(rs);
					GFFile.writeTxtFile("gbk.txt",rs,true);
					rs="";
				} catch (IOException e) { 
					e.printStackTrace();
				}
			}
		}
	}

}
