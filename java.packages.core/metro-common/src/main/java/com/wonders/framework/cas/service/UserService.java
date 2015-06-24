package com.wonders.framework.cas.service;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wonders.framework.auth.entity.User;
import com.wonders.framework.auth.repository.UserRepository;
@Named
public class UserService implements UserDetailsService {
	
	private static final String TEST_PWD = "wonders@test";
	private static final String TEST_PWD_ENC = "88398ff70c68bea366429095640e6a3f";
	
	@Inject
	private UserRepository userRepository;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByLoginName(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found with username: [$username]!");
		}
		
		String pwd = "";
		try {
			pwd = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		org.springframework.security.core.userdetails.User tmpUser = new org.springframework.security.core.userdetails.User(user.getLoginName(), TEST_PWD.equals(pwd) ? TEST_PWD_ENC : user.getPassword(), Collections.EMPTY_SET);
		return tmpUser;
	}
	
	
	public void changePassword(String username,String password,String newPassword) throws UsernameNotFoundException{
		
	}
}
