package org.ictclas4j.remote;

import com.gftech.util.GFCommon;


public class MsgHead {
	private int totalLen;

	private byte cmdID;

	public byte getCmdID() {
		return cmdID;
	}

	public void setCmdID(byte cmdID) {
		this.cmdID = cmdID;
	}

	public int getTotalLen() {
		return totalLen;
	}

	public void setTotalLen(int totalLen) {
		this.totalLen = totalLen;
	} 

	public byte[] getBytes(){
		byte[] bs=new byte[5];
		GFCommon.bytesCopy(bs,GFCommon.int2bytes(totalLen),0,4);
		bs[4]=cmdID;
		return bs;
	}
}
