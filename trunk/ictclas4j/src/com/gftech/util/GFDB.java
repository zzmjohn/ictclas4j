/*
 * Created on 2004-5-24
 *
 * 和数据库相关的一些类
 */
package com.gftech.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 
 * 访问数据库的公共接口。 此接口封装了访问数据库的一切底层操作，使用此接口之后不需要客户端程序
 * 关心访问数据库底层的任何细节，客户端程序只需告诉是要执行SELECT或者UPDATE动作即可。
 * 此接口实现了连接池的管理，可以在访问频繁的时候自动增加连接数目，同时也可以在空闲的时
 * 把多余的连接自动关闭掉。并且可以做到在和数据库的连接被重置之后，自动和数据库建立新的 连接。
 * 
 * （说明：此版本增加了和数据库的自动连接）
 * 
 * @author sinboy
 * @vesion 2.0
 */

public class GFDB {
	private String dbName;

	private ArrayList<GFConn> idleConnPool;// 空闲池

	private ArrayList<GFConn> usingConnPool;// 使用池

	private final int maxConns = 150;// 最大连接数目

	private final int maxUserCount = 149;// 每个连接允许的最大用户数目

	private final int timeout = 60000;// 连接的最大空闲时间

	private final int waitTime = 30000;// 用户的最大等待时间

	private String dbUrl = null;// 连接地址

	private String dbDriver = null;// 数据库驱动

	private String dbUser = null;// 登陆用户名

	private String dbPwd = null;// 登陆密码

	private ThreadGroup group = null;

	public GFDB(String dbName, String driver, String url, String user, String pwd) {

		this.dbName = dbName;
		dbDriver = driver;
		dbUrl = url;
		dbUser = user;
		dbPwd = pwd;
		if (driver != null && url != null)
			init();
	}

