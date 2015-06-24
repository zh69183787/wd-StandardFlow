package com.wonders.util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
/**
 * 
 */

import com.wonders.flowWork.entity.FlowWorkThread;

/** 
 * @ClassName: Msg 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zhoushun 
 * @date 2013-6-7 上午9:56:56 
 *  
 */
public class HttpRequestHelper {
	

	/**调用portal外部接口方法
	 * @param method
	 * @param paramsXml
	 * @return
	 */
	public static String portalService(String mobile,String urls) {
		
		String result = "";
		//String msg  = "您好！机关工会为您办理了“上海市退休职工住院补充医疗互助保障”，费用由集团统筹。具体保障内容等有关说明已通过信函、快递等形式寄给您，请注意查收。另，值此端午佳节来临之际，祝您节目快乐，身体健康，阖家幸福！申通地铁集团机关工会";
		//String msg  = "周四在文三路马腾路东部软件园，创新大厦B座1F，进门左手边，交通物流公共信息平台。杭州东站下车的话出租车或者179路（到站：文三路口）。城站就打车吧。";
		try {
			
			//String urls = "http://211.136.163.68:8000/httpserver?enterpriseid=00323&accountid=666&pswd=4Y3j78z2&mobs="+mobile+"&msg="+msg;
			URL url = null;
			HttpURLConnection http = null;
			
			try {
				url = new URL(urls);
				http = (HttpURLConnection) url.openConnection();
				http.setDoInput(true);
				http.setDoOutput(true);
				http.setUseCaches(false);
				http.setConnectTimeout(50000);
				http.setReadTimeout(50000);
				http.setRequestMethod("POST");
				// http.setRequestProperty("Content-Type",
				// "text/xml; charset=UTF-8");
				http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				http.connect();
				String param = "&data="+ mobile;  
				  
				    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "utf-8");  
				    osw.write(param);  
				    osw.flush();  
				    osw.close();  
				  
				    
				if (http.getResponseCode() == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						result += inputLine;
					}
					in.close();
					//result = "["+result+"]";
				}
			} catch (Exception e) {
				System.out.println("err");
				e.printStackTrace();
			} finally {
				if (http != null) http.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
		
	
	public static void main(String[] args) throws IOException{
/*		String s = "http://10.1.48.16:8080/workflow/send-tDocSend/toFormPage.action?modelName=%E6%96%B0%E5%8F%91%E6%96%87%E6%B5%81%E7%A8%8B&incidentNo=65&processName=%E6%96%B0%E5%8F%91%E6%96%87%E6%B5%81%E7%A8%8B&pinstanceId=65&taskUserName=ST/G001000001612549&stepName=%E5%8F%91%E6%96%87%E9%80%9A%E7%9F%A5&taskId=12261064757e7498937e6b29ea80ca&taskuser=ST/G001000001612549&codeId=13";
		String ms = "{\"app\": \"standardWork\",\"type\": 0,"
			+ "\"occurTime\": \"2013-11-14 11:22:02\",\"title\": \"-------流程标题-------\","
			+ "\"loginName\": \"ST/G01008000311\",\"status\": 0,\"removed\": 0,"
			+ " \"typename\": \"流程名称11\","
			+ "\"url\": \""+URLEncoder.encode(s,"UTF-8")+"\","
			+ "\"pname\": \"主流程名称\",\"pincident\": 1,"
			+ "\"cname\": \"子流程实例号\",\"cincident\": 1,"
			+ "\"stepName\": \"当前步骤\","
			+ "\"initiator\": \"ST/G01008000311\"}";
		String id = "8a81a97c441bba8c01441bbf2bde0002";
		List<String> list = new ArrayList<String>();
		//String[] mmm = ms.split(",");
		String url = "http://10.1.14.20:8088/workflowController/service/todo/addTask";
		System.out.println(portalService(ms,url));
		
		String g = " 的发顺丰     发的是    发的   ";
		System.out.println(g);
		System.out.println(g.replaceAll("\\s+", ""));
		*/
		
		
		Map param = new HashMap();
		param.put("app", "standardWork");
		param.put("type", "0");
		param.put("occurTime", "2014-02-12 11:22:02");
		param.put("title", "标准制修订");
		param.put("loginName", "ST/G00200000115");
		param.put("status", "0");
		param.put("removed", "0");
		param.put("typename", "标准制修订");
		param.put("url", URLEncoder.encode("http://10.1.48.16:7001/StdWf/flowwork/flowInfo?flowUid=b02fa944-7a4e-47f8-93ad-fe264c89b046&editFlag=true", "UTF-8"));
		param.put("pname", "标准制修订");
		param.put("pincident", "1");
		param.put("cname", "子流程实例号");
		param.put("cincident", "1");
		param.put("stepName", "部门领导审核");
		param.put("initiator", "ST/G00200000115");
		System.out.println(portalService(StringUtil.writeBean2Json(param), "http://10.1.14.20:8088/workflowController/service/todo/addTask"));
	}
}
