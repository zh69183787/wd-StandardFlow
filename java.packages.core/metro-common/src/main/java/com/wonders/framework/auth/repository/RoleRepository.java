package com.wonders.framework.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.auth.entity.Role;
import com.wonders.framework.repository.MyRepository;

public interface RoleRepository extends MyRepository<Role, Long> {
	@Query("select a.roles from Account a where a.id = ?1")
	List<Role> findByAccountId(Long accountId);
}
