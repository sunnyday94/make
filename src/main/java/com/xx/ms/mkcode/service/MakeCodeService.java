package com.xx.ms.mkcode.service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xx.ms.mkcode.bean.Table;
import com.xx.ms.mkcode.bean.TableCols;
import com.xx.ms.mkcode.utils.BaseUtils;
import com.xx.ms.mkcode.utils.FieldsMapp;
import com.xx.ms.mkcode.utils.FileUtils;
import com.xx.ms.mkcode.utils.VelocityUtils;

public class MakeCodeService {

	private String basePkg;

	String mappingName;
//	public MakeCodeService(){}
	
	public MakeCodeService(String basePkg,String mappingName){
		this.basePkg=basePkg;
		this.mappingName=mappingName;
	}
	
	/**
	 * 生成文件
	 * @param con 数据库连接
	 * @param dbName 数据库名称
	 * @param tabs 需要处理的表 1、可以为空 2、可以是逗号分割的多个表名称（用in查询） 3、使用含有%的字符串（like查询），eg(t_%)会like t_%
	 * @param fileSrcPath 源文件存放位置
	 * @param fileMapperPath mapper存放的位置
	 * @param userName 开发负责人
	 * @param model 业务模块 是为了指定类放在对应的包名称
	 * @param makeFlg 是第一次还是第n次 第一次 true 其他false
	 * @throws Exception
	 */
	public void makeCode(Connection con,String dbName,String tabs,String fileSrcPath,String fileMapperPath,String userName,String model,Map<String,Boolean> makeFlg) throws Exception {
		List<Table> tabList = null;//表对象
		List<TableCols> tcList = null;//列对象
		List<String> beanImpList = null;//需要引入的类 在bean中
		List<String> serviceImpList = null;//需要引入的类 在bean中
		String[] names = null; //模板传递对象
		Object[] values = null; //模板传递对象
		//==获取模板
		String tmpPath = getClass().getResource("/").getPath() + "template/";
		//==获取存放地址
		if(BaseUtils.isNull(fileSrcPath)) {
			throw new Exception("请传入存放的文件路径");
		}
		if(!fileSrcPath.endsWith("/")) fileSrcPath = fileSrcPath + "/";
		if(BaseUtils.isNull(fileMapperPath))fileMapperPath = fileSrcPath + "../../../../";
		//==设置文件创建时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = format.format(new Date());
		//==处理开发负责人
		if(BaseUtils.isNull(userName))userName = "sys";
		//==1.获取表对象
		tabList = this.getTabs(con,dbName,tabs,model);
		String tmpFilePath = null;
		StringBuilder sb = new StringBuilder();
		for (Table tab : tabList) {
			if (tab == null) continue;
			tcList = this.getTabCols(con,dbName,tab);
			beanImpList = getBeanImpList(tcList);
			serviceImpList = getServiceImpList(tab);
			tab.setTabDesc(this.getTabDesc(tcList));
			tab.setUserName(userName);//设置开发负责人
			tab.setCreateTime(createTime);//设置当前创建时间
			names = new String[] { "tab", "tcList","beanImpList","serviceImpList"};
			values = new Object[] { tab, tcList,beanImpList,serviceImpList};
			sb.setLength(0);
			sb.append("========table[").append(tab.getTable_name()).append("]").append("\r\n");
			if(makeFlg.get("bean")) {
				//==生成模型 javabean
				tmpFilePath = fileSrcPath + "/metadata/entity/"+tab.getModel() +"/"+ tab.getClsName() + ".java";
				String log = makeFile(tmpFilePath, tmpPath, "bean.vm", names, values, "}");
				sb.append(log);
			}
			if(makeFlg.get("bo")) {
				//==生成模型 bo
				tmpFilePath = fileSrcPath + "/../api/pojo/"+tab.getModel() +"/"+ tab.getClsName() + "VO.java";
				String log = makeFile(tmpFilePath, tmpPath, "bo.vm", names, values, "}");
				sb.append(log);
			}
			if(makeFlg.get("controller")) {
				//==生成cotroller
				tmpFilePath = fileSrcPath +"/controller/" + tab.getModel() +"/"+ tab.getClsName() + "Controller.java";
				String log = makeFile(tmpFilePath, tmpPath, "controller.vm", names, values, "}");
				sb.append(log);
			}
			if(makeFlg.get("iservice")) {
				//==生成service接口
				tmpFilePath = fileSrcPath +"/service/" + tab.getModel()  +  "/I"+tab.getClsName() + "Service.java";
				String log = makeFile(tmpFilePath, tmpPath, "iservice.vm", names, values, "}");
				sb.append(log);
			}
			if(makeFlg.get("service")) {
				//==生成service实现
				tmpFilePath = fileSrcPath +"/service/" + tab.getModel()+"/impl" +"/" + tab.getClsName() + "Service.java";
				String log = makeFile(tmpFilePath, tmpPath, "service.vm", names, values, "}");
				sb.append(log);
			}
			if(makeFlg.get("dao")) {
				//==生成dao接口
				tmpFilePath = fileSrcPath +"/dao/" + tab.getModel()+ "/I"+tab.getClsName() + "Dao.java";
				String log = makeFile(tmpFilePath, tmpPath, "dao.vm", names, values, "}");
				sb.append(log);
			}
			if(makeFlg.get("mapper")) {
				//==生成mapper
				tmpFilePath = fileMapperPath + "../../resources/mapper/"+tab.getModel() +"/"+ tab.getClsName() + "Mapper.xml";
				String log = makeFile(tmpFilePath, tmpPath, "mapper.vm", names, values, "</mapper>");
				sb.append(log);
			}
			if(makeFlg.get("mapping")) {
				//==生成mapper
				tmpFilePath = fileSrcPath + "/../api/call/v1/"+tab.getModel() +"/I"+ tab.getClsName() + "RemoteCall.java";
				String log = makeFile(tmpFilePath, tmpPath, "mapping.vm", names, values, "}");
				sb.append(log);
			}
			System.out.println(sb.toString());
		}
	}

