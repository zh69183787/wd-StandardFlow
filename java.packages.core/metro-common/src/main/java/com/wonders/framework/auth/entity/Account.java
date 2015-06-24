package com.wonders.framework.auth.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "sec_account")
public class Account extends AbstractPersistable<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7914615723864758751L;

	@Version
	private int version;

	private String name;
	
	private String headimg;
	
	@Column(name = "workno", length=200)
	private String workno;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Group group;
	
	@ManyToMany
	@JoinTable(name = "sec_acc_auth",
		joinColumns = @JoinColumn(name = "acc_id"),
		inverseJoinColumns = @JoinColumn(name = "auth_id")
	)
	@OrderBy
	private Set<Authority> authorities;

	@ManyToMany
	@JoinTable(name = "sec_acc_role",
		joinColumns = @JoinColumn(name = "acc_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	@OrderBy
	private Set<Role> roles;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getWorkno() {
		return workno;
	}

	public void setWorkno(String workno) {
		this.workno = workno;
	}

}
