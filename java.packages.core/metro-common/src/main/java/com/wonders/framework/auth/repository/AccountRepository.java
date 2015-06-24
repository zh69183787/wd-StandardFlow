package com.wonders.framework.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.repository.MyRepository;

public interface AccountRepository extends MyRepository<Account, Long> {
	
	@Query("from Account a join fetch a.group where a.id = ?1")
	Account findByIdWithGroup(Long id);
	
	@Query("from Account a join fetch a.group where a.user.loginName = ?1")
	List<Account> findByUserLoginName(String loginName);

	@Query("from Account a join fetch a.group where a.user.id = ?1")
	List<Account> findByUserId(Long userId);

	@Query("from Account a join fetch a.group where a.group.id = ?1")
	List<Account> findByGroupId(Long groupId);
	
	@Query("from Account a join fetch a.group where a.group.groupcode = ?1 ")
	List<Account> findByGroupCode(String groupCode);
	
	@Query("from Account a join fetch a.group where a.user.loginName <> ?1")
	List<Account> findByUserLoginNameNot(String loginName);
	
	@Query("from Account a join fetch a.group where a.name = ?1 and a.group.id = ?2 and a.user.id = ?3")
	List<Account> validateAccout(String name, Long groupId, Long userId);
	
	@Query("select distinct a from Account a join fetch a.roles b where b.code = ?1")
	List<Account> findByRoleCode(String code);
	
	@Query("from Account a left join fetch a.authorities left join fetch a.group left join fetch a.roles ar left join fetch ar.authorities  where a.id = ?1")
	Account findByIdAll(Long id);
	
	@Query("from Account a join fetch a.user left join fetch a.authorities left join fetch a.group left join fetch a.roles ar left join fetch ar.authorities  where a.workno = ?1")
	List<Account> findByWorkNoAll(String loginName);
	
	@Query("select distinct a from Account a join fetch a.group left join fetch a.roles r where a.group.groupcode = ?1 and r.code = ?2")
	List<Account> findHasRoleByGroupCode(String groupCode,String roleCode);
}
