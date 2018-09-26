package com.xx.ms.mkcode.utils;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * <p>Title: 数据库连接的各种方式</p>
 * <p>Description:
 *  提供了4种数据连接方式,access、oracle、sqlServer、jndi
 * </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * @author lizk
 * @version 1.0
 */
public class DBConnect {

  /**
   * 构造函数
   */
  public DBConnect() {
  }

  /**
   * 通过jndi来取得数据库的资源然后取得连接
   * @param dsName String 数据源名称
   * @throws Exception
   * @return Connection
   * @exception
   */
  public static Connection getConByJNDI(String dsName) throws Exception {
	  Connection con=null;
	  DataSource ds = null;
    try {
      //获的当前环境
      Context ctx = new InitialContext();
      ds = (DataSource) ctx.lookup("java:comp/env/" + dsName);
      con = ds.getConnection();
    }
    catch (Exception ex) {
      throw new Exception("JNDI得到数据源出错了！dsName=" + dsName +";具体原因："+ ex.getMessage());
    }
    return con;
  }

  /**
   * 连接access数据库
   * @param dbName String   &nbsp;是数据库的名称和地址，一定是绝对的地址 :E:\test\mode\mode\db.mdb
   * @param userName String &nbsp;是系统用户登陆数据库的名称
   * @param userPass String &nbsp;是系统用户登陆数据库的密码
   * @throws Exception
   * @return Connection
   * @exception   如果用户名称和密码是null则返回是null
   */
  public static Connection getConByCFAccess(String dbName, String userName,
                                     String userPass) throws Exception {
	  Connection con=null;
    //连接access
    String ClassForName = "sun.jdbc.odbc.JdbcOdbcDriver";
    String ServerAndDB =
        "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=" + dbName;
    try {
      Class.forName(ClassForName);
      if (userName != null)
        con = DriverManager.getConnection(ServerAndDB, userName,
                                          (userPass == null ? "" : userPass));
    }
    catch (Exception ex) {
      throw new Exception("access数据库连接出错了！dbName=" + dbName + ";userName=" +
                        userName + ";pass=" + userPass + ex.getMessage());
    }
    return con;
  }

  /**
   * 连接sqlServer数据库
   * @param ip String  是服务器地址
   * @param dbName String 是数据库的名称和地址，一定是绝对的地址
   * @param userName String 用户登陆的名称
   * @param userPass String 用户登陆的密码
   * @throws Exception
   * @return Connection
   * @exception   如果用户名称和密码是null则返回是null
   */
  public static Connection getConByCFSqlServer(String ip, String dbName,
                                        String userName, String userPass) throws
      Exception {
	  Connection con=null;
    //连接sqlserver
    String ClassForName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    String ServerAndDB = "jdbc:microsoft:sqlserver://" + ip +
        ":1433;DatabaseName=" + dbName;
    try {
      Class.forName(ClassForName);
      if (userName != null && ip != null && dbName != null)
        con = DriverManager.getConnection(ServerAndDB, userName,
                                          (userPass == null ? "" : userPass));
    }
    catch (Exception ex) {
      throw new Exception("sqlserver数据库连接出错了！ip=" + ip + ";dbName=" + dbName +
                        ";userName=" + userName + ";userPass" + userPass +
                        ex.getMessage());
    }
    return con;
  }

  /**
   * 连接oracle数据库
   * @param ip String  是服务器地址
   * @param dbName String 是数据库的名称和地址，一定是绝对的地址
   * @param userName String 用户登陆的名称
   * @param userPass String 用户登陆的密码
   * @throws Exception
   * @return Connection
   * @exception   如果用户名称和密码是null则返回是null
   */
  public static Connection getConByCFOracle(String ip, String dbName,
                                     String userName, String userPass) throws
      Exception {
	  Connection con=null;
    //连接oracle
    String ClassForName = "oracle.jdbc.driver.OracleDriver";
    String ServerAndDB = "jdbc:oracle:thin:@" + ip + ":1521:" + dbName;
    try {
      Class.forName(ClassForName).newInstance();
      if (userName != null && ip != null && dbName != null)
        con = DriverManager.getConnection(ServerAndDB, userName,
                                          (userPass == null ? "" : userPass));
    }
    catch (Exception ex) {
      throw new Exception("oracle数据库连接出错了！ip=" + ip + ";dbName=" + dbName +
                        ";userName=" + userName + ";userPass" + userPass +
                        ex.getMessage());
    }
    return con;
  }
  
