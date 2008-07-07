package org.ictclas4j.bean;

import java.io.Serializable;


/**
 * 邻接词性标记
 * 
 * @author sinboy
 * @since 2007.5.22
 * 
 */
public class AdjoiningPos  implements Cloneable, Serializable{ 
	private Pos pos;// 词性标记 

	private double value; // 邻接值

	private int prev;// 前一个词的N个词性中和该词性最匹配的那一个（下标位置）

	private boolean isBest;
 
	private static final long serialVersionUID = 10000L;
	public AdjoiningPos() {

	}
	
	public AdjoiningPos(int tag,double value) {
      this.pos=new Pos();
      this.pos.setTag(tag);
      this.value=value;
	}
	

	public AdjoiningPos(Pos pos, double value) {
		this.pos = pos;
		this.value = value;
	}

 
	public Pos getPos() {
		return pos;
	}

	public void setPos(Pos pos) {
		this.pos = pos;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getPrev() {
		return prev;
	}

	public void setPrev(int prevPos) {
		this.prev = prevPos;
	}

	public boolean isBest() {
		return isBest;
	}

	public void setBest(boolean isBest) {
		this.isBest = isBest;
	}

	 

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("pos:").append(pos);
		sb.append(",values:").append(value);
		sb.append(",isBest:").append(isBest);
		return sb.toString();

	}
	
	public AdjoiningPos clone() throws CloneNotSupportedException {
		return (AdjoiningPos) super.clone();

	}

}
