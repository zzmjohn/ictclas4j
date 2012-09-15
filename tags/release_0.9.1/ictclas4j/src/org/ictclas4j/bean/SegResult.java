package org.ictclas4j.bean;

import org.ictclas4j.remote.MsgBody;

import com.gftech.util.GFCommon;

/**
 * 分词结果，包括分词后的词性、词频
 * 
 * @author sinboy
 * 
 */
public class SegResult implements MsgBody {

	private String rawContent;// 原始内容

	private SegAtom[] atoms; // 经过分词后的原子单元

	public SegResult() {

	}

	public SegResult(byte[] bs) {
		parseBytes(bs);
	}

	public void parseBytes(byte[] bs) {
		if (bs != null && bs.length > 4) {
			int from = 0;
			int size = GFCommon.bytes2int(GFCommon.bytesCopy(bs, from, 4));
			from += 4;
			atoms = new SegAtom[size];
			for (int i = 0; i < size; i++) {
				byte len = bs[from++];
				byte[] ab = GFCommon.bytesCopy(bs, from, len);
				atoms[i] = new SegAtom(ab);
			}
		}
	}

	public byte[] getBytes() {
		byte[] result = null;
		if (atoms != null) {
			int from = 0;
			int size = atoms.length;
			byte[] temp = new byte[20 * size + 4];
			GFCommon.bytesCopy(temp, GFCommon.int2bytes(size), from, 4);
			from += 4;
			for (int i = 0; i < atoms.length; i++) {
				byte[] ab = atoms[i].getBytes();
				temp[from++] = (byte) ab.length;
				GFCommon.bytesCopy(temp, GFCommon.int2bytes(size), from, ab.length);
				from += ab.length;
			}

			result = new byte[from];
			GFCommon.bytesCopy(result, temp, 0, from);
		}
		return result;
	}

	public SegAtom[] getAtoms() {
		return atoms;
	}

	public void setAtoms(SegAtom[] atoms) {
		this.atoms = atoms;
	}

	public String getRawContent() {
		return rawContent;
	}

	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		if (atoms != null) {
			for (int i = 0; i < atoms.length; i++) {
				SegAtom atom = atoms[i];
				if(atom!=null &&("始##始".equals(atom.getWord())||"末##末".equals(atom.getWord())))
					continue;
				sb.append(atom.toString());
				if (i < atoms.length - 1)
					sb.append(" ");
			}
		}

		return sb.toString();
	}

	public void merge(SegResult sr) {
		if (sr != null) {
			SegAtom[] atoms2 = sr.getAtoms();
			if (atoms2.length > 0) {
				if (rawContent == null)
					rawContent = sr.getRawContent();
				else if(sr.getRawContent()!=null)
					rawContent += sr.getRawContent();

				int size = atoms2.length;
				if (atoms == null)
					atoms = atoms2;
				else {
					int i = 0;
					size += atoms.length;
					SegAtom[] mergeAtoms = new SegAtom[size];
					for (; i < atoms.length; i++)
						mergeAtoms[i] = atoms[i];
					for (int j = 0; j < atoms2.length; j++, i++)
						mergeAtoms[i] = atoms2[j];
					atoms=mergeAtoms;
				}
			}

		}
	}

}
