package com.wonders.framework.auth.repository;

import com.wonders.framework.auth.entity.User;
import com.wonders.framework.repository.MyRepository;

public interface UserRepository extends MyRepository<User, Long> {
	User findByLoginName(String loginName);
	
	User findByLoginNameAndIdNot(String loginName,Long id);
}
