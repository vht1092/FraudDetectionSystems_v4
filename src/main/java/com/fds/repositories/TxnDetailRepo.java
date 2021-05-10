package com.fds.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fds.entities.FdsTxnDetail;
import com.fds.entities.FdsTxnDetailPK;

public interface TxnDetailRepo extends CrudRepository<FdsTxnDetail, FdsTxnDetailPK> {

	public FdsTxnDetail findOneByF9Oa008CreTmsAndFxOa008UsedPan(BigDecimal cretms, String usedpan);

	@Query(nativeQuery = true, value = "select substr(trim(fdstxndeta.fx_oa008_ref_cde), 1, 2) as ref_cde from {h-schema}fds_txn_detail fdstxndeta  where fdstxndeta.f9_oa008_cre_tms = ?1 and fdstxndeta.fx_oa008_used_pan = ?2")
	public String findRefCdeByCreTmsAndUsedPan(BigDecimal cretms, String usedpan);

	@Query(nativeQuery = true, value = "select trim(fdstxndeta0_.fx_oa008_merc_st_cntry) cntry_cde from fds_txn_detail fdstxndeta0_ where fdstxndeta0_.fx_oa008_used_pan = ?1 and fdstxndeta0_.f9_oa008_cre_tms = ?2")
	public String findOneFxOa008CntryCdeByFxOa008UsedPanAndF9Oa008CreTms(String pan, BigDecimal cretms);
	
	@Query(nativeQuery = true, value = "select trim(F9_OA008_ECI_SEC_LVL) as eci_val from {h-schema}fds_txn_detail fdstxndeta  where fdstxndeta.f9_oa008_cre_tms = ?1 and fdstxndeta.fx_oa008_used_pan = ?2")
	public String findEciValByCreTmsAndUsedPan(BigDecimal cretms, String usedpan);

}
