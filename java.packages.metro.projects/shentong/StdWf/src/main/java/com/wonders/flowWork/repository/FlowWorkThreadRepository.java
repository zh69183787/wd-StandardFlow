package com.wonders.flowWork.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.flowWork.entity.FlowWorkThread;
import com.wonders.framework.repository.MyRepository;

public interface FlowWorkThreadRepository extends MyRepository<FlowWorkThread, Long>{

	/**
	 * 根据流程信息查询出所有的用户操作信息
	 * @param flowUid
	 * @return
	 */
	@Query("select 1 from FlowWorkThread f where  f.flowUid=?1 and f.state=0 ")
	List<FlowWorkThread> findByFlowUid(String flowUid);
	
	

	/**
	 * 根据 用户id 以及 处理状态 统计
	 * @param userId  state
	 * @return
	 */
	@Query("select count(*) from FlowWorkThread f where  f.userId=?1 and f.state=?2 ")
	Long countByUserIdAndState(Long userId,int state);
	
	/**
	 * 根据进程  uid  找到 发起人节点
	 * 发起人节点  默认nodeIndex =0 即为第一个节点，该节点不允许存在多个处理人
	 * @param flowUid   f.state=0  接受未开始  缓存阶段
	 * @return
	 *//*
	@Query("select 1 from FlowThread f , FlowNode n where n.id=f.nodeId and f.flowUid=?1 and n.nodeIndex =1  and n.flowUid=?1 and f.state=0 ")
	public FlowThread findByStartThread(String flowUid);*/

	/**
	 * 根据主流程UID查询出所有的信息
	 * @param flowUid
	 * @return
	 */
	@Query(" from FlowWorkThread f where  f.flowUid=?1  order by f.orderIndex asc , f.id asc ")
	List<FlowWorkThread> findByFlowUidAll(String flowUid);
	
	/**
	 * 根据主流程UID查询   loopindex
	 * @param flowUid
	 * @return
	 */
	@Query(" from FlowWorkThread f where  f.flowUid=?1 and f.loopIndex=?2 and f.orderIndex<=?3  order by f.orderIndex asc, f.id asc  ")
	List<FlowWorkThread> findByFlowUidAndLoopIndex(String flowUid,Long loopIndex,int orderIndex);
	
	/**
	 * 根据主流程UID查询   loopindex
	 * @param flowUid
	 * @return
	 */
	@Query(" from FlowWorkThread f where  f.flowUid=?1 and f.orderIndex>=?2  order by f.orderIndex asc , f.id asc ")
	List<FlowWorkThread> findByFlowUidAndOrderIndex(String flowUid,int orderIndex);
	
	/**
	 * 根据主流程UID查询   最大loopindex
	 * @param flowUid
	 * @return
	 */
	@Query("select max(f.loopIndex) from FlowWorkThread f where  f.flowUid=?1 ")
	Long findMaxLoopIndex(String flowUid);
	
	/**
	 * 用于验证
	 * @param 
	 * @return
	 */
	@Query("select count(*) from FlowWorkThread f where  f.id=?1 and f.orderIndex=?2 and f.state=1 ")
	long countOfindex(Long threadid,int orderIndex);
	
	/**
	 * 根据主流程UID  orderindex查询出所有的信息
	 * @param flowUid
	 * @return
	 */
	@Query(" from FlowWorkThread f where  f.flowUid=?1 and (f.orderIndex = ?2 or f.orderIndex=?3) and (f.state=0 or f.state=1) ")
	List<FlowWorkThread> findByFlowUidAndIndexAll(String flowUid,int orderindex,int nextorderindex);
	
	/**
	 * 根据主流程UID  orderindex查询出所有的信息
	 * @param flowUid
	 * @return
	 */
	@Query(" from FlowWorkThread f where  f.flowUid=?1 and f.orderIndex = ?2 ")
	List<FlowWorkThread> findByFlowUidAndIndexAll(String flowUid,int orderindex);
	/**
	 * 根据主流程UID  deforderindex查询出所有的信息
	 * @param flowUid
	 * @return
	 */
	@Query(" from FlowWorkThread f where  f.flowUid=?1 and f.defOrderIndex = ?2 ")
	List<FlowWorkThread> findByFlowUidAnddefIndexAll(String flowUid,int defOrderIndex);
	
	@Modifying 
	@Query(" update FlowWorkThread a set a.state = ?1,a.startTime = ?2 where a.id in ?3 ")
	int updateStateAndsTime(int state,String time,List<Long> ids);
	
	@Modifying 
	@Query(" update FlowWorkThread a set a.taskUid = ?2 where a.id = ?1 ")
	int updateTaskUid(Long id,String taskUid);
	
	@Modifying 
	@Query(" update FlowWorkThread a set a.orderIndex = a.orderIndex+?2 where a.orderIndex>?1 and a.flowUid=?3 ")
	int updateOrderindex(int index,int addNum ,String flowUid);
	
	@Modifying 
	@Query(" update FlowWorkThread a set a.defOrderIndex = a.defOrderIndex+?2 where a.defOrderIndex>?1 and a.flowUid=?3 ")
	int updateDefOrderindex(int index,int addNum ,String flowUid);
	
	/**
	 * 查询正在进行的发起人操作
	 * @param flowUid
	 * @return
	 */
	@Query(" select count(f.id) from FlowWorkThread f where f.flowUid=?1 and f.userId = ?2 and f.defOrderIndex = 0 and f.state = 1")
	Long findOngoingStartThread(String flowUid,Long userId);
	
	/**
	 * 根据主流程UID删除 当前index之后的结点 用于否决
	 * @param flowUid
	 * @return
	 */
	@Modifying 
	@Query(" delete from FlowWorkThread f where  f.flowUid=?1 and f.orderIndex>=?2 and f.id !=?3")
	int deleteByFlowUidAndOrderIndex(String flowUid,int orderIndex,Long threadId);
	
	@Modifying 
	@Query(" update FlowWorkThread set userId = ?2,userName = ?3 where id = ?1 ")
	int updateOperator(Long threadId, Long userId, String userName);

}
