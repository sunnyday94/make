package com.xx.ms.docker;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

/**
 * @author sunny
 *
 */
public class ExecLinuxCmd {

	/**
	 * 192.168.25.21
	 */
	private static String ip="10.3.155.220";//49
//	192.168.25.49
	/**
	 * 192.168.25.26
	 */
//	private static String testip="192.168.25.26";
	
	/**
	 * 
	 */
	private static MyScpClient client;
	
	/** Image名字要包含tag在里面 如：discovery-server:1.0.0
	 * @param dockerImage
	 * @return
	 */
	public static boolean buildImage(String dockerImage,String dockerFilePath) {  
		
		client.exec("cd "+dockerFilePath+";docker build -t "+dockerImage+" .");
        return true;  
    }
	
	public static boolean pushRegistry(String dockerImage,String dockerFilePath) {  
		//docker tag 现有Image名:tag  想要的Image名:tag(重命名)
		client.exec("cd "+dockerFilePath+";docker tag "+dockerImage+" "+ip+":5000/"+dockerImage
				+";docker push "+ip+":5000/"+dockerImage);
//		docker push 10.3.151.220:5000/
        return true;  
    }
	
	/** Image名字要包含tag在里面 如：discovery-server:1.0.0
	 * @param dockerImage
	 * @param serverPort
	 * @param volumn 如：/docker-dev/xm-fsp20-xxxx/
	 * @return
	 */
	public static boolean runImage(String dockerImage,int serverPort,String volumn) {  
		String cmd="docker run  -m 512m -e arg1=\"--server.port="+serverPort
				+"\" -p "+serverPort+":"+serverPort;
		if(volumn!=null)
			cmd+= " -v "+volumn+":/ms ";
		cmd+= "--net=host";
		cmd+= " -itd "+dockerImage;
		client.exec(cmd);
		
		client.exec("docker ps -a");
        return true;  
    }
	
	/**重启容器
	 * @param containerId docker里的容器Id
	 * @return
	 */
	public static boolean restrartDocker(String containerId) {  
		
		client.exec("docker restart "+containerId);
        return true;  
    }
	/**停止容器
	 * @param containerId  docker里的容器Id
	 * @return
	 */
	public static boolean stopDocker(String containerId) {  
		
		client.exec("docker stop "+containerId);
        return true;  
    }
	
	
	static String imageName="xm-ms-resources:1.0.0";
	static String volumnPath="/docker-dev/xm-ms-resources/";
	
	/**参考config中设置
	 * @param args
	 */
	public static void main(String[] args) {
		client=MyScpClient.getInstance(ip,false);
//		client=MyScpClient.getInstance(testip,false);
		try {
//			admin(true,null);
//			order(true,null);
//			prduct(true,null);
//			jre(true,"123");
//			esdata(true,"ea9c04d057ac");
//			idvr(false,"63fe734f4ee2");//26= aa6738d3cbe8 //21= 2ceaffc10d5c
//			res(true,"c84f5fa87d6a");  //c84f5fa87d6a //   
//			cf(false,"03d0eb0f053a");// 4a589a8ea168 // 0d72c7ac9f93
//			openapi(false,"d9221a9fc8f3");
//			config(false,"112b10ba579b");
//			auth(true,"9fd15e5aaa99");// b7294617c0b6 //
//			eureka(true,"dc14451bb12b",8761);
			eureka(true,"d729a6be641c",8762);
//			zipKin(false,"1d4212a85395");
//			client.exec("docker restart d9221a9fc8f3");//7439ce5d2547 d9221a9fc8f3
			client.exec("docker ps -a");
			client.exec("docker images");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			//手动关闭
			client.close();
		}
		
	}

