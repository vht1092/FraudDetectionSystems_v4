package com.fds.components;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.CustomerInfo;
import com.fds.entities.FdsPosSysTask;
import com.fds.services.CustomerInfoService;
import com.fds.services.DescriptionService;
import com.fds.services.SysTaskAuditTrailService;
import com.fds.services.SysTaskService;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
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
public class ExceptionDvcnt extends CustomComponent {
	private static final long serialVersionUID = 4527061434376139210L;
	public static final String TYPE = "EXCEPTION";
	public static final String CAPTION = "GHI NHẬN YÊU CẦU ĐVCNT";
	public static final String ACTION_UPDATE = "U";
	public static final String ACTION_DELETE = "D";
	// private final transient VerticalLayout mainLayout = new VerticalLayout();
	private final transient HorizontalLayout mainLayout = new HorizontalLayout();
	private final transient FormLayout formLayout = new FormLayout();
	private final transient Label lbMid = new Label("MID");
	private final transient Label lbTid = new Label("TID");
	private final transient TextField tfMid;
	private final transient TextField tfTid;
	private final transient Button btLoad;
	private final transient Panel panelComment = new Panel("NỘI DUNG YÊU CẦU");
	private final transient Panel panelDvcnt = new Panel("THÔNG TIN ĐVCNT");
	private final transient BeanItemContainer<CustomerInfo> container = new BeanItemContainer<CustomerInfo>(CustomerInfo.class);
	private final transient SysTaskService sysTaskService;
	private final transient CustomerInfoService custInfoService;
	private final transient Label lbInfoDvcnt;
	private final transient DescriptionService descService;
	private final transient SysTaskAuditTrailService sysTaskAuditTrailService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionDvcnt.class);

	private TextField tfUpdTime;
	private TextField tfUserInput;
	
	public ExceptionDvcnt() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		custInfoService = (CustomerInfoService) helper.getBean("customerInfoService");
		sysTaskService = (SysTaskService) helper.getBean("sysTaskService");
		descService = (DescriptionService) helper.getBean("descriptionService");
		sysTaskAuditTrailService = (SysTaskAuditTrailService) helper.getBean("sysTaskAuditTrailService");
		
		final VerticalLayout leftSide = new VerticalLayout();
		leftSide.setSpacing(true);
		leftSide.setSizeFull();

		final HorizontalLayout headerSide = new HorizontalLayout();
		headerSide.setSpacing(true);
//		headerSide.setSizeFull();

		panelComment.setVisible(false);
		panelDvcnt.setVisible(false);

		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		tfMid = new TextField();
//		tfMid.addValidator(new StringLengthValidator("Vui lòng nhập MID", 1, 20, false));
		tfMid.setValidationVisible(false);
		
		tfTid = new TextField();
//		tfTid.addValidator(new StringLengthValidator("Vui lòng nhập TID", 1, 20, false));
		tfTid.setValidationVisible(false);
		
		lbInfoDvcnt = new Label();
		lbInfoDvcnt.setCaptionAsHtml(true);

		btLoad = new Button("Lấy thông tin");
		btLoad.setIcon(FontAwesome.SEARCH);
		btLoad.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btLoad.addClickListener(evt -> {

			if(checkValidator())
				cmdButton_GetDataInfoDvcnt(tfMid.getValue().trim());

		});

		headerSide.addComponent(lbMid);
		headerSide.addComponent(tfMid);
		headerSide.addComponent(lbTid);
		headerSide.addComponent(tfTid);
		headerSide.addComponent(btLoad);
		headerSide.setComponentAlignment(lbMid, Alignment.MIDDLE_CENTER);
		headerSide.setComponentAlignment(lbTid, Alignment.MIDDLE_CENTER);
		formLayout.addComponent(headerSide);

		leftSide.addComponent(formLayout);
		leftSide.addComponent(panelDvcnt);
		leftSide.addComponent(panelComment);

		mainLayout.addComponent(leftSide);

		setCompositionRoot(mainLayout);

