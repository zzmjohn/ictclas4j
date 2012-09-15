package org.ictclas4j.run;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.ictclas4j.bean.DictLib;

/**
 * 服务器适配器，接收 1.分词处理请求 2.索引创建请求 3.索引删除请求 4.索引结果查询请求
 * 
 * @author sinboy
 * 
 */
public class ServerAdapter implements Runnable {

	private DictLib dictLib;

	static Logger logger = Logger.getLogger(ServerAdapter.class);

	public ServerAdapter(DictLib dictLib) {
		this.dictLib = dictLib;  
		manager();
	} 

	public void run() {
		try {
			Socket client = null;
			int listenPort = Config.listen_port();
			ServerSocket ss = new ServerSocket(listenPort);
			logger.info("侦听" + listenPort + "端口，等待客户端的连接...");
			while (true) {
				client = ss.accept();
				logger.info("接收到的连接" + client.toString());
				SegmentService service = new SegmentService(dictLib, client);
				service.start();
			}
		} catch (IOException e) {
			logger.error("无法接收来自客户端的连接", e);
		}
	}

	// 每天晚上半夜时分自动运行
	private void manager() {
		Timer timer = new Timer();
		TimerTask tt2 = new TimerTask() {
			public void run() {
				loadNewWords();
			}
		};

		Date date = Config.update_keys_time();
		timer.schedule(tt2, date, Config.update_keys_interval() * 60000);

	}

	private void loadNewWords() {
		//TODO
//		String newBiWord = "data" + GFFinal.FILE_SEP + "new_bi_word.txt";
//		NewWordsUtil.loadNewBiWords(dictLib, newBiWord);
//		String newWord = "data" + GFFinal.FILE_SEP + "new_word.txt";
//		NewWordsUtil.loadNewWords(dictLib, newWord);
	}

}
