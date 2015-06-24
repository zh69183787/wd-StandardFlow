package com.wonders.flowWork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.flowWork.entity.FlowWorkProcess;
import com.wonders.framework.repository.MyRepository;

public interface FlowWorkProcessRepository extends MyRepository<FlowWorkProcess, Long>{
	
	@Query(" from FlowWorkProcess f where f.removed = 0 and f.userId=?1 and f.flowTypeCode=?2 ")
	List<FlowWorkProcess> findFlowProcessByStartUser(Long userid,String code);
	
	@Query(" from FlowWorkProcess f where f.removed = 0 and f.flowUid=?1 ")
	FlowWorkProcess findFlowProcessByFlowUid(String flowUid);
	
	/*@Query(" from CostLogPlan clp where clp.removed = '0' and clp.id = ?1 ")
	CostLogPlan findById(Long costLogPlanId);
	
	@Query(" from CostLogPlan clp where clp.removed = '0' and clp.cost.id = ?1 order by clp.beginDate desc")
	List<CostLogPlan> findByProjectId(Long projectId);
	
	@Query(" from CostLogPlan clp where clp.removed = '0' ")
	List<CostLogPlan> findAll();
	
	@Modifying
	@Query(" update CostLogPlan clp set clp.removed = ?2 where clp.id =?1 ")
	public int updateRemoveById(Long id, String value);*/
	
	
	
	/**
	 * 更新最新进度
	 * @return
	 */
	@Modifying
	@Query(" update FlowWorkProcess f set f.flowHandler = ?2 , f.state=?4, f.flowStage = ?3, f.updateTime = ?5 where f.flowUid =?1 ")
	public int updateProcess(String flowUid, String hander, String stage, int state, String updateTime);
	
	
	/**
	 * 更新最新进度连加
	 * @return CONCAT(s1,s2)
	 */
	@Modifying
	@Query(" update FlowWorkProcess f set f.flowHandler = CONCAT(?2,f.flowHandler), f.state=?3, f.updateTime = ?4 where f.flowUid =?1 ")
	public int updateProcessLeftJoin(String flowUid, String hander,int state, String updateTime);
	
	/**
	 * 完结流程
	 * @return
	 */
	@Modifying
	@Query(" update FlowWorkProcess f set f.flowHandler = ?2 , f.state=?3, f.endTime=?4, f.updateTime = ?5 where f.flowUid =?1 ")
	public int updateProcess(String flowUid, String hander,int state,String endtime, String updateTime);
	
}
