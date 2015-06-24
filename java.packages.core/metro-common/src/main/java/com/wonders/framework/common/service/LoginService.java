package com.wonders.framework.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	@Value("${urlStdWork}")
	private String urlStdWork;

	public String getUrlStdWork() {
		return urlStdWork;
	}

	public void setUrlStdWork(String urlStdWork) {
		this.urlStdWork = urlStdWork;
	}
	
}
