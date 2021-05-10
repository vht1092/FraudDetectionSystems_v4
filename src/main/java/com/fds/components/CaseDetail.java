package com.fds.components;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.CaseRegister;
import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.CustomerInfo;
import com.fds.entities.FdsPosCaseStatus;
import com.fds.entities.FdsPosCasesDetail;
import com.fds.entities.FdsPosRule;
import com.fds.entities.FdsPosSysTask;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.fds.services.CustomerInfoService;
import com.fds.services.RuleService;
import com.fds.services.SysTaskService;
import com.fds.views.MainView;
import com.ibm.icu.math.BigDecimal;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Hien thi man hinh xu ly case
 * Thong tin cactrang thai case xem table FDS_DESCRIPTION
 */


//DIC:Ket thuc gd
//TRA: chuyen gd
//REO: mo lai gd
@SpringComponent
@Scope("prototype")
public class CaseDetail extends CustomComponent implements CaseRegister {

	private static final long serialVersionUID = 1L;

	private final transient CaseDetailService caseDetailService;
	private final transient CaseStatusService caseStatusService;
	private final transient CustomerInfoService custInfoService;
	private final transient SysTaskService sysTaskService;
	private final transient RuleService ruleService;
	private static final String STATUS = "CASEDETAIL";
	private static final String ERROR_MESSAGE = "Lỗi ứng dụng";
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseDetail.class);

	private transient String sCaseno = "";
	private transient String sTid = "";
	private transient String sMid = "";
	private transient FdsPosCasesDetail fdsPosCasesDetail = null;
	private transient String sCifNo = "";
	private transient List<Object[]> listCaseDetail;
	private Window window;
	private final transient TimeConverter timeConverter = new TimeConverter();
	private final transient NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	
	private final Grid gridTranx;
	private final IndexedContainer contTranx;
		
	
	private final Grid gridHistTranx;
	private final IndexedContainer contHistTranx;

	public CaseDetail(final String caseno) {
		super();
		this.sCaseno = caseno;

		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		custInfoService = (CustomerInfoService) helper.getBean("customerInfoService");
		sysTaskService = (SysTaskService) helper.getBean("sysTaskService");
		ruleService = (RuleService) helper.getBean("ruleService");

		window = new Window();
		window.setWidth(90f, Unit.PERCENTAGE);
		window.setHeight(70f, Unit.PERCENTAGE);
		window.center();
		window.setModal(true);
		
		
		// --<< KHOI TAO GRID GIAO DICH >>--
		contTranx = new IndexedContainer();
		contTranx.addContainerProperty("case", String.class, "");
		contTranx.addContainerProperty("rule", String.class, "");
		contTranx.addContainerProperty("mid", String.class, "");
		contTranx.addContainerProperty("midName", String.class, "");
		contTranx.addContainerProperty("tid", String.class, "");
		contTranx.addContainerProperty("cardNo", String.class, "");
		contTranx.addContainerProperty("cardType", String.class, "");
		contTranx.addContainerProperty("trnxDate", String.class, "");
		contTranx.addContainerProperty("orginAmount", Double.class, ""); //so tien goc
		contTranx.addContainerProperty("loaiTien", String.class, "");
		contTranx.addContainerProperty("posMode", String.class, "");
		contTranx.addContainerProperty("apvCode", String.class, "");//ma chuan chi
		contTranx.addContainerProperty("address", String.class, "");
		contTranx.addContainerProperty("soHoaDon", String.class, "");
		contTranx.addContainerProperty("tienTip", String.class, "");
		contTranx.addContainerProperty("kqGD", String.class, ""); //KQ giao dich
		contTranx.addContainerProperty("daoHuy", String.class, "");
		contTranx.addContainerProperty("mcc", String.class, "");
		contTranx.addContainerProperty("onOffline", String.class, "");
		contTranx.addContainerProperty("nhtt", String.class, "");
		contTranx.addContainerProperty("maLoi", String.class, "");
		contTranx.addContainerProperty("baoCo", String.class, "");
		
//		contTranx.addContainerProperty("trnxId", String.class, "");
//		contTranx.addContainerProperty("soHD", String.class, "");
		
		// Khoi tao grid
		gridTranx = new Grid();
		gridTranx.setContainerDataSource(contTranx);
		gridTranx.setHeightMode(HeightMode.ROW);
		gridTranx.setHeightByRows(6);
		gridTranx.setWidth(100f, Unit.PERCENTAGE);
		//gridTranx.setSelectionMode(SelectionMode.MULTI);
		gridTranx.getColumn("case").setRenderer(new ButtonRenderer(event -> {
			final String sCaseNo = gridTranx.getContainerDataSource().getItem(event.getItemId()).getItemProperty("case").getValue().toString();
			if (!"".equals(sCaseNo) && sCaseNo != null) {
				final MainView mainview = (MainView) UI.getCurrent().getNavigator().getCurrentView();
				mainview.addTab(new CaseDetail(sCaseNo), sCaseNo);
			}
		}, null));
		// Can le content trong cell
		gridTranx.setCellStyleGenerator(cell -> {
			return "v-align-center";
		});
		
		gridTranx.getColumn("case").setHeaderCaption("Case");
		gridTranx.getColumn("rule").setHeaderCaption("Rule");
		gridTranx.getColumn("mid").setHeaderCaption("MID");
		gridTranx.getColumn("midName").setHeaderCaption("Ten_MID");
		gridTranx.getColumn("tid").setHeaderCaption("TID");
		gridTranx.getColumn("cardNo").setHeaderCaption("So_The");
		gridTranx.getColumn("cardType").setHeaderCaption("Loai_The");
		gridTranx.getColumn("trnxDate").setHeaderCaption("Ngay GD");
		gridTranx.getColumn("address").setHeaderCaption("Dia_Chi_GD");
//		gridTranx.getColumn("trnxId").setHeaderCaption("Loai GD");
//		gridTranx.getColumn("soHD").setHeaderCaption("HD");
		gridTranx.getColumn("soHoaDon").setHeaderCaption("Hoa_Don");
		gridTranx.getColumn("apvCode").setHeaderCaption("Ma_ChuanChi");
		gridTranx.getColumn("orginAmount").setHeaderCaption("So_Tien_Goc");
		gridTranx.getColumn("loaiTien").setHeaderCaption("Loai_Tien");
		gridTranx.getColumn("tienTip").setHeaderCaption("Tien_Tip");
		gridTranx.getColumn("kqGD").setHeaderCaption("KetQua_GD");
		gridTranx.getColumn("daoHuy").setHeaderCaption("Dao_Huy");
		gridTranx.getColumn("posMode").setHeaderCaption("Pos_Mode");
		gridTranx.getColumn("mcc").setHeaderCaption("MCC");
		gridTranx.getColumn("onOffline").setHeaderCaption("On_Offline");
		gridTranx.getColumn("nhtt").setHeaderCaption("nhtt");
		gridTranx.getColumn("maLoi").setHeaderCaption("Ma_Loi");
		gridTranx.getColumn("baoCo").setHeaderCaption("Bao_Co");
		// --<< // KHOI TAO GRID GIAO DICH >>--
		
		
		// --<< KHOI TAO GRID LICH SU GIAO DICH >>--
		// Khoi tao cotainer datasource
		contHistTranx = new IndexedContainer();
		contHistTranx.addContainerProperty("case", String.class, "");
		contHistTranx.addContainerProperty("rule", String.class, "");
		contHistTranx.addContainerProperty("mid", String.class, "");
		contHistTranx.addContainerProperty("midName", String.class, "");
		contHistTranx.addContainerProperty("tid", String.class, "");
		contHistTranx.addContainerProperty("cardNo", String.class, "");
		contHistTranx.addContainerProperty("cardType", String.class, "");
		contHistTranx.addContainerProperty("trnxDate", String.class, "");
		contHistTranx.addContainerProperty("orginAmount", Double.class, ""); //so tien goc
		contHistTranx.addContainerProperty("loaiTien", String.class, "");
		contHistTranx.addContainerProperty("posMode", String.class, "");
		contHistTranx.addContainerProperty("apvCode", String.class, "");//ma chuan chi
		contHistTranx.addContainerProperty("mcc", String.class, "");
		contHistTranx.addContainerProperty("onOffline", String.class, "");
		contHistTranx.addContainerProperty("userTiepNhan", String.class, "");
		contHistTranx.addContainerProperty("caseStatus", String.class, "");
//		
//		contHistTranx.addContainerProperty("address", String.class, "");
////		contHistTranx.addContainerProperty("trnxId", String.class, "");
////		contHistTranx.addContainerProperty("soHD", String.class, "");
//		contHistTranx.addContainerProperty("soHoaDon", String.class, "");
//		
//		
//		contHistTranx.addContainerProperty("tienTip", String.class, "");
//		contHistTranx.addContainerProperty("kqGD", String.class, ""); //KQ giao dich
//		contHistTranx.addContainerProperty("daoHuy", String.class, "");
//		
//		
//		contHistTranx.addContainerProperty("maLoi", String.class, "");
//		contHistTranx.addContainerProperty("baoCo", String.class, "");
//		contHistTranx.addContainerProperty("caseStatus", String.class, "");
//		
//		contHistTranx.addContainerProperty("thoiGianTiepNhan", String.class, "");
		
		
		// Khoi tao grid
		gridHistTranx = new Grid();
		gridHistTranx.setContainerDataSource(contHistTranx);
		gridHistTranx.setHeightMode(HeightMode.ROW);
		gridHistTranx.setHeightByRows(6);
		gridHistTranx.setWidth(100f, Unit.PERCENTAGE);
		gridHistTranx.setSelectionMode(SelectionMode.MULTI);
		gridHistTranx.getColumn("case").setRenderer(new ButtonRenderer(event -> {
			final String sCaseNo = gridHistTranx.getContainerDataSource().getItem(event.getItemId()).getItemProperty("case").getValue().toString();
			if (!"".equals(sCaseNo) && sCaseNo != null) {
				final MainView mainview = (MainView) UI.getCurrent().getNavigator().getCurrentView();
				mainview.addTab(new CaseDetail(sCaseNo), sCaseNo);
			}
		}, null));
		// Can le content trong cell
		gridHistTranx.setCellStyleGenerator(cell -> {
			if ("amount".equals(cell.getPropertyId()) || "amountvnd".equals(cell.getPropertyId())) {
				return "v-align-right";
			}
			else
			{
				return "v-align-center";
			}
		});
		gridHistTranx.getColumn("case").setHeaderCaption("Case");
		gridHistTranx.getColumn("rule").setHeaderCaption("Rule");
		gridHistTranx.getColumn("mid").setHeaderCaption("MID");
		gridHistTranx.getColumn("midName").setHeaderCaption("Ten_MID");
		gridHistTranx.getColumn("tid").setHeaderCaption("TID");
		gridHistTranx.getColumn("cardNo").setHeaderCaption("So_The");
		gridHistTranx.getColumn("cardType").setHeaderCaption("Loai_The");
		gridHistTranx.getColumn("trnxDate").setHeaderCaption("Ngay GD");
		gridHistTranx.getColumn("orginAmount").setHeaderCaption("So_Tien_Goc");
		gridHistTranx.getColumn("loaiTien").setHeaderCaption("Loai_Tien");
		gridHistTranx.getColumn("posMode").setHeaderCaption("Pos_Mode");
		gridHistTranx.getColumn("apvCode").setHeaderCaption("Ma_ChuanChi");
		gridHistTranx.getColumn("mcc").setHeaderCaption("MCC");
		gridHistTranx.getColumn("onOffline").setHeaderCaption("On_Offline");
		gridHistTranx.getColumn("userTiepNhan").setHeaderCaption("UserTiepNhan");
		gridHistTranx.getColumn("caseStatus").setHeaderCaption("Trang_Thai_Case");
		
//		gridHistTranx.getColumn("address").setHeaderCaption("Dia_Chi_GD");
////		gridHistTranx.getColumn("trnxId").setHeaderCaption("Ma_GD");
////		gridHistTranx.getColumn("soHD").setHeaderCaption("HD");
//		gridHistTranx.getColumn("soHoaDon").setHeaderCaption("Hoa_Don");
//		gridHistTranx.getColumn("tienTip").setHeaderCaption("Tien_Tip");
//		gridHistTranx.getColumn("kqGD").setHeaderCaption("KetQua_GD");
//		gridHistTranx.getColumn("daoHuy").setHeaderCaption("Dao_Huy");
//		gridHistTranx.getColumn("maLoi").setHeaderCaption("Ma_Loi");
//		gridHistTranx.getColumn("baoCo").setHeaderCaption("Bao_Co");
//		gridHistTranx.getColumn("caseStatus").setHeaderCaption("Trang_Thai_Case");
//		gridHistTranx.getColumn("thoiGianTiepNhan").setHeaderCaption("ThoiGianTiepNhan");
		// --<< // KHOI TAO GRID LICH SU GIAO DICH >>--

		createForm();
	}

	/**
	 * Thong tin chi tiet ve case
	 */

	private void createForm() {
		String txnIds = null;
		
		
		if (this.sCaseno != null) {
			List<String> listTxns = caseDetailService.getListTxnIdByCaseNo(this.sCaseno);
			fdsPosCasesDetail = caseDetailService.findOneByCaseNo(this.sCaseno);
			
			if (listTxns != null && fdsPosCasesDetail != null) {
				txnIds = splitTxns(listTxns);
				listCaseDetail = caseDetailService.getListTxnDetail(txnIds, this.sCaseno);
				addDataToTranxGrid(listCaseDetail);
			} else {
				Notification.show("Không tìm thấy dữ liệu của caseNo " + this.sCaseno, Type.ERROR_MESSAGE);
				LOGGER.error("Khong tim thay tham so caseno");
			}
			
		} else {
			Notification.show("Không tìm thấy dữ liệu", Type.ERROR_MESSAGE);
			LOGGER.error("Khong tim thay tham so caseno");
		}
		//----------
		if (listCaseDetail.isEmpty()) {
			Notification.show("Không tìm thấy dữ liệu", Type.ERROR_MESSAGE);
			LOGGER.error("Khong tim thay du lieu theo caseno: " + this.sCaseno);
		} else {

			if (" ".equals(userProcesing())) {
				registerProcessingCase();
			}

			window.addCloseListener(this.eventCloseWindow());
			
			//Giám sát	Cung cấp chứng từ	Xử lý khác

			final VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setMargin(true);
			verticalLayout.setSpacing(true);

			final HorizontalLayout actionLayout = new HorizontalLayout();
			actionLayout.setSpacing(true);

			final Button btDiscard = new Button("Kết thúc"); //Kết thúc
			btDiscard.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btDiscard.addClickListener(eventClickBTDiscard());

			final Button btMonitor = new Button("Giám sát"); //Gọi lại
			btMonitor.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btMonitor.addClickListener(eventClickBTMonitor());
			
			final Button btProcDifference = new Button("Xử lý khác"); //Chuyển
			btProcDifference.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btProcDifference.setVisible(false);
			btProcDifference.addClickListener(eventClickBTTDifference());
			

			final Button btReopen = new Button("Mở lại");
			btReopen.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btReopen.addClickListener(eventClickBTReopen());

			final Button btComment = new Button("Thêm nội dung xử lý");
			btComment.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btComment.addClickListener(eventClickBTAddComment());
			btComment.setVisible(false);
			
			final Button btSupplyBill = new Button("Cung cấp chứng từ"); //Trả Case về đang chờ xử lý
			btSupplyBill.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btSupplyBill.addClickListener(eventClickBTbtSupplyBill());
			
			final Label lbCaseDetail = new Label();
			lbCaseDetail.setContentMode(ContentMode.HTML);

			final Label lbHolderDetail = new Label();
			lbHolderDetail.setContentMode(ContentMode.HTML);

			final Label lbComment = new Label();
			lbComment.setContentMode(ContentMode.HTML);

			final Label lbRuleList = new Label();
			lbRuleList.setContentMode(ContentMode.HTML);

			final Label lbTransaction = new Label();
			lbTransaction.setContentMode(ContentMode.HTML);

			final Panel panelInvestNotes = new Panel("Nội dung xử lý");
			panelInvestNotes.setHeight(140, Unit.PIXELS);
			panelInvestNotes.setStyleName(Reindeer.PANEL_LIGHT);

			final Panel panelRuleList = new Panel("Rule đánh giá giao dịch");
			panelRuleList.setStyleName(Reindeer.PANEL_LIGHT);
			panelRuleList.setSizeFull();

			final Panel panelCaseDetail = new Panel("Chi tiết giao dịch");
			panelCaseDetail.setStyleName(Reindeer.PANEL_LIGHT);
			panelCaseDetail.setSizeFull();

			final Panel panelDetailTrans = new Panel("Lịch sử giao dịch");
			panelDetailTrans.setStyleName(Reindeer.PANEL_LIGHT);
			panelDetailTrans.setSizeFull();

			
			final String sUserId = fdsPosCasesDetail.getUsrId();
			final String sTxnCreTms = String.valueOf(fdsPosCasesDetail.getCheckDt());
			final String sCaseStatus = fdsPosCasesDetail.getCaseStatus();
			sTid = fdsPosCasesDetail.getTid();
			sMid = fdsPosCasesDetail.getMid();

			/* ----- CHI TIET GIAO DICH ----- */
			panelCaseDetail.setCaption("Chi tiết giao dịch");
			
			final VerticalLayout transactionDetailLayout = new VerticalLayout();
			//transactionDetailLayout.setSpacing(true);
			transactionDetailLayout.addComponent(gridTranx);
			panelCaseDetail.setContent(transactionDetailLayout);
			 
			final String txncreatetms = timeConverter.convertStrToDtTranx(sTxnCreTms);

			actionLayout.removeAllComponents();

			// Neu case da ket thuc, hoac danh dau la giao dich fraud, se hien
			// thi button mo lai
			// Neu case da dong se khong hien thi thong bao
			if ("DIC".equals(sCaseStatus) || "CAF".equals(sCaseStatus)) {
				actionLayout.addComponent(btReopen);
				btComment.setEnabled(false);
			} else if ("MON".equals(sCaseStatus)) { //Trang thai monitoring giao dich
				btMonitor.setEnabled(false);
				actionLayout.addComponent(btDiscard);
				actionLayout.addComponent(btProcDifference);
				actionLayout.addComponent(btSupplyBill);
			} else if ("DIF".equals(sCaseStatus)) { //Trang thai xu lý khac
				btProcDifference.setEnabled(false);
				actionLayout.addComponent(btDiscard);
				actionLayout.addComponent(btMonitor);
				actionLayout.addComponent(btSupplyBill);
			} else if ("BIL".equals(sCaseStatus)) { //Trang thai cung cap chung tu
				btSupplyBill.setEnabled(false);
				actionLayout.addComponent(btDiscard);
				//actionLayout.addComponent(btMonitor);
				actionLayout.addComponent(btProcDifference);	
			} else {
				if (!" ".equals(sUserId) && !sUserId.equals(SecurityUtils.getUserId().toUpperCase())) {
					Notification.show(sUserId.toUpperCase() + " đang xử lý case", Type.ERROR_MESSAGE);
				} else {
					actionLayout.addComponent(btDiscard);
					actionLayout.addComponent(btProcDifference);
					actionLayout.addComponent(btMonitor);
					actionLayout.addComponent(btSupplyBill);
					
					btComment.setEnabled(true);
				}
			}
			/* ----- END - CHI TIET GIAO DICH ----- */
			

			/* ----- NOI DUNG XU LY ----- */
			final List<FdsPosCaseStatus> listCaseStatus = caseStatusService.findAllByCaseNo(this.sCaseno);
			StringBuilder sHtmlComment = new StringBuilder("<table>");
			String action = "";
			for (final FdsPosCaseStatus a : listCaseStatus) {
				if ("DIC".equals(a.getCaseAction()) || "CAF".equals(a.getCaseAction())) {
					action = "Kết thúc";
				}
				if ("REO".equals(a.getCaseAction())) {
					action = "Mở lại";
				}
				if ("TRA".equals(a.getCaseAction())) {
					action = "Chuyển";
				}
				if ("ACO".equals(a.getCaseAction())) {
					action = "Thêm nội dung";
				}
				if ("BIL".equals(a.getCaseAction())) {
					action = "Cung cấp chứng từ";
				}
				if ("MON".equals(a.getCaseAction())) {
					action = "Case theo dõi";
				}
				sHtmlComment.append("<tr><td>" + timeConverter.convertStrToDateTime(a.getCreTms().toString()) + " " + a.getUserId() + " - " + action
						+ "</td><td>" + (a.getCaseComment().trim().isEmpty() ? "" : ": " + a.getCaseComment()) + "</td></tr>");
			}
			sHtmlComment.append("</table>");
			lbComment.setValue(sHtmlComment.toString());
			panelInvestNotes.setContent(lbComment);
			/* ----- END - NOI DUNG XU LY ----- */
			
			
			
			/* ----- DANH SACH RULE ----- */
			String[] listRules = fdsPosCasesDetail.getRuleId().split(","); 
			StringBuilder ruleList = new StringBuilder();
			if (listRules.length > 0) {
				ruleList.append("<table><tr><th>Rule</th><th>Mô tả</th></tr>");
				for(String sRuleId : listRules) {
					FdsPosRule fdsPosRule = ruleService.findRuleByRuleId(sRuleId.trim());
					if (fdsPosRule != null)
						ruleList = ruleList.append("<tr><td>" + fdsPosRule.getRuleId() + ": &nbsp</td><td>" + fdsPosRule.getRuleDesc() + "</td></tr>");
				}
				if ("NEW".equals(sCaseStatus) || " ".equals(sCaseStatus) || "REO".equals(sCaseStatus) || "CAL".equals(sCaseStatus)|| "TL2".equals(sCaseStatus) || "TRA".equals(sCaseStatus)) {
					ruleList.append(checkTaskofCase());
				}
				ruleList = ruleList.append("</table>");	
			} else {
				ruleList = ruleList.append("Không có rule");
			}
			lbRuleList.setValue(ruleList.toString());
			panelRuleList.setContent(lbRuleList);
			/* ----- END - DANH SACH RULE ----- */
			
			
			/* ----- LICH SU GIAO DICH ----- */
			final HorizontalLayout btLayout = new HorizontalLayout();
			btLayout.setSpacing(true);
						
			final Button btOneDay = new Button("1 Ngày");
			final Button btThreeDay = new Button("3 Ngày");
			final Button btSevenDay = new Button("7 Ngày");
			final Button btFifteenDay = new Button("15 Ngày");
			final Button btThirtyDay = new Button("30 Ngày");
			//final Button btAll = new Button("Tất cả");
			
			btOneDay.setStyleName(ValoTheme.BUTTON_LINK);
			btOneDay.setEnabled(false);
			btOneDay.addClickListener(btOneMonthEvt -> {
				btOneDay.setEnabled(false);
				btThreeDay.setEnabled(true);
				btSevenDay.setEnabled(true);
				btFifteenDay.setEnabled(true);
				btThirtyDay.setEnabled(true);
				//btAll.setDisableOnClick(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sTid, sMid, 1);
				});
			});
			
			btThreeDay.setStyleName(ValoTheme.BUTTON_LINK);
			btThreeDay.addClickListener(btOneMonthEvt -> {
				btOneDay.setEnabled(true);
				btThreeDay.setEnabled(false);
				btSevenDay.setEnabled(true);
				btFifteenDay.setEnabled(true);
				btThirtyDay.setEnabled(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sTid, sMid, 3);
				});
			});
			
			btSevenDay.setStyleName(ValoTheme.BUTTON_LINK);
			btSevenDay.addClickListener(btOneMonthEvt -> {
				btOneDay.setEnabled(true);
				btThreeDay.setEnabled(true);
				btSevenDay.setEnabled(false);
				btFifteenDay.setEnabled(true);
				btThirtyDay.setEnabled(true);
				//btAll.setDisableOnClick(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sTid, sMid, 7);
				});
			});
			
			btFifteenDay.setStyleName(ValoTheme.BUTTON_LINK);
			btFifteenDay.addClickListener(btOneMonthEvt -> {
				btOneDay.setEnabled(true);
				btThreeDay.setEnabled(true);
				btSevenDay.setEnabled(true);
				btFifteenDay.setEnabled(false);
				btThirtyDay.setEnabled(true);
				//btAll.setDisableOnClick(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sTid, sMid, 15);
				});
			});
			
			btThirtyDay.setStyleName(ValoTheme.BUTTON_LINK);
			btThirtyDay.addClickListener(btOneMonthEvt -> {
				btOneDay.setEnabled(true);
				btThreeDay.setEnabled(true);
				btSevenDay.setEnabled(true);
				btFifteenDay.setEnabled(true);
				btThirtyDay.setEnabled(false);
				//btAll.setDisableOnClick(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sTid, sMid, 30);
				});
			});
			
			btLayout.addComponent(btOneDay);
			btLayout.addComponent(btThreeDay);
			btLayout.addComponent(btSevenDay);
			btLayout.addComponent(btFifteenDay);
			btLayout.addComponent(btThirtyDay);
			//btLayout.addComponent(btAll);

			final VerticalLayout transactionLayout = new VerticalLayout();
			transactionLayout.setSpacing(true);
			addDataToHistTranxGrid(sTid, sMid, 1);
			transactionLayout.addComponent(btLayout);
			transactionLayout.addComponent(gridHistTranx);
			panelDetailTrans.setContent(transactionLayout);
			/* ----- END - LICH SU GIAO DICH ----- */

			verticalLayout.addComponent(panelCaseDetail);
			verticalLayout.addComponent(panelInvestNotes);
			verticalLayout.addComponent(btComment);
			verticalLayout.addComponent(panelRuleList);
			verticalLayout.addComponent(panelDetailTrans);
			verticalLayout.addComponent(actionLayout);
			setCompositionRoot(verticalLayout);
		}
	}
	
	/**
	 * Tao du lieu cho grid giao dich theo so case
	 */
	@SuppressWarnings("unchecked")
	private void addDataToTranxGrid(List<Object[]> listTransDetail) {
		if (!listTransDetail.isEmpty()) {
			if (!contTranx.getItemIds().isEmpty()) {
				contTranx.removeAllItems();
			}
			for (int i = 0; i <= listTransDetail.size() - 1; i++) {
				Item item = contTranx.getItem(contTranx.addItem());
				item.getItemProperty("case").setValue(listTransDetail.get(i)[0] != null ? listTransDetail.get(i)[0].toString() : "");
				item.getItemProperty("rule").setValue(listTransDetail.get(i)[1] != null ? listTransDetail.get(i)[1].toString() : "");
				item.getItemProperty("mid").setValue(listTransDetail.get(i)[2] != null ? listTransDetail.get(i)[2].toString() : "");
				item.getItemProperty("midName").setValue(listTransDetail.get(i)[3] != null ? listTransDetail.get(i)[3].toString() : "");
				item.getItemProperty("tid").setValue(listTransDetail.get(i)[4] != null ? listTransDetail.get(i)[4].toString() : "");
				item.getItemProperty("cardNo").setValue(listTransDetail.get(i)[5] != null ? listTransDetail.get(i)[5].toString() : "");
				item.getItemProperty("cardType").setValue(listTransDetail.get(i)[6] != null ? listTransDetail.get(i)[6].toString() : "");
				item.getItemProperty("trnxDate").setValue(listTransDetail.get(i)[7] != null ? listTransDetail.get(i)[7].toString() : "");
				item.getItemProperty("address").setValue(listTransDetail.get(i)[8] != null ? listTransDetail.get(i)[8].toString() : "");
//				item.getItemProperty("trnxId").setValue(listTransDetail.get(i)[9] != null ? listTransDetail.get(i)[9].toString() : "");
//				item.getItemProperty("soHD").setValue(listTransDetail.get(i)[10] != null ? listTransDetail.get(i)[10].toString() : "");
				item.getItemProperty("soHoaDon").setValue(listTransDetail.get(i)[11] != null ? listTransDetail.get(i)[11].toString() : "");
				item.getItemProperty("apvCode").setValue(listTransDetail.get(i)[12] != null ? listTransDetail.get(i)[12].toString() : "");
				item.getItemProperty("orginAmount").setValue(listTransDetail.get(i)[13] != null ? Double.parseDouble(listTransDetail.get(i)[13].toString()) : 0);
				item.getItemProperty("loaiTien").setValue(listTransDetail.get(i)[14] != null ? listTransDetail.get(i)[14].toString() : "");
				item.getItemProperty("tienTip").setValue(listTransDetail.get(i)[15] != null ?  listTransDetail.get(i)[15].toString() : "0");
				item.getItemProperty("kqGD").setValue(listTransDetail.get(i)[16] != null ? listTransDetail.get(i)[16].toString() : "");
				
				item.getItemProperty("daoHuy").setValue(listTransDetail.get(i)[17] != null ? listTransDetail.get(i)[17].toString() : "");
				item.getItemProperty("posMode").setValue(listTransDetail.get(i)[18] != null ? listTransDetail.get(i)[18].toString() : "");
				item.getItemProperty("mcc").setValue(listTransDetail.get(i)[19] != null ? listTransDetail.get(i)[19].toString() : "");
				item.getItemProperty("onOffline").setValue(listTransDetail.get(i)[22] != null ? listTransDetail.get(i)[22].toString() : "");
				item.getItemProperty("nhtt").setValue(listTransDetail.get(i)[23] != null ? listTransDetail.get(i)[23].toString() : "");
				item.getItemProperty("maLoi").setValue(listTransDetail.get(i)[20] != null ? listTransDetail.get(i)[20].toString() : "");
				item.getItemProperty("baoCo").setValue(listTransDetail.get(i)[21] != null ? listTransDetail.get(i)[21].toString() : "");
			}
		}
	}

	/**
	 * Tao du lieu cho grid lich su giao dich theo so ngay
	 */
	@SuppressWarnings("unchecked")
	private void addDataToHistTranxGrid(final String tid, final String mid, int numberofday) {
		final List<Object[]> listTransDetail = caseDetailService.findTransactionDetailByTidOrMid(tid, mid, numberofday);
		if (!listTransDetail.isEmpty()) {
			if (!contHistTranx.getItemIds().isEmpty()) {
				contHistTranx.removeAllItems();
			}
			for (int i = 0; i <= listTransDetail.size() - 1; i++) {
				Item item = contHistTranx.getItem(contHistTranx.addItem());
				item.getItemProperty("case").setValue(listTransDetail.get(i)[0] != null ? listTransDetail.get(i)[0].toString() : "");
				item.getItemProperty("rule").setValue(listTransDetail.get(i)[1] != null ? listTransDetail.get(i)[1].toString() : "");
				item.getItemProperty("mid").setValue(listTransDetail.get(i)[2] != null ? listTransDetail.get(i)[2].toString() : "");
				item.getItemProperty("midName").setValue(listTransDetail.get(i)[3] != null ? listTransDetail.get(i)[3].toString() : "");
				item.getItemProperty("tid").setValue(listTransDetail.get(i)[4] != null ? listTransDetail.get(i)[4].toString() : "");
				item.getItemProperty("cardNo").setValue(listTransDetail.get(i)[5] != null ? listTransDetail.get(i)[5].toString() : "");
				item.getItemProperty("cardType").setValue(listTransDetail.get(i)[6] != null ? listTransDetail.get(i)[6].toString() : "");
				item.getItemProperty("trnxDate").setValue(listTransDetail.get(i)[7] != null ? listTransDetail.get(i)[7].toString() : "");
				item.getItemProperty("orginAmount").setValue(listTransDetail.get(i)[13] != null ? Double.parseDouble(listTransDetail.get(i)[13].toString()) : 0);
				item.getItemProperty("loaiTien").setValue(listTransDetail.get(i)[14] != null ? listTransDetail.get(i)[14].toString() : "");
				item.getItemProperty("posMode").setValue(listTransDetail.get(i)[18] != null ? listTransDetail.get(i)[18].toString() : "");
				item.getItemProperty("apvCode").setValue(listTransDetail.get(i)[12] != null ? listTransDetail.get(i)[12].toString() : "");
				item.getItemProperty("mcc").setValue(listTransDetail.get(i)[19] != null ? listTransDetail.get(i)[19].toString() : "");
				item.getItemProperty("onOffline").setValue(listTransDetail.get(i)[25] != null ? listTransDetail.get(i)[25].toString() : "");
				item.getItemProperty("userTiepNhan").setValue(listTransDetail.get(i)[20] != null ? listTransDetail.get(i)[20].toString() : "");
				item.getItemProperty("caseStatus").setValue(listTransDetail.get(i)[22] != null ? listTransDetail.get(i)[22].toString() : "");
				
//				item.getItemProperty("address").setValue(listTransDetail.get(i)[8] != null ? listTransDetail.get(i)[8].toString() : "");
////				item.getItemProperty("trnxId").setValue(listTransDetail.get(i)[9] != null ? listTransDetail.get(i)[9].toString() : "");
////				item.getItemProperty("soHD").setValue(listTransDetail.get(i)[10] != null ? listTransDetail.get(i)[10].toString() : "");
//				item.getItemProperty("soHoaDon").setValue(listTransDetail.get(i)[11] != null ? listTransDetail.get(i)[11].toString() : "");
//				item.getItemProperty("tienTip").setValue(listTransDetail.get(i)[15] != null ?  listTransDetail.get(i)[15].toString() : "0");
//				item.getItemProperty("kqGD").setValue(listTransDetail.get(i)[16] != null ? listTransDetail.get(i)[16].toString() : "");
//				item.getItemProperty("daoHuy").setValue(listTransDetail.get(i)[17] != null ? listTransDetail.get(i)[17].toString() : "");
//				item.getItemProperty("maLoi").setValue(listTransDetail.get(i)[20] != null ? listTransDetail.get(i)[20].toString() : "");
//				item.getItemProperty("baoCo").setValue(listTransDetail.get(i)[21] != null ? listTransDetail.get(i)[21].toString() : "");
//				item.getItemProperty("caseStatus").setValue(listTransDetail.get(i)[19] != null ? listTransDetail.get(i)[19].toString() : "");
//				item.getItemProperty("thoiGianTiepNhan").setValue(listTransDetail.get(i)[21] != null ? listTransDetail.get(i)[21].toString() : "");
//				item.getItemProperty("caseStatus").setValue(listTransDetail.get(i)[22] != null ? listTransDetail.get(i)[22].toString() : "");
//				item.getItemProperty("userTiepNhan").setValue(listTransDetail.get(i)[23] != null ? listTransDetail.get(i)[23].toString() : "");
//				item.getItemProperty("thoiGianTiepNhan").setValue(listTransDetail.get(i)[24] != null ? listTransDetail.get(i)[24].toString() : "");
			}
		}
	}

	/**
	 * Hien thi cung cap chung tu
	 */
	private Button.ClickListener eventClickBTbtSupplyBill() {
		return event -> {
			final ArrayList<String> arCaseNo = new ArrayList<String>();
			try {
				gridHistTranx.getSelectedRows().forEach(item -> {
					final Property<?> caseProperty = gridHistTranx.getContainerDataSource().getContainerProperty(item, "case");
					try {
						if(caseProperty.getValue() != null && !"".equals(caseProperty.getValue().toString())) {
							arCaseNo.add(caseProperty.getValue().toString());
						}
					} catch (NullPointerException e) {
						// Khong lam gi het
					}
				});
				// Xoa highlight dong duoc chon tren grid
				gridHistTranx.getSelectionModel().reset();

				getUI().addWindow(createWindowComponent("Cung cấp chứng từ", new SupplyForm(this::closeWindow, this.sCaseno, this.sTid, arCaseNo)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("Monitoring TID - " + e.getMessage());
			}
		};
	}
	
	/**
	 * Hien thi form mo lai case
	 */
	private Button.ClickListener eventClickBTReopen() {
		return evt -> {
			try {
				getUI().addWindow(createWindowComponent("Mở lại case", new ReopenForm(this::closeWindow, this.sCaseno, this.sTid)));
			} catch (Exception e) {
				Notification.show("Lỗi ứng dụng", Type.ERROR_MESSAGE);
				LOGGER.error("ReopenClickListner -  " + e.getMessage());
			}
		};
	}

	/**
	 * Hien thi form giam sat
	 */
	private Button.ClickListener eventClickBTMonitor() {
		return event -> {
			final ArrayList<String> arCaseNo = new ArrayList<String>();
			try {
				gridHistTranx.getSelectedRows().forEach(item -> {
					final Property<?> caseProperty = gridHistTranx.getContainerDataSource().getContainerProperty(item, "case");
					try {
						if(caseProperty.getValue() != null && !"".equals(caseProperty.getValue().toString())) {
							arCaseNo.add(caseProperty.getValue().toString());
						}
					} catch (NullPointerException e) {
						// Khong lam gi het
					}
				});
				// Xoa highlight dong duoc chon tren grid
				gridHistTranx.getSelectionModel().reset();

				getUI().addWindow(createWindowComponent("Giám sát", new MonitorForm(this::closeWindow, this.sCaseno, this.sTid, arCaseNo)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("Monitoring TID - " + e.getMessage());
			}
		};
	}

	/**
	 * Hien thi form dong case
	 */
	private Button.ClickListener eventClickBTDiscard() {
		return event -> {
			final ArrayList<String> arCaseNo = new ArrayList<String>();
			try {
				gridHistTranx.getSelectedRows().forEach(item -> {
					final Property<?> caseProperty = gridHistTranx.getContainerDataSource().getContainerProperty(item, "case");
					try {
						if (caseProperty.getValue() != null && !"".equals(caseProperty.getValue().toString())) {
							arCaseNo.add(caseProperty.getValue().toString());
						}
					} catch (NullPointerException e) {
						// Khong lam gi het
					}
				});
				// Xoa highlight dong duoc chon tren grid
				gridHistTranx.getSelectionModel().reset();

				getUI().addWindow(createWindowComponent("Đóng case", new DiscardForm(this::closeWindow, this.sCaseno, this.sTid, arCaseNo)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("DisTIDClickListener - " + e.getMessage());
			}
		};
	}

	/**
	 * Hien thi form Xu lý khac (Hien thi form chuyen case)
	 */
	private Button.ClickListener eventClickBTTDifference() {
		return event -> {
			final ArrayList<String> arCaseNo = new ArrayList<String>();
			try {
				gridHistTranx.getSelectedRows().forEach(item -> {
					final Property<?> caseProperty = gridHistTranx.getContainerDataSource().getContainerProperty(item, "case");
					try {
						if (caseProperty.getValue() != null && !"".equals(caseProperty.getValue().toString())) {
							arCaseNo.add(caseProperty.getValue().toString());
						}
					} catch (NullPointerException e) {
						// Khong lam gi het
					}
				});
				// Xoa highlight dong duoc chon tren grid
				gridHistTranx.getSelectionModel().reset();

				getUI().addWindow(createWindowComponent("Xử lý khác", new DifferenceForm(this::closeWindow, this.sCaseno, this.sTid, arCaseNo)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("DifferenceTIDClickListener - " + e.getMessage());
			}
		};
	}

	/**
	 * Hien thi form them noi dung xu ly
	 */
	private Button.ClickListener eventClickBTAddComment() {
		return event -> {
			try {
				getUI().addWindow(createWindowComponent("Nội dung xử lý", new AddCommentForm(this::closeWindow, this.sCaseno, this.sTid)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("AddCommentClickListner - " + e.getMessage());
			}
		};
	}

	/**
	 * Danh dau giao dich la fraud chuyen trang thai case status = CAF CAF; DIC
	 * duoc xem la case ket thuc
	 */
	private CheckBox.ValueChangeListener eventClickChBoxFraud() {
		return event -> {
			if (event.getProperty().getValue().equals(true)) {
				final String sUserId = SecurityUtils.getUserId();

				try {
					caseStatusService.create(this.sCaseno, "Giao dịch Fraud", "CAF", "CAF", "", sUserId);
					caseDetailService.closeCase(this.sCaseno, sUserId, "CAF");
					createForm();
				} catch (Exception e) {
					Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
					LOGGER.error("FraudClickListner - " + e.getMessage());
				}

			}

		};
	}

	/**
	 * Tao moi cua so
	 * 
	 * @param caption
	 *            Ten cua so
	 * @param comp
	 *            Component
	 * @return Window
	 * @see ReopenClickListner
	 * @see AddCommentClickListner
	 * @see TransferClickListener
	 * @see DisCardClickListener
	 * @see CallBackClickListner
	 */
	private Window createWindowComponent(final String caption, final Component comp) {
		window.setCaption(caption);
		window.setContent(comp);
		return window;
	}

	/**
	 * Dong cua so dang mo
	 */
	private Window.CloseListener eventCloseWindow() {
		return evt -> {
			// Khi window dong lam moi du lieu
			createForm();
		};
	}

	/**
	 * Ham nay dung de ho tro callback dong cua so<br>
	 * {@link AddCommentClickListner}<br>
	 * {@link CallBackClickListner}<br>
	 * {@link DisCardClickListener}<br>
	 * {@link ReopenClickListner}<br>
	 * {@link TransferClickListener}<br>
	 */
	private void closeWindow() {
		getUI().removeWindow(window);
	}

	/**
	 * Kiem tra giao dich theo so cif cua khach hang co dang ky ngoai le hay
	 * khong !?
	 * 
	 * @return String
	 */
	private String checkTaskofCase() {
//		final FdsPosSysTask fdsSysTask = sysTaskService.findOneByObjectAndCurrentTime(sCifNo, "EXCEPTION");
		final FdsPosSysTask fdsSysTask = sysTaskService.findOneByMidOrTidAndCurrentTime(sMid, sTid, "EXCEPTION");
		if (fdsSysTask != null) {
			final String sContent = fdsSysTask.getContenttask();
			final String sFromdate = timeConverter.convertStrToDateTime(fdsSysTask.getFromdate().toString());
			final String sTodate = timeConverter.convertStrToDateTime(fdsSysTask.getTodate().toString());
			return "<tr><td style=\"border: 2px solid rgb(255, 88, 88); color: rgb(183, 19, 19);\" colspan=\"2\">"
					+ String.format("ĐVCNT thuộc %s - thời gian áp dụng từ %s đến %s", sContent, sFromdate, sTodate) + "</tr></td>";
		}
		return "";
	}

	/**
	 * Dang ky so case vao trang thai dang xu ly user khac khong the truy cap
	 */
	@Override
	public void registerProcessingCase() {
		if (!"".equals(SecurityUtils.getUserId())) {
			// Cap nhat user dang xu ly ngay khi chon case
			caseDetailService.updateAssignedUser(this.sCaseno, SecurityUtils.getUserId());
		}
	}

	/**
	 * Xoa dang ky so case o trang thai dang xu ly
	 */
	@Override
	public void closeProcessingCase() {
		try {
			sysTaskService.delete(SecurityUtils.getUserId(), this.sCaseno, STATUS);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
	}

	/**
	 * Kiem tra case da dang ky trang thai dang xu ly hay chua !
	 */
	@Override
	public String userProcesing() {
		final FdsPosCasesDetail fdsCaseDetail = caseDetailService.findOneByCaseNo(this.sCaseno);
		if (fdsCaseDetail == null) {
			return "";
		}
		return fdsCaseDetail.getUsrId();
	}
	
	public String splitTxns(List<String> listTxns) {
		List<String> listTxnId = new ArrayList<String>();
		String result = null;
		for (String str : listTxns) {
			String[] s = str.split(",");
			for (String txnId : s) {
				if (!listTxnId.contains(txnId))
					listTxnId.add(txnId);
			}
		}
		
		/*for (int i = 0; i < listTxnId.size(); i++) {
			if (i == 0)
				result = "'" + listTxnId.get(i) + "'";
			else
				result += ",'" + listTxnId.get(i) + "'"; 
		}*/
		
		for (int i = 0; i < listTxnId.size(); i++) {
			if (i == 0)
				result = listTxnId.get(i);
			else
				result += "," + listTxnId.get(i);
		}
		
		return result;
		
	}

}
