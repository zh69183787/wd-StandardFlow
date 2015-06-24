package com.wonders.framework.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.wonders.framework.attachment.entity.Attachment;
import com.wonders.framework.attachment.service.AttachmentService;
import com.wonders.framework.common.asset.AssertUtil;
import com.wonders.framework.common.io.FileUtils;

@Service
public class PDFConvertor { 
	
    private static final Logger LOG = LoggerFactory.getLogger(PDFConvertor.class);
	private static final String DEFAULT_XSLT_FILE_PATH = "classpath:config/xsl/convertTable.xsl";
	private static final String DEFAULT_USER_CONFIG_PATH = "classpath:config/fop/userconfig.xml";
	
	private static final long ADMIN_ACCOUNT_ID = 3l;
	private static final String ADMIN_USER_NAME = "admin";
	
	 // configure fopFactory as desired
    private FopFactory fopFactory;
    
    @Inject
    private DefaultResourceLoader loader;
    @Inject
    private AttachmentService attachmentService;
    
    @PostConstruct
    public void init() throws ConfigurationException, SAXException, IOException{
    	try{
    		LOG.info("Start configure FOP component...");
//    		if(loader == null)
//    			loader = new DefaultResourceLoader();
	    	fopFactory = FopFactory.newInstance();
//	    	
//	    	FOURIResolver uriResolver = (FOURIResolver) fopFactory.getURIResolver();
//	    	uriResolver.setCustomURIResolver(new FopClassPathFontsResovler());
//	    	
	    	DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
	    	Resource res = loader.getResource(DEFAULT_USER_CONFIG_PATH);
	    	fopFactory.setUserConfig(cfgBuilder.build(res.getInputStream()));
	    	LOG.info("End configure FOP component...");
    	} catch (FOPException e) {
    		LOG.error("Fop init error.",e);
			throw e;
		} catch (ConfigurationException e) {
			LOG.error("Configuration error.",e);
			throw e;
		} catch (SAXException e) {
			LOG.error("Config file parse error.",e);
			throw e;
		} catch (IOException e) {
			LOG.error("Config file read exception:",e);
			throw e;
		}finally{
			
    	}
    }
    
    
    public long buildPDF(byte[] xml,String fileName) throws Exception{
    	return buildPDF(xml, DEFAULT_XSLT_FILE_PATH, fileName);
    }
    
   /**
    * 
     * @param xml an xml formt byte array for data to be converted
     * @param xsl xslt file name can be used to convert the xml to xsl-fo file
     * @param fileName pdf file name which we should output 
     * @throws Exception
    * @return attachement ID
    * @throws Exception
    */
    public long buildPDF(byte[] xml,String xsl,String fileName) throws Exception{
    	byte[] xslt = null;
    	
    	AssertUtil.isTrue(xml!=null&&xml.length>0 , "Input data is empty.");
		AssertUtil.hasLength(xsl, " Convert file xslt is empty");
		AssertUtil.hasLength(fileName, " Output file name is empty");
    	
    	try{
    		xslt = FileUtils.readBytesFromFile(xsl);
    		return buildPDF(xml,xslt,fileName);
    	} catch (Exception e) {
			throw e;
		}finally{
			
    	}
    }
    @Transactional
    public long buildPDF(byte[] xml,byte[] xslt,String fileName) throws Exception{
    	byte[] pdf = null;
    	Attachment attach = null;
    	String group = null;
    	
    	AssertUtil.isTrue(xml!=null&&xml.length>0 , "Input data is empty.");
		AssertUtil.isTrue(xslt!=null&&xslt.length>0 , "Input data is empty.");
		AssertUtil.hasLength(fileName, " Output file name is empty");
		
    	try{
    		pdf = buildPDF(xml,xslt);
    		attach = new Attachment();
    		attach.setAccountId(ADMIN_ACCOUNT_ID);
    		attach.setUploaderName(ADMIN_USER_NAME);
    		group = attachmentService.generateGroupCode(group);
    		attach.setGroup(group);
    		attach.setCodeId(0l);
    		attach.setFileName(fileName);
    		attach.setPayloads(pdf);
    		attachmentService.uploadFile(attach);
    		return attach.getId();
    	} catch (Exception e) {
			throw e;
		}finally{
    	}
    }
    
    public byte[] buildPDF(byte[] xml,byte[] xslt) throws Exception{
    	byte[] fo = null;
    	byte[] pdf = null;
    	
    	AssertUtil.isTrue(xml!=null&&xml.length>0 , "Input data is empty.");
		AssertUtil.isTrue(xslt!=null&&xslt.length>0 , "Input data is empty.");
		
    	try{
    		//convert xml to fo by xslt
    		fo = convertXML2FO(xml, xslt);
    		//convert fo to pdf 
    		pdf = convertFO2PDF(fo);
    		return pdf;
    	} catch (Exception e) {
			throw e;
		}finally{
    	}
    }
    
