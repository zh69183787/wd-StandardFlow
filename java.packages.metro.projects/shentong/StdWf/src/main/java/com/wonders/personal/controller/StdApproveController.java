package com.wonders.personal.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.framework.auth.entity.Dictionary;
import com.wonders.framework.auth.repository.DictionaryRepository;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.personal.entity.StdApprove;
import com.wonders.personal.repository.StdApproveRepository;
import com.wonders.personal.service.StdApproveService;
import com.wonders.util.StringUtil;


@Controller
@RequestMapping("stdApprove")
public class StdApproveController extends AbstractCrudController<StdApprove, Long> {
	private static final Logger LOG = LoggerFactory.getLogger(StdApproveController.class);
	@Inject
	private StdApproveService stdApproveService;
	
	@Inject
	private StdApproveRepository stdApproveRepository;
	
	@Inject
	private DictionaryRepository dictionaryRepository;

	@Override
	protected MyRepository<StdApprove, Long> getRepository() {
		return stdApproveRepository;
	}

	@RequestMapping("list")
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("personal/std_approve_list");
		return mv;  
	}
	
	
	@RequestMapping("add")
	public ModelAndView add(@RequestParam(required=false) Long id,@RequestParam(value="editable",required=false,defaultValue="true") boolean editable,
			@RequestParam(required=false) Long srId,
			HttpSession session){
		ModelAndView mv = new ModelAndView();
		StdApprove sa = null;
		if(id != null && id != 0){
			sa = stdApproveRepository.findOne(id);
		}
		if(sa == null){
			sa = stdApproveService.initWithReview(srId);
			mv.addObject("today",DateUtil.getToday());
			mv.setViewName("personal/std_approve_edit");
		}else{
			if(editable)
				mv.setViewName("personal/std_approve_edit");
			else{
				Long userId = (Long)session.getAttribute("SEC_CUR_ACCOUNT_ID");
				String flowUid = sa.getFlowGroup();
				if(stdApproveService.allowModifyInFlow(userId, flowUid)){ //退回到发起人时 可以进行修改
					mv.setViewName("personal/std_approve_edit");	
				}else{
					mv.setViewName("personal/std_approve_detail");					
				}				
			}
		}
		
		if(mv.getViewName().endsWith("edit")){
			// 字典项  招标方式
			List<Dictionary> stdFunDics =  dictionaryRepository.findByTypeCode("STD_FUN");	
			mv.addObject("stdFunDics", stdFunDics);
			List<Dictionary> stdRemarkDics =  dictionaryRepository.findByTypeCode("STD_REMARK");	
			mv.addObject("stdRemarkDics", stdRemarkDics);
			List<Dictionary> stdStageDics =  dictionaryRepository.findByTypeCode("STD_STAGE");	
			mv.addObject("stdStageDics", stdStageDics);			
		}
		
		mv.addObject("version", stdApproveService.getVersion(sa.getFlowGroup()));
		mv.addObject("stdApprove", sa);
		return mv;  
	}
	
	@RequestMapping(value = "save")
	protected @ResponseBody
	StdApprove save(@RequestParam Map<String, ?> params, HttpSession session) throws Exception{
		StdApprove sa = new StdApprove();
		Object oId =  params.get("stdApprove.id");
		if(oId != null && !"".equals(oId) && !"0".equals(oId)){
			sa = stdApproveRepository.findOne(Long.valueOf(oId.toString()));
		}else{
			sa.setcUserId((Long)session.getAttribute("SEC_CUR_ACCOUNT_ID"));
			sa.setApplyDeptId((Long)session.getAttribute("SEC_CUR_DEPT_ID"));
			sa.setcDate(DateUtil.getToday());
		}
		
		StringUtil.setValue(params,sa,"stdApprove");
		
		if(params.get("stdApprove.stdMinorTypeId") == null || "".equals(params.get("stdApprove.stdMinorTypeId"))){
			sa.setStdMinorTypeId(null);
		}
		
		sa.setuDate(DateUtil.getToday());
		sa.setuUser((Long)session.getAttribute("SEC_CUR_ACCOUNT_ID"));
		
		sa = stdApproveService.save(sa,params);

		return sa;
	}

	@RequestMapping(value = "deleteByIds", method = RequestMethod.POST)
	protected @ResponseBody // @RequestParam List<Long> idList parms
	String deleteByIds(@RequestParam(value="ids") Long[] ids){
		stdApproveService.logicDelete(ids);
		return "{success:true}";
	}
	
}
