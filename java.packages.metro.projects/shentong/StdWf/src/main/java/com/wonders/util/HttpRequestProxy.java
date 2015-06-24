package com.wonders.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestProxy {
	

    /**
     * 连接超时
     */
    private static int connectTimeOut = 10000;

    /**
     * 读取数据超时
     */
    private static int readTimeOut = 600000;

    /**
     * 请求编码
     */
    private static String requestEncoding = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(HttpRequestProxy.class);

    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, Map parameters,
            String recvEncoding)
    {
        HttpURLConnection url_con = null;
        String responseContent = "";
        try
        {
            String p = StringUtil.writeBean2Json(parameters);
            

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(HttpRequestProxy.connectTimeOut);
            url_con.setReadTimeout(HttpRequestProxy.readTimeOut);
            url_con.setDoOutput(true);
            
            byte[] b = p.getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf=System.getProperty("line.separator");
            while (tempLine != null)
            {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            logger.error("网络故障", e);
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }
        return responseContent;
    }
	
    public static String getPort() {
    	try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
			Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
			
			Iterator<ObjectName> i = objs.iterator();
			if(i.hasNext()){
				return i.next().getKeyProperty("port");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return "";
    }
    
	public static void main(String[] args) throws IOException{
		String s = "http://10.1.48.16:8080/workflow/send-tDocSend/toFormPage.action?modelName=%E6%96%B0%E5%8F%91%E6%96%87%E6%B5%81%E7%A8%8B&incidentNo=65&processName=%E6%96%B0%E5%8F%91%E6%96%87%E6%B5%81%E7%A8%8B&pinstanceId=65&taskUserName=ST/G001000001612549&stepName=%E5%8F%91%E6%96%87%E9%80%9A%E7%9F%A5&taskId=12261064757e7498937e6b29ea80ca&taskuser=ST/G001000001612549&codeId=13";
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
		System.out.println(ms);
		List<String> list = new ArrayList<String>();
		//String[] mmm = ms.split(",");
		String url = "http://10.1.14.20:8088/workflowController/service/todo/addTask";
	//	System.out.println(portalService(ms,url));
		
		String g = " 的发顺丰     发的是    发的   ";
		System.out.println(g);
		System.out.println(g.replaceAll("\\s+", ""));
		
		
	}
}
