package com.wonders.personal.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.repository.MyRepository;
import com.wonders.personal.entity.StdSch;

public interface StdSchRepository extends MyRepository<StdSch, Long> {
		
	@Modifying
	@Query(" update StdSch set removed = 1 where id = ?1")
	public void logicDelete(Long id);

}

