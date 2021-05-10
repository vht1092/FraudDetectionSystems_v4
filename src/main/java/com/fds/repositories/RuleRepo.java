package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fds.entities.FdsPosRule;

public interface RuleRepo extends JpaRepository<FdsPosRule, String> {

	FdsPosRule findOneByruleId(String ruleid);

	@Query(value = "select r.* from {h-schema}fds_pos_case_hit_rules t join {h-schema}fds_pos_rules r on t.rule_id=r.rule_id where t.case_no=:caseno order by r.rule_priority", nativeQuery = true)
	List<FdsPosRule> findAllByCaseNo(@Param("caseno") String caseno);

	@Query(value = "select rule_level from (select r.rule_level from {h-schema}fds_pos_rules r, {h-schema}fds_pos_case_hit_rules h where h.rule_id = r.rule_id and h.case_no = :caseno order by r.rule_priority asc) where rownum = 1", nativeQuery = true)
	String findColorByCaseNo(@Param("caseno") String caseno);
	
	@Query(value = "select r.* from fds_pos_rules r where RULE_ID not in ('RULE14', 'RULE15', 'RULE16', 'RULE17', 'RULE18') order by r.rule_priority", nativeQuery = true)
	List<FdsPosRule> findByRuleNoneFinance();
	
	@Query(value = "select r.* from fds_pos_rules r where RULE_ID in ('RULE14', 'RULE15', 'RULE16', 'RULE17', 'RULE18') order by r.rule_priority", nativeQuery = true)
	List<FdsPosRule> findByRuleVietinBank();

}
