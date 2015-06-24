package com.wonders.framework.node.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.framework.node.entity.NodeOperation;
import com.wonders.framework.node.repository.NodeOperationRepository;

@Service
public class NodeOperationService {
	
	@Inject
	private NodeOperationRepository nodeOperationRepository;
	
	@Transactional
	public NodeOperation save(NodeOperation no) throws Exception{
		try{
			no = this.nodeOperationRepository.save(no);
		}catch(Exception e){
			throw e;
		}
		return no;
	}
	
	@Transactional
	public int updateRemovedById(Long id, int value, Long uId, String uDate) throws Exception{
		try{
			return this.nodeOperationRepository.updateRemovedById(id, value, uId, uDate);
		}catch(Exception e){
			throw e;
		}
	}

}
