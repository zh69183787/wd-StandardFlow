package com.wonders.flowWork.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.flowWork.entity.FlowWorkType;
import com.wonders.flowWork.entity.FlowWorkUsers;
import com.wonders.flowWork.repository.FlowWorkTypeRepository;
import com.wonders.flowWork.repository.FlowWorkUsersRepository;
import com.wonders.flowWork.service.FlowWorkTypeService;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.util.StringUtil;

@Controller
@RequestMapping("flowworkType")
public class FlowWorkTypeController extends AbstractCrudController<FlowWorkType,Long>{
	
	@Inject
	FlowWorkTypeRepository flowWorkTypeRepository;
	
	@Inject
	FlowWorkTypeService flowWorkTypeService;
	@Inject
	FlowWorkUsersRepository flowWorkUsersRepository;
	
	@Override
	protected MyRepository<FlowWorkType, Long> getRepository() {
		// TODO Auto-generated method stub
		return flowWorkTypeRepository;
	}

	
	/**
	 * 创建   或修改 新流程定义
	 * 传id  修改  id 为null  新增
	 * @param flowTypeId
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView flowTypeAdd(@RequestParam Map<String, ?> params,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		FlowWorkType flowType = new FlowWorkType();
		List<FlowWorkUsers>flowWorkUsers = new ArrayList<FlowWorkUsers>();
		Object id = params.get("flowTypeId");
		if(id!=null&&!"".equals(id)){
			flowType = flowWorkTypeRepository.findOne(Long.parseLong(id.toString()));//findOne(flowType.getId());
			flowWorkUsers = flowWorkUsersRepository.findByTypeId(Long.parseLong(id.toString()));
		}
	
		
		mv.addObject("flowType", flowType);
		mv.addObject("flowWorkUsers", flowWorkUsers);
		mv.setViewName("flowwork/flow_type_add");
		return mv;
	}
	/**
	 * 查看已经定义的  流程信息
	 * @return
	 */
	@RequestMapping("list")
	public ModelAndView flowTypeList() {
		ModelAndView mv = new ModelAndView();
		//查询出目前所有的 流程定义，用于新增时验证流程定义的  名字 及code 的唯一性
		/*List<FlowType>flowTypes = flowTypeRepository.findAll();
		mv.addObject("flowTypes", flowTypes);*/
		mv.setViewName("flowwork/flow_type_list");
		return mv;
	}
	/**
	 * 保存新增数据
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "saveData")
	public @ResponseBody
	String saveData(@RequestParam Map<String, ?> params,
			HttpSession session){
		FlowWorkType flowType = new FlowWorkType();
		Object id = params.get("flowType.id");
		boolean isadd = false;
		if(id==null||"".equals(id)){
			isadd = true;
			flowType.setcUser(session.getAttribute("SEC_CUR_USER_NAME").toString());
			flowType.setcDate(DateUtil.getToday());
		}else{
			flowType = flowWorkTypeRepository.findOne(Long.parseLong(id.toString()));//findOne(flowType.getId());
		}
		StringUtil.setValueNc(params, flowType, "flowType");
		flowType.setuDate(DateUtil.getToday());
		flowType.setuUser(session.getAttribute("SEC_CUR_USER_NAME").toString());
		
		//验证唯一性
		Object oldName = params.get("oldFlowTypeName");
		Object oldCode = params.get("oldFlowTypeCode");
		if(!flowType.getFlowTypeName().equals(oldName)){
			Long has = flowWorkTypeRepository.countOfName(flowType.getFlowTypeName());
			if(has.longValue()>0)return "{success: false,message:'名字已存在，请重新输入！'}";
		}
		
		if(!flowType.getFlowTypeCode().equals(oldCode)){
			List<FlowWorkType> has = flowWorkTypeRepository.findListByCode(flowType.getFlowTypeCode());
			if(has.size()>0)return "{success: false,message:'编码已存在，请重新输入！'}";
		}
		
		Object userIds = params.get("userIds");
		Object userNames =  params.get("userNames");
		Object userIndexs = params.get("userIndexs");
		Object userNodeNames = params.get("userNodeNames");
		Object userAlts = params.get("userAlts");
		
		flowWorkTypeService.saveBo(flowType,isadd,userIds,userNames,userIndexs,userNodeNames,userAlts);
		return "{success: true,message:''}";
	}
}
