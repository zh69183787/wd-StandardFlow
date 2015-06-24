package com.wonders.framework.attachment.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wonders.framework.attachment.entity.AttachType;
import com.wonders.framework.attachment.entity.Attachment;
import com.wonders.framework.attachment.exception.AttachUploadException;
import com.wonders.framework.attachment.repository.AttachmentRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.framework.utils.StringUtil;
@Service
public class AttachmentService {
	private static final Logger LOG = LoggerFactory.getLogger(AttachmentService.class);
	
	private static final String DEFUALT_UPLOAD_FILE_PATH = "d:"+File.separator+"upload";
	private static final long DEFUALT_UPLOAD_MAX_SIZE = 10;//10M
	@Inject
	private AttachmentRepository attachmentRepository;
	
//	@Inject
//	private AttachmentConfiguration attachmentConfiguration;
	@Value("${file.upload.path}")
	private String fileUploadPath;
	@Value("${file.upload.maxsize}")
	private long maxSize;
	private static String[] allowExtType = null;
	private static String[] ignoreExtType = null;
	
	@Value("${preview.converter.url}")
	private String converterUrl;
	
	@Transactional
	public int updateRemovedById(Long id, Integer value){
		int count = 0;
		try{
			count = this.attachmentRepository.unpdateRemovedById(id, value);
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	@PostConstruct
	private  void init(){
//		fileUploadPath = attachmentConfiguration.getEnv().getProperty("file.upload.path");
		if(StringUtils.isEmpty(fileUploadPath)){
			fileUploadPath = DEFUALT_UPLOAD_FILE_PATH;
		}
		LOG.debug("file.upload.path:",fileUploadPath);
		try{
//			maxSize = Long.parseLong(attachmentConfiguration.getEnv().getProperty("file.upload.maxsize"));
		}catch(Exception e){
			maxSize = DEFUALT_UPLOAD_MAX_SIZE*1024*1024;
		}
		if(maxSize <= 0){
			maxSize = DEFUALT_UPLOAD_MAX_SIZE*1024*1024;
		}else{
			maxSize = maxSize*1024*1024;
		}
//		LOG.debug("file.upload.maxsize:",maxSize);
//		String ignoreExt = attachmentConfiguration.getEnv().getProperty("file.upload.ignore.file");
//		if(!StringUtils.isEmpty(fileUploadPath)){
//			ignoreExtType = ignoreExt.split(",");
//		}
//		LOG.debug("file.upload.ignore.file:",ignoreExt);
//		String allowExt = attachmentConfiguration.getEnv().getProperty("file.upload.allow.file");
//		if(!StringUtils.isEmpty(fileUploadPath)){
//			allowExtType = allowExt.split(",");
//		}
//		LOG.debug("file.upload.allow.file:",allowExt);
	}
	
	
	@Transactional
	public void uploadFiles(List<Attachment> attaches, String localFileDir, String version, boolean origNameUsed) throws AttachUploadException, IOException{
		//initial parameters
		String fileName = null;
		String fileFullPath = null;
		String fileNameCutExt = null;
		String fileExtName = null;
		String curTime = null;
		MultipartFile file = null;
		long fileSize = 0;
		File destDir = null;
		File dest = null;
		String tmpFileName = null;
		
		try{
			//get current date
			curTime = DateUtil.formartDate(new Date(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
			
			for(Attachment att:attaches){
				file = att.getFile();
				//get information from file
				fileName = file.getOriginalFilename();
				fileNameCutExt = fileName.substring(0, fileName.lastIndexOf("."));
				fileExtName = fileName.substring(fileName.lastIndexOf("."));
				if(!StringUtil.isNull(localFileDir)){
					fileFullPath = fileUploadPath + File.separator + localFileDir + File.separator;
				}else{
					fileFullPath = fileUploadPath + File.separator+DateUtil.getToday()+File.separator+att.getGroup()+File.separator;					
				}
				fileSize = file.getSize();
				
				vaildateFile(fileExtName,fileSize);
				//transfer the file to dest file path
				destDir = new File(fileFullPath);
				if(!destDir.exists()){
					destDir.mkdirs();
				}
				
				tmpFileName = origNameUsed ? fileNameCutExt + (!StringUtils.isEmpty(version) ? "_v"+version : "") : UUID.randomUUID().toString();	
				dest = new File(destDir,tmpFileName+fileExtName);
				if(dest.exists()){
					tmpFileName = tmpFileName + "_" + UUID.randomUUID().toString();
					dest = new File(destDir,tmpFileName+fileExtName);
				}
				file.transferTo(dest);
				
				att.setFileExtName(fileExtName);
				att.setFileName(fileName);
				att.setFileFullPath(dest.getAbsolutePath());
				att.setAttachType(AttachType.COSTS);
				att.setRemoved(0);
				att.setUploadTime(curTime);
				att.setSize(fileSize);
				att.setVersion(version);
				
				attachmentRepository.save(att);
			}
		}catch(Exception e){
			LOG.error("File upload error.",e);
			throw new AttachUploadException(e.getMessage());
		}
	}
	
	@Transactional
	public List<ArrayList<ArrayList<String>>> uploadFilesImport(List<Attachment> attaches) throws AttachUploadException{
		//initial parameters
		String fileName = null;
		String fileFullPath = null;
		String fileExtName = null;
		String curTime = null;
		MultipartFile file = null;
		long fileSize = 0;
		File destDir = null;
		File dest = null;
		String tmpFileName = null;
		
		try{
			//get current date
			curTime = DateUtil.formartDate(new Date(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
			
			for(Attachment att:attaches){
				file = att.getFile();
				//get information from file
				fileName = file.getOriginalFilename();
				fileExtName = fileName.substring(fileName.lastIndexOf("."));
				fileFullPath = fileUploadPath +File.separator+att.getGroup()+File.separator;
				fileSize = file.getSize();
				
				tmpFileName = UUID.randomUUID().toString();
				//allowExtType = new String[]{".xls",".xlsx"}; 
//				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@"+fileExtName);
				vaildateFiles(new String[]{".xls",".xlsx"},fileExtName,fileSize);
				//transfer the file to dest file path
				destDir = new File(fileFullPath);
				if(!destDir.exists()){
					destDir.mkdirs();
				}
				dest = new File(destDir,tmpFileName+fileExtName);
				if(!destDir.exists()){
					dest.createNewFile();
				}
				file.transferTo(dest);
				
				att.setFileExtName(fileExtName);
				att.setFileName(fileName);
				att.setFileFullPath(dest.getAbsolutePath());
				att.setAttachType(AttachType.COSTS);
				att.setRemoved(0);
				att.setUploadTime(curTime);
				att.setSize(fileSize);
				
				attachmentRepository.save(att);
			}
		}catch(Exception e){
			LOG.error("File upload error.",e);
			throw new AttachUploadException(e.getMessage());
		}
		return readExcel(dest.getAbsolutePath());
	}
	@Transactional
	public void uploadFile(Attachment attach) throws AttachUploadException{
		String fileFullPath = null;
		String curTime = null;
		String tmpFileName = null;
		File dest = null;
		File destDir = null;
		String fileExtName = null;
		
		try{
			curTime = DateUtil.formartDate(new Date(), DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
			fileFullPath = fileUploadPath +File.separator+attach.getGroup()+File.separator;
			fileExtName = attach.getFileName().substring( attach.getFileName().lastIndexOf("."));
			attach.setFileExtName(fileExtName);
			tmpFileName = UUID.randomUUID().toString();
			destDir = new File(fileFullPath);
			if(!destDir.exists()){
				destDir.mkdirs();
			}
			dest = new File(destDir,tmpFileName+attach.getFileExtName());
			if(!destDir.exists()){
				dest.createNewFile();
			}
			
			FileUtils.writeByteArrayToFile(dest, attach.getPayloads());
			attach.setFileFullPath(fileFullPath);
			attach.setAttachType(AttachType.COSTS);
			attach.setRemoved(0);
			attach.setUploadTime(curTime);
			attach.setSize(attach.getPayloads().length);
			attachmentRepository.save(attach);
		}catch(Exception e){
			//LOG.error("File upload error.",e);
			throw new AttachUploadException(e.getMessage());
		}
	}
	
	private void vaildateFiles(String[] allowExtType ,String fileExtName,long fileSize) throws AttachUploadException{
		if(allowExtType != null && allowExtType.length > 0){
			if(!ArrayUtils.contains(allowExtType, fileExtName)){
				/*String allStr = "";
				for(int i=0;i<allowExtType.length;i--){
					if(i==allowExtType.length-1){
						allStr += allowExtType[i]+"";
					}else{
						allStr += allowExtType[i]+",";
					}
					
				}*/
				throw new AttachUploadException("附件类型不在白名单中。");
			}
		} 
		if(ignoreExtType != null && ignoreExtType.length > 0){
			if(ArrayUtils.contains(ignoreExtType, fileExtName)){
				throw new AttachUploadException("附件类型在黑名单中。");
			}
		}
		if(fileSize > maxSize){
			throw new AttachUploadException("附件超过规定大小。" + "附件不能大于：" + this.maxSize/(1024*1024) + "M.");
		}
	}
	private void vaildateFile(String fileExtName,long fileSize) throws AttachUploadException{
		if(allowExtType != null && allowExtType.length > 0){
			if(!ArrayUtils.contains(allowExtType, fileExtName)){
				/*String allStr = "";
				for(int i=0;i<allowExtType.length;i--){
					if(i==allowExtType.length-1){
						allStr += allowExtType[i]+"";
					}else{
						allStr += allowExtType[i]+",";
					}
					
				}*/
				throw new AttachUploadException("附件类型不在白名单中。");
			}
		} 
		if(ignoreExtType != null && ignoreExtType.length > 0){
			if(ArrayUtils.contains(ignoreExtType, fileExtName)){
				throw new AttachUploadException("附件类型在黑名单中。");
			}
		}
		if(fileSize > maxSize){
			throw new AttachUploadException("附件超过规定大小。" + "附件不能大于：" + this.maxSize/(1024*1024) + "M.");
		}
	}
	
	public String generateGroupCode(String group){
		if(StringUtils.isEmpty(group))
			return UUID.randomUUID().toString();
		return group;
	}
	
	public List<ArrayList<ArrayList<String>>> readExcel(String fileFullPath) {
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<ArrayList<ArrayList<String>>> list = new ArrayList<ArrayList<ArrayList<String>>>(); 
		
 		//System.out.println(fileFullPath);
		FormulaEvaluator evaluator = null;  
		try {
			Workbook workBook = null;
            try {
        	workBook = new XSSFWorkbook(fileFullPath);
            } catch (Exception ex) {
            workBook = new HSSFWorkbook(new FileInputStream(fileFullPath));
        } 
            evaluator = workBook.getCreationHelper().createFormulaEvaluator(); 
			for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
				ArrayList<ArrayList<String>> rows =new ArrayList<ArrayList<String>>();
				Sheet sheet = workBook.getSheetAt(numSheet);
				String sheetName = workBook.getSheetName(numSheet);
				if (sheet == null) {
					continue;
				}
				// 循环行Row
				for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}
					
					// 循环列Cell
					ArrayList<String> arrCell =new ArrayList<String>();
					for (int cellNum = 0; cellNum <= row.getLastCellNum(); cellNum++) {
						Cell cell = row.getCell(cellNum);
						if (cell == null) {
							continue;
						}
						CellValue cellValue = evaluator.evaluate(cell);  
						arrCell.add(getValueEvaluator(cellValue,cell));
					}
					rows.add(arrCell);
				}
				list.add(rows);
			}
		} catch (IOException e) {
			System.out.println("e:"+e);
		}
	
		return list;
	}
	private String getValue(Cell cell) {
		
		if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
			String cellvalue = "";
			if(HSSFDateUtil.isCellDateFormatted(cell)){ //判断是不是 date 类型
				Date date = cell.getDateCellValue();
				cellvalue = DateUtil.formartDate(date,DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
			}else{
				cellvalue = String.valueOf(cell.getNumericCellValue());
			}
			return cellvalue;
		} else if(cell.getCellType() == cell.CELL_TYPE_FORMULA){
			return String.valueOf(cell.getNumericCellValue());
		}else {
			return String.valueOf(cell.getStringCellValue());
		}
	}
	
	private String getValueEvaluator (CellValue cellValue,Cell cell) {
		if(cellValue==null) return "";
		
		switch (cellValue.getCellType()) {  
		    case Cell.CELL_TYPE_BOOLEAN:  
		    	return String.valueOf(cellValue.getBooleanValue());  
		    case Cell.CELL_TYPE_NUMERIC:  
		    	String cellvalue2 = "";
				if(HSSFDateUtil.isCellDateFormatted(cell)){ //判断是不是 date 类型
					Date date = cell.getDateCellValue();
					cellvalue2 = DateUtil.formartDate(date,DateUtil.DF_YYYY_MM_DD_HH_MM_SS);
				}else{
					double d = cellValue.getNumberValue();
					int i = (int)d;
					cellvalue2 = (d == i ? String.valueOf(i) : String.valueOf(d));
				}
		    	return cellvalue2;  
		    case Cell.CELL_TYPE_STRING:  
		    	return String.valueOf(cellValue.getStringValue());  
		    case Cell.CELL_TYPE_BLANK:  
		        break;  
		    case Cell.CELL_TYPE_ERROR:  
		        break;  
		    // CELL_TYPE_FORMULA will never happen  
		    case Cell.CELL_TYPE_FORMULA:   
		        break;  
		}
		return "";
	}
	
	public String getConverterUlr(){
		return this.converterUrl;
	}
}
