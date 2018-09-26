package com.xx.ms.mkcode.bean;

import java.io.Serializable;

/**
 * 表的列
 * @author lizk
 *
 */
public class TableCols implements Serializable {
	private static final long serialVersionUID = -7501102338993473082L;
	
	private String column_name="";//名称
	private String is_nullable="";// YES|NO
	private String data_type=""; //varchar
	private String column_type=""; //varchar(20)
	private String column_comment=""; //备注
	private String column_default="";//默认值
	private String jdbcType = "";//mybatis 字符集
	private String isTime = "NO"; //YES | NO
	private long var_len=0;//长度
	private int num_len;//数字的长度
	private int num_sca;//浮点精度
	
	private String fieldName ="";//java字段名称
	private String fieldNameU = "";//字段名称 与上面的区别是第一个字母需要大写,是为了get、set方法使用的
	private String fieldType = "";//字段类型
	private String fieldDefault = "";//默认值
	private String fieldTypeFull = "";//字段类型全路径
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getIs_nullable() {
		return is_nullable;
	}
	public void setIs_nullable(String is_nullable) {
		this.is_nullable = is_nullable;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getColumn_type() {
		return column_type;
	}
	public void setColumn_type(String column_type) {
		this.column_type = column_type;
	}
	public String getColumn_comment() {
		return column_comment;
	}
	public void setColumn_comment(String column_comment) {
		this.column_comment = column_comment;
	}
	public String getColumn_default() {
		return column_default;
	}
	public void setColumn_default(String column_default) {
		this.column_default = column_default;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldNameU() {
		return fieldNameU;
	}
	public void setFieldNameU(String fieldNameU) {
		this.fieldNameU = fieldNameU;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldTypeFull() {
		return fieldTypeFull;
	}
	public void setFieldTypeFull(String fieldTypeFull) {
		this.fieldTypeFull = fieldTypeFull;
	}
	public String getFieldDefault() {
		return fieldDefault;
	}
	public void setFieldDefault(String fieldDefault) {
		this.fieldDefault = fieldDefault;
	}
	public String getJdbcType() {
		if(jdbcType.equals("INT"))return "INTEGER";
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public String getIsTime() {
		return isTime;
	}
	public void setIsTime(String isTime) {
		this.isTime = isTime;
	}
	public int getNum_len() {
		return num_len;
	}
	public void setNum_len(int num_len) {
		this.num_len = num_len;
	}
	public int getNum_sca() {
		return num_sca;
	}
	public void setNum_sca(int num_sca) {
		this.num_sca = num_sca;
	}
	public long getVar_len() {
		return var_len;
	}
	public void setVar_len(long var_len) {
		this.var_len = var_len;
	}
	
}
