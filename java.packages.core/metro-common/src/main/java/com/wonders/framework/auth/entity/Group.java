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
@Table(name = "sec_group")
public class Group extends AbstractTreeNode<Group, Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8889538829841098043L;

	@NotNull
	private String name;
	
	private boolean enabled;
	
	private boolean leaf;
	
	@NotNull
	private int ordernum;
	
	private long parentId;
	
	private String description;
	
	private String nodetype	;
	
	private String groupcode;
	
	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	public Group getParent() {
		return super.getParent();
	}
	
	@Override
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	@JsonIgnore
	public Set<Group> getChildren() {
		return super.getChildren();
	}
	
	@Override
	@JsonProperty
	@Transient
	public String getText() {
		return this.name;
	}
	
	public void setText(String text) {
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
	
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNodetype() {
		return nodetype;
	}

	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
