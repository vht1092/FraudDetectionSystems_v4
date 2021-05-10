package com.fds.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsPosSysRoletxn;

@Repository
public interface SysRoleTxnRepo extends CrudRepository<FdsPosSysRoletxn, Long> {
	List<FdsPosSysRoletxn> findAllByIdrole(int roleid);	
}
