package com.wonders.flowWork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;
@SuppressWarnings("serial")
@Entity
@Table(name = "t_flowwork_type")
public class FlowWorkType extends AbstractPersistable<Long>{
	
	/**
	 * 流程名称
	 */
	@Column(name="FLOW_TYPE_NAME")
	private String flowTypeName;
	
	/**
	 * 对应流程
	 */
	@Column(name="FLOW_TYPE_CODE")
	private String flowTypeCode;
	

	/**
	 * 主流程描述
	 */
	@Column(name="FLOW_DESCRIPTION")
	private String flowDescription;
	
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
	 * 处理人,  逗号分割  串行执行
	 */
	@Column(name="DEAL_USERS")
	private String dealUsers;
	
	/**
	 * 创建人  不做关联  存中文做记录
	 */
	@Column(name="C_USER")
	private String cUser;
	
	/**
	 * 状态 是否启用 0 启用 1 未启用
	 */
	@Column(name="state")
	private Long state=0l;
	
	/**
	 * 删除位 0 未删除 1删除
	 */
	@Column(name="removed")
	private Long removed=0l;
	/**
	 * 创建时间
	 */
	@Column(name="C_DATE")
	private String cDate;
	
	/**
	 * 修改人  不做关联  存中文做记录
	 */
	@Column(name="U_USER")
	private String uUser;
	
	/**
	 * 修改时间
	 */
	@Column(name="U_DATE")
	private String uDate;

	public String getFlowTypeName() {
		return flowTypeName;
	}

	public void setFlowTypeName(String flowTypeName) {
		this.flowTypeName = flowTypeName;
	}

	public String getFlowTypeCode() {
		return flowTypeCode;
	}

	public String getLinkStr() {
		return linkStr;
	}

	public void setLinkStr(String linkStr) {
		this.linkStr = linkStr;
	}

	public void setFlowTypeCode(String flowTypeCode) {
		this.flowTypeCode = flowTypeCode;
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

	public String getFlowDescription() {
		return flowDescription;
	}

	public void setFlowDescription(String flowDescription) {
		this.flowDescription = flowDescription;
	}

	public String getcUser() {
		return cUser;
	}

	public String getObjectMethodClass() {
		return objectMethodClass;
	}

	public void setObjectMethodClass(String objectMethodClass) {
		this.objectMethodClass = objectMethodClass;
	}

	public String getEndToDoHql() {
		return endToDoHql;
	}

	public void setEndToDoHql(String endToDoHql) {
		this.endToDoHql = endToDoHql;
	}

	public void setcUser(String cUser) {
		this.cUser = cUser;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Long getRemoved() {
		return removed;
	}

	public void setRemoved(Long removed) {
		this.removed = removed;
	}

	public String getcDate() {
		return cDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}

	public String getuUser() {
		return uUser;
	}

	public void setuUser(String uUser) {
		this.uUser = uUser;
	}

	public String getuDate() {
		return uDate;
	}

	public void setuDate(String uDate) {
		this.uDate = uDate;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public String getDealUsers() {
		return dealUsers;
	}

	public void setDealUsers(String dealUsers) {
		this.dealUsers = dealUsers;
	}
	
	


}
