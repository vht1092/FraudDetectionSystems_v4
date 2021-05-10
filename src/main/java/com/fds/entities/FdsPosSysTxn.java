package com.fds.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the FDS_SYS_TXN database table.
 * 
 */

/*IDTXN       NOT NULL CHAR(4)       
DESCRIPTION NOT NULL VARCHAR2(100) 
ISENABLE    NOT NULL CHAR(1)       
ICON                 VARCHAR2(20)  
PARENT      NOT NULL CHAR(4)       
WEIGHT               NUMBER        
CAPTION              VARCHAR2(100) */

@Entity
@Table(name="FDS_POS_SYS_TXN")
@NamedQuery(name="FdsPosSysTxn.findAll", query="SELECT f FROM FdsPosSysTxn f")
public class FdsPosSysTxn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=4)
	private String idtxn;

	@Column(length=100)
	private String caption;

	@Column(nullable=false, length=100)
	private String description;

	@Column(length=20)
	private String icon;

	@Column(nullable=false, length=1)
	private String isenable;

	@Column(nullable=false, length=4)
	private String parent;

	private int weight;

	public FdsPosSysTxn() {
	}	
	
	
	public FdsPosSysTxn(String idtxn, String caption, String description, String icon, String isenable, String parent, int weight) {		
		this.idtxn = idtxn;
		this.caption = caption;
		this.description = description;
		this.icon = icon;
		this.isenable = isenable;
		this.parent = parent;
		this.weight = weight;
	}




	public String getIdtxn() {
		return this.idtxn;
	}

	public void setIdtxn(String idtxn) {
		this.idtxn = idtxn;
	}

	public String getCaption() {
		return this.caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIsenable() {
		return this.isenable;
	}

	public void setIsenable(String isenable) {
		this.isenable = isenable;
	}

	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getWeight() {
		return this.weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}