package com.fds.components;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.CustomerInfo;
import com.fds.entities.FdsPosSysTask;
import com.fds.services.CustomerInfoService;
import com.fds.services.SysTaskService;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Tab ghi nhan yeu cau khong goi cho khach hang
 */

@SpringComponent
@Scope("prototype")
public class ExceptionCase extends CustomComponent {
	private static final long serialVersionUID = 4527061434376139210L;
	public static final String TYPE = "EXCEPTION";
	public static final String CAPTION = "GHI NHẬN YÊU CẦU KHÁCH HÀNG";
	// private final transient VerticalLayout mainLayout = new VerticalLayout();
	private final transient HorizontalLayout mainLayout = new HorizontalLayout();
	private final transient FormLayout formLayout = new FormLayout();
	private final transient TextField tfCif;
	private final transient Panel panelComment = new Panel("NỘI DUNG YÊU CẦU");
	private final transient Panel panelInfocust = new Panel("THÔNG TIN KHÁCH HÀNG");
	private final transient BeanItemContainer<CustomerInfo> container = new BeanItemContainer<CustomerInfo>(CustomerInfo.class);
	private final transient SysTaskService sysTaskService;
	private final transient CustomerInfoService custInfoService;
	private final transient Label lbInfocust;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCase.class);

	public ExceptionCase() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		custInfoService = (CustomerInfoService) helper.getBean("customerInfoService");
		sysTaskService = (SysTaskService) helper.getBean("sysTaskService");

		final VerticalLayout leftSide = new VerticalLayout();
		leftSide.setSpacing(true);
		leftSide.setSizeFull();

		final VerticalLayout rightSide = new VerticalLayout();
		rightSide.setSpacing(true);
		rightSide.setSizeFull();

		panelComment.setVisible(false);
		panelInfocust.setVisible(false);

		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		tfCif = new TextField("Số CIF");
		tfCif.addValidator(new StringLengthValidator("Vui lòng nhập số CIF", 1, 20, false));
		tfCif.setValidationVisible(false);
		lbInfocust = new Label();
		lbInfocust.setCaptionAsHtml(true);

		final Button btLoad = new Button("Lấy thông tin");
		btLoad.setIcon(FontAwesome.SEARCH);
		btLoad.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btLoad.addClickListener(evt -> {

			tfCif.setValidationVisible(false);
			try {
				tfCif.validate();
				cmdButton_GetDataInfoCust(tfCif.getValue().trim());

			} catch (InvalidValueException ex) {
				tfCif.setValidationVisible(true);
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage());
			}
		});

		final Grid grid = new Grid();
		grid.setColumns("cust_cif", "cust_name");
		grid.getColumn("cust_name").setHeaderCaption("Họ Tên Khách Hàng");
		grid.getColumn("cust_cif").setHeaderCaption("Số CIF");
		grid.setReadOnly(true);
		grid.setHeightByRows(60);
		grid.setSizeFull();
		grid.setContainerDataSource(container);
		grid.addItemClickListener(itemEvent -> {
			String cifno = String.valueOf(itemEvent.getItem().getItemProperty("cust_cif").getValue()).trim();
			tfCif.setValue(cifno);
			cmdButton_GetDataInfoCust(String.valueOf(itemEvent.getItem().getItemProperty("cust_cif").getValue()).trim());
		});

		formLayout.addComponent(tfCif);
		formLayout.addComponent(btLoad);

		leftSide.addComponent(formLayout);
		leftSide.addComponent(panelInfocust);
		leftSide.addComponent(panelComment);

		rightSide.addComponent(grid);

		mainLayout.addComponent(leftSide);
		mainLayout.addComponent(rightSide);

		setCompositionRoot(mainLayout);

		listData();
	}

	private void listData() {
		if (container.size() > 0) {
			container.removeAllItems();
		}
		container.addAll(custInfoService.findAllTypetask("EXCEPTION"));
	}

	private FormLayout buildCommentForm() {

		final FdsPosSysTask fdsSysTask = sysTaskService.findOneByObjectTaskAndTypeTask(tfCif.getValue().toString().trim(), TYPE);
		final TimeConverter timeConverter = new TimeConverter();
		final FormLayout sFormLayout = new FormLayout();
		sFormLayout.setSpacing(true);
		sFormLayout.setMargin(true);

		final DateField dfFromDate = new DateField("Từ ngày");
		dfFromDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
		dfFromDate.setResolution(Resolution.SECOND);

		final DateField dfToDate = new DateField("Đến ngày");
		dfToDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
		dfToDate.setResolution(Resolution.SECOND);

		final CheckBox chBoxToDate = new CheckBox("Vô thời hạn");
		chBoxToDate.addValueChangeListener(evt -> {
			if (!chBoxToDate.getValue()) {
				if (!dfFromDate.isVisible()) {
					dfFromDate.setVisible(true);
				}
				if (!dfToDate.isVisible()) {
					dfToDate.setVisible(true);
					dfToDate.setValue(new Date());
				}
			} else {
				if (dfFromDate.isVisible()) {
					dfFromDate.setVisible(false);
				}
				if (dfToDate.isVisible()) {
					dfToDate.setVisible(false);
				}
			}
		});

		final TextArea txtareaComment = new TextArea();
		txtareaComment.setSizeFull();

		// Button
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);

		// Button Luu
		final Button btSave = new Button("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);
		btSave.addClickListener(evt -> {
			final String sUserId = SecurityUtils.getUserId();
			if (!chBoxToDate.getValue()) {
				if (dfFromDate.getValue() != null && dfFromDate.getValue() != null && !"".equals(txtareaComment.getValue())) {
					final BigDecimal bigFromDate = new BigDecimal(timeConverter.convertDateTimeToStr(dfFromDate.getValue()));
					final BigDecimal bigToDate = new BigDecimal(timeConverter.convertDateTimeToStr(dfToDate.getValue()));
					try {
						sysTaskService.save(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId);
						Notification.show("Đã lưu", Type.WARNING_MESSAGE);
					} catch (Exception ex) {
						LOGGER.error(ex.getMessage());
						Notification.show("Lưu thông tin thất bại", Type.ERROR_MESSAGE);
					}

				} else {
					Notification.show("Vui lòng nhập thông tin", Type.WARNING_MESSAGE);
				}
			} else {
				if (!"".equals(txtareaComment.getValue())) {
					final BigDecimal bigFromDate = new BigDecimal(timeConverter.getCurrentTime());
					// Vo thoi han
					final BigDecimal bigToDate = new BigDecimal("23820104145253562");
					try {
						sysTaskService.save(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId);
						Notification.show("Đã lưu", Type.WARNING_MESSAGE);
					} catch (Exception ex) {
						LOGGER.error(ex.getMessage());
						Notification.show("Lưu thông tin thất bại", Type.ERROR_MESSAGE);
					}
				} else {
					Notification.show("Vui lòng nhập nội dung", Type.WARNING_MESSAGE);
				}
			}
		});
		// Button xoa yeu cau
		final Button btDel = new Button("Xóa");
		btDel.setStyleName(ValoTheme.BUTTON_DANGER);
		btDel.setIcon(FontAwesome.CLOSE);
		btDel.addClickListener(delEvent -> {
			getUI().getWindows().forEach(s -> {
				getUI().removeWindow(s);

			});
			sysTaskService.deleteByObjecttaskAndTypetask(String.valueOf(tfCif.getValue()), "EXCEPTION");
			txtareaComment.setValue("");
			Notification.show("Đã Xóa Thông tin", Type.WARNING_MESSAGE);
		});

		buttonLayout.addComponent(btSave);
		buttonLayout.addComponent(btDel);

		// Xu ly neu da co thong tin dang ky truoc
		if (fdsSysTask != null) {
			final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			try {
				final Date dFromDate = formatter.parse(fdsSysTask.getFromdate().toString());
				final Date dToDate = formatter.parse(fdsSysTask.getTodate().toString());

				dfFromDate.setValue(dFromDate);
				dfToDate.setValue(dToDate);

				// Neu vo thoi han
				if ("23820104145253562".equals(fdsSysTask.getTodate().toString())) {
					dfFromDate.setVisible(false);
					dfToDate.setVisible(false);
					chBoxToDate.setValue(true);
				}

				txtareaComment.setValue(fdsSysTask.getContenttask());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		sFormLayout.addComponent(txtareaComment);
		sFormLayout.addComponent(dfFromDate);
		sFormLayout.addComponent(dfToDate);
		sFormLayout.addComponent(chBoxToDate);
		sFormLayout.addComponent(buttonLayout);

		return sFormLayout;
	}

	private void cmdButton_GetDataInfoCust(final String cifno) {
		final CustomerInfo customerInfo = custInfoService.findOneAll(cifno);
		if (customerInfo != null) {
			final String sCustFullName = customerInfo.getCust_name();
			final String sPhone = customerInfo.getCust_hp();
			final String sOffTel2 = customerInfo.getCust_off_tel_2();
			final String sEmail = customerInfo.getCust_email_addr();
			final String sHtml = String.format(
					"<ul><li>Họ tên chủ thẻ: %s </li><li>Số điện thoại 1: %s</li><li>Số điện thoại 2: %s </li><li>Email: %s</li></ul>", sCustFullName,
					sPhone, sOffTel2, sEmail);
			lbInfocust.setValue(sHtml);
			lbInfocust.setContentMode(ContentMode.HTML);
			panelComment.setVisible(true);
			panelComment.setContent(buildCommentForm());
			panelInfocust.setVisible(true);
			panelInfocust.setContent(lbInfocust);
		} else {
			Notification.show("Không tìm thấy thông tin khách hàng", Type.WARNING_MESSAGE);
			if (panelInfocust.isVisible()) {
				panelInfocust.setVisible(false);
			}
			if (panelComment.isVisible()) {
				panelComment.setVisible(false);
			}
		}
	}
	
}
