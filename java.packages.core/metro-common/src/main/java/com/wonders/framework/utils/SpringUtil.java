package com.wonders.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtil implements ApplicationContextAware {
	
    private static ApplicationContext applicationContext;

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }
	public static Object getBean(Class<?> name){
//		System.out.println(applicationContext+":############################");
        return applicationContext.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }
}