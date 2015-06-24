package com.wonders.personal.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.wonders.framework.auth.entity.Dictionary;

@Entity
@Table(name = "t_std_sch")
public class StdSch extends AbstractPersistable<Long> {

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "STD_APPLY_ID")
	private StdApply stdApply = new StdApply();
	
	/**
	 * 工作阶段
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name = "STD_STAGE_ID", nullable = true, insertable = false, updatable = false)	
	private Dictionary stdStage;
	@Column(name = "STD_STAGE_ID", columnDefinition="INT(11)")
	private Long stdStageId;
	
	/**
	 * 姓名
	 */
	@Column(name="FINISH_DATE", columnDefinition="VARCHAR(20)")
	private String finishDate;

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

	public StdApply getStdApply() {
		return stdApply;
	}

	public void setStdApply(StdApply stdApply) {
		this.stdApply = stdApply;
	}

	public Dictionary getStdStage() {
		return stdStage;
	}

	public void setStdStage(Dictionary stdStage) {
		this.stdStage = stdStage;
	}

	public Long getStdStageId() {
		return stdStageId;
	}

	public void setStdStageId(Long stdStageId) {
		this.stdStageId = stdStageId;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
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

}
