package com.wonders.personal.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.repository.MyRepository;
import com.wonders.personal.entity.StdReview;

public interface StdReviewRepository extends MyRepository<StdReview, Long> {
		
	@Modifying
	@Query(" update StdReview set removed = 1 where id = ?1")
	public void logicDelete(Long id);
	
	@Modifying
	@Query(" update StdReview set stdApproveId = ?2 where id = ?1")
	public void updateApproveId(Long id, Long approveId);
}

