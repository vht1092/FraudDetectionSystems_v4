package com.fds.components;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.CustomerInfo;
import com.fds.entities.FdsPosCasesDetail;
import com.fds.entities.FdsPosRule;
import com.fds.entities.FdsPosSysTask;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.fds.services.CustomerInfoService;
import com.fds.services.RuleService;
import com.fds.services.SysTaskService;
import com.fds.services.TxnDetailService;
import com.fds.views.MainView;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Component tao danh sach case
 * 
 * @see Inbox, CaseDistribution, ClosedCase
 */

@SpringComponent
@Scope("prototype")
public class CaseDetailGridComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseDetailGridComponent.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	private final transient Grid grid;
	private final transient RuleService ruleService;
	private final transient CaseDetailService caseDetailService;
	private final transient SysTaskService sysTaskService;
	private final transient TxnDetailService txnDetailService;
	private final transient CaseStatusService caseStatusService;
	private boolean color = false;
	private final transient Page<FdsPosCasesDetail> dataSource;
	private final transient Label lbNoDataFound;
	private final transient IndexedContainer container;
	private String getColumn;
	private final transient CustomerInfoService custInfoService;

	/*
	 * @color: De to mau so the theo rule mac dinh la false
	 */

	public CaseDetailGridComponent(final Page<FdsPosCasesDetail> dataSource, final boolean color, final String getColumn) {
		setSizeFull();
		this.color = color;
		this.dataSource = dataSource;
		this.getColumn = getColumn;

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		ruleService = (RuleService) helper.getBean("ruleService");
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		sysTaskService = (SysTaskService) helper.getBean("sysTaskService");
		txnDetailService = (TxnDetailService) helper.getBean("txnDetailService");
		custInfoService = (CustomerInfoService) helper.getBean("customerInfoService");
		caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		// init label
		lbNoDataFound = new Label("Không tìm thấy dữ liệu");
		lbNoDataFound.setVisible(false);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_FAILURE);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		lbNoDataFound.setSizeUndefined();

		// init grid
		grid = new Grid();
		grid.setVisible(false);
		grid.setSizeFull();
		grid.setHeightByRows(20);
		grid.setReadOnly(true);
		grid.setHeightMode(HeightMode.ROW);
		// init container

		container = new IndexedContainer();
		container.addContainerProperty("ngayGd", String.class, "");
		container.addContainerProperty("checkDate", String.class, "");
		container.addContainerProperty("ruleId", String.class, "");
		container.addContainerProperty("caseNo", String.class, "");
		container.addContainerProperty("mid", String.class, "");
		container.addContainerProperty("nameMID", String.class, "");
		container.addContainerProperty("tid", String.class, "");
		container.addContainerProperty("userId", String.class, "");