	/**
	 * 获取用户下的所有表
	 * 
	 * @param con
	 * @param dbName
	 * @param tabs
	 * @param model
	 * @return
	 * @throws Exception
	 */
	private List<Table> getTabs(Connection con,String dbName,String tabs,String model) throws Exception {
		List<Table> list =  this.getDbTables(con,dbName,tabs);
		String base=basePkg,pkg = "";
		for (Table tab : list) {
			String str[] = tab.getTable_name().split("_");
			if (str.length < 2)  continue;//如果不满足条件则不处理
			pkg = str[1].toLowerCase();
			if(BaseUtils.isNotNull(model)) {
				pkg = model;
			}
			tab.setPkgBean(base + ".metadata.entity." + pkg);
			String vo=base.substring(0,base.lastIndexOf("."));
			tab.setPkgBo(vo + ".remote.pojo." + pkg);
			tab.setPkgDao(base + ".dao"  + "."+pkg );
			tab.setPkgAction(base  + ".controller"+ "."+pkg );
			tab.setIpkgService(base +".service"+"."+pkg );
			tab.setPkgService(base   +".service"+ "."+pkg+".impl");
			tab.setPkgMapping(vo + ".remote.call.v1." + pkg);
			
			tab.setMappingName(mappingName);
			String clsName = toUpperUpper(tab.getTable_name());
			//==处理className 只有以T开头的才可以
			if(BaseUtils.isNotNull(clsName)&&clsName.startsWith("T")) clsName = clsName.substring(1);
			tab.setClsName(clsName);
			String instName = this.toLowerUpper(tab.getTable_name());
			if(BaseUtils.isNotNull(instName)&&instName.startsWith("T")) instName = instName.substring(1);
			tab.setInstName(instName);
			tab.setModel(pkg.toLowerCase());
		}
		return list;
	}

