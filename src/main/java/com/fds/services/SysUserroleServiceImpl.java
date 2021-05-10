package com.fds.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsPosSysUserrole;
import com.fds.entities.FdsPosSysUserrolePK;
import com.fds.repositories.SysUserroleRepo;

@Service("sysUserroleService")
public class SysUserroleServiceImpl implements SysUserroleService {

	@Autowired
	private SysUserroleRepo sysUserroleRepo;

	@Override
	public void save(String iduser, int idrole) {
		FdsPosSysUserrole fdsSysUserrole = new FdsPosSysUserrole();
		fdsSysUserrole.setId(new FdsPosSysUserrolePK(iduser, idrole));
		sysUserroleRepo.save(fdsSysUserrole);
	}

	@Override
	public List<FdsPosSysUserrole> findAllByUserId(String iduser) {
		return sysUserroleRepo.findAllByIdUser(iduser);
	}

	@Override
	public void deleteByIduser(String iduser) {
		List<FdsPosSysUserrole> fdsSysUserrole = sysUserroleRepo.findAllByIdUser(iduser);
		if (fdsSysUserrole != null) {
			sysUserroleRepo.delete(fdsSysUserrole);
		}
	}

}
