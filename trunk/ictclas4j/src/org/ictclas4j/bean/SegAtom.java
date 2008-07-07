package org.ictclas4j.bean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class SegAtom implements Cloneable, Serializable {
	// 关键词
	private String word;

	// 词性列表
	private ArrayList<Pos> posList;

	// 该词的权重
	private double weight;

	private static final long serialVersionUID = 10000L;

	public SegAtom() {

	}

	/**
	 * @param whiteChar
	 *            无需进行分词处理的空白符，比如回车换行、制表符等
	 */
	public SegAtom(String whiteChar) {
		word = whiteChar;
	}

	public SegAtom(String word, int p, int f) {
		this.word = word;
		posList = new ArrayList<Pos>(1);
		Pos pos = new Pos(p, f, false);
		posList.add(pos);
	}

	public SegAtom(String word, ArrayList<Pos> posList) {
		this.word = word;
		this.posList = posList;
	}

	public SegAtom(byte[] bs){
		//TODO
	}
	
	public byte[] getBytes(){
		//TODO:
		return null;
	}
	/**
	 * 从输入流中读取一个SegAtom对象
	 * 
	 * @param in
	 *            输入流
	 * @param offset
	 *            输入流的偏移量，从这里开始读起
	 * @return 读取的字节数
	 * @throws IOException
	 */
	public int read(DataInputStream in, long offset) throws IOException {
		int result = 0;
		if (in != null && offset >= 0) {
			in.skip(offset);
			int posCount = in.readByte();
			result++;
			if (posCount > 0) {
				// pos = new int[posCount];
				// freq = new int[posCount];
				posList = new ArrayList<Pos>(posCount);
				for (int i = 0; i < posCount; i++) {
					// pos[i] = in.readInt();
					// freq[i] = in.readInt();
					Pos pos = new Pos();
					int offset2 = pos.read(in);
					result += offset2;
					posList.add(pos);
				}

				int wordLen = in.readByte();
				result++;
				if (wordLen > 0) {
					byte[] bs = new byte[wordLen];
					in.read(bs);
					word = new String(bs, "GBK");
					result += wordLen;
				}
			}
		}
		return result;
	}

	/**
	 * 把SegAtom对象写入输出流中
	 * 
	 * @param out
	 *            输出流
	 * @return 写入输出流中的字节数
	 * @throws IOException
	 */
	public int write(DataOutputStream out) throws IOException {
		int result = 0;

		if (out != null) {
			byte posCount = posList != null ? (byte) posList.size() : 0;
			out.writeByte(posCount);
			result++;
			for (int i = 0; i < posCount; i++) {
				// out.writeInt(pos[i]);
				// out.writeInt(freq[i]);
				// result += 8;
				Pos pos = posList.get(i);
				int offset2 = pos.write(out);
				result += offset2;
			}
			byte wordLen = word != null ? (byte) word.getBytes().length : 0;
			out.writeByte(wordLen);
			out.write(word.getBytes());
			result += wordLen + 1;

		}
		return result;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void addWord(String word) {
		if (this.word == null)
			this.word = word;
		else
			this.word += word;
	}

	public int getLen() {
		if (word != null)
			return word.length();
		else
			return -1;
	}

	 public Pos getPos(int index) {
		if (posList != null && index >= 0 && index < posList.size()) {
			return posList.get(index);
		}
		return null;
	 }

	public ArrayList<Pos> getPosList() {
		return posList;
	}

	public void setPosList(ArrayList<Pos> posList) {
		this.posList = posList;
	}

	public void addPos(Pos p) {
		if (posList == null)
			posList = new ArrayList<Pos>();
		posList.add(p);
	}

	public boolean hasPos(int p) {
		if (posList != null) {
			for (Pos pos : posList) {
				if (p == pos.getTag()) {
					return true;
				}
			}
		}

		return false;
	}

	public int getFreqByPos(int p) {
		if (posList != null) {
			for (Pos pos : posList) {
				if (p == pos.getTag()) {
					return pos.getFreq();
				}
			}
		}

		return 0;
	}

	public int getTotalFreq() {
		int result = 0;
		if (posList != null) {
			for (Pos pos : posList)
				result += pos.getFreq();
		}
		return result;
	}

	// 词性个数
	public int getPosCount() {
		return posList != null ? posList.size() : 0;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (word != null) {
			sb.append(word);
			if (posList != null) {
				sb.append("[");
				for (int i = 0; i < posList.size(); i++) {
					Pos pos = posList.get(i);
					sb.append(pos.toString());
					if (i != posList.size() - 1)
						sb.append("/");
				}
				sb.append("]");
			}
		}
		return sb.toString();
	}
	
	public void merge(SegAtom sa){
		if(sa!=null){
			String word=sa.getWord();
			if(word!=null && word.equals(this.word)){
				if(posList==null)
					posList=new ArrayList<Pos>();
				ArrayList<Pos> posList2=sa.getPosList();
				if(posList2!=null){
					for(int i=0;i<posList2.size();i++){
						boolean flag=false;
						Pos pos2=posList2.get(i);
						for(Pos pos:posList){
							if(pos.getTag()==pos2.getTag()){
								pos.setFreq(pos2.getFreq());
								pos.setVisible(pos2.isVisible());
								flag=true;
								break;
							}
						}
						
						if(!flag){
							posList.add(pos2);
						}
					}
				}
			}
		}
	}

	public SegAtom clone() throws CloneNotSupportedException {
		return (SegAtom) super.clone();
	}
}
