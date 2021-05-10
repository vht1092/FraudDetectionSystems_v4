package com.fds.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.FdsPosCasesDetail;
import com.fds.services.CaseDetailService;
import com.fds.services.DescriptionService;
import com.fds.services.SysUserService;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Man hinh tim kiem
 * 
 */

@SpringComponent
@ViewScope
public class Search extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Search.class);
	private final transient CaseDetailService caseDetailService;
	
	
	private CaseDetailGridComponent grid;
	
	public static final String CAPTION = "TÌM KIẾM";
	
	
	//-------------------------------OLD----------------------------------
		private static final String VALIDATE_NUMBER = "Chỉ nhận giá trị số";
	//private static final String FROM_DATE = "TỪ NGÀY";
	
	//private static final String CARD_BRAND = "LOẠI THẺ"; // MasterCard, Visa
	//private static final String TO_DATE = "ĐẾN NGÀY";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String SEARCH = "TÌM KIẾM";
		//private transient String sCardBrand = "";
	//-------------------------------OLD----------------------------------
	
	private static final String RULE_ID = "RULE ID";
	private static final String TID = "TID";
	private static final String MID = "MID";
	private static final String TEN_MID = "TÊN MID";
	private static final String DVQL = "ĐVQL";
	private static final String CASE_ID = "CASE NO.";
	private static final String RESULT_TXN = "KẾT QUẢ GD";
	private static final String POS_MODE = "POS_MODE";
	private static final String FROM_DATE = "TỪ NGÀY";
	private static final String TO_DATE = "ĐẾN NGÀY";
	private static final String CONTENT_PROC = "NỘI DUNG XỬ LÝ";
	
	private transient String sRuleId = "";
	private transient String sTID = "";
	private transient String sMID = "";
	private transient String sCASE = "";
	private transient String sTenMID = "";
	private transient String sDvql = "";