	/**
     * Converts an FO file to a PDF file using FOP
     * @param fo the FO file
     * @param pdf the target PDF file
	 * @return 
     * @throws FactoryConfigurationError In case of a problem with the JAXP factory configuration
     * @throws ParserConfigurationException In case of a problem with the parser configuration
     * @throws SAXException In case of a problem during XML processing
     * @throws IOException In case of an I/O problem
     */
    private byte[] convertFO2PDF(byte[] fo) throws SAXException, IOException, ParserConfigurationException{

        FOUserAgent foUserAgent = null;
        ByteArrayInputStream in = null;
        ByteArrayOutputStream bos = null;
        SAXParserFactory factory = null;
        SAXParser parser = null;
        DefaultHandler dh = null;
        
        try {
        	
        	foUserAgent = fopFactory.newFOUserAgent();
        	bos = new ByteArrayOutputStream();
            

            // Construct fop and setup output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, bos);

            // Setup SAX parser
            // throws FactoryConfigurationError
            factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            // throws ParserConfigurationException
            parser = factory.newSAXParser();

            // Obtain FOP's DefaultHandler
            // throws FOPException
            dh = fop.getDefaultHandler();

            // Start parsing and FOP processing
            // throws SAXException, IOException
            parser.parse(in = new ByteArrayInputStream(fo), dh);
            
            return bos.toByteArray();
        }catch(SAXException e){
        	throw e;
        } catch (IOException e) {
        	throw e;
		} catch (ParserConfigurationException e) {
        	throw e;
		} finally {
			IOUtils.closeQuietly(bos);
        	IOUtils.closeQuietly(in);
        }
    }
	
	
	 /**
     * Converts an XML file to an XSL-FO file using JAXP (XSLT).
     * @param xml the XML file
     * @param xslt the stylesheet file
     * @param fo the target XSL-FO file
     * @throws IOException In case of an I/O problem
     * @throws TransformerException In case of a XSL transformation problem
     */
    private byte[] convertXML2FO(byte[] xml, byte[] xslt) throws TransformerException{

        //Setup output
        ByteArrayOutputStream out  = null;
        ByteArrayInputStream xmlIn = null;
        ByteArrayInputStream xslIn = null;
        Source in = null;
        Source xsl = null;
        Result res = null;
        TransformerFactory factory = null;
        Templates stylesheet = null;
        Transformer transformer = null;
        
        try {
        	out = new ByteArrayOutputStream();
            xmlIn = new ByteArrayInputStream(xml);
            xslIn = new ByteArrayInputStream(xslt);
            
            factory = TransformerFactory.newInstance();
            
            in = new StreamSource(xmlIn);
            xsl = new StreamSource(xslIn);
            res = new StreamResult(out);
            
            stylesheet = factory.newTemplates(xsl);
            
            transformer = stylesheet.newTransformer();
            
            transformer.transform(in, res);
            return out.toByteArray();
        }catch(TransformerException t){
        	throw t;
        }finally {
        	IOUtils.closeQuietly(out);
        	IOUtils.closeQuietly(xmlIn);
        	IOUtils.closeQuietly(xslIn);
        }
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			byte[] xml = FileUtils.readBytesFromFile("classpath:config/xsl/table.xml");
			byte[] xslt = FileUtils.readBytesFromFile("classpath:config/xsl/convertTable.xsl");
//			byte[] xslt = new String("<?xml version=\"1.0\"?><xsl:stylesheet version=\"1.1\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ></xsl:stylesheet>").getBytes("utf-8");
//			LOG.debug(new String(xslt));
			PDFConvertor convertor = new PDFConvertor();
			convertor.init();
//			
//			InputStream resourceAsStream = ClassLoader.getSystemResourceAsStream("fonts/SIMHEI.TTF");
//			if(resourceAsStream == null)
//				System.out.println(11111);
//			else
//				System.out.println(222);
//			convertor.buildPDF("classpath:fo/test1.fo", "test1.pdf");
//			byte[] fo  = convertor.convertXML2FO(xml, xslt);
//			LOG.debug(new String(fo));
			convertor.buildPDF(xml, xslt);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("",e);
		}
		
//		String strs[] ={"E:/workspace-metro/java.packages.metro/java.packages.core/metro-common/src/main/resources/fonts/SIMHEI.TTF","E:/workspace-metro/java.packages.metro/java.packages.core/metro-common/src/main/resources/fonts/SIMHEI.xml"};
//		org.apache.fop.fonts.apps.TTFReader.main(strs);
	}

}
