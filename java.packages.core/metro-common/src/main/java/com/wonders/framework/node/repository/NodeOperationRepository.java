package com.wonders.framework.node.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.node.entity.NodeOperation;
import com.wonders.framework.repository.MyRepository;

public interface NodeOperationRepository extends MyRepository<NodeOperation, Long> {
	
	@Query(" from NodeOperation no where no.nodeBaseId=?1 and no.removed=0 ")
	public List<NodeOperation> userFindByNodeBaseId(Long nodeBaseId);
	
	@Query(" from NodeOperation no left join no.nodeBase nb where no.nodeBaseId=?1 and no.removed=0 and nb.removed=0 ")
	public List<NodeOperation> userFindByNodeBaseIdForFetch(Long nodeBaseId);
	
	@Modifying
	@Query(" update NodeOperation e set e.removed=?2, e.uUserId=?3, e.uDate=?4 where e.id=?1 ")
	public int updateRemovedById(Long id, int value, Long uId, String uDate);

}
