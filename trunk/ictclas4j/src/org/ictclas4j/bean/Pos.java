package org.ictclas4j.bean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * 词性
 * 
 * @author sinboy
 * @since 2008.6.4
 * 
 */
public class Pos implements Cloneable, Serializable {
	private int tag;// 词性，用4个字节的整数表示，第个字节代表一个ASCII码的字符，见POSTag

	private int freq;// 词频

	private boolean isVisible;// 如果最终的词性标记不是该词性，是否强制可见。

	private static final long serialVersionUID = 10000L;

	public Pos() {

	}

	public Pos(int tag, int freq, boolean isVisible) {
		this.tag = tag;
		this.freq = freq;
		this.isVisible = isVisible;
	}

	public int read(DataInputStream in) throws IOException {
		int offset = 0;
		if (in != null) {
			tag = in.readInt();
			freq = in.readInt();
			offset += 8;
		}
		return offset;
	}

	public int write(DataOutputStream out) throws IOException {
		int offset = 0;
		if (out != null) {
			out.writeInt(tag);
			out.writeInt(freq);
			offset += 8;
		}
		return offset;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(POSTag.int2str(tag)); 
		return sb.toString();
	}
	
	public String toString2() {
		StringBuffer sb = new StringBuffer();
		sb.append(POSTag.int2str(tag));
		sb.append(",").append(freq);
		sb.append(",").append(isVisible ? 1 : 0);
		return sb.toString();
	}


	public Pos clone() throws CloneNotSupportedException {
		return (Pos) super.clone();
	}
}
