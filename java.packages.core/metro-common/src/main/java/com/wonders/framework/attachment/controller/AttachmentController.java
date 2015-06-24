package com.wonders.framework.attachment.controller;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.framework.attachment.configuration.AttachContentType;
import com.wonders.framework.attachment.entity.Attachment;
import com.wonders.framework.attachment.exception.AttachUploadException;
import com.wonders.framework.attachment.repository.AttachmentRepository;
import com.wonders.framework.attachment.service.AttachmentService;
import com.wonders.framework.auth.entity.Dictionary;
import com.wonders.framework.auth.repository.DictionaryRepository;
import com.wonders.framework.common.controller.LoginController;
import com.wonders.framework.utils.DateUtil;

@Controller
@RequestMapping("attachment")
public class AttachmentController {

	protected static final Logger LOG = LoggerFactory.getLogger(AttachmentController.class);

	@Inject
	protected AttachmentRepository attachmentRepository;
	
	@Inject
	protected DictionaryRepository dictionaryRespository;

	@Inject
	protected AttachmentService attachmentService;
	
	@RequestMapping(value = "findByGroup/{group}", method = RequestMethod.GET)
	public @ResponseBody
	List<Attachment> findByGroup(@PathVariable String group) {
		return attachmentRepository.findByGroup(group);
	}
	
	@RequestMapping(value = "findHistoryByGroup")
	public ModelAndView findHistoryByGroup(@RequestParam(value="group",required=false) String group) {
		ModelAndView mv = new ModelAndView("attachment/historyList");
		mv.addObject("lists", attachmentRepository.findHistoryByGroup(group));
		return mv;
	}
	
