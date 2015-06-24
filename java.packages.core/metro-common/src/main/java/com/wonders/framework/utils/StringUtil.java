/*
 * Created on 2006-4-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.wonders.framework.utils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonders.framework.auth.entity.Dictionary;
import com.wonders.framework.common.controller.LoginController;


public class StringUtil {
	private static final Logger LOG = LoggerFactory.getLogger(StringUtil.class);
	
	

	/**
	 * 过滤字典项   没有  FILE_  开头的节点
	 * 
	 * **/
	public static List<Dictionary> outFileStr(List<Dictionary> list){
//		List<Dictionary> nList = new ArrayList<Dictionary>();
		for(Dictionary d:list){
			if(!d.getCode().startsWith("FILE_")){
//				list.R
				if(!d.isLeaf()){
					for(Dictionary c:d.getChildren()){
						if(!c.getCode().startsWith("FILE_")){
							
						}else{
							d.getChildren().remove(c);
						}
					}
				}
				
			}else{
				list.remove(d);
			}
		}
		
		return list;
	}
	
	/**
	 * 验证权限
	 * 
	 * **/
	public static Boolean isLimits(HttpSession session,String code){
		
		@SuppressWarnings("unchecked")
		Map<String,Boolean> mapLimits = (Map<String, Boolean>) session.getAttribute(LoginController.SEC_LIMITS);
		
		if("admin".equals(session.getAttribute("SEC_CUR_USER_NAME"))){
			return true;
		}else if(mapLimits.get(code) == null){
			return false;
		}else if(mapLimits.get(code)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * map  to bean
	 *  
	 * @param  String str
	 * @return String str
	 */
	@SuppressWarnings("rawtypes")
	public static void setValueNc(Map map,Object thisObj,String objName)
	  {
		
		Map nm = new HashMap();
	    Set set = map.keySet();
	    Iterator iterator = set.iterator();
	    String oName = objName+".";
	    while (iterator.hasNext())
	    {
	      Object obj = iterator.next();
	      Object val = map.get(obj);
	      
	      String s = obj.toString();

	      if(s.startsWith(oName)){
	    	  int objlen = oName.length();
	    	  String newObj = s.substring(objlen);//.split(oName);
	    	  
		      if(val == null||val.toString().equals("")){
		    	  Field field = ReflectionUtils.findField(thisObj.getClass(), newObj);
		    	  if(field != null && !field.getType().isAssignableFrom(String.class)){
		    		  continue;		    		  
		    	  }
		      }
		      
	    	  nm.put(newObj, val);
	    	  
//	    	  setMethod(newObj, val, thisObj);
	      }else if(s.endsWith("_txt_val")){
	    	  
	      }else if(s.endsWith("_val")){
	      }
	      
	    }
	    try {
			BeanUtils.populate(thisObj, nm);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	/**
	 * map  to bean
	 *  
	 * @param  String str
	 * @return String str
	 */
	@SuppressWarnings("rawtypes")
	public static void setValue(Map map,Object thisObj,String objName)
	  {
		
		
	    Set set = map.keySet();
	    Iterator iterator = set.iterator();
	   String oName = objName+".";
	    while (iterator.hasNext())
	    {
	      Object obj = iterator.next();
	      Object val = map.get(obj);
	      
	      
	      String s = obj.toString();
	      if(s.startsWith(oName)){
	    	  
	    	  String[] ary = s.split(objName+"\\.");
	    	  //System.out.println("map:------ossl-----"+s);
	    	  //System.out.println("map:------ossl-----"+ary[1]);
	    	  setMethod(ary[1], val, thisObj);
	      }else if(s.endsWith("_val")){
	    	  
	    	  String[] ary = s.split("_val");
	    	  //System.out.println("map:------ossl-----"+s);
	    	  //System.out.println("map:------ossl-----"+ary[0]);
	    	  setMethod(ary[0], val, thisObj);
	      }
	      
	    }
	  }
	
	public static <T> void setValues(Map<String,?> map,final T o,String oName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		final SimpleTypeConverter typeConverter = new SimpleTypeConverter();
		String fieldName = null;
		Object value = null;
		
		final Map<String,Object> subMap = new HashMap<String,Object>(map.size());
		Set<String> keys = map.keySet();
		for(String key:keys){
			value = map.get(key);

			if(key.startsWith(oName)){
				fieldName = key.substring(oName.length()+1);
				
			      if(value == null||value.toString().equals("")){
			    	  Field field = ReflectionUtils.findField(o.getClass(), fieldName);
			    	  if(field != null && !field.getType().isAssignableFrom(String.class)){
			    		  continue;		    		  
			    	  }
			      }		
			      
				subMap.put(fieldName, value);
			}
		}
		ReflectionUtils.doWithFields(o.getClass(), new FieldCallback(){

			@Override
			public void doWith(Field field)
					throws IllegalArgumentException, IllegalAccessException {
				
				Object value = subMap.get(field.getName());
			//	LOG.info("process field: "+field.getName());
			//	LOG.info("process value: "+value);
				if(value != null){
					Object _value = null;
					if(java.io.Serializable.class.getName().equals(field.getType().getName())){
						_value = typeConverter.convertIfNecessary(value, (Class)(((ParameterizedType) o.getClass()
								.getGenericSuperclass()).getActualTypeArguments()[0]),field);
					}else if(field.getType().isEnum()){
						_value  = EnumUtils.getEnum((Class<? extends Enum>)field.getType(), value.toString().toUpperCase());
					}else{
						 _value = typeConverter.convertIfNecessary(value, field.getType(),field);
					}
					
					 //使变量域可用，并且转换后的配置值注入其中
					 ReflectionUtils.makeAccessible(field);
			//		 LOG.info("process type "+field.getType().getName());
					 field.set(o, _value);
				}
			}
			
		});
		
	}
	  
	@SuppressWarnings("rawtypes")
	public static void setMethod(String name, Object value ,Object thisObj)
	  {
	      if(value == null||value.toString().equals("")){
	    	  Field field = ReflectionUtils.findField(thisObj.getClass(), name);
	    	  if(field != null && !field.getType().isAssignableFrom(String.class)){
	    		  return;	    		  
	    	  }
	      }
		try {
			BeanUtils.copyProperty(thisObj, name, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	  }
	    /** 
	     * 将map转换成Javabean 
	     * 
	     * @param javabean javaBean 
	     * @param params map数据 
	     */ 
	    public static Object toJavaBean(Object javabean, Map<String, ?> params,String objName) 
	    { 
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	Iterator it = params.keySet().iterator();
	    	objName = objName+".";
	    	while(it.hasNext()){	
	    		String key = (String) it.next();	
	    		if(key.startsWith(objName)){
	    			map.put(key.split(".")[1], params.get(key));
	    		}
//	    		List value = map.get(key);
	    	}
	    	
	        Method[] methods = javabean.getClass().getDeclaredMethods(); 
	        for (Method method : methods) 
	        { 
	            try 
	            { 
	                if (method.getName().startsWith("set")) 
	                { 
	                    String field = method.getName(); 
	                    field = field.substring(field.indexOf("set") + 3); 
	                    field = field.toLowerCase().charAt(0) + field.substring(1); 
	                    method.invoke(javabean, new Object[] 
	                    { 
	                    		map.get(field) 
	                    }); 
	                } 
	            } 
	            catch (Exception e) 
	            { 
	            } 
	        } 

	        return javabean; 
	    } 
	/**
	 * KindEditor替换文本
	 * @param  String str
	 * @return String str
	 */
	public static String htmlspecialchars(String str) {
		if(str==null) return "";
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}
	/**
	 * 手机号码有效性检查
	 * 简单规则：1开头 11位数字
	 * @param n
	 * @return
	 */
	public static boolean checkMobile(String n) {
		if (n == null || n.length() != 11) return false;
		if (!n.startsWith("1")) return false;
		
		try {
			Long.parseLong(n.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * �õ�html���ӵ�String,��������ǿգ�����toStringȥ2�߿ո�󳤶�Ϊ0������һ��ո�(&nbsp;)
	 * @param o:Ҫ��ʾ�Ķ���
	 * @return
	 */
	public static String getHtmlFormatString(Object o){
		String showValue = "&nbsp;";
		if(o!=null&&!o.toString().trim().equals("")){
			showValue = o.toString(); 
		}
		return showValue;
	}
	
	/**
	 * 验证对象是否为NULL  为NULL  返回空
	 * @param o
	 * @return
	 */
	public static String getNotNullValueString(Object o){
		String showValue = "";
		if(o!=null){
			showValue = o.toString();
			if(showValue.trim().equals("null")){
				showValue = "";
			}
		}
		return showValue;
	}
	public static String getNotNullTime(Object o){
		String showValue = "";
		if(o!=null){
			showValue = o.toString();
			if(showValue.trim().equals("----/--/--")){
				showValue = "";
			}
		}
		return showValue;
	}
	public static String getNotNullValueNumber(Object o){
		String showValue = "0";
		try {
			if(o!=null){
				showValue = o.toString().trim();
				if(StringUtil.isNull(showValue)){
					showValue = "0";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			showValue = "0";
		}
		return showValue;
	}
	public static boolean isNumFloat(String str){
		if(isNull(str)){return false;}
		  try{
		   Float.parseFloat(str);
		   return true;
		  }catch(NumberFormatException e){
//		   System.out.println("异常：" + str + "不是数字/整数...");
		   return false;
		  }
	}
	public static boolean isNumInt(String str){
		if(isNull(str)){return false;}
		  try{
		   Integer.parseInt(str);
		   return true;
		  }catch(NumberFormatException e){
//		   System.out.println("异常：" + str + "不是数字/整数...");
		   return false;
		  }
	}

	public static String paseFloat(String str,String defaultVal){
		if(isNumFloat(str)){
			return str;
		}else{
			return defaultVal;
		}
	}
	public static String getNotNullValue(Object o,String value){
		String showValue = value;
		if(o!=null){
			showValue = o.toString();
			if(showValue.trim().equalsIgnoreCase("null")||showValue.trim().equals("")){
				showValue = value;
			}
		}
		return showValue;
	}
	
	/**
	 * ��ʽ���ɼ۸�ı�����ʽ
	 * @param value
	 * @param pattern
	 * @return
	 */
	public static String getMoneyFormatString(double value, String pattern) {
		if (value == 0.0) {
		    return "";
		} else {
		    try {
		        DecimalFormat format = new DecimalFormat(pattern);
		        return format.format(value);
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException(e.getMessage());
		    }
		}
	}
	
    /**
     * <p>���������Լ����˵����ַ�ת���ɶ�Ӧ��utf-8�����ַ��������GBK˫�ֽ��ַ�</p>
     * @param oriStirng ԭʼGBK�ַ�
     * @return ת�����utf-8�ַ�
     */
    public static String gbk2UTF8(String oriStirng) {
        if ( oriStirng == null )
            return null;
        else if(oriStirng.trim().length() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < oriStirng.length(); i++) {
            char thisChar = oriStirng.charAt(i);
            sb.append(gbk2UTF8(thisChar));
        }
        return sb.toString();

    }
    /**
     * <p>���������Լ����˵����ַ��GBKת���ɶ�Ӧ��utf-8���룬�������˫�ֽ��ַ�</p>
     * @param oriChar ԭʼGBK�ַ�
     * @return ת�����utf-8�ַ�
     */
    private static String gbk2UTF8(char oriChar) {
        String desString = "";
        //���˫�ֽ��ַ�
        if (oriChar > 128) {
            desString = "\\u" + Integer.toHexString(oriChar);
        }else{
            desString = String.valueOf(oriChar);
        }
        return desString;
    }
	
	public static String getMoneyFormatString(float value, String pattern) {
		return getMoneyFormatString((double)value,pattern);
	}
	/**
	 * �ж��Ƿ�Ϊ��
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		if(str==null||str.trim().equals("")||str.trim().equalsIgnoreCase("null")){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * ������ݿ�ȡ���Timestamp�������ת����----��--��--�ո�ʽ
	 * @param temp
	 * @return
	 * Frank_cp
	 */
	public static String getDateFormatCN(Object temp){
		String time="----��--��--��";
		if(temp!=null){
			String str=temp.toString();
			String yearStr = str.substring(0,4);
			String monthStr = str.substring(5,7);
			String dayStr = str.substring(8,10);
			time=yearStr+"��"+monthStr+"��"+dayStr+"��";
		}
		return time;
	}
	/**
	 * ������ݿ�ȡ���Timestamp�������ת����----/--/--/��ʽ
	 * @param temp
	 * @return
	 * Frank_cp
	 */
	public static String getDateFormatNUM(Object temp){
		String time="----/--/--";
		if(temp!=null&&!temp.toString().trim().equals("")){
			String str=temp.toString();
			String yearStr = str.substring(0,4);
			String monthStr = str.substring(5,7);
			String dayStr = str.substring(8,10);
			time=yearStr+"/"+monthStr+"/"+dayStr;
		}
		return time;
	}
	/**
	 * ������ݿ�ȡ���Timestamp�������ת����2000-10-10��ʽ
	 * @param temp
	 * @return
	 * Frank_cp
	 */
	public static String getDateFormatEN(Object temp){
		String time="";
		if(temp!=null){
			String str=temp.toString();
			String yearStr = str.substring(0,4);
			String monthStr = str.substring(5,7);
			String dayStr = str.substring(8,10);
			time=yearStr+"-"+monthStr+"-"+dayStr;
		}
		return time;
	}
    public static Timestamp putDateIn(String str){
		StringBuffer str1 = new StringBuffer(str.substring(0,4));
		str1.append("-");
		str1.append(str.substring(5,7));
		str1.append("-");
		str1.append(str.substring(8,10));	
		return Timestamp.valueOf(""+str1+" "+"00"+":"+"00"+":"+"00"+"."+"0");
			 
    }
    
    /**
     * ȥ���ַ�ǰ������ַ�
     * @param str
     * @param c
     * @return
     */
	public static String pField3(String str,char c){
		String r = str;
		if(!StringUtil.isNull(str)){
			if(str.startsWith(String.valueOf(c))){
				int i=0;
				while(i<str.length()&&str.charAt(i)==c){
					i++;
				}
				r = str.substring(i,str.length());
			}
		}
		return r;
	}
	
	/**
	 * ȥ���ַ�ǰβ��������ַ�
	 * @param str
	 * @param c
	 * @return
	 */
	public static String pField3tail(String str,char c){
		String r = str;
		if(!StringUtil.isNull(str)){
			if(str.endsWith(String.valueOf(c))){
				int i=str.length()-1;
				while(i>0&&str.charAt(i)==c){
					i--;
				}
				r = str.substring(0,i+1);
				if(r.equals(String.valueOf(c))){
					r = "";
				}
			}
		}
		return r;
	}
	/**
	 * ������ת����001��ʽ���ַ�
	 * @param temp
	 * @return
	 * Frank_cp
	 */
	public static String convertNum2StringWithZero(int num, int length){
		String strNum=Integer.toString(num);
		StringBuffer strBuffer=new StringBuffer();
		
		int strLen = strNum.length();
		
		for (int iLen = 0; iLen <  (length - strLen); iLen++) {
			strBuffer.append("0");
		}
		strBuffer.append(strNum);
		
		return strBuffer.toString();
	}
	
	/**
	 * ��001��ʽ���ַ�ת��������
	 * @param temp
	 * @return
	 * Frank_cp
	 */
	public static String convertStringWithZero2Num(String strNum, int length){
		
		String strTemp = null;
		int strLen = strNum.length();
		int numLen = 0;
		
		for (int iLen = 0; iLen < strLen; iLen++) {
			if ( numLen == 0 ) {
				strTemp = strNum.substring(iLen,iLen+1);
				if (!strTemp.equals("0")) {
					numLen = iLen;
				}
			}
		}
		
		return strNum.substring(numLen,strLen);
	}
	
	/**
	 * ������ݿ�ȡ���Timestamp�������ת����20001010��ʽ
	 * @param temp
	 * @return
	 * Frank_cp
	 */
	public static String getDateFormatNoHenxian(Object temp){
		String time="";
		if(temp!=null){
			String str=temp.toString();
			String yearStr = str.substring(0,4);
			String monthStr = str.substring(5,7);
			String dayStr = str.substring(8,10);
			time=yearStr+monthStr+dayStr;
		}
		return time;
	}
	public static int getInt(String str){
		int r = 0;
		if(str!=null && !"".equals(str)){
			try {
				if(str.indexOf(".")>=0){
					str = str.substring(0,str.indexOf("."));
				}
				r = new Integer(str).intValue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}
	
	public static float getFloat(String str){
		float r = 0;
		if(str!=null){
			try {
				r = new Float(str).floatValue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}
	
	public static BigDecimal getDecimal(String str){
		BigDecimal r = new BigDecimal(0);
		if(!isNull(str)){
			try {
				r = new BigDecimal(str);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}
	
	public static String getChinese(String str) {
		try {
			String str1 = str;
			// byte[] str2=str1.getBytes("ISO-8859-1");
			byte[] str2 = str1.getBytes("ISO8859-1");
			String temp = new String(str2);
			;
			return temp;
		} catch (Exception e) {
			return null;
		}
	}
		
	public static boolean StringEquals(String s1,String s2){
		if(s1 == null && s2 == null){
			return true;
		}else if(s1 == null && s2 != null && !s2.equals("")){
			return false;
		}else if(s1 != null && s2 == null && !s1.equals("")){
			return false;
		}else if(s1 == null && s2.equals("")){
			return true;
		}else if(s1.equals("") && s2 == null){
			return true;
		}else if(s1.equals(s2)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * ���ص�ǰ����(yyyy/mm/dd)
	 * 
	 * @return
	 */
	public static String getCurrDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(date);
	}
	
	/**
	 * �����ڸ�ʽΪ��yyyy/mm/ddת���ɸ�ʽ��yyyy-mm-dd
	 * 
	 * @param olddate
	 * @return
	 */
	public static String DateConversion(String olddate) {
		if (null == olddate || olddate.length() < 10) {
			return null;
		}
		String newstring = olddate.substring(0, 4);
		newstring += "-";
		newstring += olddate.substring(5, 7);
		newstring += "-";
		newstring += olddate.substring(8);
		return newstring;
	}
	/**
	 * �����ڸ�ʽΪ��yyyy/mm/ddת���ɸ�ʽ��yyyy-mm
	 * 
	 * @param olddate
	 * @return
	 */
	public static String DateConversion_yyyymm(String olddate) {
		if (null == olddate || olddate.length() < 10) {
			return null;
		}
		String newstring = olddate.substring(0, 4);
		newstring += "-";
		newstring += olddate.substring(5, 7);
		return newstring;
	}
	/**
	 * �����ڸ�ʽΪ��2000-01-31 00:00:00.0ת���ɸ�ʽ��yyyy/mm/dd
	 * 
	 * @param olddate
	 * @return
	 */
	public static String conversionDate(String d){
		if(d.length() < 10){
			return "";
		}
		StringBuffer nd = new StringBuffer();
		nd.append(d.substring(0, 4));
		nd.append("/");
		nd.append(d.subSequence(5, 7));
		nd.append("/");
		nd.append(d.substring(8,10));
		return nd.toString();
	}
	
	/**
	 * ���ص�ǰ����(yyyy/mm/dd)
	 * 
	 * @return
	 */
	public static String getCurrDate_yyyymmddhhmiss() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sdf.format(date);
	}
	
	public static String getNotNullValueStringWithBlank(Object o){
		String showValue = "&nbsp;";
		if(o!=null){
			showValue = o.toString();
			if(showValue.trim().equals("null")){
				showValue = "&nbsp;";
			}
		}
		return showValue;
	}
	
	public static String dealNull(Object str){
		if(str==null||"null".equals(str))
			return "";
		else
			return str.toString();
	}
	
	//content 原始字符串，length 目标长度
	public static String fillWithBlank(String content,int length){
		
		String result = "";
		
		for(int i=0;i<(length-content.getBytes().length);i++){
			result += " ";
		}
		result = content + result;
		
		return result;
	}
	
	public static boolean isLetter(char c) { 
        int k = 0x80; 
        return c / k == 0 ? true : false; 
    } 

      /** 
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1 
     * 
     * @param String 
     *            s ,需要得到长度的字符串 
     * @return int, 得到的字符串长度 
     */ 
    public static int length(String s) { 
        if (s == null) 
            return 0; 
        char[] c = s.toCharArray(); 
        int len = 0; 
        for (int i = 0; i < c.length; i++) { 
            len++; 
            if (!isLetter(c[i])) { 
                len++; 
            } 
        } 
        return len; 
    } 
     /** 
     * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位 
     * 
     * @author patriotlml 
     * @param String 
     *            origin, 原始字符串 
     * @param int 
     *            len, 截取长度(一个汉字长度按2算的) 
     * @return String, 返回的字符串 
     */ 
    public static String MySubstring(String origin, int len) { 
        if (origin == null || origin.equals("")||len<1) 
            return ""; 
        len = len*2;
        byte[] strByte = new byte[len]; 
        if (len >= length(origin)){ 
            return origin;} 
        System.arraycopy(origin.getBytes(), 0, strByte, 0, len); 
        int count = 0; 
        for (int i = 0; i < len; i++) { 
            int value = (int) strByte[i]; 
            if (value < 0) { 
                count++; 
            } 
        } 
        if (count % 2 != 0) { 
            len = (len == 1) ? ++len : --len; 
        } 
        return new String(strByte, 0, len)+"..."; 
    } 
    
    public static long StringToLong(String str,Long defaultValue){
    	Long ret = defaultValue;
    	try{
    		ret = Long.valueOf(StringUtil.getNotNullValueString(str));
    	}catch(Exception e){}
    	
    	return ret;
    }
    
    public static Long StrToLong(String str,Long defaultValue){
    	Long ret = defaultValue;
    	try{
    		ret = Long.valueOf(StringUtil.getNotNullValueString(str));
    	}catch(Exception e){}
    	
    	return ret;
    }
    
    public static String FormatTextArea(String str)
    {

        //str 从数据库里取得的数据
        while (str.indexOf("\n") != -1)
        {
            str = str.substring(0, str.indexOf("\n")) + "<br>" + str.substring(str.indexOf("\n") + 1);
        }
        while (str.indexOf(" ") != -1)
        {
            str = str.substring(0, str.indexOf(" ")) + "&nbsp;" + str.substring(str.indexOf(" ") + 1);
        }
        return str;
    } 
    public static List<String> getOneHourPartOfDay(){
		List<String> list =  new ArrayList<String>(); 
		list.add("00:00-00:59");
		list.add("01:00-01:59");		
		list.add("02:00-02:59");
		list.add("03:00-03:59");
		list.add("04:00-04:59");
		list.add("05:00-05:59");
		list.add("06:00-06:59");
		list.add("07:00-07:59");
		list.add("08:00-08:59");
		list.add("09:00-09:59");
		list.add("10:00-10:59");
		list.add("11:00-11:59");
		list.add("12:00-12:59");
		list.add("13:00-13:59");
		list.add("14:00-14:59");
		list.add("15:00-15:59");
		list.add("16:00-16:59");
		list.add("17:00-17:59");
		list.add("18:00-18:59");
		list.add("19:00-19:59");
		list.add("20:00-20:59");
		list.add("21:00-21:59");
		list.add("22:00-22:59");
		list.add("23:00-23:59");
		
		return list;
	}
    
	public static String writeList2Json(List list){
		 ObjectMapper mapper = new ObjectMapper();
		 String result = "";
		 try {
			result = mapper.writeValueAsString(list);
			result = result.replaceAll("\\\\", "\\\\\\\\");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.info(e.getMessage());
		}
		return result;
	}
	
	public static String writeBean2Json(Object o){
		 ObjectMapper mapper = new ObjectMapper();
		 String result = "";
		 try {
			result = mapper.writeValueAsString(o);
			result = result.replaceAll("\\\\", "\\\\\\\\");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.info(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 去掉字符串的最后一个字符
	 * @param str
	 * @return
	 */
	public static String  reLastOne(String str){
		if(str==null||"".equals(str)) 
				return str;
		if(str.length()<1){
			return str;
		}
		return str.substring(0, str.length()-1);
	}
	/**
	 * 根据用户登录名  和时间  返回编码
	 * @param str
	 * @return
	 */
	public static String getNoByTimeAndLoginName(String time,String name){
		String no = "NC-";
		String[] tempDateTime = time.split(" ");
		String[] tempDate = tempDateTime[0].split("-");
		String[] tempTime = tempDateTime[1].split(":");
		
		String year = tempDate[0].substring(2, 4);
		String month = tempDate[1];
		String day = tempDate[2];
		
		String hour = tempTime[0];
		String minute = tempTime[1];
		String second = tempTime[2];
		no=no+year+month+day+hour+minute+second+"-"+name.toUpperCase();
		return no;
	}
	
    public static String evaluateEL(String expression, Map<String, Object> properties)
    {
        String regex = "\\$\\{(\\w+)\\.(\\w+[\\.|\\w]*)\\}";
        List<String> matches = new LinkedList<String>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find())
        {
            String key = matcher.group(1);
            if (properties.containsKey(key))
            {
                String fieldName = matcher.group(2);
                Object obj = properties.get(key);

                try
                {
                    String value = BeanUtils.getProperty(obj, fieldName);
                    matches.add(value);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            while (!matches.isEmpty())
            {
                String match = matches.remove(0);
                expression = expression.replaceFirst(regex, match);
            }

        }
        
        return expression;
    }
}




