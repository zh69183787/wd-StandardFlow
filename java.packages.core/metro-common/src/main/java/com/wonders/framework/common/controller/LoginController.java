package com.wonders.framework.common.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.auth.entity.Authority;
import com.wonders.framework.auth.entity.Group;
import com.wonders.framework.auth.entity.Role;
import com.wonders.framework.auth.repository.AccountRepository;
import com.wonders.framework.auth.repository.RoleRepository;
import com.wonders.framework.auth.repository.UserRepository;
import com.wonders.framework.common.service.LoginService;

@Controller
@RequestMapping("secure")
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	private static final String SPRING_SECURITY_CONTEXT ="SPRING_SECURITY_CONTEXT";
	public static final String SEC_CUR_USER ="SEC_CUR_USER";
	public static final String SEC_CUR_USER_NAME ="SEC_CUR_USER_NAME";
	public static final String SEC_CUR_USER_LOGIN_NAME ="SEC_CUR_USER_LOGIN_NAME";
	public static final String SEC_CUR_ACCOUNT_ID ="SEC_CUR_ACCOUNT_ID";
	public static final String SEC_CUR_ACCOUNT ="SEC_CUR_ACCOUNT";
	public static final String SEC_CUR_DEPT ="SEC_CUR_DEPT";
	public static final String SEC_CUR_DEPT_ID ="SEC_CUR_DEPT_ID";
	
	public static final String SEC_CUR_ROLES = "SEC_CUR_ROLES";
	public static final String SEC_CUR_ROLES_MAP = "SEC_CUR_ROLES_MAP";
	public static final String SEC_CUR_USER_TYPE = "SEC_CUR_USER_TYPE";
	
	public static final String SEC_LIMITS ="SEC_LIMITS"; // 权限限制
	public static final String SEC_ADMIN ="SEC_ADMIN"; // 权限限制
	
	private static final String SSO_FLAG ="SSO_FLAG";	
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private AccountRepository accountRepository;
	
	@Inject
	private RoleRepository roleRepository;
	
	@Inject
	private LoginService loginService;
	
//	@Inject
//	private Md5PasswordEncoder md5Md5PasswordEncoder;
	
	@RequestMapping(value = "index")
	protected String index(@RequestParam(value="account",required=false) Long accountId,HttpSession session,HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("index");
		
/*		if(session.getAttribute(SEC_CUR_ACCOUNT_ID)!=null){ 
				logger.info("escape user initial in session account ID:%s",session.getAttribute(SEC_CUR_ACCOUNT_ID));
			return mv;
		}else{
				logger.info("initial user in session account ID:%d",accountId);
		}*/
		
		SecurityContext sc = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
		Authentication auth = sc.getAuthentication();
		User user = (User)auth.getPrincipal();
		
		//獲取當前用戶
		com.wonders.framework.auth.entity.User secUser = userRepository.findByLoginName(user.getUsername());
		
		//獲取當前帳戶
		if(accountId == null){   //SSO
			accountId = (Long)session.getAttribute(SEC_CUR_ACCOUNT_ID);
		}else{
			session.setAttribute(SSO_FLAG, "false");
		}
		Account account = accountRepository.findByIdAll(accountId);
		//account.
		Set<Authority> authorities = account.getAuthorities();
		
		Set<Role> roles = account.getRoles();
		Map<String,Boolean> rolesMap = new HashMap<String,Boolean>();
		
		Map<String,Boolean> mapLimits = new HashMap<String,Boolean>();
		for(Authority authoritie : authorities){
			if(authoritie.getEnabled())
				mapLimits.put(authoritie.getCode(), true);
			
		}
		for(Role role:roles){
			for(Authority authoritie : role.getAuthorities()){
				if(authoritie.getEnabled())
					mapLimits.put(authoritie.getCode(), true);
			}
			rolesMap.put(role.getCode(), true);
		}
		
//		List<Role> roles2 = roleRepository.findByAccountId(accountId);
		
		//獲取當前帳號部門
		Group group = account.getGroup();
		session.setAttribute(SEC_CUR_USER, secUser);
		session.setAttribute(SEC_CUR_USER_LOGIN_NAME, secUser.getLoginName());
		session.setAttribute(SEC_CUR_ACCOUNT_ID, accountId);
		session.setAttribute(SEC_CUR_ACCOUNT, account);
		session.setAttribute(SEC_CUR_DEPT,group);
		session.setAttribute(SEC_CUR_DEPT_ID,group.getId());
		session.setAttribute(SEC_CUR_USER_NAME,secUser.getUsername());
		session.setAttribute(SEC_CUR_USER_TYPE,secUser.getUserType().ordinal());
		session.setAttribute(SEC_CUR_ROLES,roles);
		session.setAttribute(SEC_CUR_ROLES_MAP,rolesMap);
		session.setAttribute(SEC_LIMITS,mapLimits);
		if("admin".equals(user.getUsername())){
			session.setAttribute(SEC_ADMIN,true);
		}else{
			session.setAttribute(SEC_ADMIN,false);
		}
		
		saveCookie(response, "stdSharedId", String.valueOf(accountId));
		saveCookie(response, "tloginName", account.getWorkno());
		
		return "redirect:"+loginService.getUrlStdWork();
	}
	@RequestMapping(value = "changepassword")
	public @ResponseBody ModelAndView passwordChange(
				@RequestParam(value="loginname",required=false) String loginname,
				@RequestParam(value="oldpassword",required=false) String oldpassword,
				@RequestParam(value="newpassword",required=false) String newpassword,
				@RequestParam(value="repeatpassword",required=false) String repeatpassword
				) throws Exception{
		logger.info("enter passwordchange:%s",loginname);
		try{
			ModelAndView mv = new ModelAndView("secure/passwordchange");
			Md5PasswordEncoder md5Md5PasswordEncoder = new Md5PasswordEncoder();
			if(!StringUtils.hasLength(loginname)){
				return mv;
			}
			com.wonders.framework.auth.entity.User secUser = userRepository.findByLoginName(loginname);
			if(secUser == null){
				logger.info("user not found");
				mv.addObject("error", String.format("用户名:%s无法找到。",loginname));
				return mv;
			}
			
			if(!secUser.getPassword().equals(md5Md5PasswordEncoder.encodePassword(oldpassword, null))){
				logger.info("old password wrong");
				mv.addObject("error", "旧密码错误。");
				return mv;
			}
			
			if(!newpassword.equals(repeatpassword)){
				logger.info("old password wrong");
				mv.addObject("error", "两次输入密码不一致");
				return mv;
			}
			secUser.setPassword(md5Md5PasswordEncoder.encodePassword(newpassword, null));
			userRepository.save(secUser);
			mv.addObject("success", "保存密码成功");
			return mv;
		}catch(Exception e){
			logger.error("error when change password",e);
			throw e;
		}
	}
	
    private void saveCookie(HttpServletResponse response,String Name,String value){
		Cookie cookie;
		try {
			cookie = new Cookie(Name, java.net.URLEncoder.encode(value,"utf-8"));
			cookie.setPath("/");		
			cookie.setMaxAge(60*60);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
