package com.wonders.flowWork.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wonders.flowWork.entity.FlowWorkProcess;
import com.wonders.flowWork.entity.FlowWorkThread;
import com.wonders.flowWork.repository.FlowWorkProcessRepository;
import com.wonders.flowWork.repository.FlowWorkThreadRepository;
import com.wonders.flowWork.repository.FlowWorkTypeRepository;
import com.wonders.flowWork.utils.FlowWorkContents;
import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.auth.repository.AccountRepository;
import com.wonders.framework.exception.WondersException;
import com.wonders.framework.utils.DateUtil;
import com.wonders.framework.utils.SpringUtil;
import com.wonders.util.HttpRequestHelper;
import com.wonders.util.StringUtil;

@Service
public class FlowWorkThreadService {
	
	private static final Logger LOG = LoggerFactory.getLogger(FlowWorkThreadService.class);
	
	@Inject
	FlowWorkProcessRepository flowWorkProcessRepository;
	@Inject
	FlowWorkTypeRepository flowWorkTypeRepository;
	@Inject
	FlowWorkThreadRepository flowWorkThreadRepository;
	@Inject
	AccountRepository accountRepository;

	@Value("${urlAddTask}")
	private String urlAddTask;
	
	@Value("${urlUpdateTask}")
	private String urlUpdateTask;

	@Value("${urlDealFlow}")
	private String urlDealFlow;
	
	/**
	 * 新增发起流程
	 * @param flowGroup 对应发起方实体 流程群组
	 * @param flowGroup 对应发起方实体 流程群组
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public FlowWorkThread startFlow(String startParams ) throws WondersException{
		
		return null;
	}
	/**
	 * 删除
	 * @param threadid
	 */
	@Transactional
	public Map<String,Object> del(Long threadId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", false);
		map.put("message", "删除失败，您可能与其他操作人操作冲突！");
		
		FlowWorkThread fthread = flowWorkThreadRepository.findOne(threadId);
		int orderIndex = fthread.getOrderIndex();
		int defOrderIndex = fthread.getDefOrderIndex();
		String flowUid = fthread.getFlowUid();
		
		if(fthread.getState()==2){//该被删除人  已经填写完意见不能删除
			map.put("success", false);
			map.put("message", "删除失败，该操作人已经填写完意见！");
			return map;
		}
		
		flowWorkThreadRepository.delete(threadId);
		
		List<FlowWorkThread> fs = flowWorkThreadRepository.findByFlowUidAndIndexAll(flowUid, orderIndex);
		if(fs!=null&&fs.size()>0){//删除时有同级的人员并行
			List<FlowWorkThread> fsDef = flowWorkThreadRepository.findByFlowUidAnddefIndexAll(flowUid, defOrderIndex);
			if(fsDef!=null&&fsDef.size()>1){
				
			}else{
				if(fsDef.size()!=1){
					return map;
				}
				flowWorkThreadRepository.updateDefOrderindex(orderIndex,-1,flowUid);
			}
			
			if(fthread.getState()==1){//被删除人员如果是正在进行中时  更新主进程最新进度
				String haner = "";
				for(FlowWorkThread f:fs){
					haner+=f.getUserName()+",";
				}
				flowWorkProcessRepository.updateProcess(flowUid,haner+FlowWorkContents.FLOW_HANDER_ING,fthread.getNodeName(),1,DateUtil.getNowTime());
			}
			
			
		}else{//删除时无同级的人员并行
			if(fs.size()!=0){return map;}
			flowWorkThreadRepository.updateOrderindex(orderIndex,-1,flowUid);
		}
		
		
		
		List<FlowWorkThread> threads = flowWorkThreadRepository.findByFlowUidAndOrderIndex(flowUid,orderIndex);
		map.put("threads", threads);
		map.put("success", true);
		map.put("message", "删除成功！");
		return map;
	}
	
	/**
	 * 替换选人
	 * @param threadid
	 */
	@Transactional
	public Map<String,Object> replace(Long threadId,Long userIds,String userNames){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", false);
		map.put("message", "选择失败，您可能与其他操作人操作冲突！");
		
		FlowWorkThread fthread = flowWorkThreadRepository.findOne(threadId);
		
		if(fthread.getState()!=0){// 已经开始不能替换
			map.put("success", false);
			map.put("message", "选择失败，该操作人已经开始审批！");
			return map;
		}
		
		flowWorkThreadRepository.updateOperator(threadId,userIds,userNames);
		
		map.put("success", true);
		map.put("message", "选择成功！");
		return map;
	}
	
