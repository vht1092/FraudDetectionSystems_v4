package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsPosCaseStatus;

@Repository
public interface CaseStatusRepo extends CrudRepository<FdsPosCaseStatus, Long> {

	List<FdsPosCaseStatus> findAllByCaseNo(@Param("caseno") String caseno, Pageable page);

	@Query(value = "select to_number(to_char(SYSDATE, 'yyyyMMddHH24MISSSSS')) from dual", nativeQuery = true)
	BigDecimal getCurrentTime();
	
	@Query(value = " select CASE_COMMENT " +
				   " from FDS_POS_CASE_STATUS " +
                   " where CASE_NO = :caseno " +
                   "       and CRE_TMS = (select max(CRE_TMS) from FDS_POS_CASE_STATUS where CASE_NO = :caseno)", nativeQuery = true)
	String getCaseComment(@Param(value = "caseno") String caseno);
}
