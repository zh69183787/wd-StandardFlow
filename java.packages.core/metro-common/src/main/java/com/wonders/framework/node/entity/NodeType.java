package com.wonders.framework.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wonders.framework.auth.entity.Account;

import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
@Table(name = "t_node_type")
public class NodeType extends AbstractPersistable<Long> {
	
	public void setId(Long id){
		if((id instanceof Long) && !id.equals(Long.valueOf(0))){
			super.setId(id);
		}else{
			super.setId(null);
		}
	}
	
	/**
	 * 创建人
	 */
	@ManyToOne(optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="C_USER ",insertable = false, updatable = false)
	private Account cUser;
	@Column(name="C_USER", length=11)
	private Long cUserId;
	
	/**
	 * 创建时间
	 */
	@Column(name = "C_DATE", length=200)
	private String cDate; 
	
	/**
	 * 修改人
	 */
	@ManyToOne(optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="U_USER ",insertable = false, updatable = false)
	private Account uUser;
	@Column(name="U_USER", length=11)
	private Long uUserId;
	
	/**
	 * 修改时间
	 */
	@Column(name = "U_DATE", length=200)
	private String uDate; 
	
	/**
	 * 移除标识 0未移除 1 移除
	 */
	@Column(name = "REMOVED", length=1)
	private int removed=0;
	
	/**
	 * 版本号
	 */
	@Column(name = "VERSIONS", length=200)
	private String versions="0"; 
	
	/**
	 * 备注
	 */
	@Column(name = "REMARK", length=2000)
	private String remark;
	
	/**
	 * 类型名称
	 */
	@Column(name = "NAME", length=200)
	private String name; 
	
	/**
	 * 类型CODE
	 */
	@Column(name = "CODE", length=200)
	private String code; 
	
	/**
	 * 排序
	 */
	@Column(name = "ORDER_NUM", length=11)
	private int orderNum; 
	
	/**
	 * 阶段类型
	 */
	@Column(name = "TYPE", length=4)
	private int type;
	
	/**
	 * NodeType 是否已停用 0 为未停用，1 为已停用。
	 */
	@Column(name = "DISCARD_FLAG", columnDefinition="INT(11)", length=11)
	private int discardFlag = 0;

	public int getDiscardFlag() {
		return discardFlag;
	}

	public void setDiscardFlag(int discardFlag) {
		this.discardFlag = discardFlag;
	}

	public Account getcUser() {
		return cUser;
	}

	public void setcUser(Account cUser) {
		this.cUser = cUser;
	}

	public Long getcUserId() {
		return cUserId;
	}

	public void setcUserId(Long cUserId) {
		this.cUserId = cUserId;
	}

	public String getcDate() {
		return cDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}

	public Account getuUser() {
		return uUser;
	}

	public void setuUser(Account uUser) {
		this.uUser = uUser;
	}

	public Long getuUserId() {
		return uUserId;
	}

	public void setuUserId(Long uUserId) {
		this.uUserId = uUserId;
	}

	public String getuDate() {
		return uDate;
	}

	public void setuDate(String uDate) {
		this.uDate = uDate;
	}

	public int getRemoved() {
		return removed;
	}

	public void setRemoved(int removed) {
		this.removed = removed;
	}

	public String getVersions() {
		return versions;
	}

	public void setVersions(String versions) {
		this.versions = versions;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	} 
	

}
