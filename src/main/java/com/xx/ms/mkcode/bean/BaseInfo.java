package com.xx.ms.mkcode.bean;

import java.io.Serializable;

/**
 * 数据库信息
 * @author lizk
 *
 */
public class BaseInfo implements Serializable{
	private static final long serialVersionUID = 778881777164252094L;
	
	
	private String ip = "";
	private String dbName = "";//数据库名称
	private String userName = "";
	private String userPass = "";
	private String filePath = "";//所有文件存放路径
	private String tables ="";//需要处理的表 如果位空 则处理全部

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getTables() {
		return tables;
	}
	public void setTables(String tables) {
		this.tables = tables;
	}
}
