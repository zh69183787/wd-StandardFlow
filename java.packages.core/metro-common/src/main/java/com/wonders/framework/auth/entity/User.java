package com.wonders.framework.auth.entity;

import static javax.persistence.TemporalType.DATE;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "sec_user")
public class User extends AbstractPersistable<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5568433962697874764L;

	@Version
	private int version;

	@NotNull
	private String username;
	
	@NotNull
	@Column(name = "login_name", unique = true)
	private String loginName;
	
	@NotNull
	private String password;
	
	private boolean enabled;

	private String telephone;
	
	private String address;
	
	@Temporal(DATE)
	private Date birthday;

	@Enumerated
	@Column(nullable = false)
	private Gender gender;
	
	@Enumerated
	@Column(name = "user_type", nullable = false)
	private UserType userType;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Set<Account> accounts;
	
	boolean getEnabled() {
		return this.enabled;
	}
	
	enum Gender {
		MALE, FEMALE
	}
	
	public enum UserType {
		NORMAL, ADVINCED, ADMINISTRATOR
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
}
