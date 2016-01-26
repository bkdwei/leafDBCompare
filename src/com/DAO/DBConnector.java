package com.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DBConnector {
	private String host;
	private String DBName;
	private String username;
	private String password;
	private String port;
	private String DBType;
	private String DBDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private Connection conn;
	private JdbcTemplate jdbcTemplate;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDbname() {
		return DBName;
	}

	public void setDbname(String DBname) {
		this.DBName = DBname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDBType() {
		return DBType;
	}

	public void setDBType(String dBType) {
		DBType = dBType;
	}

	public String getDBDriver() {
		return DBDriver;
	}

	public void setDBDriver(String dBDriver) {
		DBDriver = dBDriver;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	DBConnector() {
		host = "locahost";
		// host ="SN-201504190851";
		username = "sa";
		password = "1";
		port = "1433";
		DBName = "master";
	}

	DBConnector(String host, String username, String password, String port, String dbname) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
		this.DBName = dbname;
	}

	public Boolean testConnection() {
		String ConnStr = "jdbc:sqlserver://" + host + ":" + port + ";DatabaseName=" + DBName;

		Connection conn = null;
		System.out.println("测试开始……\n测试地址：" + ConnStr);
		try {
			Class.forName(DBDriver).newInstance();
			conn = DriverManager.getConnection(ConnStr, username, password);
			if (conn != null) {
				JOptionPane.showMessageDialog(null, "数据库连接成功。\n连接地址：" + ConnStr, "测试结果", 1, null);
			}
			this.setConn(conn);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "数据库连接失败。\n失败原因：" + e.getMessage(), "测试结果", 0, null);
			System.out.println(e.getMessage());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "数据库连接失败。\n失败原因：" + e.getMessage(), "测试结果", 0, null);
			System.out.println(e.getMessage());
		}
		System.out.println("测试结束");
		return false;
	}
	// 测试数据库是否能连通
	public int testConnectionByJdbcTemplate() {
		int testResult = 0;
		Connection conn = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			// 初始化驱动程序的名称
			String driverName = conn.getMetaData().getDriverName();
			if (conn != null) {
				testResult = 1;
				System.out.println("数据库连接成功！");
			} else {
				testResult = 0;
				System.out.println("数据库连接失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "数据库连接测试", 0, null);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return testResult;
	}
}
