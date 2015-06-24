package com.wonders.framework.node.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.node.entity.NodeAttr;
import com.wonders.framework.repository.MyRepository;

public interface NodeAttrRepository extends MyRepository<NodeAttr, Long> {
	
	@Modifying
	@Query(" update NodeAttr e set e.removed=?2, e.uUserId=?3, e.uDate=?4 where e.id=?1 ")
	public int updateRemovedById(Long id, int value, Long uId, String uDate);
	
	@Query(" from NodeAttr e where e.code=?1 ")
	public List<NodeAttr> userFindByCode(String code);

}
