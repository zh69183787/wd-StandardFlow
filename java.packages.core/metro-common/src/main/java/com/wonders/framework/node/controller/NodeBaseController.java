package com.wonders.framework.node.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import com.wonders.framework.node.entity.NodeAttr;
import com.wonders.framework.node.entity.NodeBase;
import com.wonders.framework.node.entity.NodeType;
import com.wonders.framework.node.repository.NodeBaseRepository;
import com.wonders.framework.node.repository.NodeTypeRepository;
import com.wonders.framework.node.service.NodeBaseService;
import com.wonders.framework.node.util.ListSortComparator;
import com.wonders.framework.repository.MyRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.framework.utils.StringUtil;

@Controller
@RequestMapping("nodeBase")
public class NodeBaseController extends AbstractCrudController<NodeBase, Long> {
	
	@Inject
	private NodeBaseRepository nodeBaseRepository;
	@Inject
	private NodeBaseService nodeBaseService;
	
	@Inject
	private NodeTypeRepository nodeTypeRepository;

	@Override
	protected MyRepository<NodeBase, Long> getRepository() {
		// TODO Auto-generated method stub
		return this.nodeBaseRepository;
	}
	
	@RequestMapping(value="findAllNodeBase", method=RequestMethod.GET)
	public @ResponseBody Page<NodeBase> findAllNodeBase(@RequestParam Map<String, ?> params, Pageable pageable){
		Pageable paramPage = new PageRequest(0, 49999, pageable.getSort());
		Map<?, ?> searchParams = getSearchParams(params);
		Page<NodeBase> page = this.nodeBaseRepository.findAll(searchParams, paramPage);
		List<NodeBase> contentList = this.nodeBaseService.listToTreeListMap(page.getContent(), Long.valueOf(1));
		if(contentList != null && contentList.size()>1){
			Collections.sort(contentList, new ListSortComparator(ListSortComparator.ASC)); 
		}
		if(contentList == null)contentList = new ArrayList<NodeBase>();
		page = new PageImpl<NodeBase>(contentList);
		return page;
	}
	
	@RequestMapping("nodeBaseListView")
	public ModelAndView nodeBaseListView(@RequestParam(value="nodeTypeCode", required=true)String nodeTypeCode, 
			HttpSession session){
		ModelAndView mv = new ModelAndView("node/node_base_list");
		
		List<NodeType> listNT = this.nodeTypeRepository.userFindByCodeNotDiscard(nodeTypeCode);
		mv.addObject("listNT", listNT);
		return mv;
	}
	
	@RequestMapping("nodeBaseEditView")
	public ModelAndView nodeBaseEditView(@RequestParam(value="nodeTypeId", required=false, defaultValue="0")Long nodeTypeId,
			@RequestParam(value="praentId", required=false, defaultValue="1")Long praentId, 
			@RequestParam(value="id", required=false, defaultValue="0")Long id,
			@RequestParam(value="nearId", required=false, defaultValue="0")Long nearId,
			@RequestParam(value="editFlag", required=false, defaultValue="true")boolean editFlag,
			HttpSession session) throws Exception{
		ModelAndView mv = new ModelAndView();
		try{
			NodeBase nodeBase = this.nodeBaseRepository.findOne(id);
			
			if(nodeBase == null){
				if(Long.valueOf(0).equals(praentId)){
					throw new Exception("nodeBaseEditView praentId is 0 ");
				}
				nodeBase = new NodeBase();
				NodeType nodeType = this.nodeTypeRepository.findOne(nodeTypeId);
				if(nodeType == null){
					throw new Exception("nodeBaseEditView nodeType is null");
				}
				nodeBase.setNodeType(nodeType);
				nodeBase.setNodeTypeId(nodeTypeId);
				nodeBase.setPraentId(praentId);
			}
			
			if(editFlag){
				mv.setViewName("node/node_base_edit");
			}
			
			mv.addObject("nearId", nearId);
			mv.addObject("nodeBase", nodeBase);
		}catch(Exception e){
			throw e;
		}
		return mv;
	}
	
