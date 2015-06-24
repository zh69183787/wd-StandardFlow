package com.wonders.framework.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.auth.entity.Authority;
import com.wonders.framework.auth.entity.Role;
import com.wonders.framework.auth.repository.AccountRepository;
import com.wonders.framework.auth.repository.AuthorityRepository;
import com.wonders.framework.auth.repository.RoleRepository;

@Service
public class AccountService {
	@Inject
	private AccountRepository accountRepository;
	
	@Inject
	private RoleRepository roleRepository;
	
	@Inject
	private AuthorityRepository authorityRepository;

	@Transactional
	public Account addRolesToAccount(long accountId, long... roleIds) {
		
		Account account = accountRepository.findOne(accountId);
		
		if (account != null) {
			List<Long> list = new ArrayList<Long>(roleIds.length);
			for(long id:roleIds){
				list.add(id);
			}
			List<Role> roles = (List<Role>) roleRepository.findAll(list);
			
			account.getRoles().addAll(roles);
		}
		
		return account;
	}
	
	@Transactional
	public Account removeRolesFromAccount(long accountId, long... roleIds) {
		
		Account account = accountRepository.findOne(accountId);
		
		if (account != null) {
			
			List<Long> list = new ArrayList<Long>(roleIds.length);
			for(long id:roleIds){
				list.add(id);
			}
			
			List<Role> roles = (List<Role>) roleRepository.findAll(list);
			
			account.getRoles().removeAll(roles);
		}
		
		return account;
	}
	
	@Transactional
	public Account addAccountAuthority(long accountId, long... authIds) {
		
		Account account = accountRepository.findOne(accountId);
		
		if (account !=null) {
			
			List<Long> list = new ArrayList<Long>(authIds.length);
			for(long id:authIds){
				list.add(id);
			}
			
			List<Authority> auths = (List<Authority>) authorityRepository.findAll(list);
			
			Set<Authority> autorities = account.getAuthorities();
			for(Authority auth:autorities){
				if (!auths.contains(auth)) {
					autorities.remove(auth);
				}
			}
			
			account.getAuthorities().addAll(auths);
		}
		return account;
	}
	
	@Transactional(readOnly = true)
	public String findAccountAuthority(long accountId) {
		
		Account account = accountRepository.findOne(accountId);
		
		if(account != null){
			Set<Authority> autorities = account.getAuthorities();
			StringBuilder ids = new StringBuilder();
			for(Authority auth:autorities){
				ids.append(auth.getId()).append(',');
			}
			return ids.substring(0,ids.length()-1);
		}
		return StringUtils.EMPTY;
	}

	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
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
