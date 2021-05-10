package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsPosTxnV2;

@Repository
public interface FdsPosTxnV2Repo extends CrudRepository<FdsPosTxnV2, String> {

	List<FdsPosTxnV2> findAll();
	
	@Query(value = "SELECT * FROM FDS_POS_TXN_V2 " + 
			"WHERE NGAY_GD BETWEEN :tungay AND :denngay " + 
			"AND ((:nhtt='VTB' AND VTB_CHKS_STAT='Y') OR (:nhtt='EIB' AND EIB_CHKS_STAT='Y'))", nativeQuery = true)
	List<FdsPosTxnV2> findAllBetweensNgayGd(@Param("tungay") BigDecimal tungay,@Param("denngay") BigDecimal denngay,@Param("nhtt") String nhtt);
	
	@Query(value = "select SEQNO_FDS_POS_TXN_V2.nextval SEQ  from dual", nativeQuery = true)
	String getSeqnoFdsPosTxnV2();
	
	@Modifying
    @Query(value = "insert into fds_pos_cases_detail  " + 
    		"select  " + 
    		"  b.case_id,  " + 
    		"  tid,  " + 
    		"  (select listagg(RULE_ID,',') WITHIN GROUP (ORDER BY RULE_ID) from fds_pos_case_hit_rule_v2 a where a.CASE_ID = b.case_id) as rule_id,  " + 
    		"  to_char(sysdate,'yyyymmdd') as check_dt,  " + 
    		"  upd_tms,  " + 
    		"  case_status,  " + 
    		"  'N' as check_new,  " + 
    		"  asg_tms,  " + 
    		"  ' ' as usr_id,  " + 
    		"  mid,  " + 
    		"  ten_mid,  " + 
    		"  mcc, ngay_gd  " + 
    		"from fds_pos_case_detail_v2 b  " + 
    		"where (b.ngay_gd between :txnStartdate and :txnEnddate)  " + 
    		"and (:nhtt='VTB' or (:nhtt='EIB' and b.CASE_ID IN :listCaseId))", nativeQuery = true)
    public int insertFdsPosCasesDetail(@Param("nhtt") String nhtt,@Param("txnStartdate") BigDecimal txnStartdate,@Param("txnEnddate") BigDecimal txnEnddate,@Param("listCaseId") Set<String> listCaseId);
	
	@Modifying
    @Query(value = "insert into fds_pos_case_hit_rules  " + 
    		"select CASE_ID, RULE_ID, TID, MA_GD, NGAY_TAO_GD from fds_pos_casehitruledetail_v2 b  " + 
    		"where  (b.ngay_gd between :txnStartdate and :txnEnddate)  " + 
    		"and (:nhtt='VTB' or (:nhtt='EIB' and b.MA_GD IN :listCaseId))", nativeQuery = true)
    public int insertFdsPosCaseHitRules(@Param("nhtt") String nhtt,@Param("txnStartdate") BigDecimal txnStartdate,@Param("txnEnddate") BigDecimal txnEnddate,@Param("listCaseId") Set<String> listCaseId);
	
	@Modifying
    @Query(value = "insert into fds_pos_txn  " + 
    		"select   " + 
    		"  ma_gd,  mid,  ten_mid,  tid,  so_hd,  dia_chi_gd,  ngay_tao_gd,   " + 
    		"  ngay_gio_gd,  so_hoa_don,  ma_chuan_chi,  so_tien_gd_goc,  loai_tien,   " + 
    		"  so_tien_tip,  so_bin,  so_the,  loai_the,  ket_qua_gd,  dao_huy,   " + 
    		"  pos_mode,  ma_loi,  bao_co,  ngay_gd,  scb_chks_stat,  mcc, on_offline, case when tid is null then 'EIB' else 'VTB' end nhtt   " + 
    		"from fds_pos_txn_v2 b  " + 
    		"where (b.ngay_gd between :txnStartdate and :txnEnddate)  " + 
    		"and (:nhtt='VTB' or (:nhtt='EIB' and b.MA_GD IN :listCaseId))", nativeQuery = true)
    public int insertFdsPosTxn(@Param("nhtt") String nhtt,@Param("txnStartdate") BigDecimal txnStartdate,@Param("txnEnddate") BigDecimal txnEnddate,@Param("listCaseId") Set<String> listCaseId);
	
	@Modifying
    @Query(value= "insert into fds_pos_cases  " + 
    		"select  " + 
    		"  b.case_id,  " + 
    		"  b.tid,  " + 
    		"  b.rule_id,  " + 
    		"  b.so_bin,  " + 
    		"  to_char(sysdate,'yyyymmdd') as check_dt,  " + 
    		"  (select listagg(MA_GD,',') WITHIN GROUP (ORDER BY MA_GD) from fds_pos_casehitruledetail_v2 a where a.CASE_ID = b.case_id and a.rule_id = b.rule_id) as TXN_CDE_LIST  " + 
    		"from fds_pos_casehitruledetail_v2 b  " + 
    		"where (b.ngay_gd between :txnStartdate and :txnEnddate)  " + 
    		"and (:nhtt='VTB' or (:nhtt='EIB' and b.CASE_ID IN :listCaseId))  " + 
    		"group by  " + 
    		"  b.case_id,  " + 
    		"  b.tid,  " + 
    		"  b.rule_id,  " + 
    		"  b.so_bin,  " + 
    		"  b.ngay_gd", nativeQuery = true)
    public int insertFdsPosCases(@Param("nhtt") String nhtt,@Param("txnStartdate") BigDecimal txnStartdate,@Param("txnEnddate") BigDecimal txnEnddate,@Param("listCaseId") Set<String> listCaseId);


}
