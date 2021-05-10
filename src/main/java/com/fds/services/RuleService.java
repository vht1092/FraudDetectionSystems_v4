package com.fds.services;

import java.util.List;

import com.fds.entities.FdsPosRule;

public interface RuleService {
	List<FdsPosRule> findAll();

	//List<FdsPosRule> findByCaseNo(String id);

	void updateByRuleid(String id, String color);

	String findColorByCaseNo(String caseno);
	
	FdsPosRule findRuleByRuleId(String ruleId);
	
	List<FdsPosRule> findByRuleNoneFinance();
	
	List<FdsPosRule> findByRuleVietinBank();

}
