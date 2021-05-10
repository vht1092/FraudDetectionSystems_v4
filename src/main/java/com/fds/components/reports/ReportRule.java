package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.RuleService;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
public class ReportRule extends ReportForm  {
	private static final long serialVersionUID = -1L;
	public static final String CAPTION = "BC THEO RULE";
	
	public ReportRule() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final RuleService ruleService = (RuleService) helper.getBean("ruleService");
		cbboxRuleId.setVisible(true);
		ruleService.findAll().forEach(result -> {
			cbboxRuleId.addItems(result.getRuleId());
		});
		
		super.filename = "ReportRulePos.jasper";
	}
	
	@Override
	public boolean checkValidator() {
		try {
			dffromDate.validate();
			dfToDate.validate();
			return true;
		} catch (InvalidValueException ex) {
			dffromDate.setValidationVisible(true);
			dfToDate.setValidationVisible(true);			
		}
		return false;
	}
	
	@Override
	public Map<String, Object> getParameter() {
		if (dffromDate.getValue() != null && dfToDate.getValue() != null) {
			final TimeConverter timeConverter = new TimeConverter();
			final Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("p_fromdate", timeConverter.convertDate(dffromDate.getValue(), false));
			parameters.put("p_todate", timeConverter.convertDate(dfToDate.getValue(), true));

			if (cbboxRuleId.getValue() != null) {
				parameters.put("p_ruleid", String.valueOf(cbboxRuleId.getValue()));
			} else {
				parameters.put("p_ruleid", null);
			}
			return parameters;
		}
		return null;
	}
	
}
