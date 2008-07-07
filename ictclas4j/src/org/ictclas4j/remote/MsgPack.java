package org.ictclas4j.remote;

import com.gftech.util.GFCommon;

/**
 * 消息包，包括消息头、消息体
 * 
 * @author sinboy
 * 
 */
public class MsgPack {
	private MsgHead head;

	private MsgBody body;

	public MsgPack() {

	}

	public MsgPack(byte cmdID, MsgBody body) {
		this.body = body;
		MsgHead head = new MsgHead();
		byte[] bs = body.getBytes();
		if (bs != null) {
			int len = 5 + bs.length;
			head.setCmdID(cmdID);
			head.setTotalLen(len);
			this.head = head;
		}
	}

	public MsgBody getBody() {
		return body;
	}

	public void setBody(MsgBody body) {
		this.body = body;
	}

	public MsgHead getHead() {
		return head;
	}

	public void setHead(MsgHead head) {
		this.head = head;
	}

	public byte[] getBytes() {
		byte[] bs = null;
		if (head != null && body != null) {
			int len = head.getTotalLen();
			byte cmdID = head.getCmdID();
			byte[] bb = body.getBytes();
			bs = new byte[len];
			GFCommon.bytesCopy(bs, GFCommon.int2bytes(len), 0, 4);
			bs[4] = cmdID;
			GFCommon.bytesCopy(bs, bb, 5, bb.length);
		}

		return bs;
	}

}
