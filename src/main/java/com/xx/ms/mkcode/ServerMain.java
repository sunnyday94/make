package com.xx.ms.mkcode;

import com.xx.ms.mkcode.service.MakeCodeService;
import com.xx.ms.mkcode.utils.DBConnect;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {

	public static void main(String[] args) throws Exception {
		String 
		 	ip = "10.64.104.1", //数据库地址
			dbName="vphotos",//数据库名称
			user="root",//数据库用户
			pass="xxxx";//数据库密码 
		String tabs = "V_TBL_SYSUSER";//"//需要处理的表 可以使用逗号分割的多个表   还可以使用带%号的条件
		
		String fileSrcPath = "";//文件存放路径
		String fileMapperPath = "";//mapper文件存放位置 。如果为空则会根据fileSrcPath存放到对应的目录
		String mappingName="mall-xx-v1";//微服务名
		
		String userName = "sunny";//开发负责人
		String module = "token";//
		Map<String,Boolean> makeFlg = new HashMap<String,Boolean>();
		boolean bean=true;
		makeFlg.put("bean", bean);
		makeFlg.put("bo", bean);
		makeFlg.put("mapper", false);
		boolean yw=true;
		makeFlg.put("controller", yw);
		makeFlg.put("dao", yw);
		makeFlg.put("iservice", yw);
		makeFlg.put("service", yw);
		makeFlg.put("mapping", yw);

		String basePkg="com.xx.mall.token.provider";
		
		Connection con = null;
		try {
			System.out.println("====1.开始执行====");
			con = DBConnect.getConByCFMysql(ip, dbName, user, pass);
			System.out.println("====2.数据库连接成功====");
			MakeCodeService service = new MakeCodeService(basePkg, mappingName);
			service.makeCode(con, dbName, tabs, fileSrcPath, fileMapperPath, userName,module,makeFlg);
			System.out.println("====3.执行完成====");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(con!=null)
				con.close();
		}
		

	}

}
