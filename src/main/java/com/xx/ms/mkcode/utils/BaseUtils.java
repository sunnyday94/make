package com.xx.ms.mkcode.utils;

import java.math.BigDecimal;
import java.util.Map;


/**
 * @Description: 工具类的基础类,判空处理
 * @Author: sunny
 * @Date: 2018/9/26 12:44
 */
public class BaseUtils {

	//=================1.字符串与空的处理=============================================
	/**
	 * null2string<br>
	 * 1.把指针NULL 转换成""字符串<br>
	 * 2.把字符串 null 转换成""<br>
	 * 校验str字符串是否是空、null(字符串)、nullnull等，<br>
	 * 如果是直接返回"" <br>
	 * 如果不是返回原字符串<br>
	 * @param str
	 * @return String
	 */
	public static String null2String(String str) {
		if (str == null || str.trim().length() <= 0 || str.trim().toLowerCase().equals("null")
				|| str.trim().toLowerCase().equals("nullnull") || str.trim().toLowerCase().equals("nullnullnull")
				|| str.trim().toLowerCase().equals("nullnullnullnull")) {
			return "";
		}
		return str;
	}

	/**
	 * 
	 * @param l
	 * @return
	 */
	public static Long null2long(Long l) {
		if(l==null)return 0L;
		return l;
	}
	/**
	 * 过滤null
	 * @param obj
	 * @return
	 */
	public static String null2String(Object obj) {
		if(obj==null)return "";
		return obj.toString();
	}
	
	/**
	 * 判断字符串是否为空<br>
	 * 
	 * @param str
	 * @return 如果为null或者为"" 则返回true
	 */
	public static boolean isNull(String str) {
		if (null2String(str).length() <= 0) {
			return true;
		}
		return false;
	}

	
	/**
	 * 判断对象是否为空
	 * @param o
	 * @return boolean
	 */
	public static boolean isNull(Object o) {
		if (o == null)
			return true;
		if (o instanceof String) {
			if (null2String(String.valueOf(o)).length() <= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param str
	 * @return 如果字符串 不为null 并且不为 "" 返回true 否则返回false
	 */
	public static boolean isNotNull(String str) {
		if (null2String(str).length() <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断对象是否不为空
	 * 
	 * @param o
	 * @return 如果为不为null 返回true 否则返回false
	 */
	public static boolean isNotNull(Object o) {
		if (o == null)
			return false;
		if (o instanceof String) {
			if (null2String(String.valueOf(o)).length() <= 0) {
				return false;
			}
		}
		return true;
	}

	//==================2.字符串转换成数字或============================================
	/**
	 * 字符串转换成int <br>
	 * 如果str为空、不是字符串 则返回0<br>
	 * @param str
	 * @return
	 */
	public static int string2int(String str) {
		return string2int(str, 0);
	}
	
	/**
	 * 字符串转换成int <br>
	 * 
	 * @param str 对应字符串
	 * @param defaultValue 如果字符串为空或者出错 默认返回的值
	 * @return str=null 则返回 defaultValue；  str不是整数，则返回  defaultValue
	 */
	public static int string2int(String str,int defaultValue) {
		if (isNotNull(str)) {
			try {
				return Integer.valueOf(str).intValue();
			} catch (Exception ex) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 字符串转换成long <br>
	 * 如果str为空、不是字符串 则返回0<br>
	 * @param str
	 * @return
	 */
	public static long string2long(String str) {
		return string2long(str,0);
	}
	
	/**
	 * 字符串转换成long <br>
	 * 
	 * @param str 对应字符串
	 * @param defaultValue 如果字符串为空或者出错 默认返回的值
	 * @return str=null 则返回 defaultValue；  str不是整数，则返回  defaultValue
	 */
	public static long string2long(String str,long defaultValue) {
		if (isNotNull(str)) {
			try {
				return Long.valueOf(str).longValue();
			} catch (Exception ex) {
				return defaultValue;
			}
		}
		return defaultValue;
	}
	
	/**
	 * 字符串转换成double <br>
	 * 如果str为空、不是字符串 则返回0.0<br>
	 * @param str
	 * @return
	 */
	public static double string2double(String str){
		return string2double(str, 0.0);
	}

	/**
	 * 字符串转换成double <br>
	 * 这里没有对double精度进行处理，要想保留精度，必须再调用另外一个方法： doubleFormat<br>
	 * @param str 对应字符串
	 * @param defaultValue 如果字符串为空或者出错 默认返回的值
	 * @return str=null 则返回 defaultValue；  str不是整数，则返回  defaultValue
	 */
	public static double string2double(String str,double defaultValue){
		if(isNotNull(str)){
			try{
				return Double.valueOf(str).doubleValue();
			}catch(Exception ex){
				return defaultValue;
			}
		}
		return defaultValue;
	}
	
	/**
	 * 格式化double 保留对应的位数<br>
	 * 是按照四舍五入的方法保留小数位数的<br>
	 * @param d 待处理double
	 * @param decimal 保留的小数位数
	 * @return
	 */
	public static double doubleFormat(double d,int decimal) {
		BigDecimal b = new BigDecimal(d);  
		//==1.ROUND_DOWN  直接把多余的位数删除
		//==2.ROUND_UP 进位处理
		//==3.ROUND_HALF_DOWN 四舍五入 1.5 会转换为1.0
		//==4.ROUND_HALF_UP 四舍五入 1.5会转换为2.0
		return b.setScale(decimal, BigDecimal.ROUND_HALF_UP).doubleValue(); //四舍五入 
	}
	
	/**
	 * 字符串转换成BigDecimal <br>
	 * 如果str为空、不是字符串 则返回0.0<br>
	 * @param str
	 * @return
	 */
	public static BigDecimal string2bigDecimal(String str){
		return string2bigDecimal(str, 0.0);
	}
	
	/**
	 * 字符串转换成BigDecimal <br>
	 * @param str 对应字符串
	 * @param defaultValue 如果字符串为空或者出错 默认返回的值
	 * @return str=null 则返回 defaultValue；  str不是整数，则返回  defaultValue
	 */
	public static BigDecimal string2bigDecimal(String str,double defaultValue){
		if(isNotNull(str)){
			try{
				return new BigDecimal(str);
			}catch(Exception ex){
				return new BigDecimal(defaultValue);
			}
		}
		return new BigDecimal(defaultValue);
	}
	
	/**
	 * 对字符串进行trim
	 * 
	 * @param str
	 * @return String
	 */
	public static String stringTrim(String str) {
		return null2String(str).trim();
	}
	
	
	//================================map转换成字符串=======================================
	/**
	 * map转换成字符串 使用 gap隔开<br>
	 * 假设gap为分号 则 转换后的格式：k1=v1;k2=v2; 
	 * @param map
	 * @param gap
	 * @return String
	 * @throws Exception
	 */
	public static String map2string(Map<String, String> map, String gap) throws Exception {
		if (isNull(map))
			return "";
		gap = null2String(gap);
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> m : map.entrySet()) {
			try{
				sb.append(m.getKey()).append("=").append(null2String(m.getValue())).append(gap);
			}catch(Exception ex){
				//
			}
		}
		return sb.toString();
	}
}
