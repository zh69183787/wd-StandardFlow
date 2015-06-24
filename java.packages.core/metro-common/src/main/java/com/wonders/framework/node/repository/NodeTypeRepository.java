package com.wonders.framework.node.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.node.entity.NodeType;
import com.wonders.framework.repository.MyRepository;

public interface NodeTypeRepository extends MyRepository<NodeType, Long> {
	
	@Query(" from NodeType nt where nt.removed=0 and nt.code=?1 and nt.type=?2 ")
	public NodeType userFindByCodeAndType(String code, int type);
	
	@Query(" from NodeType nt where nt.removed=0 and nt.code=?1  order by nt.orderNum ")
	public List<NodeType> userFindByCode(String code);
	
	@Query(" from NodeType nt where nt.removed=0 and nt.code=?1 and nt.discardFlag=0  ")
	public List<NodeType> userFindByCodeNotDiscard(String code);
	
	@Query(" from NodeType nt where nt.removed=0 and nt.discardFlag=0 ")
	public List<NodeType> userFindAllNotDiscard();
	
	@Query(" select distinct(e.code) from NodeType e where e.removed=0 ")
	public List<String> userFindAllNodeTypeCode();
	
	@Modifying
	@Query(" update NodeType e set e.discardFlag=?2, e.uUserId=?3, e.uDate=?4 where e.id=?1 ")
	public int updateDiscardFlag(Long id, int value, Long uId, String uDate);
	
	@Query(" select max(type) from NodeType ")
	public int userFindMaxType();

}
