package com.wonders.flowWork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;
@SuppressWarnings("serial")
@Entity
@Table(name = "t_flowwork_users")
public class FlowWorkUsers extends AbstractPersistable<Long>{
	
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
	 * 流程类型id
	 */
	@Column(name="FLOW_TYPE_ID")
	private Long flowTypeId;
	/**
	 * 用户顺序号
	 */
	@Column(name="ORDER_INDEX")
	private int orderIndex=1;
	
	/**
	 * 候选人 group code
	 */
	@Column(name="ALT_AUTH_CODE",length=50)
	private String altAuthCode;
	
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

	public Long getFlowTypeId() {
		return flowTypeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public void setFlowTypeId(Long flowTypeId) {
		this.flowTypeId = flowTypeId;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getAltAuthCode() {
		return altAuthCode;
	}

	public void setAltAuthCode(String altAuthCode) {
		this.altAuthCode = altAuthCode;
	}

}
