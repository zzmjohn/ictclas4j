package com.gftech.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * 和文件相关的常用操作
 * 
 * @author Administrator
 */
public class GFFile {

	/**
	 * 从指定的配置文件中读取指定的属性信息
	 * 
	 * @param fileName
	 *            指定的配置文件名
	 * @param propName
	 *            指定的属性项的名
	 * @return 返回指定的项对应的值
	 */
	public static String getConfig(String fileName, String propName) throws IOException {
		String value = null;

		if (fileName != null && propName != null) {
			StringBuffer sb = readFile(fileName);
			if (sb != null) {
				String[] ps = sb.toString().split("\r\n");
				for (String str : ps) {
					if (str.startsWith("#") || str.startsWith("＃"))
						continue;
					int index = str.indexOf("=");
					if (index > 0 && index < str.length() - 1) {
						String name = str.substring(0, index);
						if (propName.equals(name)) {
							int index2 = str.indexOf("#");
							if (index2 > index)
								value = str.substring(index + 1, index2);
							else
								value = str.substring(index + 1);
							value = value.trim();
							break;
						}
					}
				}
			}
		}
		return value;
	}

	/**
	 * 修改配置文件的属性
	 * 
	 * @param fileName
	 *            配置文件名
	 * @param propName
	 *            属性名
	 * @param newValue
	 *            新值
	 */
	public static boolean setConfig(String fileName, String propName, String newValue) throws FileNotFoundException, IOException {

		if (fileName != null && propName != null && newValue != null) {
			StringBuffer sb = readFile(fileName);
			if (sb != null) {
				String str = null;
				boolean flag = false;
				String[] ps = sb.toString().split("\r\n");
				if (ps != null && ps.length > 0) {
					for (int i = 0; i < ps.length; i++) {
						str = ps[i];
						if (str.startsWith("#") || str.startsWith("＃"))
							continue;
						int index = str.indexOf("=");
						if (index > 0 && index < str.length() - 1) {
							String name = str.substring(0, index);
							if (propName.equals(name)) {
								flag = true;
								ps[i] = str.substring(0, index) + "=" + newValue;
								break;
							}
						}
					}

					String ws = "";
					if (!flag) {
						ws += propName + "=" + newValue + "\r\n";
					}
					for (int i = 0; i < ps.length; i++)
						ws += ps[i] + "\r\n";
					writeTxtFile(fileName, ws, false);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 从配置文件中读取所有的配置信息
	 * 
	 * @param fileName
	 *            配置文件名
	 * @return 把结果存贮在MAP中
	 */
	public static HashMap<String,String> getConfig(String fileName) throws IOException {
		HashMap<String, String> confs = null;

		if (fileName != null) {
			Properties props = new Properties();
			try {
				String name = null;
				FileInputStream fin = null;
				Enumeration propNames = null;
				File file = null;
				String value = null;

				file = new File(fileName);
				if (file.exists()) {

					fin = new FileInputStream(file);
					props.load(fin);
					propNames = props.propertyNames();
					confs = new HashMap<String, String>(1);
					while (propNames.hasMoreElements()) {
						name = (String) propNames.nextElement();
						value = props.getProperty(name);
						confs.put(name, value);

					}
					fin.close();
				}
				props.clear();
			} catch (IOException e) {
				throw new IOException();
			}
		}

		return confs;

	}

	/**
	 * 日志记录.采用直接写入的方案
	 * 
	 * @param fileName
	 *            指定存放日志的文件名，可以是相对路径，也可以是绝对路径
	 * @param msg
	 *            日志内容，可以在msg字符串中间加入"\n"来达到换行的目的
	 * @param append
	 *            写入方式是否是以追加的方式写入,如果不是追加方式，将清除文件中的内容
	 */
	public static boolean log(String fileName, String msg, boolean append) throws IOException {
		FileWriter fw = null;
		PrintWriter out = null;

		if (fileName != null && msg != null)
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

				String[] msgs = msg.split("\n");
				fw = new FileWriter(file, append);
				out = new PrintWriter(fw);
				out.println("------" + new Date() + "-------");
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

	/**
	 * 在指定的文件当中写入指定的信息。
	 * <p>
	 * 如果文件不存在，则自动重新创建一个。
	 * <p>
	 * 如果要写入的数据当中有回车换行符，则在写入的时候进行换行。
	 * <p>
	 * 在写入时，添加时间信息。
	 * 
	 * @param fileName
	 * @param msg
	 * @return
	 */
	public static boolean log(String fileName, String msg) throws IOException {
		try {

			return log(fileName, msg, true);

		} catch (IOException e) {
			throw new IOException();
		}
	}

	/**
	 * 按字符串方式读取全部数据
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文本文件的内容
	 */
	public static String readTxtFile(String fileName) throws IOException {
		String result = null;

		ArrayList<String> list = readTxtFile2(fileName);
		if (list != null) {
			result = "";

			for (String value : list) {
				result += value + "\n";
			}

		}
		return result;
	}

	public static ArrayList<String> readTxtFile2(String fileName) throws IOException {
		ArrayList<String> result = null;
		FileInputStream fin = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		File file = null;
		String value = null;

		if (fileName != null) {
			file = new File(fileName);
			if (file.exists()) {
				result = new ArrayList<String>(); 
				fin = new FileInputStream(file);
				in = new InputStreamReader(fin);
				br = new BufferedReader(in);
				while ((value = br.readLine()) != null) {
					result.add(value);
				}

			}
		}
		return result;
	}

	/**
	 * 读取文本文件的第row行数据，从０开始
	 * <p>
	 * 如果行参数是负值，则指的是倒数第几行
	 * 
	 * @param fileName
	 *            文本文件名
	 * @param row
	 *            第几行
	 * @return 指定行的内容
	 */
	public static String readTxtFile(String fileName, int row) throws IOException {
		String result = null;
		FileInputStream fin = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		File file = null;
		String value = null;
		int i = 0;

		if (fileName != null) {
			file = new File(fileName);
			if (file.exists()) {
				try {
					fin = new FileInputStream(file);
					in = new InputStreamReader(fin);
					br = new BufferedReader(in);
					while ((value = br.readLine()) != null) {
						if (row >= 0 && row == i)
							return value;
						i++;
					}

					if (row < 0) {
						row = i + row;
						if (row < 0)
							return null;
						result = readTxtFile(fileName, row);
					}
				} catch (IOException e) {
					throw new IOException();
				}
			}
		}
		return result;
	}

	/**
	 * 读取二进制文件
	 * 
	 * @param fileName
	 *            文件路径
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBinFile(String fileName) throws IOException {
		byte[] data = null;

		FileInputStream fin = null;
		DataInputStream in = null;
		List<Byte> bl = null;
		File file = null;
		int value = -1;
		if (fileName != null) {
			file = new File(fileName);
			if (file.exists()) {

				try {
					fin = new FileInputStream(file);
					in = new DataInputStream(fin);
					bl = new ArrayList<Byte>();
					while ((value = in.read()) != -1) {
						bl.add((byte) value);
					}
				} catch (IOException e) {
					throw new IOException();
				}

				data = new byte[bl.size()];
				for (int i = 0; i < bl.size(); i++) {
					data[i] = bl.get(i);
				}
			}
		}
		return data;
	}

	/**
	 * 生成二进制文件
	 * 
	 * @param fileName
	 *            文件路径
	 * @param data
	 *            二进制数据流
	 * @return
	 * @throws IOException
	 */
	public static boolean writeBinFile(String fileName, byte[] data) throws IOException {
		FileOutputStream fo = null;
		DataOutputStream out = null;

		if (fileName != null && data != null)
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

				fo = new FileOutputStream(file);
				out = new DataOutputStream(fo);
				out.write(data);
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

	public static File createFile(String fileName) throws IOException {

		if (fileName != null) {
			File file = new File(fileName);
			// 如果文件不存在，就创建一个，如果目录也不存在，也创建一个
			if (!file.exists()) {
				String regex = GFFinal.FILE_SEP;
				ArrayList<String> list = new ArrayList<String>();
				String[] strs = GFString.atomSplit(fileName);
				StringBuffer sb = new StringBuffer();
				for (String str : strs) {
					if (regex.equals(str)) {
						list.add(sb.toString());
						sb.delete(0, sb.capacity());
					} else
						sb.append(str);
				}

				StringBuffer parent = new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					if (i > 0)
						parent.append(GFFinal.FILE_SEP);
					parent.append(list.get(i));

					File fp = new File(parent.toString());
					if (!fp.isDirectory())
						fp.mkdir();
				}
				if (file.createNewFile())
					return file;

			}
			return file;
		}
		return null;
	}

	/**
	 * 写文本文件.如果写入数据中有换行符"\n"的话,自动在写入文件中换中
	 * 
	 * @param fileName
	 *            文件路径
	 * @param txt
	 *            要写入的文件信息
	 * @return
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String fileName, String txt) throws IOException {
		return writeTxtFile(fileName, txt, true);
	}

	/**
	 * 写文本文件.如果写入数据中有换行符"\n"的话,自动在写入文件中换中
	 * 
	 * @param fileName
	 *            文件路径
	 * @param txt
	 *            要写入的文件信息
	 * @param isAppend
	 *            是否以追加的方式写入
	 * @return
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String fileName, String txt, boolean isAppend) throws IOException {
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
				fw = new FileWriter(file, isAppend);
				out = new PrintWriter(fw);
				for (int i = 0; i < msgs.length; i++) {
					out.println(msgs[i]);
				}
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

	public static boolean writeTxtFile(String fileName, ArrayList<String> txtList, boolean isAppend) throws IOException {
		FileWriter fw = null;
		PrintWriter out = null;

		if (fileName != null && txtList != null && txtList.size() > 0)
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

				fw = new FileWriter(file, isAppend);
				for (String txt : txtList) {
					String[] msgs = txt.split("\n");
					out = new PrintWriter(fw);
					for (int i = 0; i < msgs.length; i++) {
						out.println(msgs[i]);
					}
					out.flush();
				}
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

	public static boolean delDir(String dirName) {
		return delDir(dirName, false);
	}

	/**
	 * 删除指定目录下所有名字为dirName的目录
	 * 
	 * @param dirName
	 *            文件夹名称
	 * @param isDelCurDir
	 *            是否删除当前文件夹
	 */
	public static boolean delDir(String dirName, boolean isDelCurDir) {
		boolean result = false;
		if (dirName != null) {
			File file = new File(dirName);
			if (file.exists() && file.isDirectory()) {
				for (File fl : file.listFiles()) {
					if (fl.isFile()) {
						System.out.println(fl.getAbsolutePath());
						fl.delete();
					} else
						delDir(fl.getPath(), true);
				}
				if (isDelCurDir) {
					String[] files = file.list();
					if (files == null || files.length == 0)
						file.delete();
				}

				result = true;
			}
		}

		return result;
	}

	/**
	 * 删除指定名称的文件夹及其下面的所有文件
	 * 
	 * @param dir
	 *            指定要删除目标所在的文件夹
	 * @param delDirName
	 *            要删除的文件夹名称
	 */
	public static void delAppointedDir(String dir, String delDirName) {
		if (dir != null && delDirName != null) {
			File file = new File(dir);
			if (file.isDirectory()) {
				String[] children = file.list();
				for (String child : children) {
					String path = dir + "\\" + child;
					File file2 = new File(path);
					if (file2.isDirectory() && path.toUpperCase().indexOf(delDirName.toUpperCase()) == path.length() - delDirName.length())
						GFFile.delDir(path, true);
					else
						delAppointedDir(path, delDirName);
				}
			}
		}
	}

	public static boolean delFile(String file) {
		if (file != null) {
			File f = new File(file);
			if (f.isFile())
				return f.delete();
		}

		return false;
	}

	public static StringBuffer readFile(String fileName) {
		StringBuffer text = new StringBuffer();
		File file = new File(fileName);
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				int bytesRead;
				byte[] buf = new byte[1024];
				while ((bytesRead = fis.read(buf)) != -1) {
					text.append(new String(buf, 0, bytesRead, "GBK"));
				}
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	public static boolean writeFile(String fileName, String text, boolean isAppend) {
		File file = new File(fileName);
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file, isAppend);
			fos.write(text.getBytes("GBK"));
			fos.flush();
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean copyFile(String from, String to) {

		File fromFile, toFile;
		fromFile = new File(from);
		if (!fromFile.exists()) {
			return false;
		}
		toFile = new File(to);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			toFile.createNewFile();
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
			int bytesRead;
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			while ((bytesRead = fis.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 把一个文件夹下的所有文件copy到指定的另外一个文件夹下
	 * 
	 * @param fromDir
	 * @param toDir
	 * @return
	 */
	public static boolean copyDir(String from, String to) {
		File fromDir = new File(from);
		if (!fromDir.exists()) {
			return false;
		}
		File toDir = new File(to);
		if (!toDir.exists())
			toDir.mkdir();
		for (File file : fromDir.listFiles()) {
			if (file.isDirectory()) {
				copyDir(file.getAbsolutePath(), to + "/" + file.getName());
			} else {
				copyFile(file.getAbsolutePath(), to + "/" + file.getName());
			}
		}
		return true;
	}

	/**
	 * 获取一个目录下的所有文件名,子目录排除在外
	 * 
	 * @param path
	 * @return
	 */
	public static ArrayList<String> getFilesOfDir(String path) {
		ArrayList<String> result = null;
		if (path != null) {
			File file = new File(path);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					result = new ArrayList<String>();
					for (File f : files) {
						if (f.isFile())
							result.add(f.getName());
					}
				}
			}
		}

		return result;
	}

	/**
	 * 列出指定目录下的所有文件和文件夹，包括子目录
	 * 
	 * @param path
	 * @return
	 */
	public static ArrayList<String> listAllFiles(String path) {
		ArrayList<String> result = null;
		if (path != null) {
			File file = new File(path);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					result = new ArrayList<String>();
					for (File f : files) {
						String absPath = f.getAbsolutePath();
						result.add(absPath);
						if (f.isDirectory()) {
							ArrayList<String> subList = listAllFiles(absPath);
							for (int i = 0; subList != null && i < subList.size(); i++) {
								String str = subList.get(i);
								result.add(str);
							}
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * 生成错误报告。
	 * <p>
	 * 生成的错误报告为两种，一种是记录到本地文件当中，一天一个文件
	 * <p>
	 * 第二种为发送错误报告到应用程序管理系统（AMS)
	 * 
	 * @param level
	 *            错误级别
	 *            <p>
	 *            0：一般错误
	 *            <p>
	 *            1：应用程序级错误
	 *            <p>
	 *            2：系统级错误
	 *            <p>
	 *            3：致命错误
	 * @param appName
	 *            应用程序名称
	 * @param className
	 *            类名
	 * @param methodName
	 *            方法名
	 * @param errDesc
	 *            错误描述
	 */
	public static void logErr(int level, String appName, String className, String methodName, String errDesc) {
		String allInfo = null;
		String sysInfo = GFCommon.getSystemInfo();

		if (errDesc != null) {
			allInfo = sysInfo + "\n\n" + "host:" + GFNet.getLocalHost() + "\n" + "appName:" + appName + "\n" + "className:" + className + "\n"
					+ "methodName:" + methodName + "\n" + "errLevel:" + level + "\n" + "errDesc:" + errDesc;

			logFile("err", allInfo);
			// sendToAMS(allInfo);//send to app manage system
		}

	}

	/**
	 * 记录日志信息。
	 * <p>
	 * 默认情况下，把日志写入到当前路径的logs文件夹当中。
	 * <p>
	 * 文件的命名规则为："logs\\"+fileName+date+".txt"
	 * 
	 * @param fileName
	 *            文件名，仅指上面的fileName部分
	 * @param content
	 *            日志内容
	 * @see com.gftech.util.GFDate
	 * @see com.gftech.util.GFFile
	 * @return 写入成功返回true,否则返回false
	 */
	public static boolean logFile(String fileName, String content) {
		boolean result = false;
		String name = null;
		String strDate = null;

		if (fileName != null && content != null) {
			strDate = GFDate.getCurrentDate("yyyymmdd");
			name = "logs\\" + fileName + strDate + ".txt";

			try {
				result = log(name, content);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 判断指定的文件是否存在
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static boolean isExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 取得一个XML文件的根节点按DOM方式进行解析
	 * 
	 * @param xmlFile
	 * @return
	 */
	public static Element getRootElement(String xmlFile) {
		Element root = null;

		// XML数据体。
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(new File(xmlFile));

			// 得到根元素
			root = doc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	/**
	 * 字符串是否指的是文件或路径
	 * 
	 * @param path
	 */
	public static boolean isFilePath(String path) {
		String[] invalidChar = { "\\", "/", "*", ":", "?", "i", "<", ">", "|" };

		if (path != null && path.length() > 0) {
			String[] ss = path.split("\\");
			for (int i = 0; i < path.length(); i++) {
				for (int j = 0; j < invalidChar.length; j++)
					if (ss[i].indexOf(invalidChar[j]) != -1) {
						if (i == 0 && ":".equals(invalidChar[j]) && ss[i].length() == 2 && GFString.isLetter(ss[i].substring(0, 1)))
							break;
						else
							return false;
					}

			}
		}

		return false;
	}

	/**
	 * 得到一个文本文件或者一个目录下所有文本文件的行数
	 * 
	 * @param path
	 *            文件路径
	 * @param fliter
	 *            需要被过滤的文件后缀
	 * @return 总行数
	 */
	public static int getAllFilesLines(String path, String... fliter) throws IOException {
		int result = 0;
		if (path != null) {
			ArrayList<String> allFiles = listAllFiles(path);
			for (int i = 0; allFiles != null && i < allFiles.size(); i++) {
				boolean flag = false;
				String file = allFiles.get(i);
				if (fliter != null) {
					for (String str : fliter) {
						if (file.lastIndexOf(str) == file.length() - str.length()) {
							flag = true;
							break;
						}
					}
				} else
					flag = true;

				if (flag) {
					ArrayList<String> lines = readTxtFile2(file);
					if (lines != null) {
						result += lines.size();
						System.out.println(file + "," + lines.size());
					}
				}
			}
		}
		return result;
	}

	public static boolean serialize(String fileName, Object obj) throws IOException {
		if (fileName != null && obj != null) {
			FileOutputStream fw = null;
			ObjectOutputStream out = null;

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

				fw = new FileOutputStream(fileName);
				out = new ObjectOutputStream(fw);
				out.writeObject(obj);
				out.flush();
				out.close();
				return true;
			} catch (IOException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		}
		return false;

	}

	public static Object diserialize(String fileName) throws IOException {
		Object result = null;
		if (fileName != null) {
			FileInputStream fw = null;
			ObjectInputStream out = null;

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

				fw = new FileInputStream(file);
				out = new ObjectInputStream(fw);
				result = out.readObject();
				out.close();
			} catch (IOException e) {
				throw new IOException();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		}
		return result;

	}
}