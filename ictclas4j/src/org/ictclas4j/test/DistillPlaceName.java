package org.ictclas4j.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.gftech.util.GFFile;
import com.gftech.util.GFString;

public class DistillPlaceName {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		t5();

	}

	private static void t1() {
		try {
			ArrayList<String> list = GFFile.readTxtFile2("test\\case1.txt");
			for (String str : list) {
				System.out.println(str);
				String[] atoms = GFString.atomSplit(str);
				StringBuffer sb = new StringBuffer();
				for (String atom : atoms) {
					if (atom.equals("省")) {
						sb.append(atom);
						sb.append("/1");
						System.out.println(sb.toString());
						sb.delete(0, sb.capacity());
					} else if (atom.equals("市")) {
						sb.append(atom);
						sb.append("/2");
						System.out.println(sb.toString());
						sb.delete(0, sb.capacity());
					} else if (atom.equals("县") || atom.equals("区")) {
						sb.append(atom);
						sb.append("/3");
						System.out.println(sb.toString());
						sb.delete(0, sb.capacity());
					} else if (atom.equals("乡") || atom.equals("镇")) {
						sb.append(atom);
						sb.append("/4");
						System.out.println(sb.toString());
						sb.delete(0, sb.capacity());
					} else if (atom.equals("村")) {
						sb.append(atom);
						sb.append("/5");
						System.out.println(sb.toString());
						sb.delete(0, sb.capacity());
					} else
						sb.append(atom);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void t2() {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			ArrayList<String> list = GFFile.readTxtFile2("test\\p1.txt");
			ArrayList<String> list2 = GFFile.readTxtFile2("test\\p2.txt");
			for (String str : list2)
				list.add(str);
			for (String str : list) {
				int index = str.indexOf("/");
				if (index > 0) {
					String p1 = str.substring(0, index);
					String p2 = str.substring(index + 1);
					String value = map.get(p1);
					if (value == null) {
						map.put(p1, p2);
					}
				}
			}

			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String p1 = (String) itr.next();
				String p2 = map.get(p1);
				System.out.println(p1 + "/" + p2);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void t3() {
		try {
			ArrayList<String> list = GFFile.readTxtFile2("test\\case1.txt");
			for (String str : list) {
				if (str.indexOf("街道") > 0) {
					// System.out.println(str);
					String[] atoms = GFString.atomSplit(str);
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < atoms.length; i++) {
						String atom = atoms[i];
						if (atom.equals("省")) {
							sb.append(atom);
							sb.append("/1");
							sb.delete(0, sb.capacity());
						} else if (atom.equals("市")) {
							sb.append(atom);
							sb.append("/2");
							sb.delete(0, sb.capacity());
						} else if (atom.equals("县") || atom.equals("区")) {
							sb.append(atom);
							sb.append("/3");
							sb.delete(0, sb.capacity());
						} else if (atom.equals("街") && atoms[i + 1].equals("道")) {
							sb.append(atom);
							sb.append(atoms[i + 1]);
							sb.append("/4");
							System.out.println(sb.toString());
							sb.delete(0, sb.capacity());
						}

						else
							sb.append(atom);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 分拆后缀,并添加词性
	private static void t4() {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			ArrayList<String> list = GFFile.readTxtFile2("test\\zj_area_name.txt");
			for (String str : list) {
				int index = str.indexOf("/");
				if (index > 0) {
					String p1 = str.substring(0, index);
					String p2 = str.substring(index + 1);
					if (p1.length() > 3 || p1.endsWith("省") || p1.endsWith("市") || (p1.endsWith("县") && p1.length() > 2)) {
						String p11 = p1.endsWith("街道") ? p1.substring(0, p1.length() - 2) : p1.substring(0, p1.length() - 1);
						put(map, p11, p2);
					}
					put(map, p1, p2);

				}
			}

			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String p1 = (String) itr.next();
				String p2 = map.get(p1);
				p2 = "ns" + p2;
				System.out.println(p1 + "/" + p2);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean put(HashMap<String, String> map, String key, String value) {
		if (map != null && key != null) {
			String v = map.get(key);
			if (v == null) {
				map.put(key, value);
				return true;
			}
		}
		return false;
	}

	// 把以“街”结束的去掉
	private static void t5() {
		try {
			ArrayList<String> list0 = new ArrayList<String>();
			ArrayList<String> list = GFFile.readTxtFile2("test\\zj_area_name.txt");
			for (String str : list) {
				int index = str.indexOf("街/");
				if (index > 1) {
					if (index > 2) {
						str = str.replaceAll("街/", "/");
						list0.add(str);
					}

				} else
					list0.add(str);
			}

			GFFile.writeTxtFile("test\\zj_area_name2.txt", list0, false);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
