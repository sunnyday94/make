package com.xx.ms.mkcode.bean;

import java.io.Serializable;

/**
 * 表
 * @author lizk
 *
 */
public class Table implements Serializable{

	private static final long serialVersionUID = 5041708844781162938L;
	
	private String table_name="";
	private String table_comment="";
	
	private String pkgAction = "";//controller
	private String pkgBean = "";//bean包路径 model的路径
	private String pkgBo = "";//bo包路径
	private String pkgDao = "";//dao包路径
	private String pkgService = "";//service包路径
	private String ipkgService = "";//service接口包路径
	private String clsName = "";//java的类名称
	private String instName = "";//属性名名称
	private String model = ""; //业务模块
	
	private String pkgMapping="";//服务包
	private String mappingName="";//服务名
	
	private String userName = "";//创建人 业务负责人
	private String createTime =""; //创建时间 是为了传递参数的

	private String nullFields = "";//所有非空字段 
	private String lengthFields = "";//所有需要校验长度的字段 char varchar
	
	private TableCols bussKey ;//业务主键
	private String idMake = "";//id生成方法， 如果是需要生成 则为0  如果是表自动生成 为1
	
	private String tabDesc = "";//表的描述
	
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getTable_comment() {
		return table_comment;
	}
	public void setTable_comment(String table_comment) {
		this.table_comment = table_comment;
	}

	public String getClsName() {
		return clsName;
	}
	public void setClsName(String clsName) {
		this.clsName = clsName;
	}
	public String getPkgBean() {
		return pkgBean;
	}
	public void setPkgBean(String pkgBean) {
		this.pkgBean = pkgBean;
	}
	public String getPkgDao() {
		return pkgDao;
	}
	public void setPkgDao(String pkgDao) {
		this.pkgDao = pkgDao;
	}
	public String getPkgService() {
		return pkgService;
	}
	public void setPkgService(String pkgService) {
		this.pkgService = pkgService;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getIpkgService() {
		return ipkgService;
	}
	public void setIpkgService(String ipkgService) {
		this.ipkgService = ipkgService;
	}
	public String getPkgAction() {
		return pkgAction;
	}
	public void setPkgAction(String pkgAction) {
		this.pkgAction = pkgAction;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNullFields() {
		return nullFields;
	}
	public void setNullFields(String nullFields) {
		this.nullFields = nullFields;
	}
	public String getLengthFields() {
		return lengthFields;
	}
	public void setLengthFields(String lengthFields) {
		this.lengthFields = lengthFields;
	}
	public String getPkgBo() {
		return pkgBo;
	}
	public void setPkgBo(String pkgBo) {
		this.pkgBo = pkgBo;
	}
	public TableCols getBussKey() {
		return bussKey;
	}
	public void setBussKey(TableCols bussKey) {
		this.bussKey = bussKey;
	}
	public String getTabDesc() {
		return tabDesc;
	}
	public void setTabDesc(String tabDesc) {
		this.tabDesc = tabDesc;
	}
	public String getIdMake() {
		return idMake;
	}
	public void setIdMake(String idMake) {
		this.idMake = idMake;
	}
	public String getMappingName() {
		return mappingName;
	}
	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}
	public String getPkgMapping() {
		return pkgMapping;
	}
	public void setPkgMapping(String pkgMapping) {
		this.pkgMapping = pkgMapping;
	}


}
