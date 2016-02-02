package com.utils;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.model.FieldJson;
import com.model.Table;
import com.model.TableList;

@Service
public class CompareTool {
	private TableList tableList1;
	private TableList tableList2;

	public void compareTables(TableList tl1, TableList tl2) {
		if (tl1 != null && tl2 != null) {
			List table1List = tl1.getTablesList();
			HashMap table1Map = tl1.getTablesMap();
			HashMap table2Map = JSON.parseObject(tl2.getTablesMap().toString().replace("=", ":"), HashMap.class);

			for (int i = 0; i < table1List.size(); i++) {
				// 获取一张表
				String table1TableName = (String) table1List.get(i);
				Table table1 = (Table) table1Map.get(table1TableName);
				Table table2 = null;
				// 对比是否存在这张表
				try {
					table2 = JSON.parseObject(table2Map.get(table1TableName).toString().replace("=", ":"), Table.class);
				} catch (Exception e) {
					System.out.println("表2中不存在：" + table1TableName + "表。");
				}

				// 对比是否存在字段
				if (table1 != null && table2 != null) {
					List fieldList1 = table1.getFieldList();
					for (int j = 0; j < fieldList1.size(); j++) {
						// 获取字段名
						String fieldName = (String) fieldList1.get(j);

						// 获取字段对象
						FieldJson fieldJson1 = (FieldJson) table1.getFieldMap().get(fieldName);
						try {
							FieldJson fieldJson2 = JSON.parseObject(
									table2.getFieldMap().get(fieldName).toString().replace("=", ":"), FieldJson.class);
							if (!fieldJson1.getFieldName().trim().equalsIgnoreCase(fieldJson2.getFieldName().trim())) {
								System.out.println("表二不存在字段：" + fieldName);
							}
						} catch (Exception e) {
							System.out.println("表二获取存在字段：" + fieldName + "失败。");
						}

						// System.out.println(fieldJson1.getFieldName() +
						// "," + fieldJson1.getFieldLength());

					}
				} else {
					System.out.println("获取表失败！");
				}
			}
			// String tableName1 = table1.getTableName();

			/*
			 * int itb2 = ltl2.size(); Table table2 = null ;
			 * System.out.println("result:"+ltl1.indexOf(table1)); for(int
			 * ii=0;ii<ltl2.size();ii++){ table2 = (Table)ltl2.get(ii);
			 * if(tableName1.equalsIgnoreCase(table2.getTableName())){
			 * System.out.println("表2存在表1的表："+tableName1); break; } } List
			 * fieldList1 = table1.getFieldList(); List fieldList2 =
			 * table2.getFieldList(); for(int j=0;j<fieldList1.size();j++){
			 * FieldJson fj1 = (FieldJson)fieldList1.get(j); }
			 */
		}
	}
}
