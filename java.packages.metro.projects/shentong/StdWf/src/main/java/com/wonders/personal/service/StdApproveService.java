package com.wonders.personal.service;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.wonders.flowWork.repository.FlowWorkThreadRepository;
import com.wonders.personal.entity.StdApprove;
import com.wonders.personal.entity.StdReview;
import com.wonders.personal.repository.StdApproveRepository;
import com.wonders.personal.repository.StdReviewRepository;

@Service
public class StdApproveService {
	
	private static final Logger LOG = LoggerFactory.getLogger(StdApproveService.class);
	
	@Inject
	private StdApproveRepository stdApproveRepository;
	
	@Inject
	private StdReviewRepository stdReviewRepository;
	
	@Inject
	private FlowWorkThreadRepository flowWorkThreadRepository;
	
	@Transactional
	public void logicDelete(Long[] ids) {
		for(Long id : ids){
			stdApproveRepository.logicDelete(id);
		}		
	}
	
	@Transactional
	public StdApprove save(StdApprove sa, Map params){
		Long oId = sa.getId();
		sa = stdApproveRepository.save(sa);
		Long saId = sa.getId();
		if(oId != saId && params.get("srId") != null){
			Long srId = Long.valueOf(params.get("srId").toString());
			stdReviewRepository.updateApproveId(srId, saId);
		}
		return sa;
	}
	
	public StdApprove initWithReview(Long reviewId){
		StdReview review = stdReviewRepository.findOne(reviewId);
		StdApprove approve = new StdApprove();
		
		if(review != null){
			approve.setStdNo(review.getStdNo());
			approve.setProjectNo(review.getProjectNo());
			approve.setStdName(review.getStdName());
			approve.setStdFunId(review.getStdFunId());
			approve.setStdMajorTypeId(review.getStdMajorTypeId());
			approve.setStdMinorTypeId(review.getStdMinorTypeId());
			approve.setStdRemarkItemId(review.getStdRemarkItemId());
		}
		return approve;
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
