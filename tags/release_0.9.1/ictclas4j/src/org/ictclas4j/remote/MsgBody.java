package org.ictclas4j.remote;

public interface MsgBody {
	public byte[] getBytes();

	public void parseBytes(byte[] bs);
}
