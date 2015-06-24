package com.wonders.flowWork.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.flowWork.entity.FlowWorkType;
import com.wonders.flowWork.entity.FlowWorkUsers;
import com.wonders.flowWork.repository.FlowWorkProcessRepository;
import com.wonders.flowWork.repository.FlowWorkTypeRepository;
import com.wonders.flowWork.repository.FlowWorkUsersRepository;

@Service
public class FlowWorkTypeService {
	
	@Inject
	FlowWorkProcessRepository flowWorkProcessRepository;
	
	@Inject
	FlowWorkTypeRepository flowWorkTypeRepository;
	
	@Inject
	FlowWorkUsersRepository flowWorkUsersRepository;
	


	/**
	 * 新增流程类型  同时插入 流程定义节点初始化数据
	 * @param FlowWorkType
	 */
	@Transactional
	public FlowWorkType saveBo(FlowWorkType flowWorkType,boolean isadd,Object userIds,Object userNames,Object userIndexs,Object userNodeNames,Object userAlts){
		flowWorkType = flowWorkTypeRepository.save(flowWorkType);
		
		
		List<FlowWorkUsers> users = flowWorkUsersRepository.findByTypeId(flowWorkType.getId());
		for(FlowWorkUsers u:users){
			flowWorkUsersRepository.delete(u);
		}
		
		if(userIds.equals("")){
			return flowWorkType;
		}
		
		String[] ids = userIds.toString().split(",",-1);
		String[] names = userNames.toString().split(",",-1);
		String[] inds = userIndexs.toString().split(",",-1);
		String[] nnames = userNodeNames.toString().split(",",-1);
		String[] alts = userAlts.toString().split(",",-1);
		for(int i=0;i<ids.length;i++){
			FlowWorkUsers user = new FlowWorkUsers();
			if(!"".equals(ids[i])){
				user.setUserId(Long.parseLong(ids[i]));
				user.setUserName(names[i]);
			}
			user.setFlowTypeId(flowWorkType.getId());
			user.setNodeName(nnames[i]);
			user.setOrderIndex(Integer.parseInt(inds[i]));
			user.setAltAuthCode(alts[i]);
			flowWorkUsersRepository.save(user);
		}
		return flowWorkType;
	}
	
	
	/**
	 * 根据审批人审批结果 执行下一步操作
	 * @param id
	 * @param value
	 * @return
	 */
	@Transactional
	public int updateRemovedById(Long id, String value){
		/*int i = 0;
		try {
			i = this.costRepository.updateRemovedById(id, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return 0;
	}
	
}
