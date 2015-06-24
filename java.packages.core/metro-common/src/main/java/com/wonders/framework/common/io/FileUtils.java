package com.wonders.framework.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.wonders.framework.common.asset.AssertUtil;

public class FileUtils {
	private static Logger LOG = LoggerFactory.getLogger(FileUtils.class);
	
	public static byte[] readBytesFromFile(String fileName) throws IOException{
		Resource res = null;
		InputStream is = null;
		try{
			AssertUtil.hasLength(fileName, "No file name found.");
			res = new DefaultResourceLoader().getResource(fileName);
			is = res.getInputStream();
			return readBytesFromFile(is);
		}catch(IOException e){
			LOG.error("Read input stream failed.",e);
			throw e;
		}finally{
			IOUtils.closeQuietly(is);
		}
	}
	
	public static byte[] readBytesFromFile(File f) throws IOException{
		InputStream is = null;
		try{
			AssertUtil.isNull(f, "No file found.");
			is = new FileInputStream(f);
			return readBytesFromFile(is);
		}catch(IOException e){
			LOG.error("Read input stream failed.",e);
			throw e;
		}finally{
			IOUtils.closeQuietly(is);
		}
	}
	
	public static byte[] readBytesFromFile(InputStream is) throws IOException{
		ByteArrayOutputStream bos = null;
		byte[] buffer = new byte[1024];
		int len = 0;
		try{
			LOG.debug("starting write input stream....");
			bos = new ByteArrayOutputStream();
			while((len = is.read(buffer)) != -1)
				bos.write(buffer, 0, len);
			LOG.debug("end write input stream....");
			return bos.toByteArray();
		} catch (IOException e) {
			LOG.error("Read input stream failed.",e);
			throw e;
		}finally{
			IOUtils.closeQuietly(bos);
		}
	}
}