	public static void admin(boolean init,String cid){
		String image="ms-admin:1.0.0-SNAPSHOT";
		String name="ms-admin";
		List<String> localFiles=new ArrayList<String>();
		String fName="C:\\Users\\yangkunguo\\git\\VPhotoAdminAction\\build\\libs\\VPhotoAdminAction-0.0.1-SNAPSHOT.jar";
		String nName="C:\\Users\\yangkunguo\\git\\VPhotoAdminAction\\build\\libs\\ms-admin-1.0.0-SNAPSHOT.jar";
    	File file=new File(fName);
    	file.renameTo(new File(nName));
		localFiles.add("C:\\Users\\yangkunguo\\git\\VPhotoAdminAction\\Dockerfile");
    	localFiles.add(nName);
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f "+image);
    			client.exec("docker rmi -f 10.3.151.220:5000/"+image);
    		}
    		client.exec("cd /;pwd;mkdir docker-dev;pwd");
    		client.exec("cd /docker-dev;pwd;mkdir "+name+";pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		buildImage(image,"/docker-dev/"+name+"/");
    		pushRegistry(image,"/docker-dev/"+name+"/");
//    		runImage(image, 8110, "/docker-dev/"+name+"/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		
    		//restrartDocker(cid);
    	}
	}
	/**
	 * @param init
	 * @param cid
	 */
	public static void prduct(boolean init,String cid){
		String image="ms-product:1.0.0-SNAPSHOT";
		String name="ms-product";
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("C:\\Users\\yangkunguo\\git\\VPhotoMall\\"+name+"/Dockerfile");
    	localFiles.add("C:\\Users\\yangkunguo\\git\\VPhotoMall\\"+name+"/target/"+name+"-1.0.0-SNAPSHOT.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f "+image);
    			client.exec("docker rmi -f 10.3.151.220:5000/"+image);
    		}
    		client.exec("cd /;pwd;mkdir docker-dev;pwd");
    		client.exec("cd /docker-dev;pwd;mkdir "+name+";pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		buildImage(image,"/docker-dev/"+name+"/");
    		pushRegistry(image,"/docker-dev/"+name+"/");
//    		runImage(image, 8110, "/docker-dev/"+name+"/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		
    		//restrartDocker(cid);
    	}
	}
	public static void order(boolean init,String cid){
		String image="ms-order:1.0.0-SNAPSHOT";
		String name="ms-order";
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("C:\\Users\\yangkunguo\\git\\VPhotoMall\\"+name+"/Dockerfile");
    	localFiles.add("C:\\Users\\yangkunguo\\git\\VPhotoMall\\"+name+"/target/"+name+"-1.0.0-SNAPSHOT.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f "+image);
    			client.exec("docker rmi -f 10.3.151.220:5000/"+image);
    		}
    		client.exec("cd /;pwd;mkdir docker-dev;pwd");
    		client.exec("cd /docker-dev;pwd;mkdir "+name+";pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		buildImage(image,"/docker-dev/"+name+"/");
    		pushRegistry(image,"/docker-dev/"+name+"/");
//    		runImage(image, 8110, "/docker-dev/"+name+"/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		
    		//restrartDocker(cid);
    	}
	}
	public static void jre(boolean init,String cid){
		String image="jre8:171";
		String name="jre1.8";
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("C:\\Workspaces\\"+name+"/Dockerfile");
//    	localFiles.add("C:\\Workspaces\\"+name+"/target/"+name+"-1.0.0.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f "+image);
    		}
    		client.exec("cd /;pwd;mkdir docker-dev;pwd");
    		client.exec("cd /docker-dev;pwd;mkdir "+name+";pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		buildImage(image,"/docker-dev/"+name+"/");
    		pushRegistry(image,"/docker-dev/"+name+"/");
//    		runImage(image, 8110, "/docker-dev/"+name+"/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		
    		restrartDocker(cid);
    	}
	}
	public static void res(boolean init, String cid){
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("D:/workspace/workspace2/xm-ms-resources/Dockerfile");
    	localFiles.add("D:/workspace/workspace2/xm-ms-resources/target/xm-ms-resources-1.0.0.jar");
    	
//    	client.putFile(localFiles.toArray(new String[localFiles.size()]),
//    			"/docker-dev/xm-ms-resources/");
//    	restrartDocker("ecd6bda92f65");
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f xm-ms-resources:1.0.0");
    		}

    		client.exec("cd /docker-dev;pwd;mkdir xm-ms-resources;pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/xm-ms-resources/");
    		buildImage("xm-ms-resources:1.0.0","/docker-dev/xm-ms-resources/");
    		runImage("xm-ms-resources:1.0.0", 8301, "/docker-dev/xm-ms-resources/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/xm-ms-resources/");
    		
    		restrartDocker(cid);
    	}

	}
	public static void esdata(boolean init,String cid){
		String image="es-data:1.0.0";
		String name="es-data";
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("C:\\Workspaces\\"+name+"/Dockerfile");
//    	localFiles.add("C:\\Workspaces\\"+name+"/target/"+name+"-1.0.0.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f "+image);
    		}
    		client.exec("cd /;pwd;mkdir docker-dev;pwd");
    		client.exec("cd /docker-dev;pwd;mkdir "+name+";pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		buildImage(image,"/docker-dev/"+name+"/");
    		
    		runImage(image, 8011, "/docker-dev/"+name+"/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+name+"/");
    		
    		restrartDocker(cid);
    	}
	}
	
	public static void cf(boolean init, String cid){
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("D:/workspace/workspace2/xm-fsp20-cf/Dockerfile");
    	localFiles.add("D:/workspace/workspace2/xm-fsp20-cf/target/xm-fsp20-cf-0.0.1.jar");
    	
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f xm-fsp20-cf:0.0.1");
    		}
    		client.exec("cd /docker-dev;pwd;mkdir xm-fsp20-cf;pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/xm-fsp20-cf/");
    		buildImage("xm-fsp20-cf:0.0.1","/docker-dev/xm-fsp20-cf/");
    		
    		runImage("xm-fsp20-cf:0.0.1", 8112, "/docker-dev/xm-fsp20-cf/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/xm-fsp20-cf/");
    		
    		restrartDocker(cid);
    	}
    	
	}
	
	public static void openapi(boolean init,String cid){
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("D:/workspace/workspace2/xm-ms-openapi/Dockerfile");
    	localFiles.add("D:/workspace/workspace2/xm-ms-openapi/target/xm-ms-openapi-1.0.0.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f xm-ms-openapi:1.0.0");
    		}
    		
    		client.exec("cd /docker-dev;pwd;mkdir xm-ms-openapi;pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/xm-ms-openapi/");
    		buildImage("xm-ms-openapi:1.0.0","/docker-dev/xm-ms-openapi/");
    		
    		runImage("xm-ms-openapi:1.0.0", 8000, "/docker-dev/xm-ms-openapi/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/xm-ms-openapi/");
    		
    		restrartDocker(cid);
    	}
    	
	}
	public static void config(boolean init,String cid){
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("D:/workspace/workspace2/xm-ms-configserver/Dockerfile");
    	localFiles.add("D:/workspace/workspace2/xm-ms-configserver/target/xm-ms-configserver-1.0.0.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f xm-ms-configserver:1.0.0");
    		}
    		
    		client.exec("cd /docker-dev;pwd;mkdir xm-ms-configserver;pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/xm-ms-configserver/");
    		buildImage("xm-ms-configserver:1.0.0","/docker-dev/xm-ms-configserver/");
    		 
    		runImage("xm-ms-configserver:1.0.0", 8765, "/docker-dev/xm-ms-configserver/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/xm-ms-configserver/");
    		
    		restrartDocker(cid);
    	}
    	
	}

	public static void zipKin(boolean init,String cid){
		String image="xm-ms-zipkinserver:1.0.0";
		String server="xm-ms-zipkinserver";
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("D:/workspace/workspace2/"+server+"/Dockerfile");
    	localFiles.add("D:/workspace/workspace2/"+server+"/target/"+server+"-1.0.0.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f "+image);
    		}
    		client.exec("cd /docker-dev;pwd;mkdir "+server+";pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+server+"/");
    		buildImage(image,"/docker-dev/"+server+"/");
    		 
    		runImage(image, 9010, "/docker-dev/"+server+"/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+server+"/");
    		
    		restrartDocker(cid);
    	}
	}
	
	public static void eureka(boolean init,String cid,int port){
		String image="discovery-server:1.0.0";
		String server="discovery-server";
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("C:\\tools\\demo\\xm-ms-discovery-server/Dockerfile");
    	localFiles.add("C:\\tools\\demo\\xm-ms-discovery-server/target/"+server+"-1.0.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
//    			client.exec("docker rmi -f "+image);
    		}
    		client.exec("cd /docker-dev;pwd;mkdir "+server+";pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+server+"/");
    		buildImage(image,"/docker-dev/"+server+"/");
    		
    		runImage(image, port, "/docker-dev/"+server+"/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+server+"/");
    		
    		restrartDocker(cid);
    	}
	}
	
	public static void auth(boolean init,String cid){
		String image="xm-ms-authserver:1.0.0";
		String server="xm-ms-authserver";
		List<String> localFiles=new ArrayList<String>();
    	localFiles.add("D:/workspace/workspace2/"+server+"/Dockerfile");
    	localFiles.add("D:/workspace/workspace2/"+server+"/target/"+server+"-1.0.0.jar");
    	
    	
    	if(init){
    		if(cid!=null){
    			
    			client.exec("docker stop "+cid+";docker rm -f "+cid);
    			client.exec("docker rmi -f "+image);
    		}
    		client.exec("cd /docker-dev;pwd;mkdir "+server+";pwd");
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+server+"/");
    		buildImage(image,"/docker-dev/"+server+"/");
    		try {
				Thread.sleep(1000*2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		runImage(image, 8201, "/docker-dev/"+server+"/");
    	}else{
    		client.putFile(localFiles.toArray(new String[localFiles.size()]),
    				"/docker-dev/"+server+"/");
    		
    		restrartDocker(cid);
    	}
	}
}