	/**
	 * 获取所有列的属性
	 * @param con 连接
	 * @param dbName 库名称
	 * @param tab 表名称
	 * @return
	 * @throws Exception
	 */
	private List<TableCols> getTabCols(Connection con,String dbName,Table tab) throws Exception {
		List<TableCols> list = this.getDbTabCols(con,dbName,tab.getTable_name());
		String lengthType ="char,varchar";
		String nullFields="",lengthFields="";;
		// 进行字段转换
		for (TableCols col : list) {
			// 设置字段名称
			col.setFieldName(this.toLowerUpper(col.getColumn_name()));
			col.setFieldNameU(this.toGetSet(col.getColumn_name()));
			col.setFieldType(FieldsMapp.getJavaType(col.getData_type()));//有数据库字段类型转换为java属性类型
			col.setJdbcType(FieldsMapp.getJdbcType(col.getData_type()));
			//==设置默认值
			String def = col.getColumn_default();
			if (BaseUtils.isNotNull(def)) {
				col.setFieldDefault(def);// 默认
				if ("String".equals(col.getFieldType())) {
					col.setFieldDefault("\"" + def + "\"");
				} else if ("Long".equals(col.getFieldType())) {
					col.setFieldDefault(def.trim() + "L");
				} else if ("Boolean".equals(col.getFieldType())) {
					if("b'0'".equals(def)) {
						col.setFieldDefault("false");
					}else {
						col.setFieldDefault("true");
					}
				} else if ("BigDecimal".equals(col.getFieldType())) {
					col.setFieldDefault(" new BigDecimal(\""+def.trim()+"\") ");
				} else if ("Double".equals(col.getFieldType())) {
					col.setFieldDefault(def.trim() + "d");
				} else if ("Float".equals(col.getFieldType())) {
					col.setFieldDefault(def.trim() + "f");
				}
			}
			if("Date".equals(col.getFieldType())){
				col.setIsTime("YES");
			}
			//==判断当前字段是否需要空校验
			if("NO".equals(col.getIs_nullable())
					&&(!"ID".equals(col.getFieldName().toUpperCase()))
					&&(!"CRUSERID".equals(col.getFieldName().toUpperCase()))
					&&(!"OPUSERID".equals(col.getFieldName().toUpperCase()))
					) {
				nullFields+= col.getFieldName()+",";
			}
			//==判断当前字段是否需要对长度进行判断
			if(lengthType.contains(col.getData_type())) {
				lengthFields+= col.getFieldName()+"-"+"0"+"-"+col.getVar_len()+",";
			}
			//==业务主键 目前只支持一个
			if((!"ID".equals(col.getFieldName().toUpperCase()))&&BaseUtils.null2String(col.getColumn_comment()).trim().contains("[UK]")&&(!col.getFieldName().toUpperCase().contains("ID"))) {
				tab.setBussKey(col);
			}
			if("ID".equals(col.getFieldName().toUpperCase())&&BaseUtils.null2String(col.getColumn_comment()).trim().contains("[0]")) {
				tab.setIdMake("0");
			}
		}
		//==设置空校验字段
		if(BaseUtils.isNotNull(nullFields)) {
			if(nullFields.endsWith(",")) nullFields = nullFields.substring(0,nullFields.length()-1);
			tab.setNullFields(nullFields);
		}
		//==设置长度校验字段
		if(BaseUtils.isNotNull(lengthFields)) {
			if(lengthFields.endsWith(",")) lengthFields = lengthFields.substring(0,lengthFields.length()-1);
			tab.setLengthFields(lengthFields);
		}
		return list;
	}

	/**
	 * 获取需要导入的包
	 * @param cols
	 * @return
	 * @throws Exception
	 */
	private List<String> getBeanImpList(List<TableCols> cols)throws Exception{
		List<String> list = new ArrayList<String>();
		Map<String,String> map = new HashMap<String,String>();
		if(cols==null||cols.size()<=0)return list;
		String baseCols = "crDate,opDate,deleteFlag,crUserId,opUserId";
		for(TableCols col:cols) {
			if(baseCols.contains(col.getFieldName())) continue;
			if("BigDecimal".equals(col.getFieldType())) {
				if(BaseUtils.isNull(map.get("BigDecimal")))	map.put("BigDecimal", "import java.math.BigDecimal;");
			}else if("Date".equals(col.getFieldType())) {
				if(BaseUtils.isNull(map.get("Date")))map.put("Date", "import java.util.Date;");
				if(BaseUtils.isNull(map.get("jsonFormat")))map.put("jsonFormat", "import com.fasterxml.jackson.annotation.JsonFormat;");
			}
		}
		for (String value : map.values()) {
			list.add(value);
		}
		return list;
	}
	
	/**
	 * 获取需要导入的包
	 * @param tab
	 * @return
	 * @throws Exception
	 */
	private List<String> getServiceImpList(Table tab)throws Exception{
		List<String> list = new ArrayList<String>();
//		if(tab.getBussKey()!=null) { //暂时不使用
//			list = new ArrayList<String>();
//			list.add("import com.ximu.ms.common.exception.XMExceType;");
//			list.add("import com.ximu.ms.common.exception.XMException;");
//		}
		return list;
	}
	
