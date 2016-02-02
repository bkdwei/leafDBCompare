package com.model;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

/*
 * 表列表
 * 
 */
@Component
public class TableList {
	private List tablesList;
	private HashMap tablesMap;

	public List getTablesList() {
		return tablesList;
	}

	public void setTablesList(List tablesList) {
		this.tablesList = tablesList;
	}

	public HashMap getTablesMap() {
		return tablesMap;
	}

	public void setTablesMap(HashMap tablesMap) {
		this.tablesMap = tablesMap;
	}

}
