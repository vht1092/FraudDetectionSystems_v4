package com.fds.components;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.components.MonitorForm.Callback;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.fds.services.DescriptionService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

/**
 * <pre>
 * Form ket thuc case, cap nhat trang thai <b>DIC</b> cho case tai bang FDS_CASE_DETAIL 
 * 
 * Cac tac dong boi user se them vao bang FDS_CASE_STATUS 
 * <b>CON</b>: Da goi ra
 * <b>SEM</b>: Da gui email
 * <b>TRA</b>: Da chuyen xac nhan
 * <b>BLC</b>: Da khoa the 
 * <b>CHE</b>: Da kiem tra
 * </pre>
 * 
 * @see CaseDetail
 */

@SpringComponent
@Scope("prototype")
public class DifferenceForm extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static final String STATUS = "DIF";

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscardForm.class);

	public DifferenceForm(final Callback callback, final String caseno, final String cardnumber, final ArrayList<String> arCaseNo) {
		super();
		final String sCardnumber = cardnumber;
		final String sCaseno = caseno;
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final CaseDetailService caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		final CaseStatusService caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		
		final String userid = SecurityUtils.getUserId();
		setSpacing(true);
		setMargin(true);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.setColumns(2);
		gridLayout.setRows(2);
		gridLayout.setSpacing(true);
		gridLayout.setSizeFull();

		final CaseCommentForm caseStatUpdateComp = new CaseCommentForm(sCardnumber, sCaseno);

		final Button btSave = new Button("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);

		btSave.addClickListener(evt -> {
			final String sComment = caseStatUpdateComp.getComment();
			if (!"".equals(sComment)) {
				try {
					if (caseDetailService.closeCase(sCaseno, userid, STATUS)) {
						caseStatusService.create(sCaseno, sComment, "", STATUS, "", userid);
					}
					// Dong tat ca cac case duoc chon trong lich su giao dich
					if (arCaseNo != null && !arCaseNo.isEmpty()) {
						for (int i = 0; i < arCaseNo.size(); i++) {
							if (arCaseNo.get(i) != null && !"".equals(arCaseNo.get(i))) {
								if (caseDetailService.closeCase(arCaseNo.get(i), userid, STATUS)) {
									caseStatusService.create(arCaseNo.get(i), sComment, "", STATUS, "", userid);
								}
							}
						}
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage() + " - User: "+userid+" cap nhat case loi - " + "Caseno: " + sCaseno);
					Notification.show("Lỗi", "Thao tác thực hiện không thành công", Type.ERROR_MESSAGE);
				}
				callback.closeWindow();
			} else {
				Notification.show("Vui lòng chọn thao tác xử lý", Type.WARNING_MESSAGE);
			}
		});

		final Button btBack = new Button("Đóng");
		btBack.setStyleName(ValoTheme.BUTTON_QUIET);
		btBack.setIcon(FontAwesome.CLOSE);
		btBack.addClickListener(evt -> {
			callback.closeWindow();
		});
		//gridLayout.addComponent(optionGroup, 0, 0, 1, 0);
		final HorizontalLayout horizalLayout = new HorizontalLayout();
		horizalLayout.addComponent(btSave);
		horizalLayout.addComponent(btBack);
		horizalLayout.setSpacing(true);
		gridLayout.addComponent(horizalLayout, 0, 1);

		addComponent(caseStatUpdateComp);
		addComponent(gridLayout);
	}

	@FunctionalInterface
	public interface Callback {
		void closeWindow();
	}
}
