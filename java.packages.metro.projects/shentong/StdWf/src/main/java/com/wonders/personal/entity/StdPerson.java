package com.wonders.personal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "t_std_person")
public class StdPerson extends AbstractPersistable<Long> {

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "STD_APPLY_ID")
	private StdApply stdApply = new StdApply();
	
	/**
	 * 部门
	 */
	@Column(name="DEPT", columnDefinition="VARCHAR(200)")
	private String dept;
	
	/**
	 * 姓名
	 */
	@Column(name="NAME", columnDefinition="VARCHAR(200)")
	private String name;
	
	/**
	 * 职务
	 */
	@Column(name="POST", columnDefinition="VARCHAR(200)")
	private String post;

	/**
	 * 创建人
	 */
	@Column(name = "C_USER", columnDefinition="int(11)")
	private Long cUser; 
	
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
	
	public void setId(Long id){
		super.setId(id);
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public Long getcUser() {
		return cUser;
	}

	public void setcUser(Long cUser) {
		this.cUser = cUser;
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

	public StdApply getStdApply() {
		return stdApply;
	}

	public void setStdApply(StdApply stdApply) {
		this.stdApply = stdApply;
	}

}
