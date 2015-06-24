package com.wonders.personal.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.framework.auth.entity.Dictionary;
import com.wonders.framework.auth.entity.Group;
import com.wonders.framework.auth.repository.DictionaryRepository;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.personal.entity.StdApply;
import com.wonders.personal.entity.StdPerson;
import com.wonders.personal.entity.StdSch;
import com.wonders.personal.helper.AutoArrayList;
import com.wonders.personal.repository.StdApplyRepository;
import com.wonders.personal.service.StdApplyService;
import com.wonders.util.StringUtil;


@Controller
@RequestMapping("stdApply")
public class StdApplyController extends AbstractCrudController<StdApply, Long> {
	private static final Logger LOG = LoggerFactory.getLogger(StdApplyController.class);
	@Inject
	private StdApplyService stdApplyService;
	
	@Inject
	private StdApplyRepository stdApplyRepository;
	
	@Inject
	private DictionaryRepository dictionaryRepository;

	@Override
	protected MyRepository<StdApply, Long> getRepository() {
		return stdApplyRepository;
	}

	@RequestMapping("list")
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("personal/std_apply_list");
		return mv;  
	}
	
	
	@RequestMapping("add")
	public ModelAndView add(@RequestParam(required=false) Long id,@RequestParam(value="editable",required=false,defaultValue="true") boolean editable){
		ModelAndView mv = new ModelAndView();
		StdApply sa = null;
		if(id != null && id != 0){
			sa = stdApplyRepository.getSAWithSubs(id);
			sa.setSsList(stdApplyRepository.getSchs(id));
		}
		if(sa == null){
			sa = new StdApply();
			mv.addObject("today",DateUtil.getToday());
			mv.setViewName("personal/std_apply_edit");
		}else{
			if(editable)
				mv.setViewName("personal/std_apply_edit");
			else{
				mv.setViewName("personal/std_apply_detail");
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
		
		mv.addObject("stdApply", sa);
		return mv;  
	}
	
	@RequestMapping(value = "save")
	protected @ResponseBody
	StdApply save(@RequestParam Map<String, ?> params, HttpSession session) throws Exception{
		StdApply sa = new StdApply();
		Object oId =  params.get("stdApply.id");
		if(oId != null && !"".equals(oId) && !"0".equals(oId)){
			sa = stdApplyRepository.findOne(Long.valueOf(oId.toString()));
		}else{
			sa.setcUserId((Long)session.getAttribute("SEC_CUR_ACCOUNT_ID"));
			sa.setApplyDeptId((Long)session.getAttribute("SEC_CUR_DEPT_ID"));
			sa.setcDate(DateUtil.getToday());
		}
		
		sa.setSpList(new AutoArrayList<StdPerson>(StdPerson.class, sa));
		sa.setSsList(new AutoArrayList<StdSch>(StdSch.class, sa));
		StringUtil.setValue(params,sa,"stdApply");
		
		if(params.get("stdApply.stdMinorTypeId") == null || "".equals(params.get("stdApply.stdMinorTypeId"))){
			sa.setStdMinorTypeId(null);
		}
		if(StringUtils.isEmpty(sa.getProjectNo())){  //新增时生成编号
			Group group = (Group)session.getAttribute("SEC_CUR_DEPT");
			sa.setProjectNo(stdApplyService.genNo(group.getGroupcode()));
		}
		
		
		sa.setuDate(DateUtil.getToday());
		sa.setuUser((Long)session.getAttribute("SEC_CUR_ACCOUNT_ID"));
		
		sa = stdApplyRepository.save(sa);

		return sa;
	}

	@RequestMapping(value = "deleteByIds", method = RequestMethod.POST)
	protected @ResponseBody // @RequestParam List<Long> idList parms
	String deleteByIds(@RequestParam(value="ids") Long[] ids){
		stdApplyService.logicDelete(ids);
		return "{success:true}";
	}
	
	@RequestMapping(value = "delPersonById", method = RequestMethod.POST)
	protected @ResponseBody
	String delPersonById(@RequestParam Long id){
		stdApplyService.logicDeletePerson(id);
		return "{success:true}";
	}
	
	@RequestMapping(value = "delSchById", method = RequestMethod.POST)
	protected @ResponseBody
	String delSchById(@RequestParam Long id){
		stdApplyService.logicDeleteSch(id);
		return "{success:true}";
	}
}
