package com.gftech.util;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * 和网络相关的操作
 * 
 * @author sinboy
 * @version 1.0 2005-9-27
 * 
 */
final public class GFNet {

	/**
	 * 得到本地主机的IP地址
	 * 
	 * @return
	 */
	public static String getLocalHost() {
		String result = null;

		try {
			result = InetAddress.getLocalHost().getHostName() + "/" + InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送数据，最好使用Buffer进行包装
	 * 
	 * @param out
	 *            输出流
	 * @param b
	 *            输出的字节数组
	 * @throws IOException
	 */
	public static void send(OutputStream out, byte[] b) throws IOException {
		if (out == null)
			throw new IOException();
		if (b != null) {
			try {
				if (b != null) {
					out.write(b);
					out.flush();
				}
			} catch (IOException e) {
				throw e;
			}
		}
	}

	/**
	 * 接收指定长度的原始字节数据。
	 * 
	 * @param in
	 *            输入流，最好用Buffer进行包装
	 * @param len
	 *            接收的长度
	 * @return
	 * @throws IOException
	 */
	public static byte[] receive(InputStream in, int len) throws IOException {
		byte[] result = null;

		if (in == null)
			throw new IOException();
		if (len > 0) {
			result = new byte[len];
			in.read(result);
		}
		return result;
	}

	/**
	 * 从输入流中读取数据直到结束符为止
	 * 
	 * @param in
	 *            输入流
	 * @return
	 * @throws IOException
	 */
	public static byte[] receive(InputStream in) throws IOException {
		byte[] result = null;

		if (in == null)
			throw new IOException();
		int b;
		ArrayList<Byte> temp = new ArrayList<Byte>();
		while ((b = in.read()) != -1) {
			temp.add((byte) b);
		}

		if (temp.size() > 0) {
			result = new byte[temp.size()];
			for (int i = 0; i < temp.size(); i++)
				result[i] = temp.get(i);
		}

		return result;
	}

	 
	public static final short readUInt8(InputStream in) throws IOException {
		int ch = in.read();
		if (ch < 0) {
			throw new EOFException();
		}
		return (short) (ch & 0xff);
	}

	public static final int readUInt16(InputStream in) throws IOException {
		return (readUInt8(in) + (readUInt8(in) << 8)) & 0xffff;
	}

	public static final int readInt32(InputStream in) throws IOException {
		return readInt32(in, false);
	}

	public static final int readInt32(InputStream in, boolean isHighFirst) throws IOException {
		if (isHighFirst)
			return (readUInt8(in) << 24) + (readUInt8(in) << 16) + (readUInt8(in) << 8) + readUInt8(in);
		else
			return readUInt8(in) + (readUInt8(in) << 8) + (readUInt8(in) << 16) + (readUInt8(in) << 24);
	}

	public static byte[] readBytes(DataInputStream in, int len) throws IOException {
		if (in != null && len > 0) {
			byte[] b = new byte[len];

			for (int i = 0; i < len; i++)
				b[i] = in.readByte();

			return b;
		}

		return null;
	}
	public static final void writeInt8(OutputStream out, int value) throws IOException { 
		  out.write(value);
	}
	
	
	public static final boolean writeInt32(OutputStream out, int value) throws IOException {

		return writeInt32(out, value, false);
	}

	public static final boolean writeInt32(OutputStream out, int value, boolean isHighFirst) throws IOException {
		boolean result = false;

		if (out != null) {
			byte[] b = GFCommon.int2bytes(value, isHighFirst);
			out.write(b);
		}
		return result;
	}

	public static int readInt(DataInputStream in, boolean isBig) throws IOException {
		if (in != null) {
			return in.readInt();
		} else
			throw new IOException();

	}

}
