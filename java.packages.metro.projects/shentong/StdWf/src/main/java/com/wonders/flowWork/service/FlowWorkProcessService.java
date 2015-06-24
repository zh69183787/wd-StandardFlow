package com.wonders.flowWork.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.flowWork.entity.FlowWorkProcess;
import com.wonders.flowWork.entity.FlowWorkThread;
import com.wonders.flowWork.entity.FlowWorkType;
import com.wonders.flowWork.entity.FlowWorkUsers;
import com.wonders.flowWork.repository.FlowWorkProcessRepository;
import com.wonders.flowWork.repository.FlowWorkThreadRepository;
import com.wonders.flowWork.repository.FlowWorkTypeRepository;
import com.wonders.flowWork.repository.FlowWorkUsersRepository;
import com.wonders.flowWork.utils.FlowWorkContents;
import com.wonders.framework.utils.DateUtil;
import com.wonders.framework.utils.StringUtil;

@Service
public class FlowWorkProcessService {
	
	@Inject
	FlowWorkProcessRepository flowWorkProcessRepository;
	@Inject
	FlowWorkTypeRepository flowWorkTypeRepository;
	@Inject
	FlowWorkThreadRepository flowWorkThreadRepository;
	@Inject
	FlowWorkUsersRepository flowWorkUsersRepository;


	/**
	 * 新增发起流程
	 */
	@Transactional(readOnly=false)
	public Map<String,Object> startNewFlow(String flowCode,String objectId,String objectName,HttpSession session) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		Long accoutid = (Long) session.getAttribute("SEC_CUR_ACCOUNT_ID");
		String accName = (String) session.getAttribute("SEC_CUR_USER_NAME");
		String loginName = (String) session.getAttribute("SEC_CUR_USER_LOGIN_NAME");
		
		FlowWorkType flowType = flowWorkTypeRepository.findByCode(flowCode);
		FlowWorkProcess flowProcess = new FlowWorkProcess();
		try {
			if(objectId!=null&&!"".equals(objectId)&&flowType.getObjectClass()!=null&&!"".equals(flowType.getObjectClass())){
				String hql = " update "+flowType.getObjectClass()+" a set a.flowGroup ='"+flowProcess.getFlowUid()+"' where a.id ="+objectId+" and ( a.flowGroup is null or a.flowGroup='' ) " ;
				int upnum = flowWorkTypeRepository.doHql(hql);
				if(upnum==0){
					return map;
				}
			}
        } catch(java.lang.Exception e) {
            System.out.println("in main, catch Exception: " + e);
            return map;
        }
		String now = DateUtil.getNowTime();

		flowProcess.setFlowName(objectName);
		
		flowProcess.setFlowTypeCode(flowType.getFlowTypeCode());
		flowProcess.setFlowTypeId(flowType.getId());
		flowProcess.setFlowTypeName(flowType.getFlowTypeName());
		flowProcess.setFlowDescription(flowType.getFlowDescription());
		flowProcess.setEndToDoHql(flowType.getEndToDoHql());
		flowProcess.setObjectClass(flowType.getObjectClass());
		flowProcess.setLinkStr(flowType.getLinkStr());
		flowProcess.setObjectMethod(flowType.getObjectMethod());
		flowProcess.setParamVals(flowType.getParamVals());
		flowProcess.setObjectMethodClass(flowType.getObjectMethodClass());
		
		flowProcess.setObjectId(objectId);
		flowProcess.setUserId(accoutid);
		flowProcess.setStartTime(now);
		flowProcess.setUserName(accName);
		flowProcess.setFlowNo(StringUtil.getNoByTimeAndLoginName(now, loginName));
		flowProcess.setUpdateTime(now);
		flowWorkProcessRepository.save(flowProcess);
		
		List<FlowWorkUsers> flowWorkUserses = flowWorkUsersRepository.findByTypeId(flowType.getId());
		List<FlowWorkThread> flowThreads = new ArrayList<FlowWorkThread>();
		
		FlowWorkThread starThread = new FlowWorkThread();//发起人设置
		starThread.setOrderIndex(0);
		starThread.setDefOrderIndex(0);
		starThread.setUserId(accoutid);
		starThread.setUserName(accName);
		starThread.setState(1);
		starThread.setStartTime(now);
		starThread.setNodeName(FlowWorkContents.FLOW_NODE_SPONSOR);
		starThread.setFlowUid(flowProcess.getFlowUid());
		starThread = flowWorkThreadRepository.save(starThread);

		for(FlowWorkUsers fu:flowWorkUserses){
			FlowWorkThread f= new FlowWorkThread();
			f.setOrderIndex(fu.getOrderIndex());
			f.setDefOrderIndex(fu.getOrderIndex());
			f.setFlowUid(flowProcess.getFlowUid());
			f.setNodeName(fu.getNodeName());
			f.setUserId(fu.getUserId());
			f.setUserName(fu.getUserName());
			f.setAltAuthCode(fu.getAltAuthCode());
			flowWorkThreadRepository.save(f);
			flowThreads.add(f);
		}
		
		map.put("flowProcess", flowProcess);
		map.put("flowThreads", flowThreads);
		map.put("starThread", starThread);
		
		return map;
	}
	
	/**
	 * 删除流程
	 * @param flowUid
	 */
	@Transactional
	public String removeFlow(String flowUid){
		
		FlowWorkProcess flowProcess = flowWorkProcessRepository.findFlowProcessByFlowUid(flowUid);
		flowWorkProcessRepository.delete(flowProcess);
		List<FlowWorkThread> flowThreads = flowWorkThreadRepository.findByFlowUidAll(flowUid);
		flowWorkThreadRepository.delete(flowThreads);
		return "";
	}
	

	/**
	 * 流程开始并  执行节点内操作
	 * @param FlowNode
	 */
	@Transactional
	public void saveDeal(FlowWorkProcess flowProcess){
		switch(flowProcess.getState()){
		case 0://未完成
			
			break;
		case 1://进行中
			/*flowProcessRepository.save(flowProcess);
			FlowNode flowNode = flowNodeRepository.findByFristIndex(flowProcess.getFlowUid());
			flowNode.setState(1);
			flowNodeService.saveDeal(flowNode);*/
			break;
		case 2://完成
			break;
		}
	}
	
	/**
	 * 将flowThread中的${*}替换为对应的值
	 * @param flowThreads
	 * @param map
	 */
	public void evaluateEL(List<FlowWorkThread> flowThreads, Map<String,Object> map){
		for(FlowWorkThread thread : flowThreads){
			String altCode = thread.getAltAuthCode();
			if(!StringUtils.isEmpty(altCode) && altCode.contains("$")){
				altCode = StringUtil.evaluateEL(altCode, map);
				thread.setAltAuthCode(altCode);
			}
		}
	}
}
