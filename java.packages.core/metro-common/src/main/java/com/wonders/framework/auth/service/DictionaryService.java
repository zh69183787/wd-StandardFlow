package com.wonders.framework.auth.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.framework.auth.entity.Dictionary;
import com.wonders.framework.auth.repository.DictionaryRepository;
import com.wonders.framework.utilities.CommonUtil;
import com.wonders.framework.utils.DictionaryComparator;

@Service
public class DictionaryService {
	
	@Inject
	private DictionaryRepository dictionaryRepository;

	/**
	 * @param 父节点id  类型code  过滤字符串
	 * @return  code不以unHasStr开头的字符串  的list  
	 */
	@Transactional
	public List<Dictionary> findDictionaryTreeHead(long parentid,String typeCode,String unHasStr) {
		
		// 字典项 
		List<Dictionary> stepDics =  dictionaryRepository.findByParentId(parentid,typeCode);
		List<Dictionary> stepDicsN = new ArrayList<Dictionary>();
//		String mothed =null;
		for(Dictionary d:stepDics){
			if(!d.getCode().startsWith(unHasStr)){
				if(!d.isLeaf()){
					Set<Dictionary> stepDicsC = null;
					for(Dictionary c:d.getChildren()){
						if(!c.getCode().startsWith(unHasStr)){
							if(stepDicsC==null) stepDicsC= new TreeSet<Dictionary>(new DictionaryComparator());
//							mothed = CommonUtil.codeToMethod(c.getCode());
//							c.setCode(mothed);
							stepDicsC.add(c);
						}
					}
					d.setChildren(stepDicsC);
				}
//				mothed = CommonUtil.codeToMethod(d.getCode());
//				d.setCode(mothed);
				stepDicsN.add(d);
			}
		}
		return stepDicsN;
	}

	public DictionaryRepository getDictionaryRepository() {
		return dictionaryRepository;
	}

	public void setDictionaryRepository(DictionaryRepository dictionaryRepository) {
		this.dictionaryRepository = dictionaryRepository;
	}
	/**
	 * 招投标  模块使用   表头  纵向
	 * @param 父节点id  类型code  过滤字符串
	 * @return  code不以unHasStr开头的字符串  的list
	 */
	public List<Dictionary> findByTypeCodeUnHasStr(String typecode,
			String unHasStr) {
		List<Dictionary> stepDics =  dictionaryRepository.findByTypecode(typecode);//.findByParentId(parentid,typeCode);
		List<Dictionary> stepDicsN = new ArrayList<Dictionary>();
		for(Dictionary d:stepDics){
			if(!d.getCode().startsWith(unHasStr)){
				stepDicsN.add(d);
				if(!d.isLeaf()){
					Set<Dictionary> stepDicsC = null;
					for(Dictionary c:d.getChildren()){
						if(!c.getCode().startsWith(unHasStr)){
							if(stepDicsC==null) stepDicsC= new LinkedHashSet<Dictionary>();
							stepDicsC.add(c);
							stepDicsN.add(c);
						}
					}
					d.setChildren(stepDicsC);
				}
			}
		}
		
		return stepDicsN;
	}

}
