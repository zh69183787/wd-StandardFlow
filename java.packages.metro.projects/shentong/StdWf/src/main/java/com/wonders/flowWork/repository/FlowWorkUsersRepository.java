package com.wonders.flowWork.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wonders.flowWork.entity.FlowWorkUsers;
import com.wonders.framework.repository.MyRepository;

public interface FlowWorkUsersRepository extends MyRepository<FlowWorkUsers, Long>{
	
	@Query(" from FlowWorkUsers f where f.flowTypeId=?1 order by f.orderIndex asc, f.id asc ")
	public List<FlowWorkUsers> findByTypeId(Long typeid);
	
}
