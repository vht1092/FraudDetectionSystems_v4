package com.fds.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.services.CaseStatusService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Form them noi dung xu ly
 * 
 * @see CaseDetail
 * 
 */
@SpringComponent
@Scope("prototype")
public class AddCommentForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AddCommentForm.class);	
	private transient String caseno = "";
	private transient String cardnumber = "";
	private static final String STATUS = "ACO";

	public AddCommentForm(final Callback callback, final String caseno, final String cardnumber) {
		
		this.cardnumber = cardnumber;
		this.caseno = caseno;

		final SpringContextHelper ctxHelper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final CaseStatusService caseStatusService = (CaseStatusService) ctxHelper.getBean("caseStatusService");

		final String sUserid = SecurityUtils.getUserId();
		setSpacing(true);
		setMargin(true);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.setColumns(2);
		gridLayout.setRows(2);
		gridLayout.setSpacing(true);

		final CaseCommentForm caseCommentForm = new CaseCommentForm(this.cardnumber, this.caseno);
		caseCommentForm.txtareaComment.setVisible(true);
		
		final Button butSave = new Button("Lưu");
		butSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		butSave.setIcon(FontAwesome.SAVE);
		butSave.addClickListener(evt -> {
			final String sComment = caseCommentForm.getComment();
			if (!"".equals(sComment)) {
				try {
					caseStatusService.create(this.caseno, sComment, "", STATUS, "", sUserid);
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
					Notification.show("Lỗi ứng dụng"+e.getMessage(), Type.ERROR_MESSAGE);
				}
				callback.closeWindow();
			} else {
				Notification.show("Vui lòng nhập nội dung", Type.WARNING_MESSAGE);
			}

		});
		final Button butBack = new Button("Đóng");
		butBack.setIcon(FontAwesome.CLOSE);
		butBack.setStyleName(ValoTheme.BUTTON_QUIET);
		butBack.addClickListener(evt -> {
			callback.closeWindow();
		});

		gridLayout.addComponent(butSave, 0, 1);
		gridLayout.addComponent(butBack, 1, 1);

		addComponent(caseCommentForm);
		addComponent(gridLayout);
	}

	public void setCaseNo(final String caseno) {
		this.caseno = caseno;
	}

	public void setCardnumber(final String cardnumber) {
		this.cardnumber = cardnumber;
	}

	@FunctionalInterface
	public interface Callback {
		void closeWindow();
	}

}
