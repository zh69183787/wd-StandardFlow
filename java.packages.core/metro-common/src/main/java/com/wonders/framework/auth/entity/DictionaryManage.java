package com.wonders.framework.auth.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "data_dictionary_manage")
public class DictionaryManage extends AbstractPersistable<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1091621783431676183L;

	@Version
	private int version;

	@NotNull
	private String name;
	
	private String typecode;

/*	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	Dictionary dictionary*/
	
	private String description;

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
	
	
}
