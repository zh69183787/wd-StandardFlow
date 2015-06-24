package com.wonders.framework.node.entity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.node.util.ListSortComparator;

@SuppressWarnings("serial")
@Entity
@Table(name = "t_node_base")
@JsonIgnoreProperties (value = {"nodeAttrList","nodeOperationList", "nodeBase"}) // 让这几个一对多的属性不参与json的解析
public class NodeBase extends AbstractPersistable<Long> {
	
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
	 * 阶段类型ID
	 */
	@ManyToOne(optional=true,fetch=FetchType.LAZY)
	@Where(clause="removed=0")
	@JoinColumn(name="NODE_TYPE_ID ",insertable = false, updatable = false)
	private NodeType nodeType;
	@Column(name="NODE_TYPE_ID", length=11)
	private Long nodeTypeId;

	/**
	 * 阶段名称
	 */
	@Column(name = "NAME", length=200)
	private String name; 
	
	/**
	 * 阶段CODE
	 */
	@Column(name = "CODE", length=200)
	private String code; 
	
	/**
	 * 排序
	 */
	@Column(name = "ORDER_NUM", length=11)
	private int orderNum; 
	
	/**
	 * 是否重点阶段 0是重点阶段 1 不是
	 */
	@Column(name = "IS_KEY", length=1)
	private int isKey=0; 
	
	/**
	 * 是否可裁剪 0可裁剪 1 不可裁剪
	 */
	@Column(name = "IS_TAILOR", length=1)
	private int isTallor=0; 
	
	/**
	 * 工作要求
	 */
	@Column(name = "WORK_ASK", length=2000)
	private String workAsk; 
	
	/**
	 * 工作原则
	 */
	@Column(name = "WORK_PRINCIPLE", length=2000)
	private String workPrinciple; 
	
	/**
	 * 管理手段
	 */
	@Column(name = "TPGJ_METHOD", length=2000)
	private String tpgjMethod; 
	
	/**
	 * 工作思路
	 */
	@Column(name = "WORK_IDEA", length=2000)
	private String workIdea; 
	
	/**
	 * 是否开启评价   0开启评价 1 不开启评价
	 */
	@Column(name = "IS_COMMENT", length=1)
	private int isComment=0; 
	
	/**
	 * 持续时间
	 */
	@Column(name = "SUSTAIN_DATE", length=20)
	private String sustainDate;
	
	/**
	 * 父阶段ID
	 */
	@ManyToOne(optional=true,fetch=FetchType.LAZY)
	@Where(clause="removed=0")
	@JoinColumn(name="PARENT_ID ",insertable = false, updatable = false)
	private NodeBase nodeBase;
	@Column(name="PARENT_ID", length=11)
	private Long praentId;
	
	/**
	 * 操作权限标识， 0 招标代理 1非招标代理
	 */
	@Column(name="OPERATION_FLAG", length=1)
	private int operationFlag=1;
	
	/**
	 * 附件类型 List
	 */
	@OneToMany(mappedBy="nodeBase", cascade=CascadeType.REMOVE, fetch = FetchType.LAZY)
	@Where(clause="removed = 0")
	private Set<NodeAttr> nodeAttrList = null;
	
	/**
	 * 业务操作 List
	 */
	@OneToMany(mappedBy="nodeBase", cascade=CascadeType.REMOVE, fetch = FetchType.LAZY)
	@Where(clause="removed = 0")
	private Set<NodeOperation> nodeOperationList = null;
	
	/**
	 * 经办人/负责人
	 */
	@Column(name="STAGE_PERSON", columnDefinition="VARCHAR(200)")
	private String stagePerson;
	
	public String getStagePerson() {
		return stagePerson;
	}

	public void setStagePerson(String stagePerson) {
		this.stagePerson = stagePerson;
	}

	public Set<NodeOperation> getNodeOperationList() {
		return nodeOperationList;
	}

	public void setNodeOperationList(Set<NodeOperation> nodeOperationList) {
		this.nodeOperationList = nodeOperationList;
	}

	public Set<NodeAttr> getNodeAttrList() {
		return nodeAttrList;
	}

	public void setNodeAttrList(Set<NodeAttr> nodeAttrList) {
		this.nodeAttrList = nodeAttrList;
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

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public Long getNodeTypeId() {
		return nodeTypeId;
	}

	public void setNodeTypeId(Long nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	public NodeBase getNodeBase() {
		return nodeBase;
	}

	public void setNodeBase(NodeBase nodeBase) {
		this.nodeBase = nodeBase;
	}

	public Long getPraentId() {
		return praentId;
	}

	public void setPraentId(Long praentId) {
		this.praentId = praentId;
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

	public int getIsKey() {
		return isKey;
	}

	public void setIsKey(int isKey) {
		this.isKey = isKey;
	}

	public int getIsTallor() {
		return isTallor;
	}

	public void setIsTallor(int isTallor) {
		this.isTallor = isTallor;
	}

	public String getWorkAsk() {
		return workAsk;
	}

	public void setWorkAsk(String workAsk) {
		this.workAsk = workAsk;
	}

	public String getWorkPrinciple() {
		return workPrinciple;
	}

	public void setWorkPrinciple(String workPrinciple) {
		this.workPrinciple = workPrinciple;
	}

	public String getWorkIdea() {
		return workIdea;
	}

	public void setWorkIdea(String workIdea) {
		this.workIdea = workIdea;
	}

	public int getIsComment() {
		return isComment;
	}

	public void setIsComment(int isComment) {
		this.isComment = isComment;
	}

	public String getSustainDate() {
		return sustainDate;
	}

	public void setSustainDate(String sustainDate) {
		this.sustainDate = sustainDate;
	}

	public int getOperationFlag() {
		return operationFlag;
	}

	public void setOperationFlag(int operationFlag) {
		this.operationFlag = operationFlag;
	}

	public String getTpgjMethod() {
		return tpgjMethod;
	}

	public void setTpgjMethod(String tpgjMethod) {
		this.tpgjMethod = tpgjMethod;
	}
	
//	@OneToMany(mappedBy="nodeBase", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//	@Where(clause="removed = 0")
//	private Set<NodeBase> children = null;
	
	@Transient
	private List<NodeBase> children = null;

	public List<NodeBase> getChildren() {
		if(children != null && children.size()>1)Collections.sort(children, new ListSortComparator(ListSortComparator.ASC)); 
		return children;
	}

	public void setChildren(List<NodeBase> children) {
		this.children = children;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof NodeBase){
			if(this.getId() != null)return this.getId().equals(((NodeBase)o).getId());
		}
		return false;
	}

}
