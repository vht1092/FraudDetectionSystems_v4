package com.fds.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.TimeConverter;
import com.fds.entities.FdsPosSysRole;
import com.fds.entities.FdsPosSysUser;
import com.fds.entities.FdsPosSysUserrole;
import com.fds.entities.FdsPosSysUserrolePK;
import com.fds.repositories.SysRoleRepo;
import com.fds.repositories.SysUserRepo;
import com.fds.repositories.SysUserroleRepo;

@Service("sysUserService")
@Transactional
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserRepo sysUserRepo;

	@Autowired
	private SysRoleRepo roleRepository;
	@Autowired
	private SysUserroleRepo userroleRepo;
	private final TimeConverter timeConverter = new TimeConverter();

	@Override
	public FdsPosSysUser findAllByEmail(String email) {
		return sysUserRepo.findByEmail(email);
	}

	@Override
	public FdsPosSysUser findByUserid(String userid) {
		return sysUserRepo.findOne(userid);
	}

	@Override
	public String createNew(String userid, String email, String fullname) {
		FdsPosSysUser sysUser = new FdsPosSysUser();
		sysUser.setUserid(userid);
		sysUser.setActiveflag(true);// Mac dinh duoc active
		sysUser.setEmail(email);
		sysUser.setFullname(fullname);
		sysUser.setUsertype("OFF");// User mac dinh la officer
		sysUser.setCreatedate(timeConverter.getCurrentTime());
		sysUser.setLastlogin(timeConverter.getCurrentTime());
		sysUserRepo.save(sysUser);
		// Gan role mac dinh
		List<FdsPosSysRole> listDefaultRole = roleRepository.findAllByDefaultroleIsTrue();

		for (FdsPosSysRole list : listDefaultRole) {
			FdsPosSysUserrolePK id = new FdsPosSysUserrolePK();
			id.setIdrole(list.getId());
			id.setIduser(sysUser.getUserid());
			userroleRepo.save(new FdsPosSysUserrole(id));
		}
		return sysUser.getUserid();
	}

	@Override
	public void updateLastLogin(String userid) {
		FdsPosSysUser user = sysUserRepo.findOne(userid);
		user.setLastlogin(timeConverter.getCurrentTime());
		sysUserRepo.save(user);
	}

	@Override
	public List<FdsPosSysUser> findAllUser() {
		return sysUserRepo.findAll();
	}

	@Override
	public void updateUserByUserId(String userid, String fullname, String usertype, Boolean active) {
		FdsPosSysUser fdsSysUser = sysUserRepo.findOne(userid);
		if (fdsSysUser == null) {
			fdsSysUser = new FdsPosSysUser();
			fdsSysUser.setUserid(userid);
		}
		fdsSysUser.setFullname(fullname);
		fdsSysUser.setUsertype(usertype);
		fdsSysUser.setActiveflag(active);
		fdsSysUser.setUpdatedate(timeConverter.getCurrentTime());
		sysUserRepo.save(fdsSysUser);

	}

	@Override
	public List<FdsPosSysUser> findAllUserByActiveflagIsTrue() {
		return sysUserRepo.findAllUserByActiveflagIsTrue();
	}

}