	/**
	 * 添加下一步操作人
	 * @param threadid
	 */
	@Transactional
	public Map<String,Object> addUsers(Long threadId,String userIds,String userNames,String addFlag,String nodeName){
		Map<String,Object> map = new HashMap<String,Object>();
		String message = "操作失败请刷新后在进行操作！";
		boolean flag = false;
		FlowWorkThread flowWorkThread = flowWorkThreadRepository.findOne(threadId);
		//验证是否备改过
//		long countOfhas = flowWorkThreadRepository.countOfindex(threadId, orderIndex);
		String flowUid = flowWorkThread.getFlowUid();
		
		if(flowWorkThread!=null&&!flowWorkThread.isNew()&&flowWorkThread.getState()!=2){
			String[] userids = userIds.split(",");
			String[] usernames = userNames.split(",");
			int len = userids.length;
			int orderIndex = flowWorkThread.getOrderIndex();
			
			int defOrderIndex = flowWorkThread.getDefOrderIndex();
			Long loopIndex = flowWorkThread.getLoopIndex();
//			String nodeName = flowWorkThread.getNodeName();
			
			if("0".equals(addFlag)){//串行添加.
				
				flowWorkThreadRepository.updateOrderindex(orderIndex,len,flowUid);
				flowWorkThreadRepository.updateDefOrderindex(defOrderIndex,len,flowUid);
				for(int i=0;i<userids.length;i++ ){
					FlowWorkThread thread = new FlowWorkThread();
					thread.setUserId(Long.parseLong(userids[i]));
					thread.setUserName(usernames[i]);
					thread.setOrderIndex(orderIndex+1+i);
					thread.setDefOrderIndex(defOrderIndex+1+i);
					thread.setLoopIndex(loopIndex);
					thread.setFlowUid(flowUid);
					thread.setNodeName(nodeName);
					flowWorkThreadRepository.save(thread);
//					thread.setId(null);
				}
				List<FlowWorkThread> threads = flowWorkThreadRepository.findByFlowUidAndOrderIndex(flowUid,flowWorkThread.getOrderIndex()+1);
				map.put("threads", threads);
			}else if("1".equals(addFlag)){//并行添加
				List<FlowWorkThread> threads = new ArrayList<FlowWorkThread>();
				String nowTime = DateUtil.getNowTime();
				String haner = "";
				for(int i=0;i<userids.length;i++ ){
					FlowWorkThread thread = new FlowWorkThread();
					thread.setUserId(Long.parseLong(userids[i]));
					thread.setUserName(usernames[i]);
					thread.setOrderIndex(orderIndex);
					thread.setDefOrderIndex(defOrderIndex);
					thread.setLoopIndex(loopIndex);
					thread.setFlowUid(flowUid);
					if(flowWorkThread.getState()==1){
						thread.setStartTime(nowTime);
					}
					thread.setState(flowWorkThread.getState());
					thread.setNodeName(nodeName);
					flowWorkThreadRepository.save(thread);
					threads.add(thread);
					haner+=usernames[i]+",";
//					thread.setId(null);
				}
				if(flowWorkThread.getState()==1){
					flowWorkProcessRepository.updateProcessLeftJoin(flowUid,haner,1,DateUtil.getNowTime());
				}
				
				map.put("threads", threads);
			}else{
				message = "操作失败，您可能与其他操作人操作冲突，请刷新后在进行操作！";
				flag=false;
			}
			
			message = "新增完成！";
			flag=true;
		}
		
		map.put("success", flag);
		map.put("message", message);
		//map.put(key, value);
		return map;
	}
	
	
	/**
	 * 根据审批人审批结果 执行下一步操作
	 * @param id
	 * @param value
	 * @return
	 */
	@Transactional
	public String saveThreadAndDoNext(FlowWorkThread fThread){
		
		String taskUid = fThread.getTaskUid();
		if(taskUid != null && !"".equals(taskUid)){
			HttpRequestHelper.portalService(taskUid, urlUpdateTask);
		}
		
		flowWorkThreadRepository.save(fThread);
		int oindex = fThread.getOrderIndex();
		List<Long> ids = new ArrayList<Long>();
		String hander = "";
		String stage = "";
		List<FlowWorkThread> fThreads =null;
		
		switch(fThread.getOperationType().intValue()){
		case 0://通过
			fThreads = flowWorkThreadRepository.findByFlowUidAndIndexAll(fThread.getFlowUid(), oindex,oindex+1);
			boolean isnext = true;
			for(FlowWorkThread f:fThreads){
				if(f.getOrderIndex()==oindex){
					isnext=false;
					break;
				}else{
					ids.add(f.getId());
					hander+=f.getUserName();
				}
			}
			if(isnext){
				if(fThreads!=null&&fThreads.size()>0){
					for(Long id: ids){
						String addTaskResult = callAddTaskService(id);
						if(!"".equals(addTaskResult) && !"error".equals(addTaskResult)){
							flowWorkThreadRepository.updateTaskUid(id, addTaskResult);
						}
					}
					stage = fThreads.get(0).getNodeName();
					flowWorkThreadRepository.updateStateAndsTime(1, fThread.getEndTime(), ids);
					flowWorkProcessRepository.updateProcess(fThread.getFlowUid(),hander+FlowWorkContents.FLOW_HANDER_ING,stage, 1,DateUtil.getNowTime());
				}else{//流程结束
					hander = fThread.getUserName()+FlowWorkContents.FLOW_HANDER_END; 
					
					FlowWorkProcess flowProcess = flowWorkProcessRepository.findFlowProcessByFlowUid(fThread.getFlowUid());
					flowWorkProcessRepository.updateProcess(fThread.getFlowUid(),hander,2,fThread.getEndTime(),DateUtil.getNowTime());
					
					//流程结束后  endToDoHql  不为空则执行该语句
					String hql = flowProcess.getEndToDoHql();
					if(hql!=null&&!"".equals(hql)){
						flowWorkProcessRepository.doHql(hql);
					}
					
					String omethod  = flowProcess.getObjectMethod();
					String oclass  = flowProcess.getObjectMethodClass();
					String oparm = flowProcess.getParamVals();
					if(omethod!=null&&!"".equals(omethod)&&oclass!=null&&!"".equals(oclass)){
						 try {
								doFinish(oclass,omethod,oparm);
							} catch (SecurityException e) {
								e.printStackTrace();
								return null;
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
								return null;
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
								return null;
							} catch (InstantiationException e) {
								e.printStackTrace();
								return null;
							} catch (IllegalAccessException e) {
								e.printStackTrace();
								return null;
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
								return null;
							} catch (InvocationTargetException e) {
								e.printStackTrace();
								return null;
							}
					}
				}
			}
			break;
		case 2:
			break;
		case 3:
			//流程否决
			hander = fThread.getUserName()+FlowWorkContents.FLOW_HANDER_ABORT; 
			
			FlowWorkProcess flowProcess = flowWorkProcessRepository.findFlowProcessByFlowUid(fThread.getFlowUid());
			flowWorkProcessRepository.updateProcess(fThread.getFlowUid(),hander,3,fThread.getEndTime(),DateUtil.getNowTime());
			flowWorkThreadRepository.deleteByFlowUidAndOrderIndex(fThread.getFlowUid(), oindex, fThread.getId());
			
			//流程结束后  endToDoHql  不为空则执行该语句
			String hql = flowProcess.getEndToDoHql();
			if(hql!=null&&!"".equals(hql)){
				flowWorkProcessRepository.doHql(hql);
			}
			
			String omethod  = flowProcess.getObjectMethod();
			String oclass  = flowProcess.getObjectMethodClass();
			String oparm = flowProcess.getParamVals();
			if(omethod!=null&&!"".equals(omethod)&&oclass!=null&&!"".equals(oclass)){
				 try {
						doFinish(oclass,omethod,oparm);
					} catch (SecurityException e) {
						e.printStackTrace();
						return null;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						return null;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return null;
					} catch (InstantiationException e) {
						e.printStackTrace();
						return null;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return null;
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						return null;
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						return null;
					}
			}			
			break;
		case 4:
			break;
		case 5://退回发起人  当前循环的  发起人位置
			
			fThreads = flowWorkThreadRepository.findByFlowUidAndLoopIndex(fThread.getFlowUid(),fThread.getLoopIndex(),fThread.getOrderIndex());
			Long maxLoopIndex = flowWorkThreadRepository.findMaxLoopIndex(fThread.getFlowUid());
			List<FlowWorkThread> fadds = new ArrayList<FlowWorkThread>();
			
			int i=1;
			int lastindex=0;
			
			int backIndex=0;
			
			if(fThreads.size()>0){
				backIndex=fThreads.get(0).getOrderIndex();
			}
			
			
			for(FlowWorkThread f:fThreads){
				
				FlowWorkThread fnew =null;
				if(f.getState()==2){//完成的节点
					fnew = new FlowWorkThread();
					fnew.setUserId(f.getUserId());
					fnew.setUserName(f.getUserName());
					fnew.setFlowUid(f.getFlowUid());
					fnew.setNodeName(f.getNodeName());
					fnew.setDefOrderIndex(f.getDefOrderIndex());
					fnew.setAltAuthCode(f.getAltAuthCode());
					fadds.add(fnew);
				}else{
					fnew = f;
					f.setStartTime(null);
				}
				fnew.setState(0);
				
				if(backIndex==f.getOrderIndex()){//发起人启动流程
					fnew.setState(1);
					fnew.setStartTime(fThread.getEndTime());
					//add task
					String addTaskResult = callAddTaskService(fnew);
					if(!"".equals(addTaskResult) && !"error".equals(addTaskResult)){
						fnew.setTaskUid(addTaskResult);  //关联待办项KEY
					}					
					flowWorkProcessRepository.updateProcess(fnew.getFlowUid(),fnew.getUserName()+FlowWorkContents.FLOW_HANDER_ING,fnew.getNodeName(),1,DateUtil.getNowTime());
				}
				if(f.getOrderIndex()!=lastindex){
					lastindex = f.getOrderIndex();
					i++;
				}
				
				fnew.setOrderIndex(i+oindex);
				fnew.setLoopIndex(maxLoopIndex+1l);
				fadds.add(fnew);
			}
			
			
			flowWorkThreadRepository.updateOrderindex(oindex,i, fThread.getFlowUid());
			flowWorkThreadRepository.save(fadds);
			break;
		}
		
		
		return "ok";
	}
	
