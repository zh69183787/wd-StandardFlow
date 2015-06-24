package com.wonders.flowWork.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.flowWork.entity.FlowWorkProcess;
import com.wonders.flowWork.entity.FlowWorkThread;
import com.wonders.flowWork.repository.FlowWorkProcessRepository;
import com.wonders.flowWork.repository.FlowWorkThreadRepository;
import com.wonders.flowWork.repository.FlowWorkTypeRepository;
import com.wonders.flowWork.service.FlowWorkProcessService;
import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.auth.repository.AccountRepository;

@Controller
@RequestMapping("flowwork")
public class FlowWorkController {
	@Inject
	FlowWorkProcessRepository flowWorkProcessRepository;
	@Inject
	FlowWorkThreadRepository flowWorkThreadRepository;
	@Inject
	FlowWorkProcessService flowWorkProcessService;
	@Inject
	FlowWorkTypeRepository flowWorkTypeRepository;
	@Inject
	AccountRepository accountRepository;
	
	
	/**
	 * 发起人编辑表单
	 * @param flowUid editFlag 是否可办理
	 * @return
	 */
	@RequestMapping("flowInfo2")
	public ModelAndView flowContent2(
			@RequestParam(value="flowCode",required=false) String flowCode,
			@RequestParam(value="flowUid",required=false) String flowUid,
			@RequestParam(value="objectId",required=false) String objectId,
			@RequestParam(value="objectId",required=false) String objectName,HttpSession session){
		ModelAndView mv = new ModelAndView();
		
		FlowWorkProcess flowProcess =null;
		
		Map<String,Object> map = null;
		
		
		if(flowUid!=null&&!"".equals(flowUid)){
			map = new HashMap<String,Object>();
			flowProcess = flowWorkProcessRepository.findFlowProcessByFlowUid(flowUid);
			List<FlowWorkThread> flowThreads = flowWorkThreadRepository.findByFlowUidAll(flowUid);
			
			//AttachmentRepository
			map.put("flowProcess", flowProcess);
			map.put("flowThreads", flowThreads);
		}else{
			map = flowWorkProcessService.startNewFlow(flowCode,objectId,objectName,session);
		}
		
		mv.addObject("map", map);
		
		mv.setViewName("flowwork/flow_info");
		return mv;
	}
	/**
	 * 发起人编辑表单
	 * @param flowUid editFlag 是否可办理
	 * @return
	 */
	@RequestMapping("flowInfo")
	public ModelAndView flowContent(
			@RequestParam(value="flowUid",required=false) String flowUid,HttpSession session){
		ModelAndView mv = new ModelAndView();
		
		FlowWorkProcess flowProcess =null;
		
		Map<String,Object> map = null;
		
		
		if(flowUid!=null&&!"".equals(flowUid)){
			map = new HashMap<String,Object>();
			flowProcess = flowWorkProcessRepository.findFlowProcessByFlowUid(flowUid);
			List<FlowWorkThread> flowThreads = flowWorkThreadRepository.findByFlowUidAll(flowUid);
			map.put("flowProcess", flowProcess);
			map.put("flowThreads", flowThreads);
			
			Object bizObject = flowWorkProcessRepository.loadObjectByHql(" from " + flowProcess.getObjectClass() + " where id = " + flowProcess.getObjectId());
			map.put("bizObject", bizObject);
			
			Account initAcc = accountRepository.findByIdWithGroup(flowProcess.getUserId());
			map.put("initAcc", initAcc);
			
			flowWorkProcessService.evaluateEL(flowThreads, map);
		}
		
		mv.addObject("map", map);
		mv.addObject("nowtime",new Date().getTime()+"");
		mv.setViewName("flowwork/flow_info");
		return mv;
	}
	/**
	 * 我发起的流程查询
	 * @param flowUid
	 * @return
	 */
	@RequestMapping("flows")
	public ModelAndView flows(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("flowwork/flow_list");
		return mv;
	}
	
	/**
	 * 带我处理的   和 我处理完成的
	 * @param flowUid
	 * @return
	 */
	@RequestMapping("flowThreads")
	public ModelAndView flows(@RequestParam(value="state",required=false) Integer state){
		ModelAndView mv = new ModelAndView();
		mv.addObject("state",state);
		mv.setViewName("flowwork/flow_deal_list");
		return mv;
	}
	
	@RequestMapping(value = "flowInfoAjax")
	public @ResponseBody
	Map<String,Object> flowInfoAjax(@RequestParam Map<String, Object> params,HttpSession session) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		String flowCode = (String) params.get("flowCode");
		String objectId = (String) params.get("objectId");
		String objectName = (String) params.get("objectName");
		
		map = flowWorkProcessService.startNewFlow(flowCode,objectId,objectName,session);
		
		//map.put("map", map);

		return map;
	}
	@RequestMapping(value = "removeFlow")
	public @ResponseBody
	Map<String,Object> removeFlow(@RequestParam Map<String, Object> params,HttpSession session) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		String flowUid = (String) params.get("flowUid");
		
		map =null; flowWorkProcessService.removeFlow(flowUid);
		
		//map.put("map", map);

		return map;
	}
}
