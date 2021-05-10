package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the FDS_SYS_USERROLE database table.
 * 
 */

/*IDUSER NOT NULL VARCHAR2(12) 
IDROLE NOT NULL NUMBER(3) */

@Entity
@Table(name="FDS_POS_SYS_USERROLE")
@NamedQuery(name="FdsPosSysUserrole.findAll", query="SELECT f FROM FdsPosSysUserrole f")
public class FdsPosSysUserrole implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FdsPosSysUserrolePK id;

	public FdsPosSysUserrole() {
	}
	

	public FdsPosSysUserrole(FdsPosSysUserrolePK id) {
		super();
		this.id = id;
	}



	public FdsPosSysUserrolePK getId() {
		return this.id;
	}

	public void setId(FdsPosSysUserrolePK id) {
		this.id = id;
	}

}