package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.RuleService;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinServlet;

public class ReportDS extends ReportForm {
	private static final long serialVersionUID = -4877706024903750843L;
	public static final String CAPTION = "BC CHENH LECH DS DVCNT";

	public ReportDS() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final RuleService ruleService = (RuleService) helper.getBean("ruleService");
		cbboxRuleId.setVisible(true);
		ruleService.findByRuleVietinBank().forEach(result -> {
			cbboxRuleId.addItems(result.getRuleId());
		});
		
		tfTid.setVisible(true);
		tfTid.setValidationVisible(false);
		tfTid.setImmediate(true);
		
		tfMid.setVisible(true);
		tfMid.setValidationVisible(false);
		tfMid.setImmediate(true);
		
		super.filename = "AAAA.jasper";
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
			
			String sTid = tfTid.getValue();
			String sMid = tfMid.getValue();
			
			if (sTid != null && !sTid.trim().equals("")) {
				parameters.put("p_tid", sTid);
			} else {
				parameters.put("p_tid", null);
			}
			
			if (sMid != null && !sMid.trim().equals("")) {
				parameters.put("p_mid", sMid);
			} else {
				parameters.put("p_mid", null);
			}
			
			return parameters;
		}
		
		return null;
	}
}
