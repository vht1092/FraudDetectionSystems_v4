package com.fds.services;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsPosTxnV2;
import com.fds.repositories.FdsPosTxnV2Repo;

import oracle.jdbc.OracleTypes;

@Service("fdsPosTxnV2Service")
@Transactional
public class FdsPosTxnV2ServiceImpl implements FdsPosTxnV2Service {
	
private static final Logger LOGGER = LoggerFactory.getLogger(FdsPosTxnV2ServiceImpl.class);
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private FdsPosTxnV2Repo fdsPosTxnV2Repo;
	// private List<FdsSysTxn> listFdsSysTxn;


	@Override
	public List<FdsPosTxnV2> findAll() {
		return fdsPosTxnV2Repo.findAll();
	}

	@Override
	public List<FdsPosTxnV2> findAllBetweensNgayGd(BigDecimal tungay, BigDecimal denngay, String nhtt) {
		// TODO Auto-generated method stub
		return fdsPosTxnV2Repo.findAllBetweensNgayGd(tungay, denngay, nhtt);
	}

	@Override
	public void save(FdsPosTxnV2 fdsPosTxnV2) {
		// TODO Auto-generated method stub
		fdsPosTxnV2Repo.save(fdsPosTxnV2);
	}

	@Override
	public boolean existsById(String magd) {
		return fdsPosTxnV2Repo.exists(magd);
	}
	

