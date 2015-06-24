package com.wonders.framework.auth.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wonders.framework.auth.entity.Dictionary;
import com.wonders.framework.auth.repository.DictionaryRepository;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;

@Controller
@RequestMapping("dictionary")
public class DictionaryController  extends AbstractCrudController<Dictionary, Long> {
	@Inject
	private DictionaryRepository dictionaryRepository;
	
	@Override
	protected MyRepository<Dictionary, Long> getRepository() {
		return dictionaryRepository;
	}
	
	@RequestMapping(value = "findByParentId/{parentId}")
	protected @ResponseBody
	List<Dictionary> findByParentId(@PathVariable long parentId,
			@RequestParam(required = false) String typecode) {
		return dictionaryRepository.findByParentId(parentId, typecode);
	}
	
	@RequestMapping(value = "validateDictionaryCode", method = RequestMethod.GET)
	protected @ResponseBody
	String validateDictionaryCode(@RequestParam String code,
			@RequestParam(required = false) long id){
		List<Dictionary> list = new ArrayList<Dictionary>();
		if (id == 0){
			list = dictionaryRepository.validateDictionaryCode(code);
		}else{
			list = dictionaryRepository.validateDictionaryCode(code,id);
		}
		if(list.size() == 0 ){
			return "{success: true}";
		}else {
			return "{success: false}";
		}
	}
	
	@RequestMapping(value = "isTypecodeExist", method = RequestMethod.GET)
	protected @ResponseBody
	String isTypecodeExist(@RequestParam String typecode){
		List<Dictionary> list = dictionaryRepository.findByTypecode(typecode);
		if(list.size() == 0 ){
			return "{success: true}";
		}else {
			return "{success: false}";
		}
	}
	

	@RequestMapping(value = "findByTypeCode/{typeCode}")
	protected @ResponseBody
	List<Dictionary> findByParentId(@PathVariable String typeCode) {
		return dictionaryRepository.findByTypeCode(typeCode);
	}
	
	@RequestMapping(value = "findByParentCode/{typeCode}/{parentCode}")
	protected @ResponseBody
	List<Dictionary> findByParentId(@PathVariable(value="typeCode") String typeCode,
			@PathVariable(value="parentCode") String parentCode) {
		return dictionaryRepository.findByParentCode(typeCode,parentCode);
	}
	
}
