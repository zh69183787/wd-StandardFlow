package com.wonders.personal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.repository.MyRepository;
import com.wonders.personal.entity.StdApply;
import com.wonders.personal.entity.StdSch;

public interface StdApplyRepository extends MyRepository<StdApply, Long> {
		
	@Modifying
	@Query(" update StdApply set removed = 1 where id = ?1")
	public void logicDelete(Long id);

	@Query("from StdApply sa left join fetch sa.spList where sa.id = ?1")
	public StdApply getSAWithSubs(Long id);
	
	@Query("from StdSch where removed = 0 and stdApply.id = ?1")
	public List<StdSch> getSchs(Long saId);	
	
	@Query(" select max(projectNo) from StdApply where substring(projectNo,1,?2) = ?1")
	public String maxNo(String prefix, int length);
	
	@Modifying
	@Query(" update StdApply set stdReviewId = ?2 where id = ?1")
	public void updateReviewId(Long id, Long reviewId);
}