	/**
	 * 获取表和实体的描述
	 * @param cols
	 * @return
	 * @throws Exception
	 */
	private String getTabDesc(List<TableCols> cols)throws Exception{
		if(cols==null||cols.size()<=0)return "";
		String baseCols = "crDate,opDate,deleteFlag";
		StringBuffer sb = new StringBuffer();
		sb.append("{").append("<br>");
		for(TableCols col:cols) {
			if(baseCols.contains(col.getFieldName())) continue;
			sb.append(col.getFieldName()).append(":(")
			  .append(BaseUtils.null2String(col.getColumn_comment()).replaceAll("\r\n", "").replaceAll("\n", "")).append(",")
			  .append(col.getColumn_type()).append(",")
			  .append(col.getIs_nullable()).append(",")
			  .append(BaseUtils.null2String(col.getColumn_default()))
			.append(")").append("<br>");
		}
		sb.append("}");
		return sb.toString();
	}
	
	
//	/**
//	 * coontroller获取需要导入的包
//	 * @param cols
//	 * @return
//	 * @throws Exception
//	 */
//	private List<String> getControllerImpList(Table tab)throws Exception{
//		List<String> list = new ArrayList<String>();
//		if(BaseUtils.isNotNull(tab.getNullFields())||BaseUtils.isNotNull(tab.getLengthFields())) {
//			list.add("import com.ximucredit.utils.check.CheckUtils;");
//		}
//		return list;
//	}
	
