package com.wonders;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;

import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.auth.entity.Authority;
import com.wonders.framework.auth.entity.Group;
import com.wonders.framework.auth.entity.Role;
import com.wonders.framework.auth.entity.User;
import com.wonders.framework.auth.repository.AccountRepository;
import com.wonders.framework.auth.repository.UserRepository;

public class LoginFilter implements Filter{
	protected final String P_IGNORE_URLS = "ignoreUrls";
	protected final String URL_SPLITER = ",";
	private String[] ignoreUrl = null;
	
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
	
	private static final String SPRING_SECURITY_CONTEXT ="SPRING_SECURITY_CONTEXT";
	private static final String SSO_FLAG ="SSO_FLAG";	
	
	@Inject
	private AccountRepository accountRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private AuthenticationManager authenticationManager;
	
	private AntPathMatcher urlMatcher = new AntPathMatcher();
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;    
	    HttpServletResponse response = (HttpServletResponse) servletResponse;  
	    HttpSession session = request.getSession();

		SecurityContext sc = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
		String ssoFlag = (String)session.getAttribute(SSO_FLAG);
		if(!"true".equals(ssoFlag) && sc != null){
			Authentication auth = sc.getAuthentication();
		    if(auth != null){
		    	chain.doFilter(servletRequest, servletResponse);
		    	return;	    
		    }			
		}
	    
	    
	    String url = getCurrUrl(request);
	    if (ignoreUrl != null && ignoreUrl.length > 0) {
		    for (int i=0; i<ignoreUrl.length; i++) {
		    	if(urlMatcher.match(ignoreUrl[i].trim(), url)) {
			    //if (url.equals(ignoreUrl[i].trim())) {
				    chain.doFilter(servletRequest, servletResponse);
				    return ;
			    }
		    }
	    }

		String returnUrl=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ request.getRequestURI();
		if(request.getQueryString()!=null){   
			returnUrl+="?"+request.getQueryString();

		}
		
	    String loginName=getCookieByName(request,"tloginName");
	    String token=getCookieByName(request,"token");
	    if(loginName==null || token == null){
	    	returnUrl = returnUrl.replace("&","%26").replace("?","%3F").replace("/","%2F"); 
	    	response.sendRedirect(request.getContextPath()+"/caClient.jsp?returnUrl="+returnUrl);
	    	return;
	    }else{
	    	if(!initSession(request,loginName)){
	    		response.sendRedirect(request.getContextPath()+"/403.jsp");
	    		return;
	    	}else{
		    	chain.doFilter(servletRequest, servletResponse);
		    	return;	    		
	    	}
	    }
	    //chain.doFilter(servletRequest, servletResponse);			//不经过ca
	}

	public void init(FilterConfig config) throws ServletException {
		String ignoreUrls = config.getInitParameter(P_IGNORE_URLS);
		if (ignoreUrls != null) {
			ignoreUrl = ignoreUrls.split(URL_SPLITER); 
		}
	}
	
	
	private static String getCurrUrl(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String ctxpath = request.getContextPath();
		if (! "".equals(ctxpath)) {
			return uri.substring(ctxpath.length());
		}else{
			return uri;
		}
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
	
	private boolean initSession(HttpServletRequest request, String loginName){
		HttpSession session = request.getSession(true);
		if(session.getAttribute(SEC_CUR_ACCOUNT_ID) != null){
			return true;
		}
		
		//獲取當前帳戶
		List<Account> accountList = accountRepository.findByWorkNoAll(loginName);
		if(accountList != null && accountList.size() > 0){
			Account account = accountList.get(0);
			
			login(request,account.getUser().getLoginName(),"wonders@test");
			
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
			
//			List<Role> roles2 = roleRepository.findByAccountId(accountId);
			
			//獲取當前帳號部門
			Group group = account.getGroup();
			session.setAttribute(SEC_CUR_ACCOUNT_ID, account.getId());
			session.setAttribute(SEC_CUR_ACCOUNT, account);
			session.setAttribute(SEC_CUR_USER_LOGIN_NAME, "ST");
			session.setAttribute(SEC_CUR_USER_NAME,account.getName());
			session.setAttribute(SEC_CUR_DEPT,group);
			session.setAttribute(SEC_CUR_DEPT_ID,group.getId());
			session.setAttribute(SEC_CUR_ROLES,roles);
			session.setAttribute(SEC_CUR_ROLES_MAP,rolesMap);
			session.setAttribute(SEC_LIMITS,mapLimits);			
			
			session.setAttribute(SSO_FLAG, "true");
			return true;
		}
		return false;
		
	}
	
	public void login(HttpServletRequest request, String userName, String password)
	{

		Md5PasswordEncoder md5Md5PasswordEncoder = new Md5PasswordEncoder();
	    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);

	    // Authenticate the user
	    Authentication authentication = authenticationManager.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    securityContext.setAuthentication(authentication);

	    // Create a new session and add the security context.
	    HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}
  
}
