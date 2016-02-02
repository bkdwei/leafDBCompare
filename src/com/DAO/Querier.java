package com.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.FileProcessor;
import com.model.FieldJson;
import com.model.Table;
import com.model.TableList;

@Service
public class Querier {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 获取所有的表
	public List<String> getTables() {
		List<String> tableList = new ArrayList<>();
		Connection conn = null;
		ResultSet tableSet = null;
		String types[] = { "table" };
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			String driverName = "";
			// 根据不同的数据库类型设置不同的表获取方式
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
			this.closeDBObject(conn);
			this.closeDBObject(tableSet);
		}
		return tableList;

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
				this.closeDBObject(stmt);
				this.closeDBObject(rs);
				this.closeDBObject(conn);
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

	public TableList getDBStructure() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String tableName = null;
		// 获取所有的表名
		List tables = this.getTables();
		// 所有表，HashMap相互嵌套，<表名,<字段名，字段属性>>
		HashMap tmpTableMap = new HashMap<String, HashMap>();
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stmt = conn.createStatement();
			for (int i = 0; i < tables.size(); i++) {
				// 第一步，获取表名
				tableName = tables.get(i).toString();
				String sql = "select * from " + tableName;
				if (tableName != null) {
					rs = stmt.executeQuery(sql);
					ResultSetMetaData rsd = rs.getMetaData();
					int number = rsd.getColumnCount();

					// 添加某张表的所有字段到fieldList,fieldList是一个hashmap对象
					Table table = new Table();
					List fieldList = new ArrayList<String>();
					HashMap fieldMap = new HashMap<>();
					for (int num = 0; num < number; num++) {
						FieldJson fj = new FieldJson();
						// 第二步，获取字段名
						fj.setTableName(tableName);
						// 第三步，获取字段属性
						fieldList.add(rsd.getColumnLabel(num + 1));
						fj.setFieldName(rsd.getColumnLabel(num + 1));
						fj.setFieldType(rsd.getColumnTypeName(num + 1));
						fj.setFieldLength(rsd.getColumnDisplaySize(num + 1));
						fj.setMandatory(rsd.isNullable(num + 1) == 1 ? true : false);
						fieldMap.put(rsd.getColumnLabel(num + 1), fj);
					}
					table.setFieldList(fieldList);
					table.setFieldMap(fieldMap);
					// 装载单张表
					tmpTableMap.put(tableName, table);
					/*
					 * table.setTableName(tableName);
					 * table.setFieldList(fieldList); tmpTableList.add(table);
					 */
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeDBObject(stmt);
			this.closeDBObject(rs);
			this.closeDBObject(conn);
		}
		TableList tl = new TableList();
		tl.setTablesList(tables);
		tl.setTablesMap(tmpTableMap);
		return tl;
	}

	public void closeDBObject(Object o) {
		if (o != null) {
			if (o instanceof Connection) {
				try {
					((Connection) o).close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (o instanceof Statement) {
				try {
					((Statement) o).close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (o instanceof ResultSet) {
				try {
					((ResultSet) o).close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
