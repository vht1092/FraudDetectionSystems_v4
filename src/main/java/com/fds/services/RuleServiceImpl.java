package com.fds.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsPosRule;
import com.fds.repositories.RuleRepo;

@Service("ruleService")
public class RuleServiceImpl implements RuleService {

	@Autowired
	private RuleRepo ruleRepo;

	@Override
	public List<FdsPosRule> findAll() {
		return ruleRepo.findAll(new Sort(Sort.Direction.ASC, "rulePriority"));
	}

	/*@Override
	@Cacheable(cacheNames = "FdsRule.findByCaseNo", key = "#caseno")
	public List<FdsPosRule> findByCaseNo(String caseno) {
		return ruleRepo.findAllByCaseNo(caseno);
	}*/

	@CacheEvict(cacheNames = {"FdsRule.findColorByCaseNo"}, allEntries = true)
	public void updateByRuleid(String id, String color) {
		FdsPosRule fdsRuleList = ruleRepo.findOneByruleId(id);
		fdsRuleList.setRuleLevel(color);
		ruleRepo.save(fdsRuleList);
	}

	@Override
	@Cacheable(cacheNames = "FdsRule.findColorByCaseNo", key = "#caseno")
	public String findColorByCaseNo(String caseno) {
		return ruleRepo.findColorByCaseNo(caseno);
	}

	@Override
	@Cacheable(cacheNames = "FdsRule.findRuleByRuleId", key = "#ruleId")
	public FdsPosRule findRuleByRuleId(String ruleId) {
		return ruleRepo.findOneByruleId(ruleId);
	}

	@Override
	public List<FdsPosRule> findByRuleNoneFinance() {
		return ruleRepo.findByRuleNoneFinance();
	}

	@Override
	public List<FdsPosRule> findByRuleVietinBank() {
		// TODO Auto-generated method stub
		return ruleRepo.findByRuleVietinBank();
	}

}
