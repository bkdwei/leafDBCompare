package com.model;

import java.util.List;

public class Table {
	private String tableName;
	private List FieldList;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List getFieldList() {
		return FieldList;
	}
	public void setFieldList(List fieldList) {
		FieldList = fieldList;
	}
}
