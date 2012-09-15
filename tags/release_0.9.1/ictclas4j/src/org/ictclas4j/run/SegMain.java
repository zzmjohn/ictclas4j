package org.ictclas4j.run;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ictclas4j.bean.DictLib;

public class SegMain {
	// ´Êµä¿â
	public static DictLib dictLib;

	static Logger logger = Logger.getLogger(SegMain.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure(Config.LOG4J_CONF);
		dictLib = new DictLib();
		Config conf = new Config();
		if (conf.isShowFrm())
			initFrm();

		ServerAdapter server = new ServerAdapter(dictLib);
		server.run();
	}

	public static void initFrm() {
		FrmMain frm = FrmMain.getInstance();
		JFrame jf = frm.getJFrame();
		jf.setSize(800, 600);
		jf.setVisible(true);
	}

}
