package com.wonders.flowWork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wonders.flowWork.entity.FlowWorkType;
import com.wonders.framework.repository.MyRepository;

public interface FlowWorkTypeRepository extends MyRepository<FlowWorkType, Long>{
	
	/*@Query(" from CostLogPlan clp where clp.removed = '0' and clp.id = ?1 ")
	CostLogPlan findById(Long costLogPlanId);
	
	@Query(" from CostLogPlan clp where clp.removed = '0' and clp.cost.id = ?1 order by clp.beginDate desc")
	List<CostLogPlan> findByProjectId(Long projectId);
	
	@Query(" from CostLogPlan clp where clp.removed = '0' ")
	List<CostLogPlan> findAll();
	
	@Modifying
	@Query(" update CostLogPlan clp set clp.removed = ?2 where clp.id =?1 ")
	public int updateRemoveById(Long id, String value);*/
	
	@Query(" from FlowWorkType f where f.removed = '0' and f.state=?1 ")
	public List<FlowWorkType> findByState(Long state);
	
	@Query(" from FlowWorkType f where f.removed = 0 and f.flowTypeCode=?1 ")
	public FlowWorkType findByCode(String code);
	
	@Query("from FlowWorkType f where f.removed = 0 and f.flowTypeCode=?1 ")
	public List<FlowWorkType> findListByCode(String code);
	
	@Query("select 1 from FlowWorkType f where f.removed = '0' and f.flowTypeCode=?1 and f.state=?2 ")
	public FlowWorkType findByCodeAndState(String code,Long state);
	
	@Query("select count(f.id) from FlowWorkType f where f.removed = 0 and f.flowTypeName=?1 ")
	public Long countOfName(String name);
}
