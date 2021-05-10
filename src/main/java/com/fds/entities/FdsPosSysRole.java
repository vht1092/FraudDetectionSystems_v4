package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;

/*ID          NOT NULL NUMBER(3)     
NAME        NOT NULL VARCHAR2(10)  
DEFAULTROLE          NUMBER(1)     
DESCRIPTION          VARCHAR2(100)*/ 

@Entity
@Table(name="FDS_POS_SYS_ROLE")
public class FdsPosSysRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	private boolean defaultrole;

	@Column(length=100)
	private String description;

	@Column(nullable=false, length=10)
	private String name;

	public FdsPosSysRole() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getDefaultrole() {
		return this.defaultrole;
	}

	public void setDefaultrole(boolean defaultrole) {
		this.defaultrole = defaultrole;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}