package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

public interface SysTaskAuditTrailService {
	
	void save(BigDecimal fromdate,
			BigDecimal todate, String content,
			String type, String userid, BigDecimal createTime,
			String userUpdate, String actionStatus, String typeOfBusiness, String mid, String tid);
	
	/**
	 * 
	 * @param objectTask
	 * @return
	 */
	public List<Object[]> getUserUpdate(String mid, String tid);
}