	@RequestMapping(value="save")
	public @ResponseBody String save(@RequestParam Map<String, ?> params, HttpSession session){
		try{
			Object oId = params.get("nodeBase.id");
			Long id;
			if(oId != null && !"".equals(oId.toString())){
				id = Long.valueOf(oId.toString());
			}else{
				id = Long.valueOf(0);
			}
			
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			NodeBase nodeBase = this.nodeBaseRepository.findOne(id);
			
			Long nearId = null;
			if(nodeBase == null){
				nodeBase = new NodeBase();
				nodeBase.setcUserId(account.getId());
				nodeBase.setcDate(today);
				Object o = params.get("nb_nearId");
				if(o == null || "".equals(o.toString())){
					nearId = Long.valueOf(0);
				}else{
					nearId = Long.valueOf(o.toString());
				}
			}
			
			StringUtil.setValueNc(params, nodeBase, "nodeBase");
			nodeBase.setuUserId(account.getId());
			nodeBase.setuDate(today);
			
			if(nodeBase.getCode() == null || "".equals(nodeBase.getCode())){
				return "{success:false, message:'code_is_null'}";
			}else{
				List<NodeBase> list = this.nodeBaseRepository.userFindByCode(nodeBase.getCode());
				if(list != null && list.size() > 0){
					boolean f = true;
					for(NodeBase na : list){
						if(na.getId().equals(nodeBase.getId())){
							f = false;
							break;
						}
					}
					if(f)return "{success:false, message:'code_is_not_only'}";
				}
			}
			
			this.nodeBaseService.save(nodeBase, nearId, account, today);
			
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
			
			count += this.nodeBaseService.updateRemovedAndChildrenByIds(ids, account, today);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "{success:"+count+", fail:0}";
	} 
	
	/**
	 * 修改排序 根据绝对位置
	 * @param id
	 * @param parentId
	 * @param orderNum
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="updateONAbsoluteLocation", method=RequestMethod.POST)
	public @ResponseBody String updateONAbsoluteLocation(@RequestParam(value="id", required=true)Long id,
			@RequestParam(value="parentId", required=false, defaultValue="1")Long parentId,
			@RequestParam(value="orderNum", required=false, defaultValue="-1")int orderNum,
			HttpSession session) throws Exception{
		try{
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			List<NodeBase> list = this.nodeBaseService.updateONAbsoluteLocation(id, parentId, orderNum, account, today);
			return "{success:true, size:"+list.size()+"}";
		}catch(Exception e){
			throw e;
		}
	} 
	
	/**
	 * 修改排序根据相对位置
	 * @param id
	 * @param parentId
	 * @param postpositionId
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="updateONByRelativeLocation", method=RequestMethod.POST)
	public @ResponseBody String updateONByRelativeLocation(@RequestParam(value="id", required=true)Long id,
			@RequestParam(value="parentId", required=false, defaultValue="1")Long parentId,
			@RequestParam(value="locationId", required=false, defaultValue="0" )Long locationId,
			HttpSession session) throws Exception{
		try{
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			List<NodeBase> list = this.nodeBaseService.updateONByRelativeLocation(id, parentId, locationId, account, today);
			return "{success:true, size:"+list.size()+"}";
		}catch(Exception e){
			throw e;
		}
	}
	
	/** 阶段上下移  下移时 参数orderFalg设置为false
	 * @param id
	 * @param orderFalg
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="updateONByIdMove", method=RequestMethod.POST)
	public @ResponseBody String updateONByIdMove(@RequestParam(value="id", required=true)Long id,
			@RequestParam(value="orderFalg", required=false, defaultValue="true")boolean orderFalg,
			HttpSession session) throws Exception{
		try{
			Account account = (Account) session.getAttribute(LoginController.SEC_CUR_ACCOUNT);
			String today = DateUtil.getToday();
			
			List<NodeBase> list = this.nodeBaseService.updateONByIdMove(id, orderFalg, account, today);
			return "{success:true, size:" + list.size() + "}";
		}catch(Exception e){
			throw e;
		}
	}

}
