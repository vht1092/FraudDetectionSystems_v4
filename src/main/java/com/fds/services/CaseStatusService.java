package com.fds.services;

import java.util.List;

import com.fds.entities.FdsPosCaseStatus;

public interface CaseStatusService {
	void create(String caseno, String comment, String closedreason, String action, String other, String userid);
	List<FdsPosCaseStatus> findAllByCaseNo(String caseno);
	
	String getCaseComment(String caseno);
}
