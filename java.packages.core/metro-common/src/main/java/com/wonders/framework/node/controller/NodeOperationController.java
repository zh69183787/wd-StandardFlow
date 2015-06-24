package com.wonders.framework.node.controller;

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
import com.wonders.framework.node.entity.NodeBase;
import com.wonders.framework.node.entity.NodeOperation;
import com.wonders.framework.node.repository.NodeBaseRepository;
import com.wonders.framework.node.repository.NodeOperationRepository;
import com.wonders.framework.node.service.NodeOperationService;
import com.wonders.framework.repository.MyRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.framework.utils.StringUtil;
@Controller
@RequestMapping("nodeOperation")
public class NodeOperationController extends AbstractCrudController<NodeOperation, Long> {

	@Inject
	private NodeOperationRepository nodeOperationRepository;
	@Inject
	private NodeOperationService nodeOperationService;
	
	@Inject
	private NodeBaseRepository nodeBaseRepository;
	
	@Override
	protected MyRepository<NodeOperation, Long> getRepository() {
		// TODO Auto-generated method stub
		return this.nodeOperationRepository;
	}
	
	@RequestMapping("nodeOperationListView")
	public ModelAndView nodeOperationListView(@RequestParam(value="nodeBaseId", required=true)Long nodeBaseId,
			HttpSession session) throws Exception{
		ModelAndView mv = new ModelAndView("node/node_operation_list");
		
		NodeBase nodeBase = this.nodeBaseRepository.findOne(nodeBaseId);
		if(nodeBase == null){
			throw new Exception(" nodeOperationListView NodeBase is null ");
		}
		
		mv.addObject("nodeBase", nodeBase);
		return mv;
	}
	
	@RequestMapping("nodeOperationEditView")
	public ModelAndView nodeOperationEditView(@RequestParam(value="nodeBaseId", required=true)Long nodeBaseId, 
			@RequestParam(value="id", required=false, defaultValue="0")Long id, 
			@RequestParam(value="editFlag", required=false, defaultValue="true")boolean editFlag,
			HttpSession session) throws Exception{
		ModelAndView mv = new ModelAndView();
		NodeBase nodeBase = this.nodeBaseRepository.findOne(nodeBaseId);
		if(nodeBase == null){
			throw new Exception(" nodeOperationEditView NodeBase is null ");
		}
		
		NodeOperation nodeOperation = this.nodeOperationRepository.findOne(id);
		if(nodeOperation == null){
			nodeOperation = new NodeOperation();
			nodeOperation.setNodeBase(nodeBase);
			nodeOperation.setNodeBaseId(nodeBase.getId());
		}
		
		if(editFlag){
			mv.setViewName("node/node_operation_edit");
		}else{
			mv.setViewName("node/node_operation_detail");
		}
		
		mv.addObject("nodeOperation", nodeOperation);
		mv.addObject("nodeBase", nodeBase);
		return mv;
	}
	
	@RequestMapping(value="save")
	public @ResponseBody String save(@RequestParam Map<String, ?> params, HttpSession session){
		try{
			Object oId = params.get("nodeOperation.id");
			Long id;
			if(oId != null && !"".equals(oId.toString())){
				id = Long.valueOf(oId.toString());
			}else{
				id = Long.valueOf(0);
			}
			
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			NodeOperation nodeOperation = this.nodeOperationRepository.findOne(id);
			if(nodeOperation == null){
				nodeOperation = new NodeOperation();
				nodeOperation.setcUserId(account.getId());
				nodeOperation.setcDate(today);
			}
			
			StringUtil.setValueNc(params, nodeOperation, "nodeOperation");
			nodeOperation.setuUserId(account.getId());
			nodeOperation.setuDate(today);
			
			this.nodeOperationService.save(nodeOperation);
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
				count += this.nodeOperationService.updateRemovedById(id, 1, account.getId(), today);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "{success:"+count+", fail:"+(ids.length-count)+"}";
	} 

}
