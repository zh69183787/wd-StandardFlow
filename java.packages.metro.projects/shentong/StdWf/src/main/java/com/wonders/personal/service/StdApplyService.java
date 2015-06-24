package com.wonders.personal.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.wonders.framework.utils.DateUtil;
import com.wonders.personal.repository.StdApplyRepository;
import com.wonders.personal.repository.StdPersonRepository;
import com.wonders.personal.repository.StdSchRepository;
import com.wonders.util.StringUtil;

@Service
public class StdApplyService {
	
	private static final Logger LOG = LoggerFactory.getLogger(StdApplyService.class);
	
	@Inject
	private StdApplyRepository stdApplyRepository;
	
	@Inject
	private StdPersonRepository stdPersonRepository;
	
	@Inject
	private StdSchRepository stdSchRepository;
	
	@Transactional
	public void logicDelete(Long[] ids) {
		for(Long id : ids){
			stdApplyRepository.logicDelete(id);
		}		
	}
	
	@Transactional
	public void logicDeletePerson(Long id) {
		stdPersonRepository.logicDelete(id);
	}
	
	@Transactional
	public void logicDeleteSch(Long id) {
		stdSchRepository.logicDelete(id);
	}
	
	@Transactional(readOnly = true)
	public String genNo(String deptCode){
		String year = DateUtil.getThisYear();
		String prefix = deptCode + "-" + year;
		String no = prefix + "-001";
		
		String curMax = stdApplyRepository.maxNo(prefix,prefix.length());
		if(StringUtils.isEmpty(curMax)){
			return no;
		}
		String[] prefixAndSeq = curMax.split("-");
		if(prefixAndSeq.length == 3){
			Integer nextOne = StringUtil.getInt(prefixAndSeq[2]) + 1;
			no = prefix + "-" + String.format("%03d", nextOne);
		}
		LOG.info("Auto generated sequence no is " + no);
		return no;
	}
}