	@Override
	public String fdsPosRulesProcessV2() {
		// TODO Auto-generated method stub
		String sp_FdsPosRulesProcessV2 = "{call " + sSchema +  ".SP_FDSPOS_RULES_PROCESS_V2(?)}";
		Connection conn = null;
		CallableStatement callableStatement = null;
		String responseCode = "";
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_FdsPosRulesProcessV2);
			callableStatement.registerOutParameter(1, OracleTypes.VARCHAR);
			callableStatement.executeUpdate();
			responseCode = callableStatement.getString(1);
		}
		catch (Exception ex){
			ex.printStackTrace();
			LOGGER.error(ex.getMessage());
		}
		finally{
			try {
				conn.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return responseCode;
	}


	@Override
	public String fdsPosRulesProcessVtb() {
		// TODO Auto-generated method stub
		String sp_FdsPosRulesProcessVtb = "{call " + sSchema +  ".SP_FDSPOS_RULES_PROCESS_VTB(?)}";
		Connection conn = null;
		CallableStatement callableStatement = null;
		String responseCode = "";
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_FdsPosRulesProcessVtb);
			callableStatement.registerOutParameter(1, OracleTypes.VARCHAR);
			callableStatement.executeUpdate();
			responseCode = callableStatement.getString(1);
		}
		catch (Exception ex){
			ex.printStackTrace();
			LOGGER.error(ex.getMessage());
		}
		finally{
			try {
				conn.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return responseCode;
	}

	@Override
	public String fdsPosRulesProcessEib() {
		// TODO Auto-generated method stub
		String sp_FdsPosRulesProcessVtb = "{call " + sSchema +  ".SP_FDSPOS_RULES_PROCESS_EIB(?)}";
		Connection conn = null;
		CallableStatement callableStatement = null;
		String responseCode = "";
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_FdsPosRulesProcessVtb);
			callableStatement.registerOutParameter(1, OracleTypes.VARCHAR);
			callableStatement.executeUpdate();
			responseCode = callableStatement.getString(1);
		}
		catch (Exception ex){
			ex.printStackTrace();
			LOGGER.error(ex.getMessage());
		}
		finally{
			try {
				conn.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return responseCode;
	}

	@Override
	public int insertFdsPosCasesDetail(String nhtt, BigDecimal txnStartdate, BigDecimal txnEnddate, Set<String> listCaseId) {
		// TODO Auto-generated method stub
//		Connection connect = null;
//		PreparedStatement preStmt = null;
//		int rows = 0;
//		StringBuilder sqlString = new StringBuilder();
//
//		sqlString 	.append("insert into " + sSchema +  ".fds_pos_cases_detail ")
//					.append("select ")
//					.append("  b.case_id, ")
//					.append("  tid, ")
//					.append("  (select listagg(RULE_ID,',') WITHIN GROUP (ORDER BY RULE_ID) from " + sSchema +  ".fds_pos_case_hit_rule_v2 a where a.CASE_ID = b.case_id) as rule_id, ")
//					.append("  to_char(to_date(b.ngay_gd,'yyyymmdd')+1,'yyyymmdd') as check_dt, ")
//					.append("  upd_tms, ")
//					.append("  case_status, ")
//					.append("  'N' as check_new, ")
//					.append("  asg_tms, ")
//					.append("  ' ' as usr_id, ")
//					.append("  mid, ")
//					.append("  ten_mid, ")
//					.append("  mcc ")
//					.append("from ")
//					.append(sSchema +  ".fds_pos_case_detail_v2 b ")
//					.append("where b.ngay_gd between ? and ? ")
//					.append("and (( " + caseId + "='VTB' and b.CASE_ID=?) or " + caseId + "='EIB')");
////					.append("  b.ngay_gd >= to_char(sysdate-?,'yyyymmdd') and b.ngay_gd <= to_char(sysdate-1,'yyyymmdd')");
//		try {
//			
//			connect = dataSource.getConnection();
//
//			preStmt = connect.prepareStatement(sqlString.toString());
//			preStmt.setBigDecimal(1, txnStartdate);
//			preStmt.setBigDecimal(2, txnEnddate);
//			preStmt.setString(3, caseId);
//			
//			rows = preStmt.executeUpdate();
//		} catch (Exception ex){
//			LOGGER.error(ex.getMessage());
//		}
//		finally{
//			try {
//				connect.close();
//				preStmt.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				LOGGER.error(e.getMessage());
//			}
//		}
//		
//		return rows;
		return fdsPosTxnV2Repo.insertFdsPosCasesDetail(nhtt, txnStartdate, txnEnddate, listCaseId);
	}


	@Override
	public int insertFdsPosCaseHitRules(String nhtt, BigDecimal txnStartdate, BigDecimal txnEnddate, Set<String> listCaseId) {
		// TODO Auto-generated method stub
//		Connection connect = null;
//		PreparedStatement preStmt = null;
//		int rows = 0;
//		StringBuilder sqlString = new StringBuilder();
//
//		sqlString 	.append("insert into " + sSchema +  ".fds_pos_case_hit_rules ")
//					.append("select CASE_ID, RULE_ID, TID, MA_GD, NGAY_TAO_GD from " + sSchema +  ".fds_pos_casehitruledetail_v2 b ")
//					.append("where  b.ngay_gd between ? and ? ")
//					.append("and (( " + caseId + "='VTB' and b.CASE_NO=?) or " + caseId + "='EIB')");
////					.append("where  b.ngay_gd >= to_char(sysdate-?,'yyyymmdd') and b.ngay_gd <= to_char(sysdate-1,'yyyymmdd')");
//		try {
//			
//			connect = dataSource.getConnection();
//
//			preStmt = connect.prepareStatement(sqlString.toString());
//			preStmt.setBigDecimal(1, txnStartdate);
//			preStmt.setBigDecimal(2, txnEnddate);
//			preStmt.setString(3, caseId);
//			
//			rows = preStmt.executeUpdate();
//		} catch (Exception ex){
//			LOGGER.error(ex.getMessage());
//		}
//		finally{
//			try {
//				connect.close();
//				preStmt.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				LOGGER.error(e.getMessage());
//			}
//		}
//		
//		return rows;
		return fdsPosTxnV2Repo.insertFdsPosCaseHitRules(nhtt, txnStartdate, txnEnddate, listCaseId);
	}


	@Override
	public int insertFdsPosTxn(String nhtt, BigDecimal txnStartdate, BigDecimal txnEnddate, Set<String> listCaseId) {
		// TODO Auto-generated method stub
//		Connection connect = null;
//		PreparedStatement preStmt = null;
//		int rows = 0;
//		StringBuilder sqlString = new StringBuilder();
//
//		sqlString 	.append("insert into " + sSchema +  ".fds_pos_txn ")
//					.append("select  ")
//					.append("  ma_gd,  mid,  ten_mid,  tid,  so_hd,  dia_chi_gd,  ngay_tao_gd,  ")
//					.append("  ngay_gio_gd,  so_hoa_don,  ma_chuan_chi,  so_tien_gd_goc,  loai_tien,  ")
//					.append("  so_tien_tip,  so_bin,  so_the,  loai_the,  ket_qua_gd,  dao_huy,  ")
//					.append("  pos_mode,  ma_loi,  bao_co,  ngay_gd,  scb_chks_stat,  mcc  ")
//					.append("from " + sSchema +  ".fds_pos_txn_v2 b ")
//					.append("where b.ngay_gd between ? and ? ")
//					.append(" and (( " + nhtt + "='VTB' and b.MA_GD=?) or " + nhtt + "='EIB')");
////					.append("where b.ngay_gd >= to_char(sysdate-?,'yyyymmdd') and b.ngay_gd <= to_char(sysdate-1,'yyyymmdd')");
//		try {
//			
//			connect = dataSource.getConnection();
//
//			preStmt = connect.prepareStatement(sqlString.toString());
//			preStmt.setBigDecimal(1, txnStartdate);
//			preStmt.setBigDecimal(2, txnEnddate);
//			preStmt.setString(3, maGd);
//			
//			rows = preStmt.executeUpdate();
//		} catch (Exception ex){
//			LOGGER.error(ex.getMessage());
//		}
//		finally{
//			try {
//				connect.close();
//				preStmt.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				LOGGER.error(e.getMessage());
//			}
//		}
//		
//		return rows;
		return fdsPosTxnV2Repo.insertFdsPosTxn(nhtt, txnStartdate, txnEnddate, listCaseId);
	}


	@Override
	public int insertFdsPosCases(String nhtt, BigDecimal txnStartdate, BigDecimal txnEnddate, Set<String> listCaseId) {
		// TODO Auto-generated method stub
//		Connection connect = null;
//		PreparedStatement preStmt = null;
//		int rows = 0;
//		StringBuilder sqlString = new StringBuilder();
//
//		sqlString 	.append("insert into " + sSchema +  ".fds_pos_cases ")
//					.append("select ")
//					.append("  b.case_id, ")
//					.append("  b.tid, ")
//					.append("  b.rule_id, ")
//					.append("  b.so_bin, ")
//					.append("  to_char(to_date(b.ngay_gd,'yyyymmdd')+1,'yyyymmdd') as check_dt, ")
//					.append("  (select listagg(MA_GD,',') WITHIN GROUP (ORDER BY MA_GD) from " + sSchema +  ".fds_pos_casehitruledetail_v2 a where a.CASE_ID = b.case_id and a.rule_id = b.rule_id) as TXN_CDE_LIST ")
//					.append("from ")
//					.append(sSchema +  ".fds_pos_casehitruledetail_v2 b ")
//					.append("where ")
//					.append("  b.ngay_gd between ? and ? ")
//					.append("and (( " + caseId + "='VTB' and b.CASE_ID=?) or " + caseId + "='EIB') ")
////					.append("  b.ngay_gd >= to_char(sysdate-?,'yyyymmdd') and ngay_gd <= to_char(sysdate-1,'yyyymmdd') ")
//					.append("group by ")
//					.append("  b.case_id, ")
//					.append("  b.tid, ")
//					.append("  b.rule_id, ")
//					.append("  b.so_bin, ")
//					.append("  b.ngay_gd");
//		
//		try {
//			
//			connect = dataSource.getConnection();
//
//			preStmt = connect.prepareStatement(sqlString.toString());
//			preStmt.setBigDecimal(1, txnStartdate);
//			preStmt.setBigDecimal(2, txnEnddate);
//			preStmt.setString(3, caseId);
//
//			rows = preStmt.executeUpdate();
//		} catch (Exception ex){
//			LOGGER.error(ex.getMessage());
//		}
//		finally{
//			try {
//				connect.close();
//				preStmt.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				LOGGER.error(e.getMessage());
//			}
//		}
//		
//		return rows;
		return fdsPosTxnV2Repo.insertFdsPosCases(nhtt, txnStartdate, txnEnddate, listCaseId);
	}


	@Override
	public String getSeqnoFdsPosTxnV2() {
		// TODO Auto-generated method stub
		return fdsPosTxnV2Repo.getSeqnoFdsPosTxnV2();
		
	}


	

}
