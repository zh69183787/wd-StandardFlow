package com.wonders.framework.node.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.common.controller.LoginController;
import com.wonders.framework.node.entity.NodeBase;
import com.wonders.framework.node.repository.NodeBaseRepository;
import com.wonders.framework.utils.DateUtil;

@Service
public class NodeBaseService {
	
	@Inject
	private NodeBaseRepository nodeBaseRepository;
	
	@Transactional
	public NodeBase save(NodeBase e, Long nearId, Account account, String today) throws Exception{
		try{
			e = this.nodeBaseRepository.save(e);
			if(nearId != null){
				this.updateONByRelativeLocation(e, e.getPraentId(), nearId, account, today);
			}
		}catch(Exception e0){
			throw e0;
		}
		return e;
	}
	
	@Transactional
	public List<NodeBase> save(List<NodeBase> eList) throws Exception{
		try{
			eList = this.nodeBaseRepository.save(eList);
		}catch(Exception e0){
			throw e0;
		}
		return eList;
	}
	
	/** 删除阶段
	 * @param ids 接受数组
	 * @param account
	 * @param today
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public int updateRemovedAndChildrenByIds(Long[] ids, Account account, String today) throws Exception{
		try{
			int count = 0;
			for(Long id : ids){
				count += this.updateRemovedById(id, 1, account.getId(), today);
				List<Long> cIds = this.nodeBaseRepository.userFindIdByParentId(id);
				if(cIds != null){
					count += updateRemovedAndChildrenByIds(cIds, account, today);
				}
			}
			
			return count;
		}catch(Exception e){
			throw e;
		}
	}
	
	/** 删除阶段
	 * @param ids 接受list
	 * @param account
	 * @param today
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public int updateRemovedAndChildrenByIds(List<Long> ids, Account account, String today) throws Exception{
		try{
			int count = 0;
			for(Long id : ids){
				count += this.updateRemovedById(id, 1, account.getId(), today);
				List<Long> cIds = this.nodeBaseRepository.userFindIdByParentId(id);
				if(cIds != null){
					count += updateRemovedAndChildrenByIds(cIds, account, today);
				}
			}
			
			return count;
		}catch(Exception e){
			throw e;
		}
	}
	
	@Transactional
	public int updateRemovedById(Long id, int value, Long uId, String uDate) throws Exception{
		try{
			return this.nodeBaseRepository.updateRemovedById(id, value, uId, uDate);
		}catch(Exception e){
			throw e;
		}
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
	@Transactional
	public List<NodeBase> updateONAbsoluteLocation(Long id, Long parentId, int orderNum, Account account, String today) throws Exception{
		try{
			
			NodeBase nodeBase = this.nodeBaseRepository.findOne(id);
			if(nodeBase == null){
				throw new Exception(" nodeBase/updateONAbsoluteLocation nodeBase is null ");
			}
			return this.updateONAbsoluteLocation(nodeBase, parentId, orderNum, account, today);
		}catch(Exception e){
			throw e;
		}
	}	
	/**
	 * 修改排序 根据绝对位置
	 * @param nodeBase
	 * @param parentId
	 * @param orderNum
	 * @param account
	 * @param today
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public List<NodeBase> updateONAbsoluteLocation(NodeBase nodeBase, Long parentId, int orderNum, Account account, String today) throws Exception{
		try{
			List<NodeBase> sourceList = this.nodeBaseRepository.userFindByParentId(parentId);
			if(sourceList == null){
				throw new Exception(" nodeBase/updateONAbsoluteLocation sourceList is null ");
			}
			
			List<NodeBase> list = new ArrayList<NodeBase>();
			nodeBase.setuUserId(account.getId());
			nodeBase.setuDate(today);
			
			nodeBase.setPraentId(parentId);
			if(orderNum < 1){
				nodeBase.setOrderNum(0);
				list.add(nodeBase);
			}
			for(NodeBase o : sourceList){
				
				if(!o.equals(nodeBase)){
					o.setOrderNum(list.size());
					o.setuUserId(account.getId());
					o.setuDate(today);
					list.add(o);
				}
				if(list.size() == orderNum){
					nodeBase.setOrderNum(list.size());
					list.add(nodeBase);
				}
			}
			if(orderNum > list.size()){
				nodeBase.setOrderNum(list.size());
				list.add(nodeBase);
			}
			
			list = this.save(list);
			return list;
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
	@Transactional
	public List<NodeBase> updateONByRelativeLocation(Long id, Long parentId, Long locationId, Account account, String today) throws Exception{
		try{
			NodeBase nodeBase = this.nodeBaseRepository.findOne(id);
			if(nodeBase == null){
				throw new Exception(" nodeBase/updateONByRelativeLocation nodeBase is null ");
			}
			return this.updateONByRelativeLocation(nodeBase, parentId, locationId, account, today);
		}catch(Exception e){
			throw e;
		}
	}
	/** 修改排序根据相对位置
	 * @param nodeBase
	 * @param parentId
	 * @param locationId
	 * @param account
	 * @param today
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public List<NodeBase> updateONByRelativeLocation(NodeBase nodeBase, Long parentId, Long locationId, Account account, String today) throws Exception{
		try{
			List<NodeBase> sourceList = this.nodeBaseRepository.userFindByParentId(parentId);
			if(sourceList == null){
				throw new Exception(" nodeBase/updateONByRelativeLocation sourceList is null ");
			}
			
			List<NodeBase> list = new ArrayList<NodeBase>();
			nodeBase.setuUserId(account.getId());
			nodeBase.setuDate(today);
			
			nodeBase.setPraentId(parentId);
			
			boolean addFlag = true;
			for(NodeBase o : sourceList){
				
				if(o.getId().equals(locationId)){
					nodeBase.setOrderNum(list.size());
					list.add(nodeBase);
					addFlag = false;
				}
				
				if(!o.equals(nodeBase)){
					o.setOrderNum(list.size());
					o.setuUserId(account.getId());
					o.setuDate(today);
					list.add(o);
				}
			}
			if(addFlag){
				nodeBase.setOrderNum(list.size());
				list.add(nodeBase);
			}
			
			list = this.save(list);
			return list;
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
	@Transactional
	public List<NodeBase> updateONByIdMove(Long id, boolean orderFalg, Account account, String today) throws Exception{
		try{
			NodeBase nodeBase = this.nodeBaseRepository.findOne(id);
			if(nodeBase == null){
				throw new Exception(" nodeBase/updateONByRelativeLocation nodeBase is null ");
			}
			return this.updateONByIdMove(nodeBase, orderFalg, account, today);
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
	@Transactional
	public List<NodeBase> updateONByIdMove(NodeBase nodeBase, boolean orderFalg, Account account, String today) throws Exception{
		try{
			Long parentId = nodeBase.getPraentId()==null?Long.valueOf(1):nodeBase.getPraentId();
			List<NodeBase> sourceList = null;
			if(orderFalg){
				sourceList = this.nodeBaseRepository.userFindByParentId(parentId);
			}else{
				sourceList = this.nodeBaseRepository.userFindByParentIdDesc(parentId);
			}
			if(sourceList == null){
				throw new Exception(" nodeBase/updateONByRelativeLocation sourceList is null ");
			}
			nodeBase.setuUserId(account.getId());
			nodeBase.setuDate(today);
			NodeBase temp = null;
			boolean saveFlag = true;
			for(NodeBase o : sourceList){
				if(o.equals(nodeBase)){
					if(temp != null){
						int orderNum = o.getOrderNum();
						o.setOrderNum(temp.getOrderNum());
						temp.setOrderNum(orderNum);
					}else{
						// 不用保存
						saveFlag = false;
					}
				}else{
					temp = o;
				}
			}
			if(saveFlag)sourceList = this.save(sourceList);
			return sourceList;
		}catch(Exception e){
			throw e;
		}
	}
	
	/** 将list NodeBase 转化为 树型结构
	 * @param sourceList
	 * @param parentId
	 * @return
	 */
	public List<NodeBase> listToTreeListMap(List<NodeBase> sourceList, Long parentId){
		Map<Long, List<NodeBase>> map = new HashMap<Long, List<NodeBase>>();
		if(sourceList != null){
			for(NodeBase e : sourceList){
				List<NodeBase> list = map.get(e.getPraentId());
				if(list == null){
					list = new ArrayList<NodeBase>();
					map.put(e.getPraentId(), list);
				}
				list.add(e);
			}
			
			for(NodeBase e : sourceList){
				e.setChildren(map.get(e.getId()));
			}
		}
		return map.get(parentId);
	}

}
