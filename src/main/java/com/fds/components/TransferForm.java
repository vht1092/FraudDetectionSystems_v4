package com.fds.components;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.entities.FdsPosSysUser;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.fds.services.SysUserService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Form chuyen case cho user khac
 * 
 */
@SpringComponent
@Scope("prototype")
public class TransferForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final String STATUS = "TRA";
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferForm.class);

	public TransferForm(final Callback callback, final String caseno, final String cardnumber) {
		final String sCardNumber = cardnumber;
		final String sCaseNo = caseno;
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final CaseDetailService caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		final CaseStatusService caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		final SysUserService sysUserService = (SysUserService) helper.getBean("sysUserService");

		final String sUserId = SecurityUtils.getUserId();
		final CaseCommentForm caseCommentForm = new CaseCommentForm(sCardNumber, sCaseNo);

		setSpacing(true);
		setMargin(true);

		final Button btSave = new Button("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);

		final Button btBack = new Button("Đóng");
		btBack.setStyleName(ValoTheme.BUTTON_QUIET);
		btBack.setIcon(FontAwesome.CLOSE);
		btBack.addClickListener(evt -> {
			callback.closeWindow();
		});

		final ComboBox cbboxUser = new ComboBox();
		cbboxUser.setNullSelectionAllowed(false);

		btSave.addClickListener(evt -> {
			// Trong databse cot comment khong cho null nen se gan gia tri rong neu usr khong nhap gi het
			final String sComment = "".equals(caseCommentForm.getComment()) ? " " : caseCommentForm.getComment();
			if (cbboxUser.getValue() != null) {
				try {
					caseStatusService.create(sCaseNo, sComment, "", STATUS, cbboxUser.getValue().toString(), sUserId);
					caseDetailService.updateAssignedUser(sCaseNo, cbboxUser.getValue().toString());
					callback.closeWindow();
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			} 
		});

		final GridLayout gridLayout = new GridLayout();
		gridLayout.setColumns(3);
		gridLayout.setRows(1);
		gridLayout.setSpacing(true);

		final List<FdsPosSysUser> listUser = sysUserService.findAllUserByActiveflagIsTrue();
		for (final FdsPosSysUser s : listUser) {
			cbboxUser.addItem(s.getUserid());
		}

		gridLayout.addComponent(cbboxUser, 0, 0);
		gridLayout.addComponent(btSave, 1, 0);
		gridLayout.addComponent(btBack, 2, 0);

		addComponent(caseCommentForm);
		addComponent(gridLayout);
	}

	@FunctionalInterface
	public interface Callback {
		void closeWindow();
	}
}
