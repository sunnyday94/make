package com.xx.ms.mkcode.utils;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.xx.ms.mkcode.utils.FileUtils;
/**
 * 简述：简要说明该类功能 <br>
 * 详细描述：<br>
 * 详细说明该类完成主要功能和注意点 <br>
 * 时间： 2017年11月20日 下午2:04:20 <br> 
 * 版权: Copyright 2017-2050©徙木金融信息服务（上海）有限公司 All Rights Reserved.<br>
 * @author  lizikui 
 * @version V1.0
 */
public class VelocityUtils {

	/**
	 * 根据模板取得传入参数后的字符串
	 * 
	 * @param tmpPath
	 *            模板路径
	 * @param tmpFile
	 *            模板名称，如果为空则抛出异常
	 * @param encoding
	 *            取模板的字符集，如果为空则按照默认的utf-8
	 * @param names
	 *            所有变量名称集合
	 * @param values
	 *            所有变令对应的值
	 * @return 返回生成的对应的视图的字符串
	 * @throws Exception
	 */
	public static synchronized String getStringByTemplateFile(String tmpPath,String tmpFile, String encoding, String[] names, Object[] values)throws Exception {
		if (tmpFile == null || tmpFile.equals(""))
			throw new Exception("取得模板文件名称为空，无法生成对应的");
		// 默认字符集为gbk
		if (encoding == null || encoding.equals(""))
			encoding = "gbk";
		int len = 0;// 设置初始化变量个数
		// 通过计算取names和values中个数少的为长度，有一个为null，则就不向里面设置变量，则得到就是视图模板
		if (names != null && values != null)
			len = names.length < values.length ? names.length : values.length;
		// 初始化环境变量
		Properties p = new Properties();
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, tmpPath);// 存放模板的地址
		VelocityEngine ve = new VelocityEngine();
		ve.init(p);
		// 根据模板生成对应的对象
//		System.out.println("tmpPath===="+tmpPath+"============tmpFile===="+tmpFile);
		Template template = ve.getTemplate(tmpFile, encoding);
		VelocityContext context = new VelocityContext();
		for (int i = 0; i < len; i++)
			context.put(names[i], values[i]);
		
		
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}
	
	/**
	 * 根据模板取得传入参数后的字符串 这儿适合于，模板里面的变量名字是对象名字.属性的，只要给对象赋值就行了
	 * 
	 * @param tmpPath
	 *            模板路径
	 * @param tmpFile
	 *            模板名称，如果为空则抛出异常
	 * @param encoding
	 *            取模板的字符集，如果为空则按照默认的utf-8
	 * @param objName
	 *            所有变量名称
	 * @param objValue
	 *            所有变令对应的
	 * @return 返回生成的对应的视图的字符串
	 * @throws Exception
	 */
	public static synchronized String getStringByTemplateFile(String tmpPath,String tmpFile, String encoding, String objName, Object objValue)throws Exception {
		return getStringByTemplateFile(tmpPath, tmpFile, encoding,new String[] { objName }, new Object[] { objValue });
	}

	/**
	 * 根据模版和参数生成文件
	 * 
	 * @param tmpPath
	 * @param tmpFile
	 * @param encoding
	 * @param names
	 * @param values
	 * @param destPath
	 * @param destFile
	 * @throws Exception
	 */
	public static synchronized void makeFileByTemplateFile(String tmpPath,String tmpFile, String encoding, String[] names, Object[] values,String destPath, String destFile) throws Exception {
		if (destPath == null || destPath.length() <= 0)
			return;
		if (destFile == null || destFile.length() <= 0)
			return;
		makeFileByTemplateFile(tmpPath, tmpFile, encoding, names, values, destPath + "/" + destFile);
	}
	
	/**
	 * 根据模版和参数生成文件
	 * 
	 * @param tmpPath
	 * @param tmpFile
	 * @param encoding
	 * @param names
	 * @param values
	 * @param destPath
	 * @param destFile
	 * @throws Exception
	 */
	public static synchronized void makeFileByTemplateFile(String tmpPath,String tmpFile, String encoding, String[] names, Object[] values,String destFilePath) throws Exception {
		if (destFilePath == null || destFilePath.length() <= 0)
			return;
		String str = getStringByTemplateFile(tmpPath, tmpFile, encoding, names,values);// 生成文件的str
		FileUtils.writeFileContentChar(destFilePath,str,encoding,false);
	}

	/**
	 * 根据模版和参数生成文件
	 * 
	 * @param tmpPath
	 * @param tmpFile
	 * @param encoding
	 * @param names
	 * @param values
	 * @param destPath
	 * @param destFile
	 * @throws Exception
	 */
	public static synchronized void makeFileByTemplateFile(String tmpPath,String tmpFile, String encoding, String objName, Object objValue,String destPath, String destFile) throws Exception {
		makeFileByTemplateFile(tmpPath, tmpFile, encoding,new String[] { objName }, new Object[] { objValue }, destPath,destFile);
	}
	
	public static synchronized void makeFileByTemplateFile(String tmpPath,String tmpFile, String encoding, String objName, Object objValue, String countName, int countValue,String destPath, String destFile) throws Exception {
		makeFileByTemplateFile(tmpPath, tmpFile, encoding,new String[] { objName,countName}, new Object[] { objValue,countValue}, destPath,destFile);
	}

}
