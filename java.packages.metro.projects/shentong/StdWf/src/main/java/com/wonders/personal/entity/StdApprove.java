package com.wonders.personal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.wonders.flowWork.entity.FlowWorkProcess;
import com.wonders.flowWork.utils.FlowWorkContents;
import com.wonders.framework.auth.entity.Account;
import com.wonders.framework.auth.entity.Dictionary;
import com.wonders.framework.auth.entity.Group;

@Entity
@Table(name = "t_std_approve")
public class StdApprove extends AbstractPersistable<Long> {

	/**
	 * 标准名称
	 */
	@Column(name="STD_NAME", columnDefinition="VARCHAR(200)")
	private String stdName;

	/**
	 * 标准编号
	 */
	@Column(name="STD_NO", columnDefinition="VARCHAR(200)")
	private String stdNo;
	
	/**
	 * 立项编号
	 */
	@Column(name="PROJECT_NO", columnDefinition="VARCHAR(200)")
	private String projectNo;
	
	/**
	 * 标准状态（送审）
	 */
	@Column(name = "STATE", columnDefinition="INT(1)")
	private Integer state = 0;
	
	/**
	 * 申报部门
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="APPLY_DEPT" ,nullable = true,insertable=false,updatable=false)
	private Group applyDept;
	@Column(name = "APPLY_DEPT", columnDefinition="int(11)")
	private Long applyDeptId;
	
	/**
	 * 标准化员
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="C_USER" ,nullable = true,insertable=false,updatable=false)
	private Account cUser;		
	@Column(name = "C_USER", columnDefinition="int(11)")
	private Long cUserId; 
	
	/**
	 * 送审时间
	 */
	@Column(name = "APPLY_DATE", columnDefinition="VARCHAR(20)")
	private String applyDate; 
	
	/**
	 * 标准功能
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name = "STD_FUN_ID", nullable = true, insertable = false, updatable = false)	
	private Dictionary stdFun;
	@Column(name = "STD_FUN_ID", columnDefinition="INT(11)")
	private Long stdFunId;
	
	/**
	 * 标准大类
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name = "STD_MAJOR_TYPE_ID", nullable = true, insertable = false, updatable = false)	
	private Dictionary stdMajorType;
	@Column(name = "STD_MAJOR_TYPE_ID", columnDefinition="INT(11)")
	private Long stdMajorTypeId;
	
	/**
	 * 标准小类
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name = "STD_MINOR_TYPE_ID", nullable = true, insertable = false, updatable = false)	
	private Dictionary stdMinorType;
	@Column(name = "STD_MINOR_TYPE_ID", columnDefinition="INT(11)")
	private Long stdMinorTypeId;
	
	/**
	 * 标准备注项
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name = "STD_REMARK_ITEM_ID", nullable = true, insertable = false, updatable = false)	
	private Dictionary stdRemarkItem;
	@Column(name = "STD_REMARK_ITEM_ID", columnDefinition="INT(11)")
	private Long stdRemarkItemId;
	
	/**
	 * 创建时间
	 */
	@Column(name = "C_DATE", columnDefinition="VARCHAR(20)")
	private String cDate; 
	
	/**
	 * 修改人
	 */
	@Column(name = "U_USER", columnDefinition="int(11)")
	private Long uUser; 
	
	/**
	 * 修改时间
	 */
	@Column(name = "U_DATE", columnDefinition="VARCHAR(20)")
	private String uDate; 
	
	/**
	 * 移除标识 0未移除 1 移除
	 */
	@Column(name = "REMOVED", columnDefinition="int(1)")
	private Integer removed = 0;
	
	/**
	 * 备注
	 */
	@Column(name = "REMARK", columnDefinition="LONGTEXT")
	private String remark; 

	/**
	 * 流程进程uid
	 */
	@Column(name="FLOW_GROUP",columnDefinition="VARCHAR(200)")
	private String flowGroup;
	@ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="FLOW_GROUP", referencedColumnName="FLOW_UID",nullable = true,insertable = false, updatable = false)
	private FlowWorkProcess flowWorkProcess;
	
	/**
	 * 附件
	 */
	@Column(name = "ATTACH_GROUP")
	private String attachGroup;
	
	public void setId(Long id){
		super.setId(id);
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getStdNo() {
		return stdNo;
	}

	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Group getApplyDept() {
		return applyDept;
	}

	public void setApplyDept(Group applyDept) {
		this.applyDept = applyDept;
	}

	public Long getApplyDeptId() {
		return applyDeptId;
	}

	public void setApplyDeptId(Long applyDeptId) {
		this.applyDeptId = applyDeptId;
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

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public Dictionary getStdFun() {
		return stdFun;
	}

	public void setStdFun(Dictionary stdFun) {
		this.stdFun = stdFun;
	}

	public Long getStdFunId() {
		return stdFunId;
	}

	public void setStdFunId(Long stdFunId) {
		this.stdFunId = stdFunId;
	}

	public Dictionary getStdMajorType() {
		return stdMajorType;
	}

	public void setStdMajorType(Dictionary stdMajorType) {
		this.stdMajorType = stdMajorType;
	}

	public Long getStdMajorTypeId() {
		return stdMajorTypeId;
	}

	public void setStdMajorTypeId(Long stdMajorTypeId) {
		this.stdMajorTypeId = stdMajorTypeId;
	}

	public Dictionary getStdMinorType() {
		return stdMinorType;
	}

	public void setStdMinorType(Dictionary stdMinorType) {
		this.stdMinorType = stdMinorType;
	}

	public Long getStdMinorTypeId() {
		return stdMinorTypeId;
	}

	public void setStdMinorTypeId(Long stdMinorTypeId) {
		this.stdMinorTypeId = stdMinorTypeId;
	}

	public Dictionary getStdRemarkItem() {
		return stdRemarkItem;
	}

	public void setStdRemarkItem(Dictionary stdRemarkItem) {
		this.stdRemarkItem = stdRemarkItem;
	}

	public Long getStdRemarkItemId() {
		return stdRemarkItemId;
	}

	public void setStdRemarkItemId(Long stdRemarkItemId) {
		this.stdRemarkItemId = stdRemarkItemId;
	}

	public String getcDate() {
		return cDate;
	}

	public void setcDate(String cDate) {
		this.cDate = cDate;
	}

	public Long getuUser() {
		return uUser;
	}

	public void setuUser(Long uUser) {
		this.uUser = uUser;
	}

	public String getuDate() {
		return uDate;
	}

	public void setuDate(String uDate) {
		this.uDate = uDate;
	}

	public Integer getRemoved() {
		return removed;
	}

	public void setRemoved(Integer removed) {
		this.removed = removed;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFlowGroup() {
		return flowGroup;
	}

	public void setFlowGroup(String flowGroup) {
		this.flowGroup = flowGroup;
	}

	public FlowWorkProcess getFlowWorkProcess() {
		return flowWorkProcess;
	}

	public void setFlowWorkProcess(FlowWorkProcess flowWorkProcess) {
		this.flowWorkProcess = flowWorkProcess;
	}

	public String getAttachGroup() {
		return attachGroup;
	}

	public void setAttachGroup(String attachGroup) {
		this.attachGroup = attachGroup;
	}

	public String getStateStr(){
		if(this.flowWorkProcess == null)
			return "未提交";
		
		switch(this.flowWorkProcess.getState()){
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
	
}
