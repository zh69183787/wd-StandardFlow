package com.wonders.framework.pdf.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wonders.framework.common.io.FileUtils;
import com.wonders.framework.pdf.PDFConvertor;

@Controller
@RequestMapping("pdf")
public class PDFtestController {
	private static final Logger LOG = LoggerFactory.getLogger(PDFtestController.class);
	
	@Inject
	private PDFConvertor pdfConvertor;
	
	@Inject
	private DefaultResourceLoader resourceLoader;
	
	@RequestMapping(value = "test")
	public @ResponseBody	String testPDFConvertor() throws Exception{
		String r = "{success:true,attachId:%d}";
		String xmlFilePath = "classpath:config/xsl/table.xml";
		byte[] xml = null;
		Resource res = null;
		long attachId = 0;
		try{
			res = resourceLoader.getResource(xmlFilePath);
			LOG.debug("read xml from ",xmlFilePath);
			
			xml = FileUtils.readBytesFromFile(res.getInputStream());
			LOG.debug("read bytes length:",xml==null?0:xml.length);
			
			
			attachId = pdfConvertor.buildPDF(xml, "test.pdf");
			LOG.debug("convert pdf attach id:",attachId);
			
			r = String.format(r, attachId);
		}catch(Exception e){
			LOG.error("test convertor pdf error.",e);
			throw e;
		}
		return r;
	}
}
