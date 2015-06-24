package com.wonders.framework.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.auth.entity.Authority;
import com.wonders.framework.repository.MyRepository;

public interface AuthorityRepository extends MyRepository<Authority, Long> {
	
	@Query("select distinct(a) from Authority a left join fetch a.children where a.parent.id = ?1")
	List<Authority> findByParentId(Long parentId);
	
	@Query("from Authority where code = ?1")
	List<Authority> validateAuthorityCode(String code);
	
	@Query("from Authority where code = ?1 and id <> ?2")
	List<Authority> validateAuthorityCode(String code, Long id);
}
