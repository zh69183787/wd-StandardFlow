package com.wonders.flowWork.controller;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.flowWork.entity.FlowWorkProcess;
import com.wonders.flowWork.repository.FlowWorkProcessRepository;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;

@Controller
@RequestMapping("flowworkProcess")
public class FlowWorkProcessController extends AbstractCrudController<FlowWorkProcess,Long>{
	
	@Inject
	FlowWorkProcessRepository flowWorkProcessRepository;
	
	@Override
	protected MyRepository<FlowWorkProcess, Long> getRepository() {
		// TODO Auto-generated method stub
		return flowWorkProcessRepository;
	}

	@RequestMapping("openFlowByUid")
	public ModelAndView openFlowByUid(@RequestParam(value="flowUid",required=false) String flowUid){
		ModelAndView mv = new ModelAndView();
		// 字典项 
		/*List<Dictionary> lineDics =  dictionaryRepository.findByParentId(1, "NC_METRO");
		List<Dictionary> contrTypeDics =  dictionaryRepository.findByParentId(1,"NC_METRO_CFILE_TYPE");	*/
/*		mv.addObject("lineDics", lineDics);
		mv.addObject("contrTypeDics", contrTypeDics);*/
		
		mv.setViewName("flow/flow_process");
		return mv;  
	}
	
	@RequestMapping("startFlowByCode")
	public ModelAndView startFlowByCode(@RequestParam(value="code",required=false) String flowTypeCode,@RequestParam(value="objectId",required=false) String objectId){
		ModelAndView mv = new ModelAndView();
		// 字典项 
		/*List<Dictionary> lineDics =  dictionaryRepository.findByParentId(1, "NC_METRO");
		List<Dictionary> contrTypeDics =  dictionaryRepository.findByParentId(1,"NC_METRO_CFILE_TYPE");	*/
/*		mv.addObject("lineDics", lineDics);
		mv.addObject("contrTypeDics", contrTypeDics);*/
		
		
		
		
		mv.setViewName("flow/flow_process");
		return mv;  
	}
	
	/**
	 * 创建新流程定义
	 * @return
	 */
	@RequestMapping("creatFlow")
	public ModelAndView creatFlow() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("flow/creat_flow");
		return mv;  
	}
	
	/**
	 * 新增流程
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "saveFlowProcess")
	protected @ResponseBody
	String saveFlowProcess(@RequestParam Map<String, ?> params){	
		/*Flow flow = new Flow();
		flow.setFlowName("非招标合同审批");
		flowRepository.save(flow);
		*/
		/*Object oId = params.get("cost.id");		
		Long id = (long) 0;
		if(oId != null && !("".equals(oId))){
			id = Long.valueOf(oId.toString());
			params.remove("cost.id");
		}
	
		Cost cost = null;
		if(id == null || id == 0){
			cost = new Cost();
		}else{		
			cost = this.costRepository.findOne(id);
		}
		if(cost != null){			
			StringUtil.setValue(params, cost, "cost");
			super.add(cost);
		}else{
			return "{success: false}";
		}*/
		return "{success: true}";
	}
	
	/**
	 * 我发起的流程查询
	 * @param flowUid
	 * @return
	 */
	@RequestMapping("startFlows")
	public ModelAndView startFlowProcess(@RequestParam(value="flowUid",required=false) String flowUid){
		ModelAndView mv = new ModelAndView();

		
		mv.setViewName("flow/flow_process");
		return mv;  
	}
}
