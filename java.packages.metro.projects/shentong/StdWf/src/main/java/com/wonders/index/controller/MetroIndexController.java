package com.wonders.index.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.flowWork.repository.FlowWorkThreadRepository;
import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.auth.entity.Authority;
import com.wonders.framework.auth.entity.Group;
import com.wonders.framework.auth.entity.Role;
import com.wonders.framework.auth.repository.AccountRepository;
import com.wonders.framework.auth.repository.GroupRepository;
import com.wonders.framework.common.controller.LoginController;
import com.wonders.framework.utils.DateUtil;

@Controller
@RequestMapping("index")
public class MetroIndexController {
	private static final Logger LOG = LoggerFactory.getLogger(MetroIndexController.class);
	
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
	
	@Inject
	private FlowWorkThreadRepository flowWorkThreadRepository;
	
	@Inject
	private GroupRepository groupRepository;
	
	@Inject
	private AccountRepository accountRepository;
	
	@RequestMapping(method = RequestMethod.GET,value="homeIndex")
	public @ResponseBody ModelAndView index(HttpSession session) throws Exception{
		ModelAndView mv = null;
		Long accountId = Long.valueOf(String.valueOf(session.getAttribute(LoginController.SEC_CUR_ACCOUNT_ID)));

		try{
			mv = new ModelAndView("home");//forward to s_index.jsp
			
			// 统计该用户有多少个 待处理的流程
			Long countOfFlow = flowWorkThreadRepository.countByUserIdAndState(accountId, 1);
			mv.addObject("countOfFlow", countOfFlow);
			
			mv.addObject("today", DateUtil.getToday());
		} catch (Exception e) {
			LOG.error("Error when we retrieve data for home page.", e);
			throw e;
		}finally{
			
		}
		return mv;
		
	}
	
	/**
	 * 单点登录
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET,value="sso")
	public @ResponseBody ModelAndView sso(HttpSession session, HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("index");
		
/*		if(session.getAttribute(SEC_LIMITS) != null){
			return mv;
		}
		
		Long accountId = Long.valueOf(getCookieByName(request,"userId"));
		//獲取當前帳戶
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
		session.setAttribute(SEC_CUR_ACCOUNT_ID, accountId);
		session.setAttribute(SEC_CUR_ACCOUNT, account);
		session.setAttribute(SEC_CUR_USER_LOGIN_NAME, "ST");
		session.setAttribute(SEC_CUR_USER_NAME,account.getName());
		session.setAttribute(SEC_CUR_DEPT,group);
		session.setAttribute(SEC_CUR_DEPT_ID,group.getId());
		session.setAttribute(SEC_CUR_ROLES,roles);
		session.setAttribute(SEC_CUR_ROLES_MAP,rolesMap);
		session.setAttribute(SEC_LIMITS,mapLimits);*/
		
		return mv;
		
	}
	
	private boolean betweenAnd(String start,String end,String comparator){
		if(comparator == null || "".equals(comparator))return false;
		return comparator.compareTo(start) >= 0 && comparator.compareTo(end) <= 0;
	}
	
	private BigDecimal getDecimal(String str){
		BigDecimal r = new BigDecimal(0);
		if(str!=null && !"".equals(str)){
			try {
				r = new BigDecimal(str);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error("Decimal parse error for string : " + str);
			}
		}
		return r;
	}
	
	private BigDecimal getDecimal(BigDecimal decimal){
		if(decimal == null){
			return new BigDecimal(0);
		}
		return decimal;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "findSubAllOfGroupByCode/{groupCode}", method = RequestMethod.GET)
	protected @ResponseBody
	List findSubAllOfGroupByCode(@PathVariable String groupCode) {
		List all = new ArrayList();
		all.addAll(groupRepository.findByParentGroupCod(groupCode));
		all.addAll(accountRepository.findHasRoleByGroupCode(groupCode,"BMLD"));
		
		return all;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "findAllAccountByRole/{roleCode}", method = RequestMethod.GET)
	protected @ResponseBody
	List findAllAccountByRole(@PathVariable String roleCode) {
		List all = new ArrayList();
		all.addAll(accountRepository.findByRoleCode(roleCode));
		return all;
	}
	
	private static String getCookieByName(HttpServletRequest request, String name) {
		String cookieValue=null;
		Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
            	Cookie cookie = cookies[i];
        		
        		if(name.equals(cookie.getName())){
        			try{
        				cookieValue = java.net.URLDecoder.decode(cookie.getValue(),"utf-8");
        			} catch (UnsupportedEncodingException e) {
        				e.printStackTrace();
        			}
        			break;
        		}
            }
        }
        
        return cookieValue;
	}

}
