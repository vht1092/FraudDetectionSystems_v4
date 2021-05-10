package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsPosSysTaskAuditTrail;


@Repository
public interface SysTaskAuditTrailRepo  extends CrudRepository<FdsPosSysTaskAuditTrail, Long> {
	
	@Query(value = " select USERUPDATE, CREATEDATE " + 
				   " from {h-schema}FDS_POS_SYS_TASK_AUDIT_TRAIL " +
				   " where OBJECTTASK = :objectTask "+ 
				   "	   and CREATEDATE = (select max(CREATEDATE) from {h-schema}FDS_POS_SYS_TASK_AUDIT_TRAIL where MID=:mid OR TID=:tid)", nativeQuery = true)      
	public List<Object[]> getUserUpdate(@Param("mid") String mid, @Param("tid") String tid);

}

