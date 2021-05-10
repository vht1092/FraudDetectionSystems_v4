package com.fds.services;

import java.util.List;

import com.fds.entities.FdsPosSysUserrole;

public interface SysUserroleService {
	void save(String iduser, int idrole);
	void deleteByIduser(String iduser);
	List<FdsPosSysUserrole> findAllByUserId(String iduser);	
}
