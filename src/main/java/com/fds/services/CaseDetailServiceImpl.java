package com.fds.services;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fds.TimeConverter;
import com.fds.entities.FdsPosCasesDetail;
import com.fds.entities.FdsPosSysTask;
import com.fds.repositories.CaseDetailRepo;
import com.fds.repositories.SysTaskRepo;

@Service("caseDetailService")
@Transactional
public class CaseDetailServiceImpl implements CaseDetailService {

	private final TimeConverter timeConverter = new TimeConverter();
	// Chuyen case
	private final static String TRANSSTAT = "TRA";
	// Dong case
	private final static String DICSTAT = "DIC";
	// Mo lai case
	private final static String REOPENSTAT = "REO";
	// Goi lai sau
	private final static String CALLBACKSTAT = "CAL";
	// Giao dich fraud
	private final static String FRASTAT = "CAF";
	// Case se load trong menu case da dong
	public static final String[] CLOSEDCASESTAT = { DICSTAT, FRASTAT };
	// Case se load trong menu dang xu ly
	public static final String[] INBOXCASESTAT = { TRANSSTAT, REOPENSTAT, " " };

	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private CaseDetailRepo caseDetailRepo;
	@Autowired
	private SysTaskRepo sysTaskRepo;

	@Override
	public Page<FdsPosCasesDetail> findAllBycheckNew(Pageable page) {
		return caseDetailRepo.findAllBycheckNewIs("N", page);
	}

	@Override
	public FdsPosCasesDetail findOneByCaseNo(String caseno) {
		return caseDetailRepo.findOneByCaseNo(caseno);
	}

	@Override
	public int countAllNewestUserNotAssigned() {
		return caseDetailRepo.countBycheckNewIs();
	}

	@Override
	public Page<FdsPosCasesDetail> findAllProcessingByUser(Pageable page, String userid) {
		return caseDetailRepo.findByUsrIIgnoreCasedAndCaseStatusIn(userid, INBOXCASESTAT, page);
	}

	@Override
	public Page<FdsPosCasesDetail> findAllClosedByUser(Pageable page, String userid) {
		return caseDetailRepo.findByUsrIIgnoreCasedAndCaseStatusIn(userid, CLOSEDCASESTAT, page);
	}

	@Override
	@CacheEvict(value = { "FdsRule.findColorByCaseNo", "FdsRule.findByCaseNo" }, key = "#caseno")
	public boolean closeCase(String caseno, String userid, String status) {
		FdsPosCasesDetail fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		if (!fdsCaseDetail.getCaseStatus().equals(status)) {
			fdsCaseDetail.setCaseStatus(status);
			if (fdsCaseDetail.getAsgTms().toString().equals("0")) {
				fdsCaseDetail.setAsgTms(caseDetailRepo.getCurrentTime());
			}
			fdsCaseDetail.setUpdTms(caseDetailRepo.getCurrentTime());

			fdsCaseDetail.setUsrId(userid.toUpperCase());
			fdsCaseDetail.setCheckNew(" ");
			caseDetailRepo.save(fdsCaseDetail);
			return true;
		}
		return false;
	}

	@Override
	public void reopenCase(String caseno, String userid) {
		FdsPosCasesDetail fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		fdsCaseDetail.setCaseStatus(REOPENSTAT);
		fdsCaseDetail.setCheckNew("N");
		fdsCaseDetail.setUpdTms(caseDetailRepo.getCurrentTime());
		caseDetailRepo.save(fdsCaseDetail);
	}

	@Override
	public void updateAssignedUser(String caseno, String userid) {
		FdsPosCasesDetail fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		fdsCaseDetail.setUsrId(userid.toUpperCase());
		fdsCaseDetail.setAsgTms(caseDetailRepo.getCurrentTime());
		fdsCaseDetail.setCaseStatus(TRANSSTAT);
		fdsCaseDetail.setCheckNew(" ");
		caseDetailRepo.save(fdsCaseDetail);
	}

