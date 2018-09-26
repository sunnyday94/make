package com.xx.ms.mkcode.utils;
import java.util.HashMap;
import java.util.Map;

import com.xx.ms.mkcode.utils.BaseUtils;

/**
 * 简述：字段类型映射<br>
 * 详细描述：<br>
 * 详细说明该类完成主要功能和注意点 <br>
 * 时间： 2017年11月30日 上午9:13:53 <br> 
 * 版权: Copyright 2017-2050©徙木金融信息服务（上海）有限公司 All Rights Reserved.<br>
 * @author  lizikui 
 * @version V1.0
 */

public class FieldsMapp {
	
	//mysql 数据字段转换成java类型
	private static Map<String,String> db2java = new HashMap<String,String>();
	static {
		db2java.put("BIGINT", "Long");
		db2java.put("TINYINT", "Integer");//Byte
		db2java.put("SMALLINT", "Short");
		db2java.put("MEDIUMINT", "Integer");
		db2java.put("INTEGER", "Integer");
		db2java.put("INT", "Integer");
		db2java.put("FLOAT", "Float");
		db2java.put("DOUBLE", "Double");
		db2java.put("DECIMAL", "BigDecimal");
		db2java.put("NUMERIC", "BigDecimal");
		db2java.put("CHAR", "String");
		db2java.put("VARCHAR", "String");
		db2java.put("TINYBLOB", "byte[]");
		db2java.put("TINYTEXT", "String");
		db2java.put("BLOB", "byte[]");
		db2java.put("TEXT", "String");
		db2java.put("MEDIUMBLOB", "byte[]");
		db2java.put("MEDIUMTEXT", "String");
		db2java.put("LONGBLOB", "byte[]");
		db2java.put("LONGTEXT", "String");
		db2java.put("DATE", "Date");
		db2java.put("TIME", "Date");
		db2java.put("YEAR", "Date");
		db2java.put("DATETIME", "Date");
		db2java.put("TIMESTAMP", "Date"); 
		
		db2java.put("ENUM", "Integer");
		db2java.put("BIT", "Boolean");
		
//		db2java.put("varchar", "String"); 	db2java.put("char", "String");
//		db2java.put("text", "String");db2java.put("longtext", "String");
//		db2java.put("blob", "byte[]");
//		db2java.put("smallint", "Integer");db2java.put("int", "Integer");db2java.put("bigint", "Long");  	//BigInteger
//		db2java.put("enum", "Integer");
//		db2java.put("double", "BigDecimal");db2java.put("float", "BigDecimal");db2java.put("decimal", "BigDecimal");
//		db2java.put("date", "Date");	db2java.put("datetime", "Date"); db2java.put("timestamp", "Date");
//		db2java.put("bit", "Boolean");
	}
	
	/**
	 * 获取java属性类型
	 * @param dbType
	 * @return
	 */
	public static String getJavaType(String dbType) {
		return db2java.get(BaseUtils.null2String(dbType).toUpperCase());
	}
	
	/**
	 * 获取jdbc类型 在mapper中
	 * @param dbType
	 * @return
	 */
	public static String getJdbcType(String dbType) {
		String type = db2jdbc.get(BaseUtils.null2String(dbType).toUpperCase());
		if(BaseUtils.isNull(type)) {
			type = BaseUtils.null2String(dbType).toUpperCase();
		}
		return type;
	}
	
	
	private static Map<String,String> db2jdbc = new HashMap<String,String>();
	static {
		db2jdbc.put("BIGINT", "BIGINT");
		db2jdbc.put("TINYINT", "TINYINT");
		db2jdbc.put("SMALLINT", "SMALLINT");
		db2jdbc.put("MEDIUMINT", "INTEGER");
		db2jdbc.put("INTEGER", "INTEGER");
		db2jdbc.put("INT", "INTEGER");
		db2jdbc.put("FLOAT", "REAL");
		db2jdbc.put("DOUBLE", "DOUBLE");
		db2jdbc.put("DECIMAL", "DECIMAL");
		db2jdbc.put("NUMERIC", "DECIMAL");
		db2jdbc.put("CHAR", "CHAR");
		db2jdbc.put("VARCHAR", "VARCHAR");
		db2jdbc.put("TINYBLOB", "BINARY");
		db2jdbc.put("TINYTEXT", "VARCHAR");
		db2jdbc.put("BLOB", "BINARY");
		db2jdbc.put("TEXT", "LONGVARCHAR");
		db2jdbc.put("MEDIUMBLOB", "LONGVARBINARY");
		db2jdbc.put("MEDIUMTEXT", "LONGVARCHAR");
		db2jdbc.put("LONGBLOB", "LONGVARBINARY");
		db2jdbc.put("LONGTEXT", "LONGVARCHAR");
		db2jdbc.put("DATE", "DATE");
		db2jdbc.put("TIME", "TIME");
		db2jdbc.put("YEAR", "DATE");
		db2jdbc.put("DATETIME", "TIMESTAMP");
		db2jdbc.put("TIMESTAMP", "TIMESTAMP");
		db2jdbc.put("ENUM", "INTEGER");
		db2jdbc.put("BIT", "BIT");
		
	}
	
}
