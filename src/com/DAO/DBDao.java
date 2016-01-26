package com.DAO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.model.TableJson;
import com.model.TableList;

@Service
public class DBDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TableJson tableJson;
	@Autowired
	private TableList tableList;
	String driverName = null;

	// 获取所有的表
	public List<String> getTables() {
		List<String> tableList = new ArrayList<>();
		Connection conn = null;
		ResultSet tableSet = null;
		String types[] = { "table" };
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			// 根据不同的数据库设置不同的表获取方式
			if (driverName.equals("Microsoft SQL Server JDBC Driver 3.0")) {
				tableSet = conn.getMetaData().getTables(null, "dbo", "%", types);
			} else {
				tableSet = conn.getMetaData().getTables(null, "%", "%", types);
			}

			while (tableSet.next()) {
				tableList.add((String) tableSet.getObject("TABLE_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (tableSet != null) {
				try {
					tableSet.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return tableList;

	}

	// 删除某张表的所有数据
	public void deleteTableData(String tableName) {
		if (tableName != null) {
			String sqlCmd = "delete from " + tableName;
			jdbcTemplate.execute(sqlCmd);
		}
	}

	// 测试数据库是否能连通
	public int testConnection() {
		int testResult = 0;
		Connection conn = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			// 初始化驱动程序的名称
			driverName = conn.getMetaData().getDriverName();
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

	// 获取某张表的数据的通用方法
	public DefaultTableModel getTableData(String tableName, String sql) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		DefaultTableModel tableMode = null;

		if (tableName != null) {
			try {
				conn = jdbcTemplate.getDataSource().getConnection();
				stmt = conn.createStatement();
				System.out.println(sql);
				rs = stmt.executeQuery(sql);
				ResultSetMetaData rsd = rs.getMetaData();
				int number = rsd.getColumnCount();

				// 添加字段名
				Vector columnNames = new Vector();
				Vector rows = new Vector();
				for (int num = 0; num < number; num++) {
					columnNames.addElement(rsd.getColumnLabel(num + 1));
				}
				// rows.addElement(columnNames);
				Vector columnType = new Vector();
				for (int num = 0; num < number; num++) {
					columnType.addElement(rsd.getColumnTypeName(num + 1) + rsd.getColumnDisplaySize(num + 1));
				}
				rows.addElement(columnType);

				// 添加数据
				while (rs.next()) {
					Vector newRow = new Vector();
					for (int i = 1; i < number; i++) {
						newRow.addElement(rs.getObject(i));
					}
					rows.addElement(newRow);
				}
				tableMode = new DefaultTableModel();
				tableMode.setDataVector(rows, columnNames);
				System.out.println("共" + (rows.size() - 1) + "条记录");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (rs != null) {
					try {
						rs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return tableMode;
	}

	// 获取某张表的数据的所有
	public DefaultTableModel getAllTableData(String tableName) {
		String sql = "select * from " + tableName;
		return getTableData(tableName, sql);
	}

	// 获取某张表的数据的某些数据
	public DefaultTableModel getSomeTableDataByCondition(String tableName, String sql) {
		return getTableData(tableName, sql);
	}

	public List exportDBStructure() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		DefaultTableModel tableMode = null;
		String tableName = "";

		List tables = this.getTables();
		List structureInfo = new ArrayList<TableJson>();
		for (int i = 0; i < tables.size(); i++) {
			tableName = tables.get(i).toString();
			String sql = "select * from " + tableName;
			if (tableName != null) {
				try {
					conn = jdbcTemplate.getDataSource().getConnection();
					stmt = conn.createStatement();
					System.out.println(sql);
					rs = stmt.executeQuery(sql);
					ResultSetMetaData rsd = rs.getMetaData();
					int number = rsd.getColumnCount();

					// 添加字段的属性
					HashMap tableNameMap = new HashMap<String, String>();
					HashMap typeMap = new HashMap<String, String>();
					HashMap lengthMap = new HashMap<String, String>();
					List filedList = new ArrayList<>();
					for (int num = 0; num < number; num++) {
						tableNameMap.put("tableName", tableName);
						typeMap.put(rsd.getColumnLabel(num + 1), rsd.getColumnTypeName(num + 1));
						lengthMap.put(rsd.getColumnLabel(num + 1), rsd.getColumnDisplaySize(num + 1));
						filedList.add(tableNameMap);
						filedList.add(typeMap);
						filedList.add(lengthMap);
					}
/*					tableJson.setTableName(tableName);
					tableJson.setTypeHashMap(typeMap);
					tableJson.setLengthHashMap(lengthMap);*/
					structureInfo.add(filedList);

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (conn != null) {
						try {
							conn.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				tableList.setTables(structureInfo);
			}
		}
		return structureInfo;

	}

	public void json2file(TableList tl) {
		String tableStructureInfo = JSON.toJSONString(tl, true);
		System.out.println(tableStructureInfo);
		File fTableStructureInfo = new File("/tmp/123");
		if (!fTableStructureInfo.exists()) {
			try {
				fTableStructureInfo.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileWriter fw;
		try {
			fw = new FileWriter(fTableStructureInfo);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(tableStructureInfo);
			System.out.println("写入数据库信息成功！");
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