	/**
	 * 把字符串转换成每个单词第一个字母大写其他全部小写的驼峰格式 1.首先吧下划线去掉 2.单词首字母大写其他全部小写
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private String toUpperUpper(String str) throws Exception {
		StringBuilder res = new StringBuilder();
		if (str == null || str.length() <= 0)
			return res.toString();
		String[] strs = str.split("_");
		if (strs.length <= 0)
			return res.toString();
		for (String s : strs) {
			if (s == null || s.length() <= 0)
				continue;
			res.append(s.substring(0, 1).toUpperCase());
			if (s.length() > 1)
				res.append(s.substring(1).toLowerCase());
		}
		return res.toString();
	}

	/**
	 * 把字符串转换成get、set方法需要的
	 *  1.首先吧下划线去掉
	 * 2.如果第一段是一个字母则字母小写，如果是多个字母则按照第一个字母大写其他小写的驼峰格式
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private String toGetSet(String str) throws Exception {
		StringBuilder res = new StringBuilder();
		if (str == null || str.length() <= 0)
			return res.toString();
		String[] strs = str.split("_");
		if (strs.length <= 0)
			return res.toString();
		String s = null;
		for (int i = 0; i < strs.length; i++) {
			s = strs[i];
			if (s == null || s.length() <= 0)
				continue;
			if (i == 0 && s.length() == 1) { // 如果第一个单词长度是一个则直接小写
				res.append(s.toLowerCase());
			} else {
				res.append(s.substring(0, 1).toUpperCase());
				if (s.length() > 1)
					res.append(s.substring(1).toLowerCase());
			}
		}
		return res.toString();
	}

	/**
	 * 把字符串转换成第一个单词全部小写，其他单词第一个字母大写，其他字符都是小写的驼峰格式 
	 * 1.首先吧下划线去掉 
	 * 2.第一个单词全部小写
	 * 3.其他单词首字母大写其他全部小写
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	private String toLowerUpper(String str) throws Exception {
		StringBuilder res = new StringBuilder();
		if (str == null || str.length() <= 0)
			return res.toString();
		String[] strs = str.split("_");
		if (strs.length <= 0)
			return res.toString();
		boolean flg = true;
		for (String s : strs) {
			if (s == null || s.length() <= 0)
				continue;
			if (flg) {
				res.append(s.toLowerCase());
				flg = false;
			} else {
				res.append(s.substring(0, 1).toUpperCase());
				if (s.length() > 1)
					res.append(s.substring(1).toLowerCase());
			}
		}
		return res.toString();
	}
	
	
	/**
	 * 获取库下的所有表
	 * @param con
	 * @param dbName
	 * @param tabs
	 * @return
	 * @throws Exception
	 */
	private List<Table> getDbTables(Connection con,String dbName,String tabs)throws Exception{
		List<Table> list = new ArrayList<Table>();
		String sql="select t.table_name,t.table_comment " + 
			"from information_schema.tables t " + 
			"where table_schema = ? ";
		if(BaseUtils.isNotNull(tabs)){
			if(tabs.indexOf("%")>0) { //like 方式查询
				sql += " and table_name like '" + tabs + "'";
			}else { //in 方式查
				tabs = tabs.replaceAll(",", "','");
				sql += " and table_name in ('" + tabs + "')";
			}
		}
		ResultSet res = null;
		try{
			res = this.query(con, sql,dbName);
			Table tab = null;
			while(res.next()){
				tab = new Table();
				tab.setTable_name(res.getString(1));
				tab.setTable_comment(res.getString(2));
				list.add(tab);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}finally{
			if(res!=null){
				if(res.getStatement()!=null){
					res.getStatement().close();
				}
				res.close();
			}
		}
		return list;
	} 
	
	/**
	 * 获取库下的所有表
	 * @param con
	 * @param dbName
	 * @param tabName
	 * @return
	 * @throws Exception
	 */
	private List<TableCols> getDbTabCols(Connection con,String dbName,String tabName)throws Exception{
		List<TableCols> list = new ArrayList<TableCols>();
		String sql="select c.column_name,c.is_nullable,c.data_type,c.column_type,c.column_comment,c.column_default,c.character_maximum_length as var_len,NUMERIC_precision num_len,NUMERIC_scale num_sca " + 
				"from information_schema.columns c " + 
				"where c.table_schema = ? " + 
				"  and c.table_name = ? " + 
				"order by c.ordinal_position";
		ResultSet res = null;
		try{
			res = this.query(con, sql,dbName,tabName);
			TableCols tc = null;
			while(res.next()){
				tc = new TableCols();
				tc.setColumn_name(res.getString("column_name"));
				tc.setIs_nullable(res.getString("is_nullable"));
				tc.setData_type(res.getString("data_type"));
				tc.setColumn_type(res.getString("column_type"));
				tc.setColumn_comment(res.getString("column_comment"));
				tc.setColumn_default(res.getString("column_default"));
				tc.setVar_len(BaseUtils.string2long(res.getString("var_len")));
				tc.setNum_len(BaseUtils.string2int(res.getString("num_len")));
				tc.setNum_sca(BaseUtils.string2int(res.getString("num_sca")));
				list.add(tc);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}finally{
			if(res!=null){
				if(res.getStatement()!=null){
					res.getStatement().close();
				}
				res.close();
			}
		}
		return list;
	} 
    /**
     * 执行带参数的查询
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws Exception
     */
    private ResultSet query(Connection con,String sql,String ...values) throws Exception {
		PreparedStatement pStatm = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		for (int i = 0; i < values.length; i++) 
			pStatm.setString(i + 1, values[i]);
		return pStatm.executeQuery();
	}
    
    /**
     * 生成文件
     * @param tmpFilePath 目标文件
     * @param tmpPath 模板文件夹
     * @param tmpName 模板文件名
     * @param names 参数名
     * @param values 参数值
     * @param endTarg 结束符
     * @return
     * @throws Exception
     */
    private String makeFile(String tmpFilePath,String tmpPath,String tmpName,String[] names,Object[] values,String endTarg) throws Exception {
			//==生成mapper mapper只替换上半部分
			File file = new File(tmpFilePath);
			if(file.exists()) {//已经存在 则需要先取出原来的内容 
				String targ = "<!-- [=下面是业务自定义代码=] -->";
				String str = VelocityUtils.getStringByTemplateFile(tmpPath, tmpName, "utf-8", names, values);
				String oldStr = FileUtils.readFileContentChar(tmpFilePath, "");
				int index = oldStr.indexOf(targ);
				if(index>0) {
					index += targ.length();
					oldStr = oldStr.substring(index);//截取业务自定义的内容
					index = oldStr.indexOf("\r\n");
					if(index>=0) {
						oldStr = oldStr.substring(index+2);//截取掉换行符
					}
					int endIndex = str.lastIndexOf(endTarg);//"(</mapper>", "");//把结尾去掉
					if(endIndex>=0) {
						str = str.substring(0,endIndex-1);
					}
					str = str + oldStr;
				}
				FileUtils.writeFileContentChar(tmpFilePath,str,"utf-8",false);
			} else { //不存在 则按照初始化处理
				VelocityUtils.makeFileByTemplateFile(tmpPath, tmpName, "utf-8", names, values,tmpFilePath );
			}
			return "========"+tmpName+"["+tmpFilePath+"]=="+"\r\n";
    }
    
}
