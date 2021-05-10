package com.fds.components;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.fds.services.DescriptionService;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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
public class DiscardForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private static final String STATUS = "DIC";

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscardForm.class);

	public DiscardForm(final Callback callback, final String caseno, final String cardnumber, final ArrayList<String> arCaseNo) {
		super();
		final String sCardnumber = cardnumber;
		final String sCaseno = caseno;
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final CaseDetailService caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		final CaseStatusService caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");

		final String userid = SecurityUtils.getUserId();
		setSpacing(true);
		setMargin(true);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.setColumns(2);
		gridLayout.setRows(2);
		gridLayout.setSpacing(true);
		gridLayout.setSizeFull();

		final CaseCommentForm caseStatUpdateComp = new CaseCommentForm(sCardnumber, sCaseno);
		
		final OptionGroup optionGroup = new OptionGroup("Các thao tác xử lý");
		optionGroup.setSizeFull();
//		optionGroup.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		Window confirmDialog;
		confirmDialog = new Window();
		TextField tfOther;
		
		confirmDialog.setCaption("Thêm nội dung khác");
		confirmDialog.setWidth(400.0f, Unit.PIXELS);
		final FormLayout content = new FormLayout();
        content.setMargin(true);
        tfOther = new TextField();
        tfOther.setSizeFull();
        tfOther.setWidth(350.0f, Unit.PIXELS);
        
        HorizontalLayout layoutBtn = new HorizontalLayout();
        layoutBtn.addComponents(tfOther);
        content.addComponent(layoutBtn);
        
        confirmDialog.setContent(content);

        // Center it in the browser window
        confirmDialog.center();
        confirmDialog.setResizable(false);
		
		this.addLayoutClickListener(event -> {
			if(UI.getCurrent().getWindows().contains(confirmDialog)) {
				confirmDialog.close();
			}
			
		});
		
		optionGroup.addValueChangeListener(evt -> {
			
			if(evt.getProperty().getValue().toString().contains("OTH")) {
                
               	UI.getCurrent().addWindow(confirmDialog);
               	
               	tfOther.addShortcutListener(new ShortcutListener("", KeyCode.ENTER, new int[10]) {

					@Override
					public void handleAction(Object sender, Object target) {
						optionGroup.setItemCaption(optionGroup.getValue(), tfOther.getValue());
						caseStatUpdateComp.setComment(optionGroup.getItemCaption(evt.getProperty().getValue()));
						
						confirmDialog.close();
						
	               		
					}
               		
               	});
			}
			
			caseStatUpdateComp.setComment(optionGroup.getItemCaption(evt.getProperty().getValue()));
			
		});

		descService.findAllByTypeByOrderBySequencenoAsc("ACTION").stream().filter(i->Integer.valueOf(i.getSequenceno())<100 || i.getSequenceno().equals("999")).forEach(item -> {
			optionGroup.addItem(item.getId());
			optionGroup.setItemCaption(item.getId(), item.getDescription());
		});
		
		final Button btSave = new Button("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);

		btSave.addClickListener(evt -> {
			final String sComment = caseStatUpdateComp.getComment();
			if (!"".equals(sComment)) {
				try {
					if (caseDetailService.closeCase(sCaseno, userid, STATUS)) {
						caseStatusService.create(sCaseno, sComment, optionGroup.getValue().toString(), STATUS, "", userid);
					}
					// Dong tat ca cac case duoc chon trong lich su giao dich
					if (arCaseNo != null && !arCaseNo.isEmpty()) {
						for (int i = 0; i < arCaseNo.size(); i++) {
							if (arCaseNo.get(i) != null && !"".equals(arCaseNo.get(i))) {
								if (caseDetailService.closeCase(arCaseNo.get(i), userid, STATUS)) {
									caseStatusService.create(arCaseNo.get(i), sComment, optionGroup.getValue().toString(), STATUS, "", userid);
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
		gridLayout.addComponent(optionGroup, 0, 0, 1, 0);
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
