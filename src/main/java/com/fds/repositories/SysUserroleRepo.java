package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsPosSysUserrole;

@Repository
public interface SysUserroleRepo extends CrudRepository<FdsPosSysUserrole, String> {

	@Query(value = "select f from FdsPosSysUserrole f where f.id.iduser=:iduser")
	List<FdsPosSysUserrole> findAllByIdUser(@Param("iduser") String iduser);

	@Query(value = "delete from {h-schema}fds_pos_sys_userrole t where t.iduser=:iduser", nativeQuery = true)
	List<FdsPosSysUserrole> deleteByIduser(@Param("iduser") String iduser);
}
