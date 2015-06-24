package com.wonders.framework.xlst;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Deprecated
public class XSLTListener {
	
	private static final String XSLT_DEFUALT_PATH = "classpath:config/xslt";
	private static final Logger LOG = LoggerFactory.getLogger(XSLTListener.class);
	
	private Map<String,byte[]> xslts = null;
	
	@Inject
	private DefaultResourceLoader loader;
	
	public XSLTListener(){
		
	}
	public void listener(){
		Resource resource = loader.getResource(XSLT_DEFUALT_PATH);
		File f = null;
		File[] xsltFile = null;
		try {
			f = resource.getFile();
			if(f.exists()&&f.isDirectory()){
				xsltFile = f.listFiles(new FilenameFilter(){
					@Override
					public boolean accept(File dir, String name) {
						
						return name.endsWith(".xsl")||name.endsWith(".xslt");
					}
				});
				if(xsltFile.length > 0){
					xslts = new HashMap<String,byte[]>(xsltFile.length);
					for(File xslt:xsltFile){
					}
				}else{
					LOG.debug("No xslt file found.");
				}
			}else{
				LOG.debug("No configured file path found. path:",XSLT_DEFUALT_PATH);
			}
			
		} catch (IOException e) {
			LOG.error("XSLTListener---listener failed ", e);
		}
	}
}
