package com.wonders.framework.auth.entity;

import java.util.Set;

import javax.persistence.Column;
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
@Table(name = "data_dictionary")
public class Dictionary extends AbstractTreeNode<Dictionary, Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5829916746871193979L;

	@NotNull
	private String name;
	
	private boolean enabled;
	
	@Column(unique = true)
	private String code;
	
	@NotNull
	private int ordernum;
	
	@NotNull
	private String typecode;
	
	private String description;
	
	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	public Dictionary getParent() {
		return super.getParent();
	}
	
	@Override
	@OneToMany(mappedBy = "parent")
	@JsonIgnore
	public Set<Dictionary> getChildren() {
		return super.getChildren();
	}
	
	@Override
	@JsonProperty
	@Transient
	public String getText() {
		return this.name;
	}
	
	public void setText(String text){}
	
	@Override
	@JsonProperty
	@Transient
	public boolean isLeaf() {
		if (!Hibernate.isInitialized(getChildren())) {
			return false;
		}
		return super.isLeaf();
	}
	
	public void setLeaf(boolean leaf){}
	
	public boolean getEnabled() {
		return this.enabled;
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

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public String getTypecode() {
		return typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
