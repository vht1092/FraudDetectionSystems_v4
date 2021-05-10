package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsPosSysUser;

@Repository
public interface SysUserRepo extends JpaRepository<FdsPosSysUser, String> {
	FdsPosSysUser findByEmail(String email);

	List<FdsPosSysUser> findAllUserByActiveflagIsTrue();
}