  /**
   * 连接mySql数据库
   * @param ip String  是服务器地址
   * @param dbName String 是数据库的名称和地址，一定是绝对的地址
   * @param userName String 用户登陆的名称
   * @param userPass String 用户登陆的密码
   * @throws Exception
   * @return Connection
   * @exception   如果用户名称和密码是null则返回是null
   */
  public static Connection getConByCFMysql(String ip, String dbName,
                                     String userName, String userPass) throws
      Exception {
	  Connection con=null;
    //连接mysql
    String ClassForName = "com.mysql.jdbc.Driver";
    String ServerAndDB = "jdbc:mysql://"+ip+":3306/"+dbName+"?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    try {
      Class.forName(ClassForName).newInstance();
      if (userName != null && ip != null && dbName != null)
        con = DriverManager.getConnection(ServerAndDB, userName,
                                          (userPass == null ? "" : userPass));
    }
    catch (Exception ex) {
      throw new Exception("mysql数据库连接出错了！ip=" + ip + ";dbName=" + dbName +
                        ";userName=" + userName + ";userPass" + userPass +
                        ex.getMessage());
    }
    return con;
  }

  /**
   * 测试方法，方便测试的
   * @param arg String[]
   */
  public static void main(String arg[]) {
//    Connection con = null;
////    DBConnect dbConnect = new DBConnect();
//    try {
    	
    	
//      //1.测试getConByCFAccess()
//      String dbName = "E:/test/mode/mode/db.mdb"; //是实际的地址和数据库名称
//      String userName = "sa";
//      String userPass = "sa";
      //
//      //如果是在网站内，则要用这个可以得到相对的地址 String path = request.getSession().getServletContext().getRealPath("db.mdb");
//      con = dbConnect.getConByCFAccess(dbName, userName, userPass);
      /*//2 测试  getConByCFSqlServer
            String ip="11.1.1.66";
            dbName = "test";//是数据库名称
            userName = "sa";
            userPass = "sa";
            con = dbConnect.getConByCFSqlServer(ip,dbName,userName,userPass);
            }
      */
     //3 测试  getConByCFOracle()
//	     String ip="127.0.0.1";
//	     String dbName = "ksfxpj";  //是数据库名称
//	     String userName = "common";
//	     String userPass = "common1234";
//	     con = dbConnect.getConByCFOracle(ip,dbName,userName,userPass);
//	     BaseDao bd = new BaseDao(con); 
//	    
//	     TestInfo ti = new TestInfo();
//	     ti.setGndbm("T10%");
//	     List<Map<String,Object>> list = bd.executeQueryMapList("select GNDBM, GNDMC from common.T_COMMON_D_DEMO where gndbm like ?",ti,new String[]{"gndbm"});
//	     for(Map<String,Object> one:list){
//	    	 for(String key:one.keySet())
//	    		 System.out.println(key+"\t"+one.get(key));
//	     }
	     
//	     Map<String,Object> one = bd.executeQueryMap("select GNDBM, GNDMC from common.T_COMMON_D_DEMO ");
//	     for(String key:one.keySet())
//	    	System.out.println(key+"\t"+one.get(key));

//	     TestInfo one = bd.executeQueryBean("select GNDBM, GNDMC from common.T_COMMON_D_DEMO ", TestInfo.class);
//	     System.out.println(one.getGndbm()+"=="+one.getGndmc());
//	     
	     
//	     List<TestInfo> list = bd.executeQueryBeanList("select GNDBM, GNDMC from common.T_COMMON_D_DEMO ", TestInfo.class);
//	     for(TestInfo one:list){
//	    	 if(one.getGndbm()==null)
//	    		System.out.println("哈哈哈哈哈哈"+"=="+one.getGndmc());
//	    	 else
//	    		System.out.println(one.getGndbm()+"=="+one.getGndmc());
//	     }
	     
//	     Object[] array = bd.executeQueryArray("select GNDBM, GNDMC from common.T_COMMON_D_DEMO ");
//	     for(Object one:array){
//	    	 System.out.println(one);
//	     }
	     
//	     List<Object[]> list = bd.executeQueryArrayList("select GNDBM, GNDMC from common.T_COMMON_D_DEMO ");
//	     for(Object[] array:list){
//		     for(Object one:array){
//		    	 System.out.println(one);
//		     }
//	     }
	     
//	     List<Object> list = bd.executeQueryColumnList("select GNDBM, GNDMC from common.T_COMMON_D_DEMO ","GNDBM");
//	     for(Object one:list){
//		     System.out.println(one);
//	     }
	     
//	     List<Object> list = bd.executeQueryColumnList("select GNDBM, GNDMC from common.T_COMMON_D_DEMO ",1);
//	     for(Object one:list){
//		     System.out.println(one);
//	     }
     
//	     Map<Object, Map<String,Object>> key = bd.executeQueryKey("select GNDBM, GNDMC from common.T_COMMON_D_DEMO ","GNDBM");
//	     Map<String,Object> m = key.get("GNDBM");
//	     for(Object one:key.get("T10").keySet()){
//	    	 System.out.println(one);
//	     }
	     
	     
//	     String one = bd.executeQueryScalar("select to_char(common.SEQ_COMMON_D_DEMO_ID.nextval) seq from dual ",1);
//	     System.out.println(one);
	     
//	     String nextValue = bd.getNextValOfSeq("common.SEQ_COMMON_D_DEMO_ID");
//	     System.out.println(nextValue);
	     
//	     int count = bd.executeQueryCount(" select count(1) from common.T_COMMON_D_DEMO");
//	     System.out.println(count);
    	
    	
//    	//测试mysql
//    	 String ip="127.0.0.1";
//	     String dbName = "test";  //是数据库名称
//	     String userName = "root";
//	     String userPass = "123456";
//	     con = dbConnect.getConByCFMysql(ip,dbName,userName,userPass);
//	     System.out.print(con);
//	     BaseDao bd = new BaseDao(con); 
//		    
//	     TestInfo ti = new TestInfo();
//	     ti.setGndbm("T10%");
//	     List<Map<String,Object>> list = bd.executeQueryMapList("select GNDBM, GNDMC from slxx.T_COMMON_D_DEMO where gndbm like ?",-1,0,ti,new String[]{"gndbm"});
//	     for(Map<String,Object> one:list){
//	    	 for(String key:one.keySet())
//	    		 System.out.println(key+"\t"+one.get(key));
//	     }
//    }
//    catch (Exception ex) { //抛出错误
//      System.out.println(ex.toString());
//    }
//    finally {
//      try {
//        con.close();
//      }
//      catch (Exception ex) {
//System.out.println(ex.toString());
//      }
//    }
  }
}
