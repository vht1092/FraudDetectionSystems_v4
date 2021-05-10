package com.fds.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.fds.TimeConverter;
import com.fds.components.reports.ReportForm;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SpringComponent
@Scope("prototype")
public class ReportContent extends ReportForm {
	private static final long serialVersionUID = -1L;
	public static final String CAPTION = "BC GD THEO NỘI DUNG XỬ LÝ";
	
	public ReportContent() {
		tfContent.setVisible(true);
		tfContent.setValidationVisible(false);
		tfContent.setImmediate(true);
		
		super.filename = "ReportContentPos.jasper";
	}
	
	@Override
	public boolean checkValidator() {
		try {
			dffromDate.validate();
			dfToDate.validate();
			if (!StringUtils.hasText(tfContent.getValue())) {
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

			if (tfContent.getValue() != null) {
				parameters.put("p_content", String.valueOf(tfContent.getValue()));
			} else {
				parameters.put("p_content", null);
			}
			return parameters;
		}
		return null;
	}
}
