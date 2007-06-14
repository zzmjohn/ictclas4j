
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Enumeration;
import java.util.Properties;

public class PingTest {
	static int LINE_COUNT = 10;

	static int SLEEP_TIME = 1200000;

	static String IP = "60.191.49.22";

	public static void myinit() {

		String temp = null;

		try {
			temp = getConfig("config.properties", "lineCount");
			if (temp != null)
				LINE_COUNT = Integer.parseInt(temp);

			temp = getConfig("config.properties", "sleepTime");
			if (temp != null)
				SLEEP_TIME = Integer.parseInt(temp) * 60000;

			temp = getConfig("config.properties", "ip");
			if (temp != null)
				IP = temp;
		} catch (IOException e) {

			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		timer();
	}

	private static void timer() {
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					myinit();
					ping();
					try {
						sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		thread.start();
	}

	private static void ping() {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		String line = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		String rs = "";
		int i = 0;
		try {
			process = runtime.exec("ping  " + IP + " -t");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String bstr = "---------------开始,每隔" + SLEEP_TIME / 60000
					+ "分钟测试一次--------------\n";
			System.out.println(bstr);
			rs = bstr;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					String lstr = line + "   --" + new java.util.Date();
					System.out.println(lstr);
					System.out.flush();
					rs += lstr + "\n";
					i++;
					if (i >= LINE_COUNT)
						break;
				}
			}

			String estr = "\n*******************结束*****************************\n\n\n";
			System.out.println(estr);
			rs += estr;
			is.close();
			isr.close();
			br.close();

			writeTxtFile("logs\\"
					+ new Date(System.currentTimeMillis()).toString() + ".txt",
					rs);
		} catch (IOException e) {
			e.printStackTrace();
			runtime.exit(1);
		}
	}

	public static boolean writeTxtFile(String fileName, String txt)
			throws IOException {
		FileWriter fw = null;
		PrintWriter out = null;

		if (fileName != null && txt != null)
			try {
				String parent;
				File fp;

				File file = new File(fileName);
				// 如果文件不存在，就创建一个，如果目录也不存在，也创建一个
				if (!file.exists()) {
					parent = file.getParent();
					if (parent != null) {
						fp = new File(parent);

						if (!fp.isDirectory())
							fp.mkdirs();
					}

				}

				String[] msgs = txt.split("\n");
				fw = new FileWriter(file, true);
				out = new PrintWriter(fw);
				for (int i = 0; i < msgs.length; i++) {
					out.println(msgs[i]);
				}
				out.println("");
				out.flush();
				out.close();
				return true;
			} catch (IOException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		return false;
	}

	public static String getConfig(String fileName, String propName)
			throws IOException {
		String value = null;

		if (fileName != null && propName != null) {
			Properties props = new Properties();
			try {
				String name = null;
				FileInputStream fin = null;
				Enumeration propNames = null;
				File file = null;

				file = new File(fileName);
				if (file.exists()) {

					fin = new FileInputStream(file);
					props.load(fin);
					propNames = props.propertyNames();

					while (propNames.hasMoreElements()) {
						name = (String) propNames.nextElement();

						// 得到属性名对应的属性值
						if (name.equalsIgnoreCase(propName)) {
							value = props.getProperty(name);
							break;
						}
					}

					fin.close();
				}
				props.clear();
			} // end try
			catch (IOException e) {
				throw new IOException();
			}
		}
		return value;
	}

}
