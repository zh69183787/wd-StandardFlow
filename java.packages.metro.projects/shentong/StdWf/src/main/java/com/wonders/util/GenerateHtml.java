package com.wonders.util;

import java.io.File;
import java.io.FileOutputStream;

public class GenerateHtml {
	

    String result = "";
    
    
	public String genHtml(String filePath, String fileName, String requestUrl) throws Exception {

		java.net.URL url = new java.net.URL(requestUrl);
		java.net.HttpURLConnection conn =(java.net.HttpURLConnection) url.openConnection();
		
		File destDir = null;
		try{
			if (conn.getResponseCode() == 200) {
			
				java.io.InputStream is = (java.io.InputStream) conn.getContent();
//				BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
//				conn.setRequestProperty("Content-Type","text/xml;charset=utf-8");   
				try{
					
					destDir = new File(filePath);
					if(!destDir.exists()){
						destDir.mkdirs();
					}
	
					String savePath = filePath+fileName;
					FileOutputStream baos = new FileOutputStream(new File(savePath));
					int buffer = 1024;
					byte[] b = new byte[buffer];
					int n = 0;
					while ((n = is.read(b, 0, buffer)) > 0) {
						baos.write(b, 0, n);
					}
					baos.flush();
					baos.close();
					is.close();
					//baos.close();
					result = "生成成功";
				}catch(Exception e){
					result="写文件过程出错，取消生成。";
				}
			}else{
				result="获得链接过程出错，取消生成。";			
			}
		}catch(Exception e){
				e.printStackTrace();
				result="获得内容过程出错，取消生成。";
		}
		return result;
	}
	

}
