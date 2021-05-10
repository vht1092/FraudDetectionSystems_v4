package com.fds.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.fds.entities.FdsPosTxnV2;

public interface FdsPosTxnV2Service {
	
	List<FdsPosTxnV2> findAll();
	
	List<FdsPosTxnV2> findAllBetweensNgayGd(BigDecimal tungay, BigDecimal denngay, String nhtt);
	
	public void save(FdsPosTxnV2 fdsPosTxnV2);
	
	public boolean existsById(String magd);
	
	public String fdsPosRulesProcessV2();
	
	public String fdsPosRulesProcessVtb();
	
	public String fdsPosRulesProcessEib();
	
	public int insertFdsPosCasesDetail(String nhtt, BigDecimal txnStartdate, BigDecimal txnEnddate, Set<String> listCaseId);
	
	public int insertFdsPosCaseHitRules(String nhtt, BigDecimal txnStartdate, BigDecimal txnEnddate, Set<String> listCaseId);
	
	public int insertFdsPosTxn(String nhtt, BigDecimal txnStartdate, BigDecimal txnEnddate, Set<String> listCaseId);
	
	public int insertFdsPosCases(String nhtt, BigDecimal txnStartdate, BigDecimal txnEnddate, Set<String> listCaseId);
	
	public String getSeqnoFdsPosTxnV2();
}
