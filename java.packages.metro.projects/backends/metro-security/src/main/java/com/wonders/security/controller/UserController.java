package com.wonders.security.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wonders.framework.auth.entity.User;
import com.wonders.framework.auth.repository.UserRepository;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;

@Controller
@RequestMapping("users")
public class UserController extends AbstractCrudController<User, Long> {

	@Inject
	private UserRepository userRepository;

	@Override
	protected MyRepository<User, Long> getRepository() {
		return userRepository;
	}
	
	@RequestMapping(value = "isLoginNameExist", method = RequestMethod.GET)
	protected @ResponseBody
	String isLoginNameExist(@RequestParam String loginName,
			@RequestParam(required = false) long id){
		User user = null;
		if(id == 0){
			user = userRepository.findByLoginName(loginName);
		}else{
			user = userRepository.findByLoginNameAndIdNot(loginName, id);
		}
		if(user == null ){
			return "{success: true}";
		}else {
			return "{success: false}";
		}
	}

}
