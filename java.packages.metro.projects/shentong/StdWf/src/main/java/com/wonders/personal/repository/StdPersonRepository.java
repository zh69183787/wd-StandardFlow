package com.wonders.personal.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.repository.MyRepository;
import com.wonders.personal.entity.StdPerson;

public interface StdPersonRepository extends MyRepository<StdPerson, Long> {
		
	@Modifying
	@Query(" update StdPerson set removed = 1 where id = ?1")
	public void logicDelete(Long id);

}

