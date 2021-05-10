package com.fds.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsPosCaseStatus;
import com.fds.repositories.CaseStatusRepo;

@Service("caseStatusService")
public class CaseStatusServiceImpl implements CaseStatusService {

	@Autowired
	private CaseStatusRepo caseStatusRepo;

	@Override
	public void create(String caseno, String comment, String closedreason, String action, String other, String userid) {

		FdsPosCaseStatus fdsCaseStatus = new FdsPosCaseStatus();
		fdsCaseStatus.setCaseNo(caseno);
		fdsCaseStatus.setClosedReason(closedreason);
		fdsCaseStatus.setCaseComment(comment);
		fdsCaseStatus.setOther(other.toUpperCase());
		fdsCaseStatus.setCreTms(caseStatusRepo.getCurrentTime());
		fdsCaseStatus.setUserId(userid.toUpperCase());
		fdsCaseStatus.setCaseAction(action);
		caseStatusRepo.save(fdsCaseStatus);
	}

	@Override
	public List<FdsPosCaseStatus> findAllByCaseNo(String caseno) {
		return caseStatusRepo.findAllByCaseNo(caseno, new PageRequest(0, 20, Sort.Direction.DESC, "creTms"));
	}

	@Override
	public String getCaseComment(String caseno) {
		return caseStatusRepo.getCaseComment(caseno);
	}
}