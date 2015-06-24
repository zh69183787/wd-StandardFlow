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
import com.wonders.personal.entity.StdReview;
import com.wonders.personal.repository.StdReviewRepository;
import com.wonders.personal.service.StdReviewService;
import com.wonders.util.StringUtil;


@Controller
@RequestMapping("stdReview")
public class StdReviewController extends AbstractCrudController<StdReview, Long> {
	private static final Logger LOG = LoggerFactory.getLogger(StdReviewController.class);
	@Inject
	private StdReviewService stdReviewService;
	
	@Inject
	private StdReviewRepository stdReviewRepository;
	
	@Inject
	private DictionaryRepository dictionaryRepository;

	@Override
	protected MyRepository<StdReview, Long> getRepository() {
		return stdReviewRepository;
	}

	@RequestMapping("list")
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("personal/std_review_list");
		return mv;  
	}
	
	
	@RequestMapping("add")
	public ModelAndView add(@RequestParam(required=false) Long id,@RequestParam(value="editable",required=false,defaultValue="true") boolean editable,
			@RequestParam(required=false) Long saId, 
			HttpSession session){
		ModelAndView mv = new ModelAndView();
		StdReview sr = null;
		if(id != null && id != 0){
			sr = stdReviewRepository.findOne(id);
		}
		if(sr == null){
			sr = stdReviewService.initWithApply(saId);
			mv.addObject("today",DateUtil.getToday());
			mv.setViewName("personal/std_review_edit");
		}else{
			if(editable)
				mv.setViewName("personal/std_review_edit");
			else{
				Long userId = (Long)session.getAttribute("SEC_CUR_ACCOUNT_ID");
				String flowUid = sr.getFlowGroup();
				if(stdReviewService.allowModifyInFlow(userId, flowUid)){ //退回到发起人时 可以进行修改
					mv.setViewName("personal/std_review_edit");	
				}else{
					mv.setViewName("personal/std_review_detail");					
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
		
		mv.addObject("version", stdReviewService.getVersion(sr.getFlowGroup()));
		mv.addObject("stdReview", sr);
		return mv;  
	}
	
	@RequestMapping(value = "save")
	protected @ResponseBody
	StdReview save(@RequestParam Map<String, ?> params, HttpSession session) throws Exception{
		StdReview sr = new StdReview();
		Object oId =  params.get("stdReview.id");
		if(oId != null && !"".equals(oId) && !"0".equals(oId)){
			sr = stdReviewRepository.findOne(Long.valueOf(oId.toString()));
		}else{
			sr.setcUserId((Long)session.getAttribute("SEC_CUR_ACCOUNT_ID"));
			sr.setApplyDeptId((Long)session.getAttribute("SEC_CUR_DEPT_ID"));
			sr.setcDate(DateUtil.getToday());
		}
		
		StringUtil.setValue(params,sr,"stdReview");
		
		if(params.get("stdReview.stdMinorTypeId") == null || "".equals(params.get("stdReview.stdMinorTypeId"))){
			sr.setStdMinorTypeId(null);
		}
		
		sr.setuDate(DateUtil.getToday());
		sr.setuUser((Long)session.getAttribute("SEC_CUR_ACCOUNT_ID"));
		
		sr = stdReviewService.save(sr,params);

		return sr;
	}

	@RequestMapping(value = "deleteByIds", method = RequestMethod.POST)
	protected @ResponseBody // @RequestParam List<Long> idList parms
	String deleteByIds(@RequestParam(value="ids") Long[] ids){
		stdReviewService.logicDelete(ids);
		return "{success:true}";
	}
	
}
