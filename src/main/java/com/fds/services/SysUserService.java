package com.fds.services;

import java.util.List;

import com.fds.entities.FdsPosSysUser;

public interface SysUserService {
	public FdsPosSysUser findAllByEmail(String email);

	public List<FdsPosSysUser> findAllUser();
	
	public List<FdsPosSysUser> findAllUserByActiveflagIsTrue();

	public String createNew(String userid, String email, String fullname);

	public void updateLastLogin(String userid);

	public void updateUserByUserId(String userid, String fullname, String usertype, Boolean active);

	public FdsPosSysUser findByUserid(String userid);
}
