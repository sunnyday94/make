package com.xx.ms.mkcode.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 文件处理类
 * 简述：简要说明该类功能 <br>
 * 详细描述：<br>
 * 详细说明该类完成主要功能和注意点 <br>
 * 时间： 2017年11月13日 下午5:00:56 <br> 
 * 版权: Copyright 2017-2050©徙木金融信息服务（上海）有限公司 All Rights Reserved.<br>
 * @author  lizikui 
 * @version V1.0
 */
public class FileUtils extends BaseUtils{

	/**
	 * 读取文件内容<br>
	 * 默认是按照 \r\n 进行分割每行的内容
	 * @param filePath
	 * @return String
	 * @throws Exception
	 */
	public static String readFileContentChar(String filePath,String split)throws Exception{
		if(isNull(filePath))return null;
		if(isNull(split))split = "\r\n";
		File f = new File(filePath);
		if(!f.exists())return null;
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = null;
			while((line=br.readLine())!=null){
				sb.append(null2String(line)).append(split);
			}
		}catch(Exception ex){
			
		}finally{
			if(br!=null)
				br.close();
		}
		return sb.toString();
	}
	
	/**
	 * 读取文件内容 
	 * @param filePath
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readFileContentByte(String filePath)throws Exception{
		if(isNull(filePath))return null;
		File f = new File(filePath);
		if(!f.exists())return null;
		return readFileContentByte(new FileInputStream(f));
	}
	
	/**
	 * 读取文件内容 
	 * @param is
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readFileContentByte(InputStream is)throws Exception{
		if(isNull(is))return null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
		int MAX_LEN = 1024*8;
		byte[] buf = new byte[MAX_LEN];
		int num = -1;
		try{
			bis = new BufferedInputStream(is);
			bos = new ByteArrayOutputStream();
			while((num=bis.read(buf, 0, MAX_LEN))!=-1){
				bos.write(buf,0,num);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}finally{
			if(bis!=null)bis.close();
			if(bos!=null)bos.close();
		}
		return bos.toByteArray();
	}
	
	
	/**
	 * 为文件写内容
	 *  
	 * @param filePath 文件路径
	 * @param fileContent 文件内容
	 * @param encoding 字符集 默认UTF-8 
	 * @param append 是否是追加，true追加，false 把内容置空 然后把最新的内容写入
	 * @throws Exception
	 */
	public static void writeFileContentChar(String filePath,String fileContent,String encoding,boolean append)throws Exception{
		writeFileContentByte(filePath, fileContent.getBytes(encoding),append);
	}
	
	/**
	 * 为文件写内容
	 * @param filePath
	 * @param fileContent
	 * @param append
	 * @throws Exception
	 */
	public static void writeFileContentByte(String filePath,byte[] fileContent,boolean append)throws Exception{
		if(fileContent==null)return;
		//==去除windows自动添加的utf-8的bom  EF BB BF 
		ByteArrayInputStream bis =  new ByteArrayInputStream(fileContent);
		if(fileContent.length>=3&&fileContent[0]==-17&&fileContent[1]==-69&&fileContent[2]==-65) {
			bis.skip(3);
		}
		writeFileContentByte(filePath, bis,append);
	}
	
	/**
	 * 为文件写内容
	 * @param filePath
	 * @param is
	 * @param append
	 * @throws Exception
	 */
	public static void writeFileContentByte(String filePath,InputStream is,boolean append) throws Exception{
		if(isNull(filePath))return;
		File f = new File(filePath);
		if(!f.exists()){//判断文件是否存在
			if(f.getParentFile().exists()) //生成文件路径
				f.getParentFile().mkdirs();
		}
		if(!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if(!f.exists())f.createNewFile();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		int MAX_LEN = 1024*5;
		byte[] buf = new byte[MAX_LEN];
		int num = -1;
		try{
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(new FileOutputStream(f,append));
			while((num=bis.read(buf, 0, MAX_LEN))!=-1){
				bos.write(buf,0,num);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}finally{
			if(bos!=null)bos.close();
			if(bis!=null)bis.close();
		}
	}
	
	/**
	 * 获取所有文件的路径
	 * @param filePath
	 * @param files
	 * @throws Exception
	 */
	public static void getFilePaths(String filePath,List<String> files)throws Exception{
		if(isNull(filePath))return;
		File f = new File(filePath);
		if(!f.exists())return;
		if(f.isFile()){
			files.add(f.getPath());
		}else{
			String fileNames[] = f.list();
			for(String fn:fileNames){
				getFilePaths(f.getPath()+"/"+fn,files);
			}
		}
	}
	
	/**
	 * 解压
	 * @param deZipName 文件名称 必须包括全路径
	 * @param dePath 解压的目录
	 * @return  boolean
	 * @throws Exception
	 */
	public static boolean deZip(String deZipName,String dePath)throws Exception{
		if(isNull(deZipName)){
			throw new Exception("解压失败，请输入待解压文件名称！");
		}
		File deFile = new File(deZipName);
		if(!deFile.exists()){
			throw new Exception("解压失败，输入的文件名【"+deZipName+"】不存在！");
		}
		if(!deFile.isFile()){
			throw new Exception("解压失败，输入的文件名【"+deZipName+"】不是文件！");
		}
		//如果没有重新指定，则直接解压到当前文件夹下
		if(isNull(dePath)){
			String tmpName = deFile.getName();
			int i = tmpName.lastIndexOf(".");
			if(i>=0){
				tmpName = tmpName.substring(0,i);
			}
			dePath = deFile.getPath()+"/"+tmpName;
		}
		dePath = dePath.replaceAll("\\\\", "/");
		if(!dePath.endsWith("/")){
			dePath+="/";
		}
		File dePathDir = new File(dePath);
		if(!dePathDir.exists())
			dePathDir.mkdirs();
		ZipFile deZipFile = new ZipFile(deFile);//实例化ZipFile，每一个zip压缩文件都可以表示为一个ZipFile
		ZipInputStream zis = new ZipInputStream(new FileInputStream(deFile));
		//解压
		ZipEntry zipEntry = null;
		InputStream is = null;
		try{
			while ((zis.available()==1)&&(zipEntry = zis.getNextEntry()) != null) {
		        //通过ZipFile的getInputStream方法拿到具体的ZipEntry的输入流
		        is = deZipFile.getInputStream(zipEntry);
		        writeFileContentByte(dePath + zipEntry.getName(), is,false);
		    }
		}catch(Exception ex){
			throw ex;
		}finally{
			deFile = null;
			dePathDir = null;
			if(deZipFile!=null){
				try{
					deZipFile.close();
				}catch(Exception ex){
				}
			}
			if(zis!=null){
				try{
					zis.closeEntry();
				}catch(Exception ex){
				}
				try{
					zis.close();
				}catch(Exception ex){
				}
				
			}
			if(is!=null){
				try{
					is.close();
				}catch(Exception ex){
				}
			}
		}
	    return true;
	}

	/**
	 * 删除单个文件
	 * @param f
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean delFile(String filePath)throws Exception{
		if(isNull(filePath))return false;
		File f = new File(filePath);
		if(!f.exists())return false;
		if(f.isFile()){
			f.delete();
		}
		return true;
	}
	
	/**
	 * 删除单个文件
	 * @param f
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean delFile(File f)throws Exception{
		if(isNull(f))return false;
		if(!f.exists())return false;
		if(f.isFile()){
			f.delete();
		}
		return true;
	}
	
	/**
	 * 删除文件夹
	 * @param filePath
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean delDir(String filePath)throws Exception{
		if(isNull(filePath))return false;
		File f = new File(filePath);
		return delFolder(f);
	}
	
	
	/**
	 * 删除文件夹
	 * @param folder
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean delFolder(File folder)throws Exception{
		if(isNull(folder))return false;
		if(folder.isFile()){
			return delFile(folder);
		}else{
			File[] subFiles = folder.listFiles();
			if(subFiles==null||subFiles.length<=0)return false;
			for(File sf : subFiles){
				if(sf.isFile()){
					delFile(sf);
				}else{
					delFolder(sf);
				}
			}
			folder.delete();//把自己删除
			return true;
		}
	}
	/**
	 * 拷贝
	 * @param oldPath
	 * @param newPath
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean copy(String oldPath, String newPath) throws Exception{ 
		  try {
			   if(isNull(oldPath)||isNull(newPath)) return false;
			   File oldFile = new File(oldPath); 
			   if(!oldFile.exists())return false;
			   if(oldFile.isFile()){
				   return copyFile(oldPath, newPath);
			   }else{
				   return copyFolder(oldPath, newPath);
			   }
		  }catch (Exception ex) { 
	           ex.printStackTrace(); 
//	           throw ex;
	           return false;
	       } 
	}
	/** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public static boolean copyFile(String oldPath, String newPath) throws Exception{ 
	   InputStream is = null;
	   OutputStream os = null;
	   byte[] buffer = new byte[1024*10];        
	   try {
		   if(isNull(oldPath)||isNull(newPath)) return false;
		   File oldFile = new File(oldPath); 
		   if(!oldFile.exists())return false;
		   File newFile = new File(newPath);
		   if(!newFile.getParentFile().exists()){
			   newFile.getParentFile().mkdirs();
		   }
		   is = new FileInputStream(oldFile);
		   os = new FileOutputStream(newFile); 
           int length; 
           while ( (length = is.read(buffer)) != -1) { 
              os.write(buffer, 0, length); 
           } 
       }catch (Exception ex) { 
           ex.printStackTrace(); 
//           throw ex;
           return false;
       } finally{
    	   if(is!=null){
    		   is.close();
    	   }
    	   if(os!=null){
    		   os.close();
    	   }
       }
       return true;
   } 
	
   /** 
    * 复制整个文件夹内容 
    * @param oldPath String 原文件路径 如：c:/fqf 
    * @param newPath String 复制后路径 如：f:/fqf/ff 
    * @return boolean 
    */ 
  public static boolean copyFolder(String oldPath, String newPath) throws Exception{ 
	  if(isNull(oldPath)||isNull(newPath)) return false;
	   File oldFolder = new File(oldPath); 
	   File newFolder = new File(newPath);
	   return copyFolder(oldFolder, newFolder);
  }
  
  /** 
   * 复制整个文件夹内容 
   * @param oldFolder String 原文件路径 如：c:/fqf 
   * @param newFolder String 复制后路径 如：f:/fqf/ff 
   * @return boolean 
   */ 
 public static boolean copyFolder(File oldFolder, File newFolder) throws Exception{ 
	 try {
	   if(!oldFolder.exists())return false;
	   if(!newFolder.exists()){
		   newFolder.mkdirs();
	   }
	   File[] fList = oldFolder.listFiles();
	   for(File f : fList){
		   if(f.isFile()){
			   copyFile(f.getPath(), newFolder.getPath()+"/"+f.getName());
	   }else{
		   copyFolder(f,new File(newFolder.getPath()+"/"+f.getName()));
		   }
	   }
	   return true;
	 }catch (Exception ex) { 
         ex.printStackTrace(); 
//         throw ex;
         return false;
     } 
 }
 /**
  * 移动
  * @param oldPath
  * @param newPath
  * @return boolean
  * @throws Exception
  */
 public static boolean moveFile(String oldPath, String newPath)throws Exception{
	 if(copyFile(oldPath, newPath)){//如果copy成功
		 return delFile(new File(oldPath));
	 }
	 return false;
 }
 /**
  * 移动文件夹
  * @param oldPath
  * @param newPath
  * @return boolean
  * @throws Exception
  */
 public static boolean moveFolder(String oldPath, String newPath)throws Exception{
	 if(copyFolder(oldPath, newPath)){
		 return delFolder(new File(oldPath));
	 }
	 return false;
 }
 
/**
 * 对文件进行排序
 * @param fliePath
 * @return  List<File>
 */
public static List<File> orderFileByTime(String fliePath) {
	if(isNull(fliePath))return null;
	File file = new File(fliePath);
	List<File> files  = null;
	if(file.isFile()){
		files = new ArrayList<File>();
		files.add(file);
		return files;
	}
    files = Arrays.asList(file.listFiles());
    Collections.sort(files, new Comparator< File>() {
    	public int compare(File o1, File o2) {
    		if (o1.isDirectory() && o2.isFile())
    			return -1;
    		if (o1.isFile() && o2.isDirectory())
    			return -1;
    		return Long.valueOf(o1.lastModified()-o2.lastModified()).intValue();
    	}
    });
  return files;
}
	
}
