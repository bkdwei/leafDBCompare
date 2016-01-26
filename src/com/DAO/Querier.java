package com.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

	public void getDBStructure() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		List tables = this.getTables();
		String tableName = null;

		List tmpTableList = new ArrayList<Table>();
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			stmt = conn.createStatement();
			for (int i = 0; i < tables.size(); i++) {
				Table table = new Table();
				tableName = tables.get(i).toString();
				String sql = "select * from " + tableName;
				if (tableName != null) {
					System.out.println(sql);
					rs = stmt.executeQuery(sql);
					ResultSetMetaData rsd = rs.getMetaData();
					int number = rsd.getColumnCount();

					// 添加字段的属性
					List fieldList = new ArrayList<>(number);
					for (int num = 0; num < number; num++) {
						FieldJson fj = new FieldJson();
						fj.setTableName(tableName);
						fj.setFieldName(rsd.getColumnLabel(num + 1));
						fj.setFieldType(rsd.getColumnTypeName(num + 1));
						fj.setFieldLength(rsd.getColumnDisplaySize(num + 1));
						fj.setMandatory(rsd.isNullable(num + 1) == 1 ? true : false);
						fieldList.add(fj);
					}
					table.setTableName(tableName);
					table.setFieldList(fieldList);
					tmpTableList.add(table);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeDBObject(stmt);
			this.closeDBObject(rs);
			this.closeDBObject(conn);
		}
		FileProcessor fp = new FileProcessor();
		TableList tl = new TableList();
		tl.setTables(tmpTableList);
		fp.json2file(tl);

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
