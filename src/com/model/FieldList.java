package com.model;

import java.util.List;

import org.springframework.stereotype.Component;

/*
 * 字段列表
 * 
 */
@Component
public class FieldList {
	private List fieldList;

	public List getFieldList() {
		return fieldList;
	}

	public void setFieldList(List fieldList) {
		this.fieldList = fieldList;
	}

}
