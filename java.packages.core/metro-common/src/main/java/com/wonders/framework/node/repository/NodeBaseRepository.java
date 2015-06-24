package com.wonders.framework.node.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.node.entity.NodeBase;
import com.wonders.framework.repository.MyRepository;

public interface NodeBaseRepository extends MyRepository<NodeBase, Long> {
	
	@Query(" from NodeBase nb left join fetch nb.nodeType nt where nb.removed =0 and nb.praentId=1 and nt.removed=0 and nt.code=?1 and nt.type=?2 ")
	public List<NodeBase> userFindByNodeType(String nodeTypeCode, int type);
	
	@Query(" select distinct(nb) from NodeBase nb where nb.removed =0 and nb.nodeTypeId=?1 ")
	public List<NodeBase> userFindByNodeTypeIdList(Long nodeTypeId);
	
	/**
	 * 获得某一流程的重点阶段列表
	 * @param flowType
	 * @return
	 */
	@Query(" from NodeBase nb left join fetch nb.nodeType nt where nb.removed = 0 and nt.removed = 0 " +
			" and nb.isKey = 0 and nt.type = ?1")
	public List<NodeBase> keyStages(Integer flowType);

	@Modifying
	@Query(" update NodeBase e set e.removed=?2, e.uUserId=?3, e.uDate=?4 where e.id=?1 ")
	public int updateRemovedById(Long id, int value, Long uId, String uDate);
	
	@Query(" from NodeBase e where e.code=?1 ")
	public List<NodeBase> userFindByCode(String code);
	
	@Query(" select id from NodeBase e where e.praentId=?1 ")
	public List<Long> userFindIdByParentId(Long parentId);
	
	@Query(" from NodeBase e where e.praentId=?1 order by e.orderNum ASC ")
	public List<NodeBase> userFindByParentId(Long id);
	
	@Query(" from NodeBase e where e.praentId=?1 order by e.orderNum DESC ")
	public List<NodeBase> userFindByParentIdDesc(Long id);
	
	/**
	 * 用户获得某一流程的重点阶段列表
	 * @param flowType
	 * @return
	 */
	@Query(" from NodeBase nb left join fetch nb.nodeType nt where nb.removed = 0 and nt.removed = 0 " +
			" and nb.isKey = 0 and nt.type = ?1 order by nt.orderNum ")
	public List<NodeBase> userFindKeyStages(Integer flowType);
	
}
