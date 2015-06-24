package com.wonders.framework.auth.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.Hibernate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wonders.framework.entity.AbstractTreeNode;

@Entity
@Table(name = "sec_authority")
public class Authority extends AbstractTreeNode<Authority, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4982049490034041789L;

	@NotNull
	private String name;
	
	private boolean enabled;
	
	private String code;
	
	private String description;
	
	private String type;
	
	@NotNull
	private int ordernum;
	
	private long parentId;
	
	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	public Authority getParent() {
		return super.getParent();
	}
	
	@Override
	@OneToMany(mappedBy = "parent")
	@JsonIgnore
	public Set<Authority> getChildren() {
		return super.getChildren();
	}
	
	@Override
	@JsonProperty
	@Transient
	public String getText() {
		return this.name;
	}
	
	void setText(String text) {
		
	}
	
	@Override
	@JsonProperty
	@Transient
	public boolean isLeaf() {
		if (!Hibernate.isInitialized(getChildren())) {
			return false;
		}
		return super.isLeaf();
	}
	
	public void setLeaf(boolean leaf) {
		
	}
	
	public boolean getEnabled() {
		return this.enabled;
	}
	
	@Transient
	public long getParentId() {
		return this.parentId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	
	
}