//	private transient String sCardBrand = "";
//	private transient String sPan = "";
//	private transient String sMCC = "";
//	private transient String sResultTxn = "";
//	private transient String sPosMode = "";
	private transient String sFromDate = "";
	private transient String sToDate = "";
	private transient String sContentProc = "";
	
	@SuppressWarnings("unchecked")
	public Search() {
		final VerticalLayout mainLayout = new VerticalLayout();
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		final DescriptionService descriptionService = (DescriptionService) helper.getBean("descriptionService");
		final SysUserService sysUserService = (SysUserService) helper.getBean("sysUserService");
		
		grid = new CaseDetailGridComponent(null, false, "All");
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		final TextField tfRuleId = new TextField(RULE_ID);
		final TextField tfTid = new TextField(TID);
		final TextField tfMid = new TextField(MID);
		final TextField tfCaseId = new TextField(CASE_ID);
		final TextField tfTenMid = new TextField(TEN_MID);
		final TextField tfDvql = new TextField(DVQL);
//		
//		final ComboBox cbboxCrdBrand = new ComboBox(CARD_BRAND);
//		cbboxCrdBrand.addContainerProperty("description", String.class, "");
//		cbboxCrdBrand.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cbboxCrdBrand.setItemCaptionPropertyId("description");
//		descriptionService.findAllByType("CARD").forEach(r -> {
//			final Item item = cbboxCrdBrand.addItem(r.getId());
//			item.getItemProperty("description").setValue(r.getDescription());
//		});
//		
//		final TextField tfPAN = new TextField(PAN);
//		final TextField tfMCC = new TextField(MCC);
//		final TextField tfResTxn = new TextField(RESULT_TXN);
//		final TextField tfPosMode = new TextField(POS_MODE);
				
		final ComboBox cbboxContentProc = new ComboBox(CONTENT_PROC);
		cbboxContentProc.addContainerProperty("description", String.class, "");
		cbboxContentProc.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbboxContentProc.setItemCaptionPropertyId("description");
		descriptionService.findAllByTypeByOrderBySequencenoAsc("ACTION").forEach(r -> {
			final Item item = cbboxContentProc.addItem(r.getId());
			item.getItemProperty("description").setValue(r.getDescription());
		});
		
//		final ComboBoxMultiselect comboBoxMultiselect = new ComboBoxMultiselect();
		
		final DateField dfDateFrom = new DateField(FROM_DATE);
		dfDateFrom.addValidator(new NullValidator(INPUT_FIELD, false));
		dfDateFrom.setDateFormat("dd/MM/yyyy");
		dfDateFrom.setValidationVisible(false);

		final DateField dfDateTo = new DateField(TO_DATE);
		dfDateTo.addValidator(new NullValidator(INPUT_FIELD, false));
		dfDateTo.setDateFormat("dd/MM/yyyy");
		dfDateTo.setValidationVisible(false);

		final Button btSearch = new Button(SEARCH);
		btSearch.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSearch.setIcon(FontAwesome.SEARCH);
		btSearch.addClickListener(evt -> {
			dfDateFrom.setValidationVisible(false);
			dfDateTo.setValidationVisible(false);
			try {
				dfDateFrom.validate();
				dfDateTo.validate();
				final TimeConverter timeConverter = new TimeConverter();
				sFromDate = timeConverter.convertDate(dfDateFrom.getValue(), false);
				sToDate = timeConverter.convertDate(dfDateTo.getValue(), true);
				
				sRuleId = tfRuleId.getValue() != null ? tfRuleId.getValue().toString().trim() : "";
				sTID = tfTid.getValue() != null ? tfTid.getValue().toString().trim() : "";
				sMID = tfMid.getValue() != null ? tfMid.getValue().toString().trim() : "";
				sTenMID = tfTenMid.getValue() != null ? tfTenMid.getValue().toString().trim() : "";
				sDvql = tfDvql.getValue() != null ? tfDvql.getValue().toString().trim() : "";
				sCASE = tfCaseId.getValue() != null ? tfCaseId.getValue().toString().trim() : "";
				
//				sCardBrand = cbboxCrdBrand.getValue() != null ? cbboxCrdBrand.getValue().toString() : "";
				
//				sPan = tfPAN.getValue() != null ? tfPAN.getValue().toString().trim() : "";
//				sMCC = tfMCC.getValue() != null ? tfMCC.getValue().toString().trim() : "";
//				sResultTxn = tfResTxn.getValue() != null ? tfResTxn.getValue().toString().trim() : "";
//				sPosMode = tfPosMode.getValue() != null ? tfPosMode.getValue().toString().trim() : "";
				sContentProc = cbboxContentProc.getValue() != null ? cbboxContentProc.getValue().toString().trim() : "";
				
				refreshData();
			} catch (InvalidValueException e) {
				dfDateFrom.setValidationVisible(true);
				dfDateTo.setValidationVisible(true);
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
				LOGGER.error(e.getMessage());
			}
		});
		
		form.addComponent(dfDateFrom);
		form.addComponent(dfDateTo);
		form.addComponent(tfMid);
		form.addComponent(tfTid);
		form.addComponent(tfTenMid);
		form.addComponent(tfRuleId);
		form.addComponent(tfDvql);
		form.addComponent(tfCaseId);
		form.addComponent(cbboxContentProc);
		
//		form.addComponent(cbboxCrdBrand);
//		form.addComponent(tfPAN);
//		form.addComponent(tfMCC);
//		form.addComponent(tfResTxn);
//		form.addComponent(tfPosMode);
		
		form.addComponent(btSearch);
		
		mainLayout.addComponent(form);
		mainLayout.addComponent(grid);
		setCompositionRoot(mainLayout);
		
	}

	private Page<FdsPosCasesDetail> getData(final String sFromDate, final String sToDate, final String sMID, final String sTID,
			final String sTenMid, final String sRuleId, final String sDvql, final String sCaseNo, final String sContentProc) {
		return caseDetailService.searchCase(sFromDate, sToDate, sMID, sTID, sTenMid, sRuleId, sDvql, sCaseNo, sContentProc);
		
	}

	protected void refreshData() {
		grid.refreshData(getData(sFromDate, sToDate, sMID, sTID, sTenMID, sRuleId, sDvql, sCASE, sContentProc));
	}

}
