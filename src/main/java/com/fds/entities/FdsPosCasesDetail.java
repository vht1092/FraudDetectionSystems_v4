package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the FDS_POS_CASES_DETAIL database table.
 * 
 */
@Entity
@Table(name="FDS_POS_CASES_DETAIL")
@NamedQuery(name="FdsPosCasesDetail.findAll", query="SELECT f FROM FdsPosCasesDetail f")
public class FdsPosCasesDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CASE_NO", unique = true, nullable = false, length = 50)
	private String caseNo;
	
	@Column(name="ASG_TMS")
	private BigDecimal asgTms;

	@Column(name="CASE_STATUS")
	private String caseStatus;

	@Column(name="CHECK_DT")
	private BigDecimal checkDt;

	@Column(name="NGAY_GD")
	private BigDecimal ngayGd;
	
	@Column(name="CHECK_NEW")
	private String checkNew;

	private String mcc;

	private String mid;

	@Column(name="NAME_MID")
	private String nameMid;

	@Column(name="RULE_ID")
	private String ruleId;

	private String tid;

	@Column(name="UPD_TMS")
	private BigDecimal updTms;

	@Column(name="USR_ID")
	private String usrId;
	
	public FdsPosCasesDetail() {
	}

	public BigDecimal getAsgTms() {
		return this.asgTms;
	}

	public void setAsgTms(BigDecimal asgTms) {
		this.asgTms = asgTms;
	}

	public String getCaseNo() {
		return this.caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public BigDecimal getCheckDt() {
		return this.checkDt;
	}

	public void setCheckDt(BigDecimal checkDt) {
		this.checkDt = checkDt;
	}

	public String getCheckNew() {
		return this.checkNew;
	}

	public void setCheckNew(String checkNew) {
		this.checkNew = checkNew;
	}

	public String getMcc() {
		return this.mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMid() {
		return this.mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getNameMid() {
		return this.nameMid;
	}

	public void setNameMid(String nameMid) {
		this.nameMid = nameMid;
	}

	public String getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getTid() {
		return this.tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public BigDecimal getUpdTms() {
		return this.updTms;
	}

	public void setUpdTms(BigDecimal updTms) {
		this.updTms = updTms;
	}

	public String getUsrId() {
		return this.usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	/**
	 * @return the ngayGd
	 */
	public BigDecimal getNgayGd() {
		return ngayGd;
	}

	/**
	 * @param ngayGd the ngayGd to set
	 */
	public void setNgayGd(BigDecimal ngayGd) {
		this.ngayGd = ngayGd;
	}

	
}