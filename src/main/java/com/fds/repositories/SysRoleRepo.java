package com.fds.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsPosSysRole;

@Repository
public interface SysRoleRepo extends CrudRepository<FdsPosSysRole, Integer> {
	List<FdsPosSysRole> findAllByDefaultroleIsTrue();
	List<FdsPosSysRole> findAll();
}
