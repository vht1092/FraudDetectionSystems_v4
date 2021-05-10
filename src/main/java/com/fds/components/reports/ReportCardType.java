package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fds.TimeConverter;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ReportCardType extends ReportForm {
	private static final long serialVersionUID = -1L;
	public static final String CAPTION = "BC GD THEO LOẠI THẺ";
	
	public ReportCardType() {
		tfCardType.setVisible(true);
		tfCardType.setValidationVisible(false);
		tfCardType.setImmediate(true);
		
		super.filename = "ReportCardTypePos.jasper";
	}
	
	@Override
	public boolean checkValidator() {
		try {
			dffromDate.validate();
			dfToDate.validate();
			if (!StringUtils.hasText(tfCardType.getValue())) {
				Notification.show("Vui điền ít nhất một trong các tiêu chí", Type.WARNING_MESSAGE);
			}
			//Han che khoan thoi gian toi da la 30 ngay
			long diff = dfToDate.getValue().getTime() - dffromDate.getValue().getTime();
			System.out.println("So ngay tao bao cao:" + diff);
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

			if (tfCardType.getValue() != null) {
				parameters.put("p_card_type", String.valueOf(tfCardType.getValue()));
			} else {
				parameters.put("p_card_type", null);
			}
			return parameters;
		}
		return null;
	}
}
