package com.wonders.framework.auth.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.auth.repository.AccountRepository;
import com.wonders.framework.auth.repository.GroupRepository;
import com.wonders.framework.auth.service.AccountService;
import com.wonders.framework.common.controller.LoginController;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;

@Controller
@RequestMapping("accounts")
public class AccountController extends AbstractCrudController<Account, Long> {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	@Inject
	private AccountRepository accountRepository;

	@Inject
	private GroupRepository groupRepository;
	
	@Inject
	private AccountService accountService;

	@Override
	protected MyRepository<Account, Long> getRepository() {
		return accountRepository;
	}

	@RequestMapping(value = "findByUserId", method = RequestMethod.GET)
	protected @ResponseBody
	List<Account> findByUserId(@RequestParam long userId) {
		return accountRepository.findByUserId(userId);
	}

	@RequestMapping("addRolesToAccount")
	protected @ResponseBody
	String addRolesToAccount(@RequestParam long accountId, 
			@RequestParam long... roleIds) {
		
		accountService.addRolesToAccount(accountId, roleIds);
		return "{success: true}";
	}

	@RequestMapping(value="editAccout", method = RequestMethod.GET)
	public @ResponseBody ModelAndView editAccout(HttpSession session) throws Exception{
		Long accountId = Long.valueOf(String.valueOf(session.getAttribute(LoginController.SEC_CUR_ACCOUNT_ID)));
		ModelAndView mv = new ModelAndView("secure/accout_edit");//
		
		Account account = accountRepository.findOne(accountId);
		
		mv.addObject("account", account);
		return mv;
	}
	/**
	 * 修改用户
	 * @param params
	 * @return
	 */
	@RequestMapping("saveAccount")
	protected @ResponseBody
	String saveAccount(@RequestParam Map<String,?> params,HttpSession session) {
		Account ac = null;
		try {
			ac = (Account)session.getAttribute(LoginController.SEC_CUR_ACCOUNT);

			//ac = accountRepository.findOne(accountId);
			BeanUtils.copyProperties(ac, params);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		accountRepository.save(ac);
		session.setAttribute(LoginController.SEC_CUR_ACCOUNT, ac);
		return "{success: true}";
	}
	
	@RequestMapping(value = "removeRolesFromAccount")
	protected @ResponseBody
	String removeRolesFromAccount(@RequestParam long accountId,
			@RequestParam long... roleIds) {
		
		accountService.removeRolesFromAccount(accountId, roleIds);
		return "{success: true}";
	}
	
	@RequestMapping(value = "findByUserLoginName", method = RequestMethod.GET)
	protected @ResponseBody
	List<Account> findByUserLoginName(@RequestParam String loginName) {
		logger.error(loginName);
		return accountRepository.findByUserLoginName(loginName);
	}
	
	@RequestMapping(value = "findByGroupId", method = RequestMethod.GET)
	protected @ResponseBody
	List<Account> findByGroupId(@RequestParam long groupId) {
		return accountRepository.findByGroupId(groupId);
	}
	
	@RequestMapping(value = "addAccountAuthority")
	protected @ResponseBody
	String addAccountAuthority(@RequestParam long accountId, 
			@RequestParam(required = false) long... authIds) {
		accountService.addAccountAuthority(accountId, authIds);
		return "{success: true}";
	}
	
	@RequestMapping(value = "findAccountAuthority", method = RequestMethod.GET)
	protected @ResponseBody
	String findAccountAuthority(@RequestParam long accountId){
		return accountService.findAccountAuthority(accountId);
	}
	
	@RequestMapping(value = "findByUserLoginNameNot", method = RequestMethod.GET)
	protected @ResponseBody
	List<Account> findByUserLoginNameNot(@RequestParam String loginName){
		return accountRepository.findByUserLoginNameNot(loginName);
	}
	
	@RequestMapping(value = "validateAccount", method = RequestMethod.GET)
	protected @ResponseBody
	String validateAccount(@RequestParam String name,
			@RequestParam long userId, @RequestParam long groupId){
		List<Account> list = accountRepository.validateAccout(name, groupId, userId);
		if(list.size() == 0 ){
			return "{success: true}";
		}else {
			return "{success: false}";
		}
	}
	
	@RequestMapping(value = "findSubAllOfGroup/{groupId}", method = RequestMethod.GET)
	protected @ResponseBody
	List findSubAllOfGroup(@PathVariable long groupId) {
		List all = new ArrayList();
		all.addAll(groupRepository.findByParentId(groupId));
		all.addAll(accountRepository.findByGroupId(groupId));
		return all;
	}

}