	/**
	 * 调用协同平台的待办添加接口
	 * @param threadId
	 * @return task key or "error"
	 */
	private String callAddTaskService(Long threadId){
		FlowWorkThread thread = flowWorkThreadRepository.findOne(threadId);
		return this.callAddTaskService(thread);
	}
	
	private String callAddTaskService(FlowWorkThread thread){
		Map p = this.getTaskParam(thread);
		String pJson = StringUtil.writeBean2Json(p);
		String result = HttpRequestHelper.portalService(pJson, urlAddTask);
		if("".equals(result) || "error".equals(result)){
			LOG.error("Failed to add task with param " + pJson);
		}
		return result;
	}
	
	/**
	 * 调用待办接口传递参数
	 * @param threadId
	 * @return
	 */
	private Map getTaskParam(FlowWorkThread thread){
		Map param = null;
		Account account = accountRepository.findOne(thread.getUserId());
		if(thread != null && account != null){
			param = new HashMap();
			param.put("app", "StdWf");
			param.put("type", "0");
			param.put("occurTime", DateUtil.getToday());
			param.put("title", "标准制修订");
			param.put("loginName", "ST/"+account.getWorkno());
			param.put("status", "0");
			param.put("removed", "0");
			param.put("typename", "标准制修订");
			try {
				param.put("url", URLEncoder.encode(urlDealFlow + "?flowUid="+thread.getFlowUid()+"&editFlag=true","UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			param.put("pname", "标准制修订");
			param.put("pincident", "1");
			param.put("cname", "子流程实例号");
			param.put("cincident", "1");
			param.put("stepName", thread.getNodeName());
			param.put("initiator", "ST/"+account.getWorkno());
		}
		return param;
	}
	
	/**
	 * 根据审批人审批结果 执行下一步操作
	 * @param id
	 * @param value
	 * 该节点处理方式  0 通过，流程自流转 1通过 并选择下一步操作人  2通过并选择处理子流程  3退回上一节点 4退回并选择退回节点 5退回起点 6默认出口人 7默认入口人
	 * @return
	 */
	@Transactional
	public int doNext(FlowWorkThread fThread){
		
		

		return 0;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public static Object doFinish(String className,String methodName,String params) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{

		String[] param = null;
		Class[] cs = null;
		if(params!=null&&!"".equals(params)){
			param = params.split(",");
			cs = new Class[param.length];
			int i = 0;
			for(String p:param){
				cs[i] = String.class;
				i++;
			}
		}
		
		Class invokeClass = Class.forName(className);
		 Object invokeObject = SpringUtil.getBean(invokeClass);
    
         Method invokeMethod = invokeClass.getMethod(methodName,cs);
         Object methodReturn = invokeMethod.invoke( invokeObject, param);
		return methodReturn;
	}
	
	 public static void main(String[] args)  {
	     
	            
		 String ss =  "123";
		 try {
			doFinish("com.wonders.flowWork.utils.Test","getMe",ss);
		} catch (SecurityException e) {
			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			
			e.printStackTrace();
		}
	 }
}























