package com.wonders.flowWork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.wonders.flowWork.utils.FlowWorkContents;
@SuppressWarnings("serial")
@Entity
@Table(name = "t_flowwork_thread")
public class FlowWorkThread extends AbstractPersistable<Long>{
	
	
	/**
	 * 流程进程uid
	 */
	@Column(name="FLOW_UID")
	private String flowUid;
	//lazy="true" constrained="true"  fetch="select"
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="FLOW_UID", referencedColumnName="FLOW_UID",insertable = false, updatable = false)
	private FlowWorkProcess flowWorkProcess=null;
	/**
	 * 流程处理人 状态 0接收未开始  1 进行中 2 完成
	 */
	@Column(name="state")
	private int state = 0;
	
	/**
	 * 该节点处理循环的次数
	 */
	@Column(name="LOOP_INDEX")
	private Long loopIndex = 0l;
	
	/**
	 * 流程处理人接收时间
	 */
	@Column(name="RECEIVE_TIME",length=19)
	private String receiveTime;
	/**
	 * 流程处理人开始时间
	 */
	@Column(name="START_TIME",length=19)
	private String startTime;
	
	/**
	 * 流程处理人结束时间
	 */
	@Column(name="END_TIME",length=19)
	private String endTime;
	
	/**
	 * 最大处理时间   小时级别
	 */
	@Column(name="MAX_TIME")
	private Long maxTime;
	
	/**
	 * 流程该节点步骤顺序号
	 */
	@Column(name="ORDER_INDEX")
	private int orderIndex=0;
	/**
	 * 最初顺序号
	 */
	@Column(name="DEF_ORDER_INDEX")
	private int defOrderIndex=0;
	
	/**
	 * 处理人id
	 */
	@Column(name="USER_ID")
	private Long userId;
	
	/**
	 * 处理人名字
	 */
	@Column(name="USER_NAME",length=50)
	private String userName;
	
	/**
	 * 操作名称
	 */
	@Column(name="NODE_NAME",length=100)
	private String nodeName;
	
	/**
	 * 处理人 处理页面  操作方法
	 */
	@Column(name="DEAL_NAMES",length=200)
	private String dealNames;
	/**
	 * 处理人备注
	 */
	@Column(name="remark",length=500)
	private String remark;
	
	/**
	 * 该节点处理方式  0 通过，流程自流转 1通过 并选择下一步操作人  2通过并选择处理子流程  3退回上一节点 4退回并选择退回节点 5退回起点 6默认出口人 7默认入口人
	 */
	@Column(name="OPERATION_TYPE")
	private Long operationType = 0l;
	
	/**
	 * 该节点处理方式  内容 
	 */
	@Column(name="OPERATION_CONTENT",length=100)
	private String operationContent;
	
	/**
	 * 处理内容
	 */
	@Column(name="CONTENTS",length=1000)
	private String contents;
	
	
	/**
	 * 附件
	 * 用户的  orderindex 为零时  为流程附件
	 */
	@Column(name="ATTACH_GROUP",length=100)
	private String attachGroup;
	
	/**
	 * 待办事项uid
	 */
	@Column(name="TASK_UID")
	private String taskUid;
	
	/**
	 * 是否结束 0 未结束  1 结束
	 */
	@Column(name="IS_END")
	private Long isEnd = 0l;
	
	/**
	 * 候选人 权限名称
	 */
	@Column(name="ALT_AUTH_CODE",length=50)
	private String altAuthCode;
	
	public String getFlowUid() {
		return flowUid;
	}
	public void setFlowUid(String flowUid) {
		this.flowUid = flowUid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Long getLoopIndex() {
		return loopIndex;
	}
	public void setLoopIndex(Long loopIndex) {
		this.loopIndex = loopIndex;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getStartTime() {
		return startTime;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
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
	public Long getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(Long maxTime) {
		this.maxTime = maxTime;
	}
	public int getOrderIndex() {
		return orderIndex;
	}
	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDealNames() {
		return dealNames;
	}
	public void setDealNames(String dealNames) {
		this.dealNames = dealNames;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getOperationType() {
		return operationType;
	}
	public void setOperationType(Long operationType) {
		this.operationType = operationType;
	}
	public String getOperationContent() {
		return operationContent;
	}
	public void setOperationContent(String operationContent) {
		this.operationContent = operationContent;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Long getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(Long isEnd) {
		this.isEnd = isEnd;
	}
	
	public FlowWorkProcess getFlowWorkProcess() {
		return flowWorkProcess;
	}
	public void setFlowWorkProcess(FlowWorkProcess flowWorkProcess) {
		this.flowWorkProcess = flowWorkProcess;
	}
	public String getStateStr(){
		
		switch(this.state){
			case 0:
				return FlowWorkContents.FLOW_UNSTART;
			case 1:
				return FlowWorkContents.FLOW_START;
			case 2:
				return FlowWorkContents.FLOW_END;
		}
		
		return "";
	}
	public void setId(Long id){
		super.setId(id);
	}
	public int getDefOrderIndex() {
		return defOrderIndex;
	}
	public void setDefOrderIndex(int defOrderIndex) {
		this.defOrderIndex = defOrderIndex;
	}
	public String getAttachGroup() {
		return attachGroup;
	}
	public void setAttachGroup(String attachGroup) {
		this.attachGroup = attachGroup;
	}
	public String getTaskUid() {
		return taskUid;
	}
	public void setTaskUid(String taskUid) {
		this.taskUid = taskUid;
	}
	public String getAltAuthCode() {
		return altAuthCode;
	}
	public void setAltAuthCode(String altAuthCode) {
		this.altAuthCode = altAuthCode;
	}
	
}
