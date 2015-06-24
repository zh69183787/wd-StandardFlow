package com.wonders.framework.node.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.framework.node.entity.NodeAttr;
import com.wonders.framework.node.repository.NodeAttrRepository;

@Service
public class NodeAttrService {
	
	@Inject
	private NodeAttrRepository nodeAttrRepository;
	
	@Transactional
	public NodeAttr save(NodeAttr na) throws Exception{
		try{
			na = this.nodeAttrRepository.save(na);
		}catch(Exception e){
			throw e;
		}
		return na;
	}
	
	@Transactional
	public int updateRemovedById(Long id, int value, Long uId, String uDate) throws Exception{
		try{
			return this.nodeAttrRepository.updateRemovedById(id, value, uId, uDate);
		}catch(Exception e){
			throw e;
		}
	}

}
