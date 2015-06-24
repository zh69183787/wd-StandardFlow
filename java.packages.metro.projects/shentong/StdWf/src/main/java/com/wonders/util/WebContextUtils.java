package com.wonders.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebContextUtils implements ServletContextListener { 

	private static WebApplicationContext context; 
    
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		context=WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()); 
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		context = null;
	}
    
	public static Object getBean(Class<?> clazz){
		return WebContextUtils.context.getBean(clazz);
	}
	
}