//		listData();
	}

	private void listData() {
		if (container.size() > 0) {
			container.removeAllItems();
		}
		container.addAll(custInfoService.findAllTypetask("EXCEPTION"));
	}

	private FormLayout buildCommentForm() {

		final FdsPosSysTask fdsSysTask = sysTaskService.findOneByMidOrTidAndTypeTask(tfMid.getValue().trim(),tfTid.getValue().trim(), TYPE);
		final TimeConverter timeConverter = new TimeConverter();
		final FormLayout sFormLayout = new FormLayout();
		sFormLayout.setSpacing(true);
		sFormLayout.setMargin(true);

		final String sUserId = SecurityUtils.getUserId();
		
		final DateField dfFromDate = new DateField("Từ ngày");
		dfFromDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
		dfFromDate.setResolution(Resolution.SECOND);

		final DateField dfToDate = new DateField("Đến ngày");
		dfToDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
		dfToDate.setResolution(Resolution.SECOND);
		
		final OptionGroup optGrpTypeOfBusiness = new OptionGroup();
		optGrpTypeOfBusiness.setSizeFull();
//		optGrpTypeOfBusiness.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		optGrpTypeOfBusiness.setCaption("Loại hình kinh doanh");
//		optGrpTypeOfBusiness.setMultiSelect(true);
		descService.findAllByTypeByOrderBySequencenoAsc("EXCEPTION-LHKD").forEach(item -> {
//			if(!item.getId().equals("EX9")) {
				optGrpTypeOfBusiness.addItem(item.getId());
				optGrpTypeOfBusiness.setItemCaption(item.getId(), item.getDescription());
//			}
		
		});
		optGrpTypeOfBusiness.setValue("EX9");
		

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
		
//		List<Object[]> listUserUpdateSysTaskAudit = sysTaskAuditTrailService.getUserUpdate(tfCif.getValue().trim());
		String userUpdate = "";
		String createDate = "";
//		if (listUserUpdateSysTaskAudit != null && listUserUpdateSysTaskAudit.size() > 0) {
//			userUpdate = listUserUpdateSysTaskAudit.get(0)[0].toString();
//			createDate = listUserUpdateSysTaskAudit.get(0)[1].toString();
//		} else {
			List<Object[]> listUserUpdateSysTask = sysTaskService.getUserUpdate(tfMid.getValue().trim(),tfTid.getValue().trim());
			if (listUserUpdateSysTask != null && listUserUpdateSysTask.size() > 0) {
				userUpdate = listUserUpdateSysTask.get(0)[0].toString();
				createDate = listUserUpdateSysTask.get(0)[1].toString();
			}
//		}
		
		
		tfUserInput = new TextField("User cập nhật gần nhất");
		tfUserInput.setValue(userUpdate);
		tfUserInput.setReadOnly(true);
		tfUserInput.setSizeFull();
		
		tfUpdTime = new TextField("Thời gian cập nhật gần nhất");
		tfUpdTime.setValue(convertStringToDate(createDate));
		tfUpdTime.setReadOnly(true);
		tfUpdTime.setSizeFull();

		final TextArea txtareaComment = new TextArea();
		txtareaComment.setSizeFull();

		
		optGrpTypeOfBusiness.addValueChangeListener(event -> {
			txtareaComment.setReadOnly(false);
			if(event.getProperty().getValue().equals("EX9")) {
				txtareaComment.setValue("");
				txtareaComment.setReadOnly(false);
			} else 
			{
				txtareaComment.setValue(optGrpTypeOfBusiness.getItemCaption(event.getProperty().getValue()));
				txtareaComment.setReadOnly(true);
			}
				
		});
		
		// Button
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);

		// Button Luu
		final Button btSave = new Button("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);
		btSave.addClickListener(evt -> {
			if (!chBoxToDate.getValue()) {
				if (dfFromDate.getValue() != null && dfToDate.getValue() != null && !"".equals(txtareaComment.getValue())) {
					final BigDecimal bigFromDate = new BigDecimal(timeConverter.convertDateTimeToStr(dfFromDate.getValue()));
					final BigDecimal bigToDate = new BigDecimal(timeConverter.convertDateTimeToStr(dfToDate.getValue()));
					try {
						if(fdsSysTask != null) {
							boolean result = saveSysTaskAuditTrail(fdsSysTask.getFromdate(), fdsSysTask.getTodate(), fdsSysTask.getContenttask(), TYPE, fdsSysTask.getUserid(), fdsSysTask.getCreatedate(), sUserId, ACTION_UPDATE, fdsSysTask.getTypeOfBusiness(), fdsSysTask.getMid(), fdsSysTask.getTid());
							//Luu thong tin xuong bang sysTask
							if (result == true) {
								sysTaskService.save(bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId,timeConverter.getCurrentTime(),optGrpTypeOfBusiness.getValue().toString(),tfMid.getValue(),tfTid.getValue(), fdsSysTask);
								Notification.show("Đã lưu", Type.WARNING_MESSAGE);
							}
								
						} else {
							sysTaskService.save(bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId,timeConverter.getCurrentTime(),optGrpTypeOfBusiness.getValue().toString(),tfMid.getValue(),tfTid.getValue(), fdsSysTask);
							Notification.show("Đã lưu", Type.WARNING_MESSAGE);
						}
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
						if (fdsSysTask != null) { //Cif nay da ton tai duoi table sysTask
							//cap nhat bang sysTaskAuditTrail
							boolean result = saveSysTaskAuditTrail(fdsSysTask.getFromdate(), fdsSysTask.getTodate(), fdsSysTask.getContenttask(), TYPE, fdsSysTask.getUserid(), fdsSysTask.getCreatedate(), sUserId, ACTION_UPDATE, fdsSysTask.getTypeOfBusiness(), fdsSysTask.getMid(), fdsSysTask.getTid());
							//Luu thong tin xuong bang sysTask
							if (result == true) {
								//sysTaskService.save(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId, getcurrentDateTime());
								//Notification.show("Đã lưu", Type.WARNING_MESSAGE);
								sysTaskService.save(bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId,timeConverter.getCurrentTime(),optGrpTypeOfBusiness.getValue().toString(),tfMid.getValue(),tfTid.getValue(), fdsSysTask);
								Notification.show("Đã lưu", Type.WARNING_MESSAGE);
							}
						} else { //Cif nay chua ton tai duoi table sysTask
							//sysTaskService.save(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId, getcurrentDateTime());
							//Notification.show("Đã lưu", Type.WARNING_MESSAGE);
							sysTaskService.save(bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId,timeConverter.getCurrentTime(),optGrpTypeOfBusiness.getValue().toString(),tfMid.getValue(),tfTid.getValue(), fdsSysTask);
							Notification.show("Đã lưu", Type.WARNING_MESSAGE);
						}
						
						
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
			
			if (fdsSysTask != null) { //Cif nay da ton tai duoi table sysTask
				//cap nhat bang sysTaskAuditTrail
				boolean result = saveSysTaskAuditTrail(fdsSysTask.getFromdate(), fdsSysTask.getTodate(), fdsSysTask.getContenttask(), 
						TYPE, fdsSysTask.getUserid(), fdsSysTask.getCreatedate(), sUserId, ACTION_DELETE,
						fdsSysTask.getTypeOfBusiness(), fdsSysTask.getMid(), fdsSysTask.getTid());
				//Luu thong tin xuong bang sysTask
				if (result == true) {
					sysTaskService.deleteByMidOrTidAndTypetask(tfMid.getValue(), tfTid.getValue(), "EXCEPTION");
					txtareaComment.setReadOnly(false);
					txtareaComment.setValue("");
					optGrpTypeOfBusiness.setValue("EX9");
					Notification.show("Đã Xóa Thông tin", Type.WARNING_MESSAGE);
				}
			}
			
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
				optGrpTypeOfBusiness.setValue(fdsSysTask.getTypeOfBusiness());

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
		sFormLayout.addComponent(tfUserInput);
		sFormLayout.addComponent(tfUpdTime);
		sFormLayout.addComponent(optGrpTypeOfBusiness);
		sFormLayout.addComponent(buttonLayout);

		return sFormLayout;
	}

	private void cmdButton_GetDataInfoDvcnt(final String cifno) {
//		final CustomerInfo customerInfo = custInfoService.findOneAll(cifno);
//		if (customerInfo != null) {
//			final String sCustFullName = customerInfo.getCust_name();
//			final String sPhone = customerInfo.getCust_hp();
//			final String sOffTel2 = customerInfo.getCust_off_tel_2();
//			final String sEmail = customerInfo.getCust_email_addr();
			final String sHtml = String.format(
					"<ul><li>Tên in trên hóa đơn: %s </li><li>Tên ĐVCNT: %s</li><li>MCC: %s </li><li>Đại diện pháp luật: %s</li><li>Tài khoản báo có: %s</li><li>Đơn vị quản lý: %s</li></ul>", 
					"[ pending ]","[ pending ]", "[ pending ]", "[ pending ]","[ pending ]","[ pending ]");
			lbInfoDvcnt.setValue(sHtml);
			lbInfoDvcnt.setContentMode(ContentMode.HTML);
			panelComment.setVisible(true);
			panelComment.setContent(buildCommentForm());
			panelDvcnt.setVisible(true);
			panelDvcnt.setContent(lbInfoDvcnt);
//		} else {
//			Notification.show("Không tìm thấy thông tin ĐVCNT", Type.WARNING_MESSAGE);
//			if (panelDvcnt.isVisible()) {
//				panelDvcnt.setVisible(false);
//			}
//			if (panelComment.isVisible()) {
//				panelComment.setVisible(false);
//			}
//		}
	}
	
	private boolean saveSysTaskAuditTrail(BigDecimal fromDate, BigDecimal toDate, String comment, String type,
			String userId, BigDecimal createTime, String userUpdate, String actionStatus, String typeOfBusiness, String mid, String tid) {
		
		try {
			//String idTask = sysTaskAuditTrailService.getValueIdTask();
			sysTaskAuditTrailService.save(fromDate,toDate, comment, type, userId, createTime, userUpdate, actionStatus, typeOfBusiness, mid, tid);
			return true;
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			Notification.show("Xử lý lưu thông tin thất bại", Type.ERROR_MESSAGE);
			return false;
		}
	}
	
	public boolean checkValidator() {
		tfMid.addValidator(new AbstractStringValidator("Chỉ nhập MID hoặc TID") {
		    @Override
		    protected boolean isValidValue(String s) {
		      return (tfMid.getValue().isEmpty() && !tfTid.getValue().isEmpty()) || (!tfMid.getValue().isEmpty() && tfTid.getValue().isEmpty());
		    }
		  });
		tfTid.addValidator(new AbstractStringValidator("Chỉ nhập MID hoặc TID") {
		    @Override
		    protected boolean isValidValue(String s) {
		      return  (tfMid.getValue().isEmpty() && !tfTid.getValue().isEmpty()) || (!tfMid.getValue().isEmpty() && tfTid.getValue().isEmpty());
		    }
		  });
		
		try {
			
			tfMid.validate();
			tfTid.validate();
			return true;
		} catch (InvalidValueException ex) {
			tfMid.setValidationVisible(true);
			tfTid.setValidationVisible(true);			
		}
		return false;
			
	}
	
	private String convertStringToDate(String sDate) {
		if (sDate == null || sDate.equals("")) {
			return "";
		} else {
			//
			DateFormat dateFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	        try {
	        	Date date = formatter.parse(sDate);
	        	String dateConvertFormat = dateFormat.format(date);
	            return dateConvertFormat;
	        } catch (ParseException e) {
	            e.printStackTrace();
	            return "";
	        }
		}
	}
}
