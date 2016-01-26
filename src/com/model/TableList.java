package com.model;

import java.util.List;

import org.springframework.stereotype.Component;

/*
 * 表列表
 * 
 */
@Component
public class TableList {
	private List tablesList;

	public List getTables() {
		return tablesList;
	}

	public void setTables(List tables) {
		this.tablesList = tables;
	}
}
