package com.wonders.framework.node.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.framework.node.entity.NodeType;
import com.wonders.framework.node.repository.NodeTypeRepository;

@Service
public class NodeTypeService {
	
	@Inject
	private NodeTypeRepository nodeTypeRepository;
	
	@Transactional
	public NodeType save(NodeType nt){
		nt = this.nodeTypeRepository.save(nt);
		return nt;
	}
	
	@Transactional
	public int updateDiscardFlag(Long id, int value, Long uId, String uDate){
		return this.nodeTypeRepository.updateDiscardFlag(id, value, uId, uDate);
	}
	
}