	private void init() {

		Connection conn = null;
		GFConn _conn = null;

		// init the connection pool
		idleConnPool = new ArrayList<GFConn>(0);
		usingConnPool = new ArrayList<GFConn>(0);

		conn = buildConn();
		if (conn != null) {
			_conn = new GFConn();
			_conn.setConn(conn);
			idleConnPool.add(_conn);
		} else {

			try {
				System.err.println("\n\n和数据库的连接被重置，试图重新建立连接。。。");
				Thread.sleep(10000);
				init();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

		if (group == null)
			group = new ThreadGroup("ThreadGroup");
		// if the manager thread is not active ,then run it
		if (!isActive(group, "manager"))
			manager();
	}

	/**
	 * 连接远程数据库
	 * 
	 * @return 连接成功返回TRUE
	 */
	private Connection buildConn() {
		Connection conn = null;
		try {
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			if (conn != null) {

				String str = "建立和远程数据库" + dbName+" "+dbUrl + "的连接:" + conn;
				System.out.println(str);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 从连接池中取出一个可用的连接. 如果空闲池中有，则从空闲池中取，然后把userCount加一，并把此连接
	 * 移动使用池中；否则，判断使用池中是否有可用的连接（一个连接上 的使用用户数目还没有达到最大值即为可用的连接），如果有则在此
	 * 连接分配给用户使用，如果使用池中没有可用的连接，并连接的总数目 还没有达到系统限制的最大值，则重新创建一个连接供用户使用，否则
	 * 等待一段时间再查看是否有可用连接。
	 * 
	 * @param timeout
	 *            最大等待时间，超出此时间则返回NULL
	 * @return
	 */
	public synchronized GFConn getConn() {
		Connection conn = null;
		GFConn _conn = null;

		if (idleConnPool != null && idleConnPool.size() > 0) {
			// 这个地方可能会出数组越界异常. 估计还是同步问题
			_conn = (GFConn) idleConnPool.remove(0);
			// System.out.println("remove from idleConnPool:"+_conn);
			_conn.setLastAccessTime();
			_conn.changeUserCount(1);
			usingConnPool.add(_conn);
			return _conn;
		} else {
			if (usingConnPool != null) {
				if (usingConnPool.size() > 0) {
					// System.out.println("usingConnPool.size():"+usingConnPool.size());
					for (int i = 0; i < usingConnPool.size(); i++) {
						_conn = (GFConn) usingConnPool.get(i);
						if (_conn.getUserCount() < maxUserCount) {
							_conn.changeUserCount(1);
							return _conn;
						}

					}
					_conn = null;
				}

				// 如果目前没有可用的连接并且没有达到最大的连接数目，则重新为用户
				// 创建一个连接，否则等待一段时间看是否有连接被释放。
				if (usingConnPool.size() < maxConns) {
					conn = buildConn();
					if (conn != null) {
						_conn = new GFConn();
						_conn.setConn(conn);
						_conn.changeUserCount(1);
						usingConnPool.add(_conn);

						return _conn;
					}
				} else {
					try {
						Thread.sleep(waitTime);
						System.out.println("No usable connection,sleep...");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					getConn();
				}
			}
		}
		return null;
	}

	public void changeUserCount(GFConn conn, int step) {
		if (conn != null) {
			conn.changeUserCount(step);
		}
	}

	/**
	 * 查询远程数据库,并且返回一个记录集
	 * 
	 * @param sql
	 *            要查询的SQL语句
	 * @return 自定义记录集
	 */
	public GFResultSet query(String sql) {
		GFConn _conn = null;
		GFResultSet myrs = null;

		if (sql != null) {
			// 从连接池中取出一个空闲的连接
			_conn = getConn();

			if (_conn != null) {
				try {
					// System.out.println("_conn:"+_conn);
					myrs = _conn.query(sql);
					_conn.changeUserCount(-1);
					// System.out.println("release the stmt");
				} catch (SQLException e) {
					init();
				}
			}

		}
		return myrs;
	}

	/**
	 * 执行诸如：INSERT，UPDATE，DELETE操作
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 返回更新的行数
	 */
	public int execute(String sql) {
		int updateCount = 0;// 更新的行数
		GFConn _conn = null;

		if (sql != null) {
			_conn = getConn();
			if (_conn != null) {
				try {
					updateCount = _conn.execute(sql);
					_conn.changeUserCount(-1);
				} catch (SQLException e) {

					init();
				}
			}

		}
		return updateCount;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	private boolean isActive(ThreadGroup group, String threadName) {

		if (group != null && threadName != null) {
			Thread[] thd = new Thread[group.activeCount()];
			group.enumerate(thd);

			for (int i = 0; i < thd.length; i++)
				if (thd[i].getName().equals(threadName))
					return true;
		}

		return false;
	}

	public synchronized void clearPool() {
		Connection conn = null;
		if (idleConnPool != null && usingConnPool != null) {
			for (GFConn gfconn : usingConnPool) {
				conn = gfconn.getConn();
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

			for (GFConn gfconn : idleConnPool) {
				conn = gfconn.getConn();
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		idleConnPool = new ArrayList<GFConn>();
		usingConnPool = new ArrayList<GFConn>();
	}

	/**
	 * 捕获异常，进行处理。尤其是IO异常
	 * 
	 * @param e
	 */
	public void catchException(Exception e) {
		if (e != null)
			clearPool();
	}

	/**
	 * 管理连接池中的连接。 如果usingConnPool中的连接的使用用户数目为0，则把此连接放到idleConnPool中；
	 * 如果idleConnPool中的连接超过最大的等待时间没有使用，则关闭此连接，并从空闲池中清除
	 * 
	 */
	private void manager() {
		Thread thread = new Thread(group, "manager") {
			public void run() {
				GFConn _conn = null;
				while (true) {
					// System.out.println("manager is running...");
					try {
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (usingConnPool != null) {
						// ("using conn pool:"+usingConnPool.size() );
						for (int i = 0; i < usingConnPool.size(); i++) {
							_conn = (GFConn) usingConnPool.get(i);
							// System.out.println(_conn+":userCount is
							// :"+_conn.getUserCount() );
							if (_conn.getUserCount() == 0) {
								_conn.setLastAccessTime();
								idleConnPool.add(_conn);
								usingConnPool.remove(_conn);
							}
						}
					}

					if (idleConnPool != null) {
						// System.out.println("idle conn
						// pool:"+idleConnPool.size() );
						if (idleConnPool.size() > 1) {
							for (int i = 1; i < idleConnPool.size(); i++) {
								_conn = (GFConn) idleConnPool.get(i);
								// System.out.println(_conn+":timeout is :"+
								// (System.currentTimeMillis()
								// -_conn.getLastAccessTime() ));
								if (_conn.isTimeout(timeout)) {
									_conn.forceClose();
									idleConnPool.remove(_conn);
								}
							}
						}
					}
				}
			}
		};

		thread.start();

	}
}
