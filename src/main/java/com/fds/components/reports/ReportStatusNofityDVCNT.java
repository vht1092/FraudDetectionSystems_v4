package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.RuleService;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinServlet;

public class ReportStatusNofityDVCNT extends ReportForm {
	private static final long serialVersionUID = -4877706024903750843L;
	public static final String CAPTION = "BC TINH TRANG CANH BAO GD ĐVCNT";
	
	public ReportStatusNofityDVCNT() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final RuleService ruleService = (RuleService) helper.getBean("ruleService");
		cbboxRuleId.setVisible(true);
		ruleService.findByRuleNoneFinance().forEach(result -> {
			cbboxRuleId.addItems(result.getRuleId());
		});
		
		tfTid.setVisible(true);
		tfTid.setValidationVisible(false);
		tfTid.setImmediate(true);
		
		tfMid.setVisible(true);
		tfMid.setValidationVisible(false);
		tfMid.setImmediate(true);
		
		tfCase.setVisible(true);
		tfCase.setValidationVisible(false);
		tfCase.setImmediate(true);
		
		tfCardType.setVisible(true);
		tfCardType.setValidationVisible(false);
		tfCardType.setImmediate(true);
		
		tfPan.setVisible(true);
		tfPan.setValidationVisible(false);
		tfPan.setImmediate(true);
		
		tfMcc.setVisible(true);
		tfMcc.setValidationVisible(false);
		tfMcc.setImmediate(true);
		
		tfResultTxn.setVisible(true);
		tfResultTxn.setValidationVisible(false);
		tfResultTxn.setImmediate(true);
				
		tfPosMode.setVisible(true);
		tfPosMode.setValidationVisible(false);
		tfPosMode.setImmediate(true);
		
		cbboxOnOffline.setVisible(true);
		
		tfContent.setVisible(true);
		tfContent.setValidationVisible(true);
		tfContent.setImmediate(true);
		
		super.filename = "ReportStatusNotifyDVCNT.jasper";
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
			
			System.out.println("From " + timeConverter.convertDate(dffromDate.getValue(), false));
			System.out.println("To " + timeConverter.convertDate(dfToDate.getValue(), true));
			
			Object ruleId = cbboxRuleId.getValue();
			String sTid = tfTid.getValue();
			String sMid = tfMid.getValue();
			String sCase = tfCase.getValue();
			String sCardType = tfCardType.getValue();
			String sPan = tfPan.getValue();
			String sMcc = tfMcc.getValue();
			String sResultTxn = tfResultTxn.getValue();
			String sPosMode = tfPosMode.getValue();
			Object onOffline = cbboxOnOffline.getValue();
			String sContent = tfContent.getValue();
			
			if (ruleId != null) {
				parameters.put("p_ruleID", ruleId.toString());
			} else {
				parameters.put("p_ruleID", null);
			}
			
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
			
			if (sCase != null && !sCase.trim().equals("")) {
				parameters.put("p_caseID", sCase);
			} else {
				parameters.put("p_caseID", null);
			}
			
			if (sCardType != null && !sCardType.trim().equals("")) {
				parameters.put("p_loaiThe", sCardType);
			} else {
				parameters.put("p_loaiThe", null);
			}
			
			if (sPan != null && !sPan.trim().equals("")) {
				parameters.put("p_soThe", sPan);
			} else {
				parameters.put("p_soThe", null);
			}
			
			if (sMcc != null && !sMcc.trim().equals("")) {
				parameters.put("p_mcc", sMcc);
			} else {
				parameters.put("p_mcc", null);
			}
			
			if (sResultTxn != null && !sResultTxn.trim().equals("")) {
				parameters.put("p_ketQuaGD", sResultTxn);
			} else {
				parameters.put("p_ketQuaGD", null);
			}
			
			if (sPosMode != null && !sPosMode.trim().equals("")) {
				parameters.put("p_posMode", sPosMode);
			} else {
				parameters.put("p_posMode", null);
			}
			
			if (sContent != null && !sContent.trim().equals("")) {
				parameters.put("p_caseComment", sContent);
			} else {
				parameters.put("p_caseComment", null);
			}
						
			if (onOffline != null) {
				parameters.put("p_onOffline", onOffline.toString());
			} else {
				parameters.put("p_onOffline", null);
			}
			
			return parameters;
		}
		return null;
	}
}
