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
public class ReportCase extends ReportForm {
	private static final long serialVersionUID = -1L;
	public static final String CAPTION = "BC GD THEO CASE";
	
	public ReportCase() {
		tfCase.setVisible(true);
		tfCase.setValidationVisible(false);
		tfCase.setImmediate(true);
		
		super.filename = "RepostCasePos.jasper";
	}
	
	@Override
	public boolean checkValidator() {
		try {
			dffromDate.validate();
			dfToDate.validate();
			if (!StringUtils.hasText(tfCase.getValue())) {
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

			if (tfCase.getValue() != null) {
				parameters.put("p_case_no", String.valueOf(tfCase.getValue()));
			} else {
				parameters.put("p_case_no", null);
			}
			return parameters;
		}
		return null;
	}
}
