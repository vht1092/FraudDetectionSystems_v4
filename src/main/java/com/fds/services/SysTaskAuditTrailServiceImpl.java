package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsPosSysTaskAuditTrail;
import com.fds.repositories.SysTaskAuditTrailRepo;
import com.fds.repositories.SysTaskRepo;

@Service("sysTaskAuditTrailService")
@Transactional
public class SysTaskAuditTrailServiceImpl implements SysTaskAuditTrailService {
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private SysTaskAuditTrailRepo sysTaskAuditTrailRepo;

	@Override
	public void save(BigDecimal fromdate, BigDecimal todate, 
			String content, String type, String userid, BigDecimal createDate,
			String userUpdate, String actionStatus, String typeOfBusiness, String mid, String tid) {
		
		FdsPosSysTaskAuditTrail fdsSysTaskAuditTrail = new FdsPosSysTaskAuditTrail();
		fdsSysTaskAuditTrail.setFromdate(fromdate);
		fdsSysTaskAuditTrail.setTodate(todate);
		fdsSysTaskAuditTrail.setContenttask(content);
		fdsSysTaskAuditTrail.setTypetask(type);
		fdsSysTaskAuditTrail.setUserid(userid);
		fdsSysTaskAuditTrail.setCreatedate(createDate);
		fdsSysTaskAuditTrail.setUserupdate(userUpdate);
		fdsSysTaskAuditTrail.setActionStatus(actionStatus);
		fdsSysTaskAuditTrail.setTypeOfBusiness(typeOfBusiness);
		fdsSysTaskAuditTrail.setMid(mid);
		fdsSysTaskAuditTrail.setTid(tid);
		
		sysTaskAuditTrailRepo.save(fdsSysTaskAuditTrail);
		
		
	}
	
	@Override
	public List<Object[]> getUserUpdate(String mid, String tid) {
		return sysTaskAuditTrailRepo.getUserUpdate(mid, tid);
	}

	
	
}
