package com.wonders.personal.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.repository.MyRepository;
import com.wonders.personal.entity.StdApprove;

public interface StdApproveRepository extends MyRepository<StdApprove, Long> {
		
	@Modifying
	@Query(" update StdApprove set removed = 1 where id = ?1")
	public void logicDelete(Long id);

}

