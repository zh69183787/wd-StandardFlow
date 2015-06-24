package com.wonders.framework.node.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.common.controller.LoginController;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.node.entity.NodeType;
import com.wonders.framework.node.repository.NodeTypeRepository;
import com.wonders.framework.node.service.NodeTypeService;
import com.wonders.framework.repository.MyRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.framework.utils.StringUtil;
@Controller
@RequestMapping("nodeType")
public class NodeTypeController extends AbstractCrudController<NodeType, Long> {

	@Inject
	private NodeTypeRepository nodeTypeRepository;
	@Inject
	private NodeTypeService nodeTypeService;
	
	@Override
	protected MyRepository<NodeType, Long> getRepository() {
		// TODO Auto-generated method stub
		return this.nodeTypeRepository;
	}
	
	@RequestMapping(value="findAllNodeType",method = RequestMethod.GET)
	protected @ResponseBody
	Page<NodeType> findAll(@RequestParam Map<String, ?> params, Pageable pageable) {
		Map<?, ?> searchParams = getSearchParams(params);
		return getRepository().findAll(searchParams, pageable);
	}
	
	@RequestMapping("nodeTypeListView")
	public ModelAndView nodeTypeListView(@RequestParam(value="nodeTypeCode", required=true)String nodeTypeCode, 
			HttpSession session){
		ModelAndView mv = new ModelAndView("node/node_type_list");
		
		List<String> listCode = this.findAllNodeTypeCode();
		mv.addObject("nodeTypeCode", nodeTypeCode);
		mv.addObject("listCode", listCode);
		return mv;
	}
	
	@RequestMapping(value="findAllNodeTypeCode", method=RequestMethod.GET)
	public @ResponseBody List<String> findAllNodeTypeCode(){
		List<String> rList = this.nodeTypeRepository.userFindAllNodeTypeCode();
		return rList == null?new ArrayList<String>():rList;
	}
	
	@RequestMapping("nodeTypeEditView")
	public ModelAndView nodeTypeEditView(@RequestParam(value="id", required=false, defaultValue="0")Long id, 
			@RequestParam(value="nodeTypeCode", required=true)String nodeTypeCode,
			@RequestParam(value="editFlag", required=false, defaultValue="true")boolean editFlag,
			HttpSession session){
		ModelAndView mv = new ModelAndView();
		NodeType nodeType = this.nodeTypeRepository.findOne(id);
		if(nodeType == null){
			nodeType = new NodeType();
			nodeType.setCode(nodeTypeCode);
		}
		
		if(editFlag){
			List<String> listCode = this.findAllNodeTypeCode();
			mv.addObject("listCode", listCode);
			mv.setViewName("node/node_type_edit");
		}else{
			mv.setViewName("node/node_type_detail");
		}
		
		mv.addObject("nodeType", nodeType);
		return mv;
	}
	
	@RequestMapping(value="save")
	public @ResponseBody String save(@RequestParam Map<String, ?> params, HttpSession session){
		try{
			Object oId = params.get("nodeType.id");
			Long id;
			if(oId != null && !"".equals(oId.toString())){
				id = Long.valueOf(oId.toString());
			}else{
				id = Long.valueOf(0);
			}
			
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			NodeType nodeType = this.nodeTypeRepository.findOne(id);
			if(nodeType == null){
				nodeType = new NodeType();
				nodeType.setcUserId(account.getId());
				nodeType.setcDate(today);
				
				int type = this.nodeTypeRepository.userFindMaxType();
				++type;
				nodeType.setType(type);
			}
			
			StringUtil.setValueNc(params, nodeType, "nodeType");
			nodeType.setuUserId(account.getId());
			nodeType.setuDate(today);
			
			this.nodeTypeService.save(nodeType);
		}catch(Exception e){
			e.printStackTrace();
			return "{success:false, fail:true}";
		}
		
		return "{success:true, fail:false}";
	} 
	
	@RequestMapping(value="updateDiscardFlagByIds", method=RequestMethod.POST)
	public @ResponseBody String updateDiscardFlagByIds(@RequestParam(value="ids", required=true)Long[] ids, HttpSession session){
		int count = 0;
		try{
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			for(Long id:ids){
				count += this.nodeTypeService.updateDiscardFlag(id, 1, account.getId(), today);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "{success:"+count+", fail:"+(ids.length-count)+"}";
	} 
}
