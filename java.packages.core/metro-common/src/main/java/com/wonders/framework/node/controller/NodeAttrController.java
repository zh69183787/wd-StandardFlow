package com.wonders.framework.node.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.common.controller.LoginController;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.node.entity.NodeAttr;
import com.wonders.framework.node.entity.NodeBase;
import com.wonders.framework.node.repository.NodeAttrRepository;
import com.wonders.framework.node.repository.NodeBaseRepository;
import com.wonders.framework.node.service.NodeAttrService;
import com.wonders.framework.repository.MyRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.framework.utils.StringUtil;
@Controller
@RequestMapping("nodeAttr")
public class NodeAttrController extends AbstractCrudController<NodeAttr, Long> {

	@Inject
	private NodeAttrRepository nodeAttrRepository;
	@Inject
	private NodeAttrService nodeAttrService;
	
	@Inject
	private NodeBaseRepository nodeBaseRepository;
	
	@Override
	protected MyRepository<NodeAttr, Long> getRepository() {
		// TODO Auto-generated method stub
		return this.nodeAttrRepository;
	}
	
	@RequestMapping("nodeAttrListView")
	public ModelAndView nodeAttrListView(@RequestParam(value="nodeBaseId", required=true)Long nodeBaseId,
			HttpSession session) throws Exception{
		ModelAndView mv = new ModelAndView("node/node_attr_list");
		
		NodeBase nodeBase = this.nodeBaseRepository.findOne(nodeBaseId);
		if(nodeBase == null){
			throw new Exception(" nodeAttrListView NodeBase is null ");
		}
		
		mv.addObject("nodeBase", nodeBase);
		return mv;
	}
	
	@RequestMapping("nodeAttrEditView")
	public ModelAndView nodeAttrEditView(@RequestParam(value="nodeBaseId", required=true)Long nodeBaseId, 
			@RequestParam(value="id", required=false, defaultValue="0")Long id, 
			@RequestParam(value="editFlag", required=false, defaultValue="true")boolean editFlag,
			HttpSession session) throws Exception{
		ModelAndView mv = new ModelAndView();
		NodeBase nodeBase = this.nodeBaseRepository.findOne(nodeBaseId);
		if(nodeBase == null){
			throw new Exception(" nodeAttrEditView NodeBase is null ");
		}
		
		NodeAttr nodeAttr = this.nodeAttrRepository.findOne(id);
		if(nodeAttr == null){
			nodeAttr = new NodeAttr();
			nodeAttr.setNodeBase(nodeBase);
			nodeAttr.setNodeBaseId(nodeBase.getId());
		}
		
		if(editFlag){
			mv.setViewName("node/node_attr_edit");
		}else{
			mv.setViewName("node/node_attr_detail");
		}
		
		mv.addObject("nodeAttr", nodeAttr);
		mv.addObject("nodeBase", nodeBase);
		return mv;
	}
	
	@RequestMapping(value="save")
	public @ResponseBody String save(@RequestParam Map<String, ?> params, HttpSession session){
		try{
			Object oId = params.get("nodeAttr.id");
			Long id;
			if(oId != null && !"".equals(oId.toString())){
				id = Long.valueOf(oId.toString());
			}else{
				id = Long.valueOf(0);
			}
			
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			NodeAttr nodeAttr = this.nodeAttrRepository.findOne(id);
			if(nodeAttr == null){
				nodeAttr = new NodeAttr();
				nodeAttr.setcUserId(account.getId());
				nodeAttr.setcDate(today);
			}
			
			StringUtil.setValueNc(params, nodeAttr, "nodeAttr");
			nodeAttr.setuUserId(account.getId());
			nodeAttr.setuDate(today);
			
			if(nodeAttr.getCode() == null || "".equals(nodeAttr.getCode())){
				return "{success:false, message:'code_is_null'}";
			}else{
				List<NodeAttr> list = this.nodeAttrRepository.userFindByCode(nodeAttr.getCode());
				if(list != null && list.size() > 0){
					boolean f = true;
					for(NodeAttr na : list){
						if(na.getId().equals(nodeAttr.getId())){
							f = false;
							break;
						}
					}
					if(f)return "{success:false, message:'code_is_not_only'}";
				}
			}
			
			this.nodeAttrService.save(nodeAttr);
		}catch(Exception e){
			e.printStackTrace();
			return "{success:false, message:'no'}";
		}
		
		return "{success:true, message:'save_ok'}";
	} 
	
	@RequestMapping(value="updateRemovedByIds", method=RequestMethod.POST)
	public @ResponseBody String updateRemovedByIds(@RequestParam(value="ids", required=true)Long[] ids, HttpSession session){
		int count = 0;
		try{
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			for(Long id:ids){
				count += this.nodeAttrService.updateRemovedById(id, 1, account.getId(), today);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "{success:"+count+", fail:"+(ids.length-count)+"}";
	} 

}
