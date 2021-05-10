package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.fds.TimeConverter;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringComponent
@Scope("prototype")
public class ReportMID extends ReportForm {
	private static final long serialVersionUID = -1L;
	public static final String CAPTION = "BC GD THEO MID";
	
	public ReportMID() {
		tfMid.setVisible(true);
		tfMid.setValidationVisible(false);
		tfMid.setImmediate(true);
		
		super.filename = "ReportMIDPos.jasper";
	}
	
	@Override
	public boolean checkValidator() {
		try {
			dffromDate.validate();
			dfToDate.validate();
			if (!StringUtils.hasText(tfMid.getValue())) {
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

			if (tfMid.getValue() != null) {
				parameters.put("p_mid", String.valueOf(tfMid.getValue()));
			} else {
				parameters.put("p_mid", null);
			}
			return parameters;
		}
		return null;
	}
}
