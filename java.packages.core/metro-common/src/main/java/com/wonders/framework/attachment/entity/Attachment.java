package com.wonders.framework.attachment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wonders.framework.auth.entity.Dictionary;
@Entity
@Table(name = "t_attachments")
public class Attachment  extends AbstractPersistable<Long>{
	
	public void setId(Long id){
		super.setId(id);
	}
	
	private static final long serialVersionUID = 2751338925404465757L;

	@Column(name="file_full_path",nullable=false)
	private String fileFullPath;
	@NotNull
	@Column(name="GROUP_CODE")
	private String group;
	@Column(name="file_name",nullable=false)
	private String fileName;
	@Column(name="file_ext_name",nullable=false)
	private String fileExtName;
	@Column(name="uploader_name",nullable=false)
	private String uploaderName;
	@Column(name="upload_time",nullable=false)
	private String uploadTime;
	@Column(name="code_id",nullable=false)
	private Long codeId;

	@ManyToOne(optional=true,fetch=FetchType.EAGER)
	@JoinColumn(name="code_id" ,nullable = true,insertable=false,updatable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	private Dictionary dict;
	
	
	@Enumerated
	@Column(name="attach_type")
	private AttachType attachType;
	
	@Column(name="account_id")
	private long accountId;
	
	private int removed;
	@Transient
	private byte[] payloads;
	
	@Transient
	@JsonIgnoreProperties
	private MultipartFile file;

	
	@Column(name="attach_size")
	private long size;
	
	@Column(name="version")
	private String version;
	
	public Long getCodeId() {
		return codeId;
	}
	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}

	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long account) {
		this.accountId = account;
	}
	public byte[] getPayloads() {
		return payloads;
	}
	public void setPayloads(byte[] payloads) {
		this.payloads = payloads;
	}
	public String getFileFullPath() {
		return fileFullPath;
	}
	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExtName() {
		return fileExtName;
	}
	public void setFileExtName(String fileExtName) {
		this.fileExtName = fileExtName;
	}
	public String getUploaderName() {
		return uploaderName;
	}
	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Dictionary getDict() {
		return dict;
	}
	public void setDict(Dictionary dict) {
		this.dict = dict;
	}
	public AttachType getAttachType() {
		return attachType;
	}
	public void setAttachType(AttachType attachType) {
		this.attachType = attachType;
	}
	public int getRemoved() {
		return removed;
	}
	public void setRemoved(int removed) {
		this.removed = removed;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
