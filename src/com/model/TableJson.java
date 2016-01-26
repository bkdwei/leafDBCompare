package com.model;

import java.util.HashMap;

import org.springframework.stereotype.Component;

/*
 * 每一张表的
 * 
 */
@Component
public class TableJson {
	private String tableName;
	private HashMap typeHashMap;
	private HashMap lengthHashMap;

	public HashMap getTypeHashMap() {
		return typeHashMap;
	}

	public void setTypeHashMap(HashMap typeHashMap) {
		this.typeHashMap = typeHashMap;
	}

	public HashMap getLengthHashMap() {
		return lengthHashMap;
	}

	public void setLengthHashMap(HashMap lengthHashMap) {
		this.lengthHashMap = lengthHashMap;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}