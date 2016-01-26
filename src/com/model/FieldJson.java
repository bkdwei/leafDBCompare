package com.model;

import org.springframework.stereotype.Component;

/*
 * 每一个字段都是一个javabean，缺点是效率较低
 * 
 */
@Component
public class FieldJson {
	private String tableName;
	private String fieldName;
	private String fieldType;
	private int fieldLength;
	private boolean primary;
	private boolean foreign;
	private boolean mandatory;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	public boolean isForeign() {
		return foreign;
	}
	public void setForeign(boolean foreign) {
		this.foreign = foreign;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
}