//		container.addContainerProperty("mcc", String.class, "");
		container.addContainerProperty("content", String.class, "");
				
		initGrid();
	}

	private void initGrid() {
		if (createDataForContainer(this.dataSource) == false) {
			if (!lbNoDataFound.isVisible() && this.dataSource != null) {
				lbNoDataFound.setVisible(true);
			}
		} else {
			if (!grid.isVisible()) {
				grid.setVisible(true);
			}
		}

		grid.setContainerDataSource(container);
		
		grid.getColumn("ngayGd").setHeaderCaption("Ngay_GD");
		grid.getColumn("checkDate").setHeaderCaption("Check_Date");
		grid.getColumn("ruleId").setHeaderCaption("RULE_ID");
		grid.getColumn("caseNo").setHeaderCaption("CASE");
		grid.getColumn("mid").setHeaderCaption("MID");
		grid.getColumn("nameMID").setHeaderCaption("Ten_MID");
		grid.getColumn("tid").setHeaderCaption("TID");
		grid.getColumn("userId").setHeaderCaption("UserId");
		//grid.getColumn("tid").setRenderer(new HtmlRenderer());
//		grid.getColumn("mcc").setHeaderCaption("MCC");
		grid.getColumn("content").setHeaderCaption("Content");
				
		//grid.getColumn("cifNo").setHeaderCaption("CIF");
		//grid.getColumn("cifNo").setHidden(true);

		// Dung cho close case --> Khoa rem code
		/*if (this.getColumn.equals("UpdateTime")) {
			grid.getColumn("updTms").setHidden(false);
			grid.getColumn("creTms").setHidden(true);
		} else if (this.getColumn.equals("All")) {
			grid.getColumn("updTms").setHidden(false);
			grid.getColumn("creTms").setHidden(false);
		} else {
			grid.getColumn("updTms").setHidden(true);
		}*/ 
		
		// Them su kien click tren row se chuyen qua man hinh chi tiet case de
		// xu ly
		// grid.addSelectionListener(evt -> {
		// if (grid.getSelectedRow() != null) {
		//
		// try {
		// final String sCaseno = container.getItem(grid.getSelectedRow()).getItemProperty("caseNo").getValue().toString();
		// final MainView mainview = (MainView) UI.getCurrent().getNavigator().getCurrentView();
		// mainview.addTab(new CaseDetail(sCaseno), sCaseno);
		// } catch (Exception e) {
		// LOGGER.error(e.getMessage());
		// }
		// grid.deselectAll();
		// }
		// });
		
		grid.addItemClickListener(evt -> {
			try {
				final String sCaseno = String.valueOf(evt.getItem().getItemProperty("caseNo").getValue());
				final MainView mainview = (MainView) UI.getCurrent().getNavigator().getCurrentView();
				mainview.addTab(new CaseDetail(sCaseno), sCaseno);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
			grid.deselectAll();

		});
		// Them tooltip mo ta rule cua case tren grid
		grid.setRowDescriptionGenerator(row -> {
			String sText = "";
			final String sCaseNo = container.getItem(row.getItemId()).getItemProperty("ruleId").getValue().toString();
			
			String[] result = null;
			if (sCaseNo != null) {
				result = sCaseNo.split(",");
				if (result != null) {
					for (String sRuleId : result) {
						FdsPosRule fdsPosRule = ruleService.findRuleByRuleId(sRuleId.trim());
						if (fdsPosRule != null) {
							sText = sText + fdsPosRule.getRuleId() + ": " + fdsPosRule.getRuleDesc() + "<br/>";
						}
					}
				}
			}
			
			return sText;
		});
		/*grid.setRowDescriptionGenerator(row -> {
			String sText = "";
			final String sCaseNo = container.getItem(row.getItemId()).getItemProperty("caseNo").getValue().toString();
			List<FdsPosRule> result = ruleService.findByCaseNo(sCaseNo);
			for (FdsPosRule r : result) {
				sText = sText + r.getRuleId() + ": " + r.getRuleDesc() + "<br/>";
			}
			return sText;

		});*/
		grid.setCellStyleGenerator(cell -> {
			if (cell.getPropertyId().equals("amount")) {
				return "v-align-right";
			}
			return "";
		});

		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);

		// mainLayout.addComponentAsFirst(label_nodatafound);
		// mainLayout.addComponentAsFirst(grid);

	}

	private String getColorByRuleId(final String caseno) {
		return ruleService.findColorByCaseNo(caseno);
	}

	public void refreshData(Page<FdsPosCasesDetail> dataSource) {
		getUI().access(() -> {
			if (createDataForContainer(dataSource) == false) {
				if (!lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(true);
				}
				if (grid.isVisible()) {
					grid.setVisible(false);
				}
			} else {
				if (lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(false);
				}
				if (!grid.isVisible()) {
					grid.setVisible(true);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private boolean createDataForContainer(final Page<FdsPosCasesDetail> listCaseDetail) {
		if (listCaseDetail != null && listCaseDetail.hasContent()) {

			container.removeAllItems();
			listCaseDetail.forEach(s -> {
				Item item = container.getItem(container.addItem());
				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("mid").setValue(s.getMid());
				item.getItemProperty("nameMID").setValue(s.getNameMid());
//				item.getItemProperty("mcc").setValue(s.getMcc());
				item.getItemProperty("ruleId").setValue(s.getRuleId());
				item.getItemProperty("caseNo").setValue(s.getCaseNo());
				item.getItemProperty("checkDate").setValue(s.getCheckDt().toString());
				item.getItemProperty("userId").setValue(s.getUsrId());
				item.getItemProperty("content").setValue(getCaseComment(s.getCaseNo()));
				item.getItemProperty("ngayGd").setValue(s.getNgayGd().toString());
			});
		} else {
			return false;
		}
		return true;
	}

	// Tao list rule cua case tren grid
	private String createListRule(final List<FdsPosRule> listrule) {
		if (listrule.isEmpty()) {
			return "";
		}
		String sRule = "";
		for (final FdsPosRule a : listrule) {
			sRule = sRule + a.getRuleId() + ", ";
		}
		return sRule.substring(0, sRule.length() - 2);
	}

	private boolean checkException(final String cifno) {
		FdsPosSysTask task = sysTaskService.findOneByObjectAndCurrentTime(cifno, "EXCEPTION");
		if (task != null) {
			return true;
		}
		return false;
	}

	private String decodeCardNoWithColor(final String cardno, final String caseno, final String cifno) {
		final String sCifNo = cifno;
		final String sCardNo = caseDetailService.getDed2(cardno);
		// Format lai so the #### #### #### ####
		String sReformatedCardNo = String.valueOf(sCardNo).replaceFirst("(\\d{4})(\\d{4})(\\d{4})(\\d{4})", "$1 $2 $3 $4");

		if (color) {
			String sColor = getColorByRuleId(caseno);
			if (checkException(sCifNo)) {
				return "<span class='v-label-exception'> </span><span style=\"padding:7px 0px; background-color:" + sColor + "\">" + sReformatedCardNo
						+ "</span>";
			}
			return "<span style=\"padding:7px 0px; background-color:" + sColor + "\">" + sReformatedCardNo + "</span>";

		}
		return sReformatedCardNo;
	}
	
	private String getCaseComment(String caseNo) {
		return caseStatusService.getCaseComment(caseNo);
	}

	// Lay so ref code tu bang txndetail ---> Khoa Rem code
	/*private String getRefCode(final BigDecimal cretms, String usedpan) {
		return txnDetailService.findRefCdeByCreTmsAndUsedPan(cretms, usedpan);
	}*/ 

	// Lay so pos country code tu bang txndetail ---> Khoa Rem code
	/*private String getPosCountryCde(final String usedpan, final BigDecimal cretms) {	
		return txnDetailService.findOneFxOa008CntryCdeByFxOa008UsedPanAndF9Oa008CreTms(usedpan, cretms);
	}*/
	
	/*---huyennt add on 20170620 add cust name to the grid layout ----> Khoa remcode */
	/*private String getCustName(final String cifno){
		final String sCifNo = cifno;
		final CustomerInfo customerInfo = custInfoService.findOneAll(sCifNo);
		// ----- THONG TIN HO TEN KHACH HANG ----- 
		String sCustFullName = "";
		if (customerInfo != null) {
			sCustFullName = customerInfo.getCust_name();
		}
		return sCustFullName;
	}*/
	
	/*------end huyennt edit----------------------*/
	/*---huyennt add on 20170726 add ECI VALUE to the grid layout----*/
	/*------Khoa remcode ----*/
	/*private String getEciVal(final String usedpan, final BigDecimal cretms) {	
		return txnDetailService.findEciValByCreTmsAndUsedPan(cretms, usedpan);
	}*/
}
