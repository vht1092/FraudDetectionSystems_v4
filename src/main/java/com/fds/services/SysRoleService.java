package com.fds.services;

import java.util.List;

import com.fds.entities.FdsPosSysRole;

public interface SysRoleService {
	List<FdsPosSysRole> findAllByDefaultroleIsTrue();
	List<FdsPosSysRole> findAll();
	void update(int id, String name, Boolean defaulrole);	
}