	/*huyennt adding 20170710*/
	@Override
	public boolean unAssignedCase(String caseno){
		FdsPosCasesDetail fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		fdsCaseDetail.setUsrId(" ");
		fdsCaseDetail.setAsgTms(BigDecimal.valueOf(0));
		fdsCaseDetail.setCaseStatus("NEW");
		fdsCaseDetail.setCheckNew("Y");
		try {
			caseDetailRepo.save(fdsCaseDetail);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public Page<FdsPosCasesDetail> search(String sRuleId, String sTID, String sMID, String sCASE, String sCardBrand,
			   String sPan, String sMCC, String sResultTxn, String sPosMode, String sFromDate, String sToDate, 
			   String sContentProc) {
		final String _fromdate = sFromDate;
		final String _todate = sToDate;
		StringBuilder partitionQuery = new StringBuilder(" AND CHECK_DT between " + sFromDate + " and " + sToDate);
		
		StringBuilder searchTerm = new StringBuilder();
		if (!"".equals(sRuleId)) {
			searchTerm.append(" AND HR.RULE_ID ='" + sRuleId + "'");
		}
		if (!"".equals(sTID)) {
			searchTerm.append(" AND PT.TID ='" + sTID + "'");
		}
		if (!"".equals(sMID)) {
			searchTerm.append(" AND PT.MID ='" + sMID + "'");
		}
		if (!"".equals(sCASE)) {
			searchTerm.append(" AND HR.CASE_NO = '" + sCASE + "'");
		}
		if (!"".equals(sCardBrand)) {
			searchTerm.append(" AND PT.LOAI_THE = '" + sCardBrand + "'");
		}
		if (!"".equals(sPan)) {
			searchTerm.append(" AND PT.SO_THE = '" + sPan + "'");
		}
		if (!"".equals(sMCC)) {
			searchTerm.append(" AND PT.MCC = '" + sMCC + "'");
		}
		if (!"".equals(sResultTxn)) {
			searchTerm.append(" AND PT.KET_QUA_GD = '" + sResultTxn + "'");
		}
		if (!"".equals(sPosMode)) {
			searchTerm.append(" AND PT.POS_MODE = '" + sPosMode + "'");
		}
		if (!"".equals(sContentProc)) {
			searchTerm.append(" AND CD.CASE_STATUS = '" + sContentProc + "'");
		}
		
		if (!searchTerm.equals("")) {
			searchTerm = searchTerm.append(partitionQuery.toString());
		} else {
			searchTerm = partitionQuery;
		}
		return caseDetailRepo.searchCase(searchTerm.toString(), _fromdate, _todate);
	}

	@Override
	public boolean callBackCase(String caseno, String userid, BigDecimal fromdate, BigDecimal todate) {
		if (sysTaskRepo.countByCaseNo(caseno, fromdate) == 0) {

			FdsPosCasesDetail fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
			fdsCaseDetail.setUsrId(userid.toUpperCase());
			fdsCaseDetail.setAsgTms(caseDetailRepo.getCurrentTime());
			fdsCaseDetail.setCaseStatus(CALLBACKSTAT);
			fdsCaseDetail.setUpdTms(caseDetailRepo.getCurrentTime());
			fdsCaseDetail.setCheckNew(" ");

			FdsPosSysTask fdsSysTask = new FdsPosSysTask();
			fdsSysTask.setObjecttask(caseno);
			fdsSysTask.setTypetask(CALLBACKSTAT);
			fdsSysTask.setPriority(new BigDecimal("1"));
			fdsSysTask.setFromdate(fromdate);
			fdsSysTask.setContenttask(" ");
			fdsSysTask.setTodate(todate);
			fdsSysTask.setUserid(userid);

			caseDetailRepo.save(fdsCaseDetail);
			sysTaskRepo.save(fdsSysTask);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int countAllClosedCase() {
		return caseDetailRepo.countByCaseStatusInAnd45Day();
	}

	@Override
	@Cacheable(value = "FdsCaseDetail.getDed2", key = "#cardno")
	public String getDed2(String cardno) {
		return caseDetailRepo.getDed2(cardno);
	}

	@Override
	public List<Object[]> findTransactionDetailByCaseNo(String tid, int numberofdate) {
		if (numberofdate != -1) {
			return caseDetailRepo.findTransactionByCase(tid, numberofdate);
		} else{
			return caseDetailRepo.findAllTransactionByCase(tid);
		}
	}
	
	@Override
	public List<Object[]> findTransactionDetailByTidOrMid(String tid,String mid, int numberofdate) {
		if (numberofdate != -1) {
			return caseDetailRepo.findTransactionByTidOrMid(tid, mid, numberofdate);
		} else{
			return caseDetailRepo.findAllTransactionByTidOrMid(tid, mid);
		}
	}

	@Override
	public Page<FdsPosCasesDetail> findAllByStatus(Pageable page, String status) {
		return caseDetailRepo.findAllBycaseStatus(status, page);
	}

	@Override
	public void updateTaskCase() {
		BigDecimal currentTime = new BigDecimal(timeConverter.convertDateTimeToStr(getTimeAfter(0)));
		Iterable<FdsPosSysTask> resultCaseDetail = sysTaskRepo.findAllByTypeAndNotInCurrenttime(currentTime, "CAL");
		resultCaseDetail.forEach(s -> {
			sysTaskRepo.delete(s);
			caseDetailRepo.updateStatusCase(s.getObjecttask(), " ");
		});
	}

	/**
	 * Thoi gian hien tai + them so phut
	 * 
	 * @param amount
	 *            So phut can them
	 * @return Date
	 */
	private Date getTimeAfter(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, amount);
		return calendar.getTime();
	}

	@Override
	public List<Object[]> findCaseDetailByCaseno(String caseno) {
		return caseDetailRepo.findCaseDetailByCaseno(caseno);
	}

	// Reports
	/*@Override
	public List<Object[]> reportCaseByUser(String fromdate, String todate, String userid, String closed_reason, String crdbrn) {
		return caseDetailRepo.reportCaseByUser(fromdate, todate, userid, closed_reason, crdbrn);
	}

	@Override
	public List<Object[]> reportAllCase(String fromdate, String todate) {
		return caseDetailRepo.reportAllCase(fromdate, todate);
	}

	@Override
	public List<Object[]> reportCaseByTxn(String fromdate, String todate, String crdbrn) {
		return caseDetailRepo.reportCaseByTxn(fromdate, todate, crdbrn);
	}

	@Override
	public List<Object[]> reportCaseByStatus(String fromdate, String todate, String crdbrn, String userid, String status) {
		return caseDetailRepo.reportCaseByStatus(fromdate, todate, crdbrn, userid, status);

	}

	@Override
	public List<Object[]> reportRuleId(String fromdate, String todate, String ruleid) {
		return caseDetailRepo.reportRuleId(fromdate, todate, ruleid);

	}*/

	@Override
	public List<Object[]> reportMerchant(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc) {
		return null;
	}

	@Override
	public List<Object[]> reportTxnCrdDet(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc) {
		String sFromdate = "";
		String sTodate = "";
		String sMerchant = "";
		String sTerminalId = "";
		String sCardNo = "";
		String sMcc = "";

		if (StringUtils.hasText(fromdate)) {
			sFromdate = fromdate;
		}
		if (StringUtils.hasText(todate)) {
			sTodate = todate;
		}
		if (StringUtils.hasText(merchant)) {
			sMerchant = merchant;
		}
		if (StringUtils.hasText(terminalId)) {
			sTerminalId = terminalId;
		}
		if (StringUtils.hasText(cardNo)) {
			sCardNo = cardNo;
		}
		if (StringUtils.hasText(mcc)) {
			sMcc = mcc;
		}

		//return caseDetailRepo.reportTxnCrdDet(sFromdate, sTodate, sMerchant, sTerminalId, sCardNo, sMcc);
		return null;
	}

	@Override
	public List<String> getListTxnIdByCaseNo(String caseno) {
		return caseDetailRepo.getListTxnIdByCaseNo(caseno);
	}

	@Override
	public List<Object[]> getListTxnDetail(String listTxnId, String caseno) {
		// TODO Auto-generated method stub
		return caseDetailRepo.getListTxnIdByCaseNo(listTxnId, caseno);
	}

	@Override
	public Page<FdsPosCasesDetail> searchCase(String sFromDate, String sToDate, String sMID, String sTID, String sTenMid, String sRuleId,
			String sDvql, String sCaseNo, String sContentProc) {
		// TODO Auto-generated method stub
		final String _fromdate = sFromDate;
		final String _todate = sToDate;
		StringBuilder partitionQuery = new StringBuilder(" AND CHECK_DT between " + sFromDate + " and " + sToDate);
		
		StringBuilder searchTerm = new StringBuilder();
		if (!"".equals(sMID)) {
			searchTerm.append(" AND PT.MID ='" + sMID + "'");
		}
		if (!"".equals(sTID)) {
			searchTerm.append(" AND PT.TID ='" + sTID + "'");
		}
		if (!"".equals(sTenMid)) {
			searchTerm.append(" AND PT.TEN_MID LIKE '%" + sTenMid + "%'");
		}
		if (!"".equals(sRuleId)) {
			searchTerm.append(" AND HR.RULE_ID ='" + sRuleId + "'");
		}
		if (!"".equals(sCaseNo)) {
			searchTerm.append(" AND HR.CASE_NO = '" + sCaseNo + "'");
		}
		if (!"".equals(sContentProc)) {
			searchTerm.append(" AND CS.CLOSED_REASON IN (:sContentProc)" ) ;
		}
		
		if (!searchTerm.equals("")) {
			searchTerm = searchTerm.append(partitionQuery.toString());
		} else {
			searchTerm = partitionQuery;
		}
		return caseDetailRepo.searchCasePos(searchTerm.toString(), _fromdate, _todate);
	}

}
