package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FDS_TXN_DETAIL database table.
 * 
 */
@Entity
@Table(name="FDS_POS_TXN_V2")
@NamedQuery(name="FdsPosTxnV2.findAll", query="SELECT f FROM FdsPosTxnV2 f")
public class FdsPosTxnV2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MA_GD", unique = true, nullable = false, length = 15)
	private String maGd;
	
	@Column(name = "MID", nullable = false, length = 16)
	private String mid;
	
	@Column(name = "TEN_MID", nullable = false, length = 50)
	private String tenMid;
	
	@Column(name = "TID", nullable = false, length = 10)
	private String tid = "";
	
	@Column(name = "SO_HD", nullable = false, length = 16)
	private String soHd;
	
	@Column(name = "DIA_CHI_GD", nullable = false, length = 100)
	private String diaChiGd;
	
	@Column(name = "NGAY_TAO_GD", nullable=false, precision=16)
	private BigDecimal ngayTaoGd;
	
	@Column(name = "NGAY_GIO_GD", nullable = false, length = 50)
	private String ngayGioGd;
	
	@Column(name = "SO_HOA_DON", nullable = false, length = 8)
	private String soHoaDon;
	
	@Column(name = "MA_CHUAN_CHI", nullable = false, length = 10)
	private String maChuanChi;
	
	@Column(name = "SO_TIEN_GD_GOC", nullable=false, precision=18, scale=2)
	private BigDecimal soTienGdGoc;
	
	@Column(name = "LOAI_TIEN", nullable = false, length = 5)
	private String loaiTien;
	
	@Column(name = "SO_TIEN_TIP", nullable=false, precision=18, scale=2)
	private BigDecimal soTienTip;
	
	@Column(name = "SO_BIN", nullable = false, length = 6)
	private String soBin;
	
	@Column(name = "SO_THE", nullable = false, length = 19)
	private String soThe;
	
	@Column(name = "LOAI_THE", nullable = false, length = 10)
	private String loaiThe;
	
	@Column(name = "KET_QUA_GD", nullable = false, length = 20)
	private String ketQuaGd;
	
	@Column(name = "DAO_HUY", nullable = false, length = 1)
	private String daoHuy;
	
	@Column(name = "POS_MDE_2DIGIT", nullable = false, length = 2)
	private String posMde2digit;
	
	@Column(name = "POS_MODE", nullable = false, length = 5)
	private String posMode;
	
	@Column(name = "MA_LOI", nullable = false, length = 200)
	private String maLoi;
	
	@Column(name = "BAO_CO", nullable = false, length = 10)
	private String baoCo;
	
	@Column(name = "NGAY_GD", nullable=false, precision=8)
	private BigDecimal ngayGd;
	
	@Column(name = "SCB_CHKS_STAT", nullable = false, length = 1)
	private String scbChksStat = " ";
	
	@Column(name = "VTB_CHKS_STAT", nullable = false, length = 1)
	private String vtbChksStat = " ";
	
	@Column(name = "EIB_CHKS_STAT", nullable = false, length = 1)
	private String eibChksStat = " ";
	
	@Column(name = "MCC", nullable=false, precision=4)
	private BigDecimal mcc;
	
	@Column(name = "ON_OFFLINE", nullable = false, length = 10)
	private String onOffline;
	
	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "CRE_TMS", nullable = false, precision = 17)
	private BigDecimal creTms;
	
	
	public FdsPosTxnV2() {
	}

	/**
	 * @return the maGd
	 */
	public String getMaGd() {
		return maGd;
	}

	/**
	 * @param maGd the maGd to set
	 */
	public void setMaGd(String maGd) {
		this.maGd = maGd;
	}

	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}

	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}

	/**
	 * @return the tenMid
	 */
	public String getTenMid() {
		return tenMid;
	}

	/**
	 * @param tenMid the tenMid to set
	 */
	public void setTenMid(String tenMid) {
		this.tenMid = tenMid;
	}

	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the soHd
	 */
	public String getSoHd() {
		return soHd;
	}

	/**
	 * @param soHd the soHd to set
	 */
	public void setSoHd(String soHd) {
		this.soHd = soHd;
	}

	/**
	 * @return the diaChiGd
	 */
	public String getDiaChiGd() {
		return diaChiGd;
	}

	/**
	 * @param diaChiGd the diaChiGd to set
	 */
	public void setDiaChiGd(String diaChiGd) {
		this.diaChiGd = diaChiGd;
	}

	/**
	 * @return the ngayTaoGd
	 */
	public BigDecimal getNgayTaoGd() {
		return ngayTaoGd;
	}

	/**
	 * @param ngayTaoGd the ngayTaoGd to set
	 */
	public void setNgayTaoGd(BigDecimal ngayTaoGd) {
		this.ngayTaoGd = ngayTaoGd;
	}

	/**
	 * @return the ngayGioGd
	 */
	public String getNgayGioGd() {
		return ngayGioGd;
	}

	/**
	 * @param ngayGioGd the ngayGioGd to set
	 */
	public void setNgayGioGd(String ngayGioGd) {
		this.ngayGioGd = ngayGioGd;
	}

	/**
	 * @return the soHoaDon
	 */
	public String getSoHoaDon() {
		return soHoaDon;
	}

	/**
	 * @param soHoaDon the soHoaDon to set
	 */
	public void setSoHoaDon(String soHoaDon) {
		this.soHoaDon = soHoaDon;
	}

	/**
	 * @return the maChuanChi
	 */
	public String getMaChuanChi() {
		return maChuanChi;
	}

	/**
	 * @param maChuanChi the maChuanChi to set
	 */
	public void setMaChuanChi(String maChuanChi) {
		this.maChuanChi = maChuanChi;
	}

	/**
	 * @return the soTienGdGoc
	 */
	public BigDecimal getSoTienGdGoc() {
		return soTienGdGoc;
	}

	/**
	 * @param soTienGdGoc the soTienGdGoc to set
	 */
	public void setSoTienGdGoc(BigDecimal soTienGdGoc) {
		this.soTienGdGoc = soTienGdGoc;
	}

	/**
	 * @return the loaiTien
	 */
	public String getLoaiTien() {
		return loaiTien;
	}

	/**
	 * @param loaiTien the loaiTien to set
	 */
	public void setLoaiTien(String loaiTien) {
		this.loaiTien = loaiTien;
	}

	/**
	 * @return the soTienTip
	 */
	public BigDecimal getSoTienTip() {
		return soTienTip;
	}

	/**
	 * @param soTienTip the soTienTip to set
	 */
	public void setSoTienTip(BigDecimal soTienTip) {
		this.soTienTip = soTienTip;
	}

	/**
	 * @return the soBin
	 */
	public String getSoBin() {
		return soBin;
	}

	/**
	 * @param soBin the soBin to set
	 */
	public void setSoBin(String soBin) {
		this.soBin = soBin;
	}

	/**
	 * @return the soThe
	 */
	public String getSoThe() {
		return soThe;
	}

	/**
	 * @param soThe the soThe to set
	 */
	public void setSoThe(String soThe) {
		this.soThe = soThe;
	}

	/**
	 * @return the laoiThe
	 */
	public String getLoaiThe() {
		return loaiThe;
	}

	/**
	 * @param laoiThe the laoiThe to set
	 */
	public void setLoaiThe(String loaiThe) {
		this.loaiThe = loaiThe;
	}

	/**
	 * @return the ketQuaGd
	 */
	public String getKetQuaGd() {
		return ketQuaGd;
	}

	/**
	 * @param ketQuaGd the ketQuaGd to set
	 */
	public void setKetQuaGd(String ketQuaGd) {
		this.ketQuaGd = ketQuaGd;
	}

	/**
	 * @return the daoHuy
	 */
	public String getDaoHuy() {
		return daoHuy;
	}

	/**
	 * @param daoHuy the daoHuy to set
	 */
	public void setDaoHuy(String daoHuy) {
		this.daoHuy = daoHuy;
	}

	/**
	 * @return the posMde2digit
	 */
	public String getPosMde2digit() {
		return posMde2digit;
	}

	/**
	 * @param posMde2digit the posMde2digit to set
	 */
	public void setPosMde2digit(String posMde2digit) {
		this.posMde2digit = posMde2digit;
	}

	/**
	 * @return the posMode
	 */
	public String getPosMode() {
		return posMode;
	}

	/**
	 * @param posMode the posMode to set
	 */
	public void setPosMode(String posMode) {
		this.posMode = posMode;
	}

	/**
	 * @return the maLoi
	 */
	public String getMaLoi() {
		return maLoi;
	}

	/**
	 * @param maLoi the maLoi to set
	 */
	public void setMaLoi(String maLoi) {
		this.maLoi = maLoi;
	}

	/**
	 * @return the baoCo
	 */
	public String getBaoCo() {
		return baoCo;
	}

	/**
	 * @param baoCo the baoCo to set
	 */
	public void setBaoCo(String baoCo) {
		this.baoCo = baoCo;
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

	/**
	 * @return the scbChksStat
	 */
	public String getScbChksStat() {
		return scbChksStat;
	}

	/**
	 * @param scbChksStat the scbChksStat to set
	 */
	public void setScbChksStat(String scbChksStat) {
		this.scbChksStat = scbChksStat;
	}

	/**
	 * @return the vtbChksStat
	 */
	public String getVtbChksStat() {
		return vtbChksStat;
	}

	/**
	 * @param vtbChksStat the vtbChksStat to set
	 */
	public void setVtbChksStat(String vtbChksStat) {
		this.vtbChksStat = vtbChksStat;
	}

	/**
	 * @return the mcc
	 */
	public BigDecimal getMcc() {
		return mcc;
	}

	/**
	 * @param mcc the mcc to set
	 */
	public void setMcc(BigDecimal mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the onOffline
	 */
	public String getOnOffline() {
		return onOffline;
	}

	/**
	 * @param onOffline the onOffline to set
	 */
	public void setOnOffline(String onOffline) {
		this.onOffline = onOffline;
	}

	/**
	 * @return the usrId
	 */
	public String getUsrId() {
		return usrId;
	}

	/**
	 * @param usrId the usrId to set
	 */
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	/**
	 * @return the creTms
	 */
	public BigDecimal getCreTms() {
		return creTms;
	}

	/**
	 * @param creTms the creTms to set
	 */
	public void setCreTms(BigDecimal creTms) {
		this.creTms = creTms;
	}

	/**
	 * @return the eibChksStat
	 */
	public String getEibChksStat() {
		return eibChksStat;
	}

	/**
	 * @param eibChksStat the eibChksStat to set
	 */
	public void setEibChksStat(String eibChksStat) {
		this.eibChksStat = eibChksStat;
	}



}