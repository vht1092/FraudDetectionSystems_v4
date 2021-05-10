package com.fds.services;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsPosSysRole;
import com.fds.repositories.SysRoleRepo;

@Service("sysRoleService")
@Transactional
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleRepo sysRoleRepo;

	@Cacheable("FdsSysRole.findAllByDefaultroleIsTrue")
	@Override
	public List<FdsPosSysRole> findAllByDefaultroleIsTrue() {
		return sysRoleRepo.findAllByDefaultroleIsTrue();
	}

	@Cacheable("FdsSysRole.findAll")
	@Override
	public List<FdsPosSysRole> findAll() {
		return sysRoleRepo.findAll();
	}

	@CacheEvict(value = {"FdsSysRole.findAll", "FdsSysRole.findAllByDefaultroleIsTrue"}, allEntries = true)
	@Override
	public void update(int id, String name, Boolean defaulrole) {
		FdsPosSysRole fdsSysRole = new FdsPosSysRole();
		fdsSysRole.setId(id);
		fdsSysRole.setName(name);
		fdsSysRole.setDefaultrole(defaulrole);
		sysRoleRepo.save(fdsSysRole);

	}
}
