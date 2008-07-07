package org.ictclas4j.run;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.gftech.util.GFDate;
import com.gftech.util.GFFile;
import com.gftech.util.GFFinal;
import com.gftech.util.GFString;

public class Config {
	public static final String LOG4J_CONF = "conf"+GFFinal.FILE_SEP+"log4j.properties";

	public static final String confFile = "conf"+GFFinal.FILE_SEP+"config.properties";

	public static final String dbFile = "conf"+GFFinal.FILE_SEP+"database.properties";

	static Logger logger = Logger.getLogger(Config.class);

	public static void main(String[] args) {
		System.out.println(valid_percent());
	}

	public static int listen_port() {
		int port = 1990;
		if (confFile != null) {
			try {
				String sport = GFFile.getConfig(confFile, "listen_port");
				port = GFString.cint(sport);
			} catch (IOException e) {
				 logger.error("load listen port",e);
			}
		}

		return port;
	}

	public static String db_driver() {
		if (dbFile != null) {
			try {
				return GFFile.getConfig(dbFile, "driver_oracle");
			} catch (IOException e) {
				 logger.error("load database driver",e);
			}
		}
		return null;
	}

	public static String db5_url() {
		if (dbFile != null) {
			try {
				return GFFile.getConfig(dbFile, "db5_url");
			} catch (IOException e) {
				 logger.error("load db5 url",e);
			}
		}
		return null;
	}

	public static String db5_user() {
		if (dbFile != null) {
			try {
				return GFFile.getConfig(dbFile, "db5_user");
			} catch (IOException e) {
				 logger.error("load db5 user",e);
			}
		}
		return null;
	}

	public static String db5_pwd() {
		if (dbFile != null) {
			try {
				return GFFile.getConfig(dbFile, "db5_pwd");
			} catch (IOException e) {
				 logger.error("load db5 pwd",e);
			}
		}
		return null;
	}
	
	public static String pkg_name() {
		if (confFile != null) {
			try {
				return GFFile.getConfig(confFile, "pkg_name");
			} catch (IOException e) {
				 logger.error("load package name",e);
			}
		}
		return null;
	}

	// 判定是否是有效信息的比例系数
	public static double valid_percent() {
		if (confFile != null) {
			try {
				String temp = GFFile.getConfig(confFile, "valid_percent");
				return Double.parseDouble(temp);
			} catch (IOException e) {
				 logger.error("load valid percent",e);
			} catch (NumberFormatException e) {
				 logger.error("load valid percent",e);
			}
		}
		return 0;
	}

	public static Date update_keys_time() {
		Date update_keys_date = null;

		// set timer to load keys from database
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		int runDate = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, runDate + 1);
		

		try {
			String temp = GFFile.getConfig(confFile, "update_keys_time");
			Calendar uc = GFDate.cdate(temp);

			// 如果当前的运行时间比指定时间早，则把运行时间设为当天（默认为下一天）
			if (uc.get(Calendar.HOUR_OF_DAY) > cal.get(Calendar.HOUR_OF_DAY)||uc.get(Calendar.HOUR_OF_DAY) == cal.get(Calendar.HOUR_OF_DAY)&&
					uc.get(Calendar.MINUTE)>cal.get(Calendar.MINUTE))
				cal.set(Calendar.DAY_OF_MONTH, runDate);
			cal.set(Calendar.HOUR_OF_DAY, uc.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, uc.get(Calendar.MINUTE));
			cal.set(Calendar.SECOND, uc.get(Calendar.SECOND));
			logger.info("update_keys_time:" + GFDate.cdate(cal, "yyyy-mm-dd hh24:mi:ss"));
		} catch (IOException e) {
			logger.error("load update keys time",e);
			cal.set(Calendar.HOUR_OF_DAY, 2);
		} catch (NullPointerException e) {
			logger.error("load update keys time",e);
			cal.set(Calendar.HOUR_OF_DAY, 2);
		}

		update_keys_date = new Date(cal.getTimeInMillis());
		return update_keys_date;
	}

	public static int update_keys_interval() {
		int update_keys_interval = 1440;

		try {
			int interval = GFString.cint(GFFile.getConfig(confFile, "update_keys_interval"));
			if (update_keys_interval > 0)
				update_keys_interval = interval;
		} catch (IOException e) {
			logger.error("update keys interval:", e);
		}

		return update_keys_interval;
	}
	
	public boolean isShowFrm(){
		boolean result = false;
		try {
			String temp = GFFile.getConfig(confFile, "is_show_frm");
			if("1".equals(temp))
					result=true;
		} catch (IOException e) { 
			logger.error("load is show frame ",e);
		}

		return result;
	}
}
