package org.ictclas4j.run;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.ictclas4j.bean.DictLib;
import org.ictclas4j.bean.SegResult;
import org.ictclas4j.remote.CmdID;
import org.ictclas4j.remote.MsgPack;
import org.ictclas4j.remote.SegRequest;
import org.ictclas4j.segment.Segment;


/**
 * 处理远程分词请求服务
 * 
 * @author sinboy
 * @since 2007.6.28
 * @updated 2008.7.2
 * 
 */
public class SegmentService extends Thread {

	private DictLib dictLib;

	private Socket client;

	private DataInputStream in;

	private DataOutputStream out;

	static Logger logger = Logger.getLogger(SegmentService.class);

	public SegmentService(DictLib dictLib, Socket client) {
		this.dictLib = dictLib;
		this.client = client;
		if (this.client != null)
			try {
				client.setSoTimeout(10 * 60000);// 10分钟没有响应则视为超时
				out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
				in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
			} catch (IOException e) {
				logger.error(e);
				close();
			}
	}

	public void run() {
		while (true) {
			try {
				byte[] bs = null;
				int len = in.readInt();
				if (len > 5) {
					byte cmdID = in.readByte();
					switch (cmdID) {
					case CmdID.SEG_REQUEST:
						bs = new byte[len - 5];
						in.read(bs);
						segRequest(bs);
						break;

					}
				}
			} catch (IOException e) {
				logger.error("conn with client is invalid", e);
				close();
				break;
			}
		}
	}

	private void segRequest(byte[] bs) {
		if (bs != null && out != null) {
			SegRequest req = new SegRequest(bs);
			String content = req.getContent();
			SegResult sr = getSeg(content);

			try {
				MsgPack pack = new MsgPack(CmdID.SEG_RESPONSE, sr);
				out.write(pack.getBytes());
				out.flush();
			} catch (IOException e) {
				logger.error("segment request", e);
				close();
			}

		}
	}

	private SegResult getSeg(String content) {
		SegResult result = null;

		// first, read from cache
		result = dictLib.getCachedSeg(content);
		// then,segment factly
		if (result == null) {
			Segment seg = new Segment(dictLib, 1);
			seg.setOutputMidResult(true);
			seg.setRecogniseUnknown(true);
			result = seg.split(content);

			// cache it
			if (result != null) {
				dictLib.addCachedSeg(content, result);
			}
		}
		return result;
	}

	private void close() {
		try {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (client != null)
				client.close();
		} catch (IOException e) {
			logger.error("Can't close the input stream or output stream or connection", e);
		}
	}
}
