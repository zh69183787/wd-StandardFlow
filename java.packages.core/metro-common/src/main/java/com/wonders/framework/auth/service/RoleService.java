package com.wonders.framework.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.framework.auth.entity.Authority;
import com.wonders.framework.auth.entity.Role;
import com.wonders.framework.auth.repository.AuthorityRepository;
import com.wonders.framework.auth.repository.RoleRepository;

@Service
public class RoleService {
	@Inject
	private RoleRepository roleRepository;
	
	@Inject
	private AuthorityRepository authorityRepository;
	

	@Transactional
	public Role addRoleAuthority(long roleId, long... authIds) {
		
		Role role = roleRepository.findOne(roleId);
		
		if (role != null) {
			List<Long> list = new ArrayList<Long>(authIds.length);
			for(long id:authIds)
				list.add(id);
			List<Authority> auths = (List<Authority>) authorityRepository.findAll(list);
			
			Set<Authority> autorities = role.getAuthorities();
			for(Authority auth:autorities){
				if (!auths.contains(auth)) {
					autorities.remove(auth);
				}
			}
			
			role.getAuthorities().addAll(auths);
		}
		return role;
	}
	
	
	@Transactional(readOnly = true)
	public String findRoleAuthority(long roleId) {
		
		Role role = roleRepository.findOne(roleId);
		if(role != null){
			Set<Authority> autorities = role.getAuthorities();
			StringBuilder ids = new StringBuilder();
			for(Authority auth:autorities){
				ids.append(auth.getId()).append(',');
			}
			return ids.substring(0,ids.length()-1);
		}
		return StringUtils.EMPTY;
	}


	public RoleRepository getRoleRepository() {
		return roleRepository;
	}


	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}


	public AuthorityRepository getAuthorityRepository() {
		return authorityRepository;
	}


	public void setAuthorityRepository(AuthorityRepository authorityRepository) {
		this.authorityRepository = authorityRepository;
	}
	
	

}
