package com.wonders.flowWork.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.wonders.flowWork.utils.FlowWorkContents;


@SuppressWarnings("serial")
@Entity
@Table(name = "t_flowwork_process")
public class FlowWorkProcess extends AbstractPersistable<Long>{
	
	
	/**
	 * 流程进程uid
	 */
	@Column(name="FLOW_UID")
	private String flowUid = UUID.randomUUID().toString();
	
	
	/**
	 * 流程进程 群组
	 */
	@Column(name="FLOW_No")
	private String flowNo;
	
	/**
	 * 流程类型id
	 */
	@Column(name="FLOW_TYPE_ID")
	private Long flowTypeId;
	
	/**
	 * 流程类型名称
	 */
	@Column(name="FLOW_TYPE_NAME",length=100)
	private String flowTypeName;
	
	/**
	 * 对应流程类型code
	 */
	@Column(name="FLOW_TYPE_CODE",length=50)
	private String flowTypeCode;
	
	/**
	 * 主流程描述
	 */
	@Column(name="FLOW_DESCRIPTION",length=500)
	private String flowDescription;
	
	
	/**
	 * 发起流程对象ID  用于更新相应字段
	 */
	@Column(name="OBJECT_ID")
	private String objectId;
	
	/**
	 * 发起流程对应的  实体类名称
	 */
	@Column(name="OBJECT_CLASS",length=200)
	private String objectClass;
	
	/**
	 * 发起流程对应的  操作类名称
	 */
	@Column(name="OBJECT_METHOD_CLASS",length=200)
	private String objectMethodClass;
	/**
	 * 流程执行结束后须执行的方法名
	 */
	@Column(name="OBJECT_METHOD",length=100)
	private String objectMethod;
	/**
	 * 流程执行结束后须执行的方法 传入 的参数值
	 */
	@Column(name="PARAM_VALS",length=1000)
	private String paramVals;
	/**
	 * 发起流程对应的  实体  需要执行的hql
	 */
	@Column(name="END_TODO_HQL",length=2000)
	private String endToDoHql;
	
	/**
	 * 对应实体展现的  打开连接
	 */
	@Column(name="LINK_STR",length=1000)
	private String linkStr;
	
	/**
	 * 流程名称
	 */
	@Column(name="FLOW_NAME",length=100)
	private String flowName;
	
	
	/**
	 * 流程状态 0发起未开始（发起人暂存）  1 进行中 2wancheng 3 否决
	 */
	@Column(name="state",length=100)
	private int state = 0;
	
	/**
	 * 流程下一步处理函数
	 */
	@Column(name="FLOW_HANDLER",length=500)
	private String flowHandler;
	
	/**
	 * 流程处理阶段
	 */
	@Column(name="FLOW_STAGE",length=500)
	private String flowStage;
	
	/**
	 * 流程开始时间
	 */
	@Column(name="START_TIME",length=19)
	private String startTime;
	
	/**
	 * 流程结束时间
	 */
	@Column(name="END_TIME",length=19)
	private String endTime;
	
	/**
	 * 操作更新时间
	 */
	@Column(name="UPDATE_TIME",length=19)
	private String updateTime;
	
	/**
	 * 流程发起人id
	 */
	@Column(name="USER_ID")
	private Long userId;
	
	/**
	 * 流程发起人处理方式 0 自动执行下一节点   1 处理人处理后执行下一节点
	 */
	@Column(name="USER_TYPE")
	private int userType=0;
	
	/**
	 * 流程发起人名字
	 */
	@Column(name="USER_NAME",length=50)
	private String userName;
	

	
	public String getObjectMethodClass() {
		return objectMethodClass;
	}

	public void setObjectMethodClass(String objectMethodClass) {
		this.objectMethodClass = objectMethodClass;
	}

	/**
	 * 流程备注
	 */
	@Column(name="remark",length=500)
	private String remark;
	
	/**
	 * shanchu
	 */
	@Column(name="REMOVED")
	private int removed=0;

	public String getFlowUid() {
		return flowUid;
	}

	public void setFlowUid(String flowUid) {
		this.flowUid = flowUid;
	}

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	public Long getFlowTypeId() {
		return flowTypeId;
	}

	public void setFlowTypeId(Long flowTypeId) {
		this.flowTypeId = flowTypeId;
	}

	public String getFlowTypeName() {
		return flowTypeName;
	}

	public String getObjectMethod() {
		return objectMethod;
	}

	public void setObjectMethod(String objectMethod) {
		this.objectMethod = objectMethod;
	}

	public String getParamVals() {
		return paramVals;
	}

	public void setParamVals(String paramVals) {
		this.paramVals = paramVals;
	}

	public void setFlowTypeName(String flowTypeName) {
		this.flowTypeName = flowTypeName;
	}

	public String getFlowTypeCode() {
		return flowTypeCode;
	}

	public void setFlowTypeCode(String flowTypeCode) {
		this.flowTypeCode = flowTypeCode;
	}

	public String getFlowDescription() {
		return flowDescription;
	}

	public void setFlowDescription(String flowDescription) {
		this.flowDescription = flowDescription;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public String getEndToDoHql() {
		return endToDoHql;
	}

	public void setEndToDoHql(String endToDoHql) {
		this.endToDoHql = endToDoHql;
	}

	public String getLinkStr() {
		return linkStr;
	}

	public void setLinkStr(String linkStr) {
		this.linkStr = linkStr;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	public String getStateStr(){
		
		switch(this.state){
			case 0:
				return FlowWorkContents.FLOW_UNSTART;
			case 1:
				return FlowWorkContents.FLOW_START;
			case 2:
				return FlowWorkContents.FLOW_END;
			case 3:
				return FlowWorkContents.FLOW_ABORT;				
		}
		
		return "";
	}
	public String getFlowHandler() {
		return flowHandler;
	}

	public void setFlowHandler(String flowHandler) {
		this.flowHandler = flowHandler;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getRemoved() {
		return removed;
	}

	public void setRemoved(int removed) {
		this.removed = removed;
	}

	public String getFlowStage() {
		return flowStage;
	}

	public void setFlowStage(String flowStage) {
		this.flowStage = flowStage;
	}
	
}
