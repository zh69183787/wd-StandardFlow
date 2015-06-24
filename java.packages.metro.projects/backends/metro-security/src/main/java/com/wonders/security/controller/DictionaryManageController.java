package com.wonders.security.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wonders.framework.auth.entity.DictionaryManage;
import com.wonders.framework.auth.repository.DictionaryManageRepository;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;

@Controller
@RequestMapping("dictionaryManage")
public class DictionaryManageController extends AbstractCrudController<DictionaryManage, Long> {

	@Inject
	private DictionaryManageRepository dictionaryManageRepository;

	@Override
	protected MyRepository<DictionaryManage, Long> getRepository() {
		return dictionaryManageRepository;
	}
	
}