	@RequestMapping(value = "findByGroup", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView findListByGroup(
			@RequestParam(value="group",required=false) String group,
			@RequestParam(value="codeType",required=false) String codeType,
			@RequestParam(value="parentCode",required=false) String parentCode,
			@RequestParam(value="parentCodeId",required=false) Long parentCodeId,
			@RequestParam(value="version",required=false) String version,
			@RequestParam Map<String,?> params
			) {
		
		ModelAndView mv = new ModelAndView("attachment/attachmentList");
		List<Attachment> lists = null;
		List<Dictionary> dicts = null;
		if(!StringUtils.isEmpty(codeType)){
			if(!StringUtils.isEmpty(codeType))
				dicts  = dictionaryRespository.findByParentCode(codeType, parentCode);
			else if(parentCodeId != null)
				dicts = dictionaryRespository.findByParentId(parentCodeId, codeType);
		}
		if(!StringUtils.isEmpty(group)){
			if(!StringUtils.isEmpty(version)){
				lists = attachmentRepository.findByGroupAndVersion(group,version);
			}else{
				lists = attachmentRepository.findByGroup(group);					
			}			
		}
		else
			group = attachmentService.generateGroupCode(group);
		
		mv.addObject("checked", allTypeCheck(lists,dicts));
		
		mv.addAllObjects(params);
		mv.addObject("refresh", "0");
		mv.addObject("group", group);
		mv.addObject("dicts", dicts);
		mv.addObject("lists", lists);
		
		return mv;
	}

	@RequestMapping(value = "findByGroups", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, List<Attachment>> findByGroups(String[] groups) {
		Map<String, List<Attachment>> map = null;
		List<Attachment> attachs = null;
		for (String group : groups) {
			if (map == null)
				map = new HashMap<String, List<Attachment>>(groups.length);
			attachs = attachmentRepository.findByGroup(group);
			map.put(group, attachs);
		}
		return map;
	}
	@RequestMapping(value = "findByGroupImport")
	public ModelAndView findListByGroupImport(
			@RequestParam(value="group",required=false) String group,
			@RequestParam(value="codeType",required=false) String codeType,
			@RequestParam(value="parentCode",required=false) String parentCode,
			@RequestParam(value="parentCodeId",required=false) Long parentCodeId,
			@RequestParam(value="importJspType",required=false) String importJspType,
			@RequestParam(value="operation",required=false) String operation,
			@RequestParam Map<String,?> params
			) throws Exception {
		ModelAndView mv = new ModelAndView("attachment/"+importJspType);
		List<Attachment> lists = null;
		List<Dictionary> dicts = null;
		if(!StringUtils.isEmpty(codeType)){
			if(!StringUtils.isEmpty(codeType))
				dicts  = dictionaryRespository.findByParentCode(codeType, parentCode);
			else if(parentCodeId != null)
				dicts = dictionaryRespository.findByParentId(parentCodeId, codeType);
		}
		if(!StringUtils.isEmpty(group))
			lists =  attachmentRepository.findByGroup(group);
		else
			group = attachmentService.generateGroupCode(group);
		
		mv.addObject("checked", allTypeCheck(lists,dicts));
		
		mv.addAllObjects(params);
		mv.addObject("refresh", "0");
		mv.addObject("today", DateUtil.getToday());
		mv.addObject("group", group);
		mv.addObject("dicts", dicts);
		mv.addObject("lists", lists);
		mv.addObject("operation", operation);
		
		return mv;
	}

	@RequestMapping(value = "uploadFiles", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView upload(
			@RequestParam(value = "group", required = false) String group,
			@RequestParam(value = "codeIds", required = false) Long[] codeIds,
			@RequestParam("files") MultipartFile[] files,
			@RequestParam(value="codeType",required=false) String codeType,
			@RequestParam(value="parentCode",required=false) String parentCode,
			@RequestParam(value="parentCodeId",required=false) Long parentCodeId,
			@RequestParam(value="localFileDir",required=false) String localFileDir,
			@RequestParam(value="version",required=false) String version,
			@RequestParam(value="origNameUsed",required=false,defaultValue="false") boolean origNameUsed,
			HttpSession session,
			@RequestParam Map<String,?> params) throws Exception {
		
		Attachment attach = null;
		long account = 0l;
		List<Attachment> attaches = null;
		String userName = null;
		ModelAndView mv = new ModelAndView("attachment/attachmentList");
		List<Dictionary> dicts = null;
		List<Attachment> lists = null;
		
		try{
			mv.addAllObjects(params);
			mv.addObject("refresh", "0");
			if(files != null && files.length> 0){
				LOG.debug("Upload File size:",files.length);
				group = attachmentService.generateGroupCode(group);
				
				LOG.debug("Current group code:",group);
				
				account =(Long)session.getAttribute(LoginController.SEC_CUR_ACCOUNT_ID);
				
				LOG.debug("Current account:",account);
				
				userName = (String)session.getAttribute(LoginController.SEC_CUR_USER_NAME);
				LOG.debug("Current userName:",userName);
				attaches = new ArrayList<Attachment>(files.length);
				LOG.debug("Start building attachement.");
				for(int i = 0,len = files.length;i<len;i++){
					
					attach = new Attachment();
					attach.setFile(files[0]);
					if(codeIds != null) attach.setCodeId(codeIds[i]);
					attach.setAccountId(account);
					attach.setGroup(group);
					attach.setUploaderName(userName);
					
					attaches.add(attach);
				}
				LOG.debug("End building attachement.");
				
				attachmentService.uploadFiles(attaches, localFileDir, version, origNameUsed);
				
				LOG.debug("Saving attachments success..");
				
				if(!StringUtils.isEmpty(version)){
					lists = attachmentRepository.findByGroupAndVersion(group,version);
				}else{
					lists = attachmentRepository.findByGroup(group);					
				}
				
				if(!StringUtils.isEmpty(codeType)){
					if(!StringUtils.isEmpty(codeType))
						dicts  = dictionaryRespository.findByParentCode(codeType, parentCode);
					else if(parentCodeId != null)
						dicts = dictionaryRespository.findByParentId(parentCodeId, codeType);
				}
				mv.addObject("checked", allTypeCheck(lists,dicts));
				mv.addObject("group", group);
				mv.addObject("dicts", dicts);
				mv.addObject("lists", lists);
				
			}else{
				LOG.warn("No file to be uploaded.");
			}
		}catch(Exception e){
			if( e instanceof AttachUploadException){
				LOG.warn("upload files failed: %s",e.getMessage());
				mv.addObject("errors", e.getMessage());
				lists = attachmentRepository.findByGroup(group);
				
				if(!StringUtils.isEmpty(codeType)){
					if(!StringUtils.isEmpty(codeType))
						dicts  = dictionaryRespository.findByParentCode(codeType, parentCode);
					else if(parentCodeId != null)
						dicts = dictionaryRespository.findByParentId(parentCodeId, codeType);
				}
				
				
				mv.addObject("checked", allTypeCheck(lists,dicts));
				mv.addObject("group", group);
				mv.addObject("dicts", dicts);
				mv.addObject("lists", lists);
			}else{
				LOG.error("upload files failed",e);
				throw e;
			}
		}
		return mv;
	}
	
	@RequestMapping(value = "removeAttach", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView remove(@RequestParam(value="group") String group,
			@RequestParam(value="attachId") long attachId,
			@RequestParam Map<String,?> params,
			HttpSession session,
			@RequestParam(value="codeType",required=false) String codeType,
			@RequestParam(value="parentCode",required=false) String parentCode,
			@RequestParam(value="parentCodeId",required=false) Long parentCodeId
			) throws Exception {
		
		ModelAndView mv = new ModelAndView("attachment/attachmentList");
		List<Dictionary> dicts = null;
		List<Attachment> lists = null;
		Attachment attach = null;
		long account = 0l;
		String userName = null;
		
		try{
				mv.addAllObjects(params);
				mv.addObject("refresh", "0");
				LOG.debug("Attachment id to be deleted:",attachId);
				
				LOG.debug("Current group code:",group);
				account =(Long)session.getAttribute(LoginController.SEC_CUR_ACCOUNT_ID);
				LOG.debug("Current account:",account);
				userName = (String)session.getAttribute(LoginController.SEC_CUR_USER_NAME);
				
				LOG.debug("Current userName:",userName);
				
				attach = attachmentRepository.findOne(attachId);
				
				if(attach == null){
					LOG.debug("Attachment not found by id:",attachId);
				}else{
					attachmentRepository.delete(attach);
					// octal think 逻辑删除
					// this.attachmentService.updateRemovedById(attach.getId(), Integer.valueOf(1));
				}
				
				lists = attachmentRepository.findByGroup(group);
				
				if(!StringUtils.isEmpty(codeType)){
					if(!StringUtils.isEmpty(codeType))
						dicts  = dictionaryRespository.findByParentCode(codeType, parentCode);
					else if(parentCodeId != null)
						dicts = dictionaryRespository.findByParentId(parentCodeId, codeType);
				}
				mv.addObject("checked", allTypeCheck(lists,dicts));
				mv.addObject("group", group);
				mv.addObject("dicts", dicts);
				mv.addObject("lists", lists);
				mv.addObject("refresh", "1");
		}catch(Exception e){
			LOG.error("delete file failed",e);
			throw e;
		}
		return mv;
	}
	
	@RequestMapping(value = "download/{attachId}")
	public 	String download(@PathVariable long attachId, HttpServletResponse response) throws Exception {
		
		Attachment attach = null;
		OutputStream out = null;
		BufferedInputStream bis = null;
		FileInputStream fis = null;
		byte[] buffer = null;
		int len = 0;
		try{
				attach = attachmentRepository.findOne(attachId);
				if(attach != null){
					response.reset();
		            response.setContentType(AttachContentType.parseContentType(attach.getFileExtName()));
		            response.setHeader( "Content-Disposition" ,  "attachment;filename="+ java.net.URLEncoder.encode(attach.getFileName(), "UTF-8"));
		            response.addHeader("Content-Length", "" +attach.getSize());
		            response.setCharacterEncoding("UTF-8");
		            
		            fis = new FileInputStream(attach.getFileFullPath());
		            bis = new BufferedInputStream(fis);
		            buffer = new byte[1024];
		            out = response.getOutputStream();
		            while((len = bis.read(buffer)) != -1){
		            	out.write(buffer, 0, len);
		            }
		            return null;
				}
		}catch(Exception e){
			LOG.error("delete file failed",e);
			throw e;
		}finally{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(fis);
		}
		return null;
	}
	
	@RequestMapping(value = "preview/{attachId}")
	public @ResponseBody
	ModelAndView preview(@PathVariable long attachId) throws Exception {
		ModelAndView mv = new ModelAndView("attachment/preview");

		Attachment attach = attachmentRepository.findOne(attachId);
		String origPath = null;
		String swfPath = null;
		boolean isReady = false;
		boolean isImage = false;
		if(attach != null){
			origPath = attach.getFileFullPath();
			File origFile = new File(origPath);

			if(!origFile.exists()){
				LOG.error("该附件原始文件未找到,ID "+attachId);	
			}else{	//是否为图片
				isImage = testImage(origFile);
			}
			
			swfPath = origPath.substring(0,origPath.lastIndexOf("."))+".swf";
			File swf = new File(swfPath);
			isReady = swf.exists();
			
			mv.addObject("attachId", attachId);
			mv.addObject("filepath", origPath.replaceAll("\\\\", "\\\\\\\\"));
		}else{
			LOG.error("该附件已被删除,ID "+attachId);
		}

		mv.addObject("isReady", isReady);
		mv.addObject("isImage", isImage);
		mv.addObject("converterUrl", attachmentService.getConverterUlr());
		
		return mv;
	}
	
	@RequestMapping(value = "loadSwf/{attachId}")
	public String loadSwf(@PathVariable long attachId, @RequestParam(value="inSwf", required=false, defaultValue="true") boolean inSwf, HttpServletResponse response) throws Exception {
		
		Attachment attach = null;
		OutputStream out = null;
		BufferedInputStream bis = null;
		FileInputStream fis = null;
		byte[] buffer = null;
		int len = 0;
		String swfPath = null;
		String origPath = null;
		String origExt = null;
		try{
			attach = attachmentRepository.findOne(attachId);
			if(attach != null){
				origPath = attach.getFileFullPath();
				origExt = attach.getFileName().substring(attach.getFileName().lastIndexOf("."));
				swfPath = origPath.substring(0,origPath.lastIndexOf("."))+".swf";
				
				response.reset();
	            response.setContentType(AttachContentType.parseContentType(inSwf ? ".swf" : origExt));
	            response.setCharacterEncoding("UTF-8");
	            
	            fis = new FileInputStream(inSwf ? swfPath : origPath);
	            bis = new BufferedInputStream(fis);
	            buffer = new byte[1024];
	            out = response.getOutputStream();
	            while((len = bis.read(buffer)) != -1){
	            	out.write(buffer, 0, len);
	            }
	            return null;
			}
		}catch(Exception e){
			LOG.error("load file failed",e);
			throw e;
		}finally{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(fis);
		}
		return null;
	}
	
	private boolean testImage(File file){
		try {
			Image image = ImageIO.read(file);
			if(image != null){
				return true;				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	private int allTypeCheck(List<Attachment> attchese,List<Dictionary> dicts){
		if(dicts != null && dicts.size() > 0 && attchese != null && attchese.size() > 0){
			Set<Long> checked = new HashSet<Long>(dicts.size());
			for(Dictionary dict:dicts){
				for(Attachment a:attchese){
					if(a.getDict() == null)
						continue;
				if((a.getDict().getId()).equals(dict.getId()))
					checked.add(a.getDict().getId());
					
				}
			}
			return dicts.size() == checked.size()?1:0;
		}
		
		if(dicts == null||dicts.size() == 0)//if dictionary has no value then check pass
			return 1;
		
		if(dicts != null && dicts.size()>0 && (attchese == null || attchese.size() == 0))//if dictionary has value and no attachment is uploaded then fail check
			return 0;
		
		return 1;
	}
}
