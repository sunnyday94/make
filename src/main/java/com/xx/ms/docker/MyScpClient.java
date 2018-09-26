package com.xx.ms.docker;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class MyScpClient {

	private String ip="10.3.151.86";//开发环境
//	private String ip="192.168.25.26";//测试环境
    private int port=22;
    private String username="root";
    private String password="king123!@#";
    
	static private MyScpClient instance;
	
	private  Connection conn =null;
	
	public static boolean autoClose = true;

    static synchronized public MyScpClient getInstance(String IP, int port,
            String username, String password) {
        if (instance == null) {
            instance = new MyScpClient(IP, port, username, password);
            MyScpClient.autoClose=true;
        }
        return instance;
    }

    /**
     * @param ip
     * @param autoClose false时要手动关闭连接
     * @return
     */
    static synchronized public MyScpClient getInstance(String ip,boolean autoClose) {
        if (instance == null) {
            instance = new MyScpClient(ip);//IP, port, username, passward
            MyScpClient.autoClose=autoClose;
            System.out.println(ip);
        }
        return instance;
    }
//    static synchronized public MyScpClient getInstance() {
//        if (instance == null) {
//            instance = new MyScpClient();//IP, port, username, passward
//            MyScpClient.autoClose=true;
//        }
//        return instance;
//    }
    
    public MyScpClient() {}
    public MyScpClient(String IP) {
        this.ip = IP;
       
    }
    public MyScpClient(String IP, int port, String username, String password) {
        this.ip = IP;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    private synchronized void init() throws IOException{
    	if(conn==null){
    		conn = new Connection(ip,port);
    		conn.connect();
    		
    		boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false) {
                System.err.println("authentication failed");
            }
    	}
    }
    public void close(){
    	
    	if(autoClose && conn!=null){
    		conn.close();
    		MyScpClient.autoClose=true;//还原
    	}
    		
    }
    /**
     * @param remoteFile
     * @param localTargetDirectory
     */
    public void getFile(String remoteFile, String localTargetDirectory) {
        try {
        	init();
//            boolean isAuthenticated = conn.authenticateWithPassword(username,
//                    password);
//            if (isAuthenticated == false) {
//                System.err.println("authentication failed");
//            }
            SCPClient client = new SCPClient(conn);
            client.get(remoteFile, localTargetDirectory);
            close();
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }

   
    /**
     * @param localFile
     * @param remoteTargetDirectory
     */
    public void putFile(String[] localFile, String remoteTargetDirectory) {
        
        try {
            init();
//            boolean isAuthenticated = conn.authenticateWithPassword(username,
//                    password);
//            if (isAuthenticated == false) {
//                System.err.println("authentication failed");
//            }
            SCPClient client = new SCPClient(conn);
            client.put(localFile, remoteTargetDirectory);
            
            close();
            System.out.println("upload success");
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }
   
   
    /**
     * @param localFile
     * @param remoteFileName
     * @param remoteTargetDirectory
     * @param mode
     */
    public void putFile(String localFile, String remoteFileName,String remoteTargetDirectory,String mode) {
        
        try {
            init();
//            boolean isAuthenticated = conn.authenticateWithPassword(username,
//                    password);
//            if (isAuthenticated == false) {
//                System.err.println("authentication failed");
//            }
            SCPClient client = new SCPClient(conn);
            if((mode == null) || (mode.length() == 0)){
                mode = "0600";
            }
            client.put(localFile, remoteFileName, remoteTargetDirectory, mode);
            
            //重命名
            Session sess = conn.openSession();
            String tmpPathName = remoteTargetDirectory +File.separator+ remoteFileName;
            String newPathName = tmpPathName.substring(0, tmpPathName.lastIndexOf("."));
            sess.execCommand("mv " + remoteFileName + " " + newPathName);//重命名回来
           
            close();
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }
   
    public void exec(String cmd) {
        
        try {
            init();
            
//            SCPClient client = new SCPClient(conn);
            
            
            System.out.println("cmd ： "+cmd);
            //
            Session sess = conn.openSession();
            sess.requestDumbPTY();
            sess.execCommand(cmd);//
            InputStream stdout = new StreamGobbler(sess.getStdout());
//            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout,"UTF-8"));
            String res=null;
            
            while((res=br.readLine())!=null)
            	System.out.println(res);
//            5.得到脚本运行成功与否的标志 ：0－成功 非0－失败
            if(res==null){
            	stdout = new StreamGobbler(sess.getStderr());
            	br = new BufferedReader(new InputStreamReader(stdout,"UTF-8"));
            	while((res=br.readLine())!=null)
            		System.out.println(res);
            }

//            6.关闭session和connection

           
            System.out.println("exec status '0' success : " + sess.getExitStatus());
            sess.close();
            close();
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }

    
   
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream(1024*1024);
            byte[] b = new byte[1024*1024];
            int i;
            while ((i = fis.read(b)) != -1) {
                byteArray.write(b, 0, i);
            }
            fis.close();
            byteArray.close();
            buffer = byteArray.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

//    public static void main(String [] s){
//    	MyScpClient scp=new MyScpClient();
//    	List<String> localFiles=new ArrayList<String>();
//    	localFiles.add("D:/tmp/Chrysanthemum.jpg");
//    	
//    	
//    	MyScpClient.getInstance().putFile(localFiles.toArray(new String[localFiles.size()]), "/docker-dev/xm-fsp20-idvr/test");
//    }
    

   
}
