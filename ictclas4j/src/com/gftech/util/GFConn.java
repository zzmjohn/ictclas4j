/*
 * Created on 2004-5-25
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.gftech.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 实现对Connection的封装，同时为连接增加两个属性：
 * 最后一次的访问时间（lastAccessTime)和使用此连接的用户数目(userCount).
 * lastAccessTime的作用是当连接处于空闲当中超过一定的时间之后，可以根据此 信息把空闲连接释放掉。
 * userCount的作用是确保每个连接上复用的用户数目不会超过此连接允许是最大数目， 以确保不会出现游标越界的异常。
 * 
 * 注：用户不需要关心此类
 * 
 * @author SINBOY
 * @version 1.0 (2004.5)
 */
public class GFConn {
	private Connection conn;// 和数据库的连接

	private long lastAccessTime;// 最后一次的访问时间

	private int userCount;// 使用此连接的用户数目
  
	public GFConn() {
		conn = null;
		userCount = 0;
		lastAccessTime = System.currentTimeMillis();
	}

	public GFConn(Connection conn, int userCount) {
		this.conn = conn;
		this.userCount = userCount;
		lastAccessTime = System.currentTimeMillis();
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime() {
		lastAccessTime = System.currentTimeMillis();
	}

	/**
	 * 此连接在空闲状态的时间是否超过了定义的超时时间
	 * 
	 * @param maxTimeout
	 * @return
	 */
	public boolean isTimeout(int maxTimeout) {
		if (System.currentTimeMillis() - lastAccessTime > maxTimeout)
			return true;
		else
			return false;
	}

	public int getUserCount() {
		return userCount;
	}

	/**
	 * 改变连接上用户的使用数目。
	 * 
	 * @param step
	 *            步长，正值说明增加，负值说明减少
	 */
	public void changeUserCount(int step) {
		userCount += step;
	}

	/**
	 * 在此连接上进行数据库查询
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @return 查询到的结果集
	 */
	public GFResultSet query(String sql) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		GFResultSet myrs = null;

		if (sql != null && conn != null) {
			try {
				// System.out.println("conn:"+conn);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				// userCount++;
				rs = stmt.executeQuery(sql);
				myrs = new GFResultSet(rs);

				// 关闭游标，以防止游标越界
				rs.close();
				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
				// System.out.println("errCode:"+e.getErrorCode() );
				if (e.getErrorCode() == 17002)
					throw new SQLException();
			}

		}

		if (myrs != null && myrs.next()) {
			myrs.beforeFirst();
			return myrs;
		}

		else
			return null;
	}

	/**
	 * 执行UPDATE操作
	 * 
	 * @param sql
	 * @return
	 */
	public synchronized int execute(String sql) throws SQLException {
		int updateCount = 0;// 更新的行数
		Statement stmt = null;

		if (sql != null && conn != null) {
			try {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				stmt.execute(sql);
				updateCount = stmt.getUpdateCount();

				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (stmt != null)
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();

						throw new SQLException();
					}

			}
		}
		return updateCount;
	}

	public void close(){
		userCount --;
	}
	/**
	 * force close the Connection
	 */
	public void forceClose() {
		if (conn != null) {
			try {
				System.out.println("关闭超出等待时间的空闭连接：" + conn);
				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
