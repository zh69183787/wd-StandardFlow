package com.wonders.personal.service;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.wonders.flowWork.repository.FlowWorkThreadRepository;
import com.wonders.personal.entity.StdApply;
import com.wonders.personal.entity.StdReview;
import com.wonders.personal.repository.StdApplyRepository;
import com.wonders.personal.repository.StdReviewRepository;

@Service
public class StdReviewService {
	
	private static final Logger LOG = LoggerFactory.getLogger(StdReviewService.class);
	
	@Inject
	private StdReviewRepository stdReviewRepository;
	
	@Inject
	private StdApplyRepository stdApplyRepository;
	
	@Inject
	private FlowWorkThreadRepository flowWorkThreadRepository;
	
	@Transactional
	public void logicDelete(Long[] ids) {
		for(Long id : ids){
			stdReviewRepository.logicDelete(id);
		}		
	}
	
	@Transactional
	public StdReview save(StdReview sr, Map params){
		Long oId = sr.getId();
		sr = stdReviewRepository.save(sr);
		Long srId = sr.getId();
		if(oId != srId && params.get("saId") != null){
			Long saId = Long.valueOf(params.get("saId").toString());
			stdApplyRepository.updateReviewId(saId, srId);
		}
		return sr;
	}
	
	public StdReview initWithApply(Long saId){
		StdApply sa = stdApplyRepository.findOne(saId);
		StdReview sr = new StdReview();
		
		if(sa != null){
			sr.setProjectNo(sa.getProjectNo());
			sr.setStdName(sa.getStdName());
			sr.setStdFunId(sa.getStdFunId());
			sr.setStdMajorTypeId(sa.getStdMajorTypeId());
			sr.setStdMinorTypeId(sa.getStdMinorTypeId());
			sr.setStdRemarkItemId(sa.getStdRemarkItemId());
		}
		return sr;
	}
	
	@Transactional(readOnly = true)
	public boolean allowModifyInFlow(Long userId,String flowUid){
		Long cnt = flowWorkThreadRepository.findOngoingStartThread(flowUid, userId);
		if(cnt > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据流程轮次(loopIndex)获得修改版本号
	 * @param flowUid
	 * @return
	 */
	@Transactional(readOnly = true)
	public Integer getVersion(String flowUid){
		if(StringUtils.isEmpty(flowUid)){
			return 1;
		}
		Long loop = flowWorkThreadRepository.findMaxLoopIndex(flowUid);
		if(loop == null){
			return 1;
		}
		return loop.intValue() + 1;
	}
}
