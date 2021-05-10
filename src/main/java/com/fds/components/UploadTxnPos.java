package com.fds.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.fds.ReloadComponent;
import com.fds.SecurityUtils;
import com.fds.SpringConfigurationValueHelper;
import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.FdsPosCasesDetail;
import com.fds.entities.FdsPosTxnV2;
import com.fds.services.CaseDetailService;
import com.fds.services.DescriptionService;
import com.fds.services.FdsPosTxnV2Service;
import com.fds.services.SysUserService;
import com.monitorjbl.xlsx.StreamingReader;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Man hinh tim kiem
 * 
 */

@SpringComponent
@ViewScope
public class UploadTxnPos extends CustomComponent implements ReloadComponent  {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadTxnPos.class);
	private final transient FdsPosTxnV2Service fdsPosTxnV2Service;
	private final CaseDetailService caseDetailService;
	public static final String CAPTION = "CẬP NHẬT DỮ LIỆU GIAO DỊCH POS";
	
	//-----------------------------------------------------------------
	private static final String VALIDATE_NUMBER = "Chỉ nhận giá trị số";
	//private static final String FROM_DATE = "TỪ NGÀY";
	
	//private static final String CARD_BRAND = "LOẠI THẺ"; // MasterCard, Visa
	//private static final String TO_DATE = "ĐẾN NGÀY";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String SEARCH = "TÌM KIẾM";
		//private transient String sCardBrand = "";
	private String UserId = "";
	private String CheckUserId = "";
	private ComboBox cbbNHTT;
	private final TransactionDetailGridComponent grid;
	public DateField dfFromDate;
	public DateField dfToDate;
	//-----------------------------------------------------------------
	private SpringConfigurationValueHelper configurationHelper;
	private String fileNameImport;
	private File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	List<FdsPosTxnV2> fdsPosTxnV2List = new ArrayList<FdsPosTxnV2>();
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	
	private String sUserId = "";
	private Page<FdsPosTxnV2> result;
	private HorizontalLayout pagingLayout;
	
	private final VerticalLayout mainLayout = new VerticalLayout();

	// Paging
	private static final int SIZE_OF_PAGE = 50;
	private static final int FIRST_OF_PAGE = 0;

	
	@SuppressWarnings("unchecked")
	public UploadTxnPos() {
		
		setCaption(CAPTION);
//		final VerticalLayout mainLayout = new VerticalLayout();
		
		final HorizontalLayout layoutImport = new HorizontalLayout();
		layoutImport.setSpacing(true);
		
		final HorizontalLayout layoutView = new HorizontalLayout();
		layoutView.setSpacing(true);
		
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		fdsPosTxnV2Service = (FdsPosTxnV2Service) helper.getBean("fdsPosTxnV2Service");
		final DescriptionService descriptionService = (DescriptionService) helper.getBean("descriptionService");
		final SysUserService sysUserService = (SysUserService) helper.getBean("sysUserService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		
		this.UserId = SecurityUtils.getUserId();
		
		Label lbNHTT= new Label("Ngân hàng thanh toán");
		cbbNHTT = new ComboBox();
		cbbNHTT.setNullSelectionAllowed(false);
		cbbNHTT.addItems("VTB","EIB");
		cbbNHTT.setItemCaption("VTB", "Vietinbank");
		cbbNHTT.setItemCaption("EIB", "Eximbank");
		cbbNHTT.setValue("VTB");
		cbbNHTT.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbNHTT.setValidationVisible(false);
		
		Label lbFromdate = new Label("Từ ngày");
		dfFromDate = new DateField();
		dfFromDate.setDateFormat("dd/MM/yyyy");
		dfFromDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfFromDate.setValidationVisible(false);

		Label lbTodate = new Label("Đến ngày");
		dfToDate = new DateField();
		dfToDate.setDateFormat("dd/MM/yyyy");
		dfToDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfToDate.setValidationVisible(false);
		
		final Button btView = new Button("VIEW");
		btView.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btView.setIcon(FontAwesome.EYE);
		btView.addClickListener(event -> {
			eventReload();
		});
		
		final Button btXLSXDowload = new Button("EXPORT");
		btXLSXDowload.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btXLSXDowload.setIcon(FontAwesome.DOWNLOAD);
		btXLSXDowload.addClickListener(event -> {
			//Export to excel START
			if(dfFromDate.getValue()!=null && dfToDate.getValue()!=null && cbbNHTT.getValue()!=null) {
				BigDecimal txnStartDate = new BigDecimal(timeConverter.convertDateTimeToStr(dfFromDate.getValue()).substring(0, 8));
				BigDecimal txnEndDate = new BigDecimal(timeConverter.convertDateTimeToStr(dfToDate.getValue()).substring(0, 8));
				String nhtt = cbbNHTT.getValue().toString();
				exportDataFdspostxnv2ToExcel(fdsPosTxnV2List,txnStartDate,txnEndDate,nhtt);
			}
			//Export to excel END
		});
		
		Label lbInsertFdsTxnV2  = new Label("Total row in excel inserted (fds_pos_txn_v2): ");
		lbInsertFdsTxnV2.setVisible(false);
		
		Label lbRuleProcessV2  = new Label("Response SP_FDSPOS_RULES_PROCESS_V2: ");
		lbRuleProcessV2.setVisible(false);
		
		Label lbRuleProcessNHTT  = new Label("Response SP_FDSPOS_RULES_PROCESS_TB: ");
		lbRuleProcessNHTT.setVisible(false);
		
		Label lbInsertFdsPosCasesDetail  = new Label("Insert fds_pos_cases_detail: ");
		lbInsertFdsPosCasesDetail.setVisible(false);
		
		Label lbInsertFdsPosCaseHitRules  = new Label("Insert fds_pos_case_hit_rules: ");
		lbInsertFdsPosCaseHitRules.setVisible(false);
		
		Label lbInsertFdsPosTxn  = new Label("Insert fds_pos_txn: ");
		lbInsertFdsPosTxn.setVisible(false);
		
		Label lbInsertFdsPosCases  = new Label("Insert fds_pos_cases: ");
		lbInsertFdsPosCases.setVisible(false);
		
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(true, false, false, true));
		
		Upload chooseFile = new Upload(null, new Upload.Receiver() {
			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				OutputStream outputFile = null;
				try {
					// TODO Auto-generated method stub
					fileNameImport = StringUtils.substringBefore(filename, ".xlsx") + "_" + timeConverter.getCurrentTime() + ".xlsx";
					
					Window confirmDialog = new Window();
					final FormLayout content = new FormLayout();
		            content.setMargin(true);
		            
		            Button bYes = new Button("OK");
					
					confirmDialog.setCaption("Dữ liệu sẽ import, vui lòng đợi trong quá trình xử lý");
					confirmDialog.setWidth(350.0f, Unit.PIXELS);
					
		        	if(!filename.isEmpty()) {
		        		fileImport = new File(configurationHelper.getPathFileRoot() + "/"+ fileNameImport);
			            if(!fileImport.exists()) {
			            	fileImport.createNewFile();
			            }
						outputFile =  new FileOutputStream(fileImport);
		        	
						bYes.addClickListener(event -> {
							try 
							{
								String nganHangTT = cbbNHTT.getValue().toString();
								fdsPosTxnV2List = new ArrayList<FdsPosTxnV2>();
								InputStream is = null;
								try {
									is = new FileInputStream(fileImport);
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						    	LOGGER.info("Reading file " + fileImport.getName());
//										    	XSSFWorkbook workbook = new XSSFWorkbook(is);
						    	Workbook workbook = StreamingReader.builder()
						        .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
						        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
						        .open(is);  
								
						    	Sheet sheet = workbook.getSheetAt(0);
						    	
						    	LOGGER.info("Reading row in " + fileImport.getName());
						    	
						    	
						    	for (Row row : sheet) {
						    		if(nganHangTT.equals("VTB")) {
						    			if(row.getRowNum()>5)
							    		{
							    			
							    			FdsPosTxnV2 txn = new FdsPosTxnV2();
							    			txn.setMaGd(getCellValue(row.getCell(1)));
							    			txn.setMid(getCellValue(row.getCell(2)));
							    			txn.setTenMid(getCellValue(row.getCell(3)));
							    			txn.setTid(getCellValue(row.getCell(4)));
							    			txn.setSoHd(getCellValue(row.getCell(5)));
							    			txn.setDiaChiGd(getCellValue(row.getCell(6)));
							    			
							    			String thoiGianGd = getCellValue(row.getCell(7));
							    			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
							    			Date thoiGianGdConvers = sdf.parse(thoiGianGd);
							    			sdf = new SimpleDateFormat("yyyyMMddhhmmss");
							    			String ngayTaoGd = sdf.format(thoiGianGdConvers);
							    			txn.setNgayTaoGd(new BigDecimal(ngayTaoGd));
							    			txn.setNgayGioGd(thoiGianGd);
							    			
							    			txn.setSoHoaDon(getCellValue(row.getCell(8)));
							    			txn.setMaChuanChi(getCellValue(row.getCell(9)));
							    			System.out.println("SoTienGdGoc: " + getCellValue(row.getCell(10)));
							    			txn.setSoTienGdGoc(new BigDecimal(getCellValue(row.getCell(10))));
							    			txn.setLoaiTien(getCellValue(row.getCell(11)));
							    			txn.setSoTienTip(new BigDecimal(getCellValue(row.getCell(12))));
							    			String soThe = getCellValue(row.getCell(13));
							    			txn.setSoBin(soThe.substring(0, 6));
							    			txn.setSoThe(soThe);
							    			txn.setLoaiThe(getCellValue(row.getCell(14)));
							    			txn.setKetQuaGd(getCellValue(row.getCell(15)));
							    			txn.setDaoHuy(getCellValue(row.getCell(16)));
							    			String posMode = StringUtils.isEmpty(getCellValue(row.getCell(17))) ? null : StringUtils.leftPad(getCellValue(row.getCell(17)),3,"0");
							    			txn.setPosMde2digit(StringUtils.isEmpty(posMode) ? null : posMode.substring(0,2));
							    			txn.setPosMode(posMode);
							    			txn.setMaLoi(getCellValue(row.getCell(21)));
							    			txn.setBaoCo(getCellValue(row.getCell(22)));
							    			txn.setNgayGd(new BigDecimal(ngayTaoGd.substring(0, 8)));
							    			txn.setScbChksStat(" ");
							    			txn.setVtbChksStat(" ");
							    			BigDecimal mcc = StringUtils.isEmpty(getCellValue(row.getCell(18))) ? null : new BigDecimal(getCellValue(row.getCell(18)));
							    			txn.setMcc(mcc);
							    			txn.setOnOffline(getCellValue(row.getCell(19)));
							    			txn.setUsrId(UserId);
							    			txn.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
							    			
//								    			for (Field field : txn.getClass().getDeclaredFields()) {
//												    field.setAccessible(true);
//												    String name = field.getName();
//												    Object value = field.get(txn);
//												    LOGGER.info(name + ": " + value);
////												    System.out.println(name + ": " + value);
//												}
							    			
							    			if(fdsPosTxnV2Service.existsById(txn.getMaGd())) {
							    				Notification.show("Lỗi","Dữ liệu có thông tin đã tồn tại, vui lòng kiểm tra lại file import", Type.ERROR_MESSAGE);
							    				confirmDialog.close();
							    				return;
							    				
							    			};
							    			
							    			fdsPosTxnV2List.add(txn);
							    		}
							    	} else {
							    		if(row.getRowNum()>1 && !isCellEmpty(row.getCell(0)) )
							    		{
							    			
							    			FdsPosTxnV2 txn = new FdsPosTxnV2();
							    			String magd = timeConverter.getCurrentTime().substring(0, 8) + "-" + StringUtils.leftPad(fdsPosTxnV2Service.getSeqnoFdsPosTxnV2(),6,"0");
							    			txn.setMaGd(magd);
							    			txn.setMid(getCellValue(row.getCell(0)));
							    			txn.setTenMid(getCellValue(row.getCell(1)));
							    			txn.setTid("");
							    			txn.setSoHd("");
							    			txn.setDiaChiGd("");
							    			
							    			String thoiGianGd = getCellValue(row.getCell(3));
							    			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							    			Date thoiGianGdConvers = sdf.parse(thoiGianGd);
							    			sdf = new SimpleDateFormat("yyyyMMdd");
							    			String ngayTaoGd = sdf.format(thoiGianGdConvers);
							    			txn.setNgayTaoGd(new BigDecimal(ngayTaoGd));
							    			txn.setNgayGioGd(thoiGianGd);
							    			
							    			txn.setSoHoaDon("");
							    			txn.setMaChuanChi(getCellValue(row.getCell(7)));
							    			txn.setSoTienGdGoc(new BigDecimal(getCellValue(row.getCell(6))));
							    			txn.setLoaiTien(getCellValue(row.getCell(5)));
							    			txn.setSoTienTip(null);
							    			String soThe = getCellValue(row.getCell(4));
							    			txn.setSoBin(soThe.substring(0, 6));
							    			txn.setSoThe(soThe);
							    			txn.setLoaiThe("");
							    			txn.setKetQuaGd("");
							    			txn.setDaoHuy("");
							    			String posMode = StringUtils.isEmpty(getCellValue(row.getCell(8))) ? null : StringUtils.leftPad(getCellValue(row.getCell(8)),3,"0");
							    			txn.setPosMde2digit(StringUtils.isEmpty(posMode) ? null : posMode.substring(0,2));
							    			txn.setPosMode(posMode);
							    			txn.setMaLoi("");
							    			txn.setBaoCo("");
							    			txn.setNgayGd(new BigDecimal(ngayTaoGd));
							    			txn.setScbChksStat(" ");
							    			txn.setVtbChksStat(" ");
							    			txn.setEibChksStat(" ");
							    			txn.setMcc(null);
							    			txn.setOnOffline("");
							    			txn.setUsrId(UserId);
							    			txn.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
							    			
//								    			for (Field field : txn.getClass().getDeclaredFields()) {
//												    field.setAccessible(true);
//												    String name = field.getName();
//												    Object value = field.get(txn);
//												    LOGGER.info(name + ": " + value);
////												    System.out.println(name + ": " + value);
//												}
							    			
							    			fdsPosTxnV2List.add(txn);
							    		}
							    	}
						    		
								} 
						    	
						    	lbInsertFdsTxnV2.setVisible(true);
								lbRuleProcessV2.setVisible(true);
								lbRuleProcessNHTT.setVisible(true);
								lbInsertFdsPosCasesDetail.setVisible(true);
								lbInsertFdsPosCaseHitRules.setVisible(true);
								lbInsertFdsPosTxn.setVisible(true);
								lbInsertFdsPosCases.setVisible(true);
								
								BigDecimal txnStartDate = fdsPosTxnV2List
						    		      .stream()
						    		      .min(Comparator.comparing(FdsPosTxnV2::getNgayGd))
						    		      .orElseThrow(NoSuchElementException::new).getNgayGd();
						    	
						    	BigDecimal txnEndDate = fdsPosTxnV2List
						    		      .stream()
						    		      .max(Comparator.comparing(FdsPosTxnV2::getNgayGd))
						    		      .orElseThrow(NoSuchElementException::new).getNgayGd();
						    	
						    		      
						    	LOGGER.info("txnStartDate: " + txnStartDate + ", txnEndDate: " + txnEndDate);
						    	
						    	lbInsertFdsTxnV2.setValue("Total row in excel inserted (fds_pos_txn_v2): " + fdsPosTxnV2List.size() + " rows");
						        LOGGER.info("totalRowInserted (fds_pos_txn_v2): " + fdsPosTxnV2List.size());
						    	
						    	if(nganHangTT.equals("VTB"))
						    	{
							    	for(FdsPosTxnV2 txn : fdsPosTxnV2List) {
							    		fdsPosTxnV2Service.save(txn);
							    	}
							    	
							    	String ruleProcessV2 = fdsPosTxnV2Service.fdsPosRulesProcessV2();
							    	lbRuleProcessV2.setValue("Response SP_FDSPOS_RULES_PROCESS_V2: " + ruleProcessV2);
							    	
							    	String ruleProcessVtb = fdsPosTxnV2Service.fdsPosRulesProcessVtb();
							    	lbRuleProcessNHTT.setValue("Response SP_FDSPOS_RULES_PROCESS_VTB: " + ruleProcessVtb);
							        
							        int insertFdsPosCasesDetail = fdsPosTxnV2Service.insertFdsPosCasesDetail(nganHangTT,txnStartDate, txnEndDate,null);
							        lbInsertFdsPosCasesDetail.setValue("Insert fds_pos_cases_detail: " + insertFdsPosCasesDetail + " rows");
							        LOGGER.info("insertFdsPosCasesDetail: " + insertFdsPosCasesDetail);
							        
							        int insertFdsPosCaseHitRules = fdsPosTxnV2Service.insertFdsPosCaseHitRules(nganHangTT,txnStartDate, txnEndDate,null);
							        lbInsertFdsPosCaseHitRules.setValue("Insert fds_pos_case_hit_rules: " + insertFdsPosCaseHitRules + " rows");
							        LOGGER.info("insertFdsPosCaseHitRules: " + insertFdsPosCaseHitRules);
							        		
							        int insertFdsPosTxn = fdsPosTxnV2Service.insertFdsPosTxn(nganHangTT,txnStartDate, txnEndDate,null);
							        lbInsertFdsPosTxn.setValue("Insert fds_pos_txn: " + insertFdsPosTxn + " rows");
							        LOGGER.info("insertFdsPosTxn: " + insertFdsPosTxn);
							        		
							        int insertFdsPosCases = fdsPosTxnV2Service.insertFdsPosCases(nganHangTT,txnStartDate, txnEndDate,null);
							        lbInsertFdsPosCases.setValue("Insert fds_pos_cases: " + insertFdsPosCases + " rows");
							        LOGGER.info("insertFdsPosCases: " + insertFdsPosCases);
						    	
						    	}
						    	else {
						    		
						    		for(FdsPosTxnV2 txn : fdsPosTxnV2List) {
							    		fdsPosTxnV2Service.save(txn);
						    		}
						    		
						    		List<String> listMaGd = fdsPosTxnV2List
							    		      .stream().map(FdsPosTxnV2::getMaGd)
							    		      .collect(Collectors.toList());	
							    	
							    	String[] maGdArray = new String[listMaGd.size()];
							    	maGdArray = listMaGd.toArray(maGdArray);
							    	Set<String> setMaGd = new HashSet<>();
									Collections.addAll(setMaGd, maGdArray); 
						    		LOGGER.info("List MaGD: " + setMaGd.toString());
									
						    		String ruleProcessEib = fdsPosTxnV2Service.fdsPosRulesProcessEib();
							    	lbRuleProcessNHTT.setValue("Response SP_FDSPOS_RULES_PROCESS_EIB: " + ruleProcessEib);
							    	LOGGER.info("Response SP_FDSPOS_RULES_PROCESS_EIB:: " + ruleProcessEib);
							    	
							        int insertFdsPosCasesDetail = fdsPosTxnV2Service.insertFdsPosCasesDetail(nganHangTT, txnStartDate, txnEndDate,setMaGd);
							        lbInsertFdsPosCasesDetail.setValue("Insert fds_pos_cases_detail: " + insertFdsPosCasesDetail + " rows");
							        LOGGER.info("insertFdsPosCasesDetail: " + insertFdsPosCasesDetail);
							        
							        int insertFdsPosCaseHitRules = fdsPosTxnV2Service.insertFdsPosCaseHitRules(nganHangTT, txnStartDate, txnEndDate,setMaGd);
							        lbInsertFdsPosCaseHitRules.setValue("Insert fds_pos_case_hit_rules: " + insertFdsPosCaseHitRules + " rows");
							        LOGGER.info("insertFdsPosCaseHitRules: " + insertFdsPosCaseHitRules);
							        		
							        int insertFdsPosTxn = fdsPosTxnV2Service.insertFdsPosTxn(nganHangTT, txnStartDate, txnEndDate,setMaGd);
							        lbInsertFdsPosTxn.setValue("Insert fds_pos_txn: " + insertFdsPosTxn + " rows");
							        LOGGER.info("insertFdsPosTxn: " + insertFdsPosTxn);
							        		
							        int insertFdsPosCases = fdsPosTxnV2Service.insertFdsPosCases(nganHangTT, txnStartDate, txnEndDate,setMaGd);
							        lbInsertFdsPosCases.setValue("Insert fds_pos_cases: " + insertFdsPosCases + " rows");
							        LOGGER.info("insertFdsPosCases: " + insertFdsPosCases);
						    		
						    	}
						    	
						    	workbook.close();
					            is.close();
								
							} catch (Exception e) {
								// TODO: handle exception
								LOGGER.error(ExceptionUtils.getFullStackTrace(e));
							}
							
							confirmDialog.close();
				        	
				        });
				        
						//-----------------
						VerticalLayout layoutBtn = new VerticalLayout();
			            layoutBtn.addComponents(bYes);
			            layoutBtn.setComponentAlignment(bYes, Alignment.BOTTOM_CENTER);
			            content.addComponent(layoutBtn);
			            
			            confirmDialog.setContent(content);

			            getUI().addWindow(confirmDialog);
			            
			            // Center it in the browser window
			            confirmDialog.center();
			            confirmDialog.setResizable(false);
		        	} else
		        		outputFile = null;
			            
				} catch (Exception e) {
					// TODO: handle exception
					LOGGER.error(ExceptionUtils.getFullStackTrace(e));
				} 
				return outputFile;
				
			}
			
			
		});
		chooseFile.setButtonCaption("IMPORT");
		chooseFile.addStyleName("myCustomUpload");
		
		layoutImport.addComponent(lbNHTT);
		layoutImport.addComponent(cbbNHTT);
		layoutImport.addComponent(chooseFile);
		
		layoutImport.setComponentAlignment(lbNHTT, Alignment.MIDDLE_LEFT);
		layoutImport.setComponentAlignment(cbbNHTT, Alignment.MIDDLE_LEFT);
		layoutImport.setComponentAlignment(chooseFile, Alignment.MIDDLE_LEFT);
		
		layoutView.addComponent(lbFromdate);
		layoutView.addComponent(dfFromDate);
		layoutView.addComponent(lbTodate);
		layoutView.addComponent(dfToDate);
		layoutView.addComponent(btView);
		layoutView.addComponent(btXLSXDowload);
		
		form.addComponent(layoutImport);
		form.addComponent(layoutView);
		form.addComponent(lbInsertFdsTxnV2);
		form.addComponent(lbRuleProcessV2);
		form.addComponent(lbRuleProcessNHTT);
		form.addComponent(lbInsertFdsPosCasesDetail);
		form.addComponent(lbInsertFdsPosCaseHitRules);
		form.addComponent(lbInsertFdsPosTxn);
		form.addComponent(lbInsertFdsPosCases);
		
		mainLayout.setSpacing(true);
		grid = new TransactionDetailGridComponent(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "updTms")));

//		pagingLayout = generatePagingLayout();
//		pagingLayout.setSpacing(true);
		
		mainLayout.addComponent(form);
		mainLayout.addComponent(grid);
//		mainLayout.addComponent(pagingLayout);
//		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
		setCompositionRoot(mainLayout);
	}

	protected void refreshData() {
	}
	
	private String getCellValue(Cell cell) {
		String value = cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? (isCellEmpty(cell) ? "0" : String.valueOf(cell.getNumericCellValue())) : (isCellEmpty(cell) ? "" : cell.getStringCellValue().replaceFirst("'", "").trim());
		return value;
	}

	
	public static boolean isCellEmpty(final Cell cell) {
	    if (cell == null) { // use row.getCell(x, Row.CREATE_NULL_AS_BLANK) to avoid null cells
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().trim().isEmpty()) {
	        return true;
	    }

	    return false;
	}
	
	private StreamResource getStream(File inputfile) {
	    
	    StreamResource.StreamSource source = new StreamResource.StreamSource() {

	        public InputStream getStream() {
	           
	            InputStream input=null;
	            try
	            {
	                input = new  FileInputStream(inputfile);
	            } 
	            catch (FileNotFoundException e)
	            {
	                LOGGER.error(ExceptionUtils.getFullStackTrace(e));
	            }
	            return input;

	        }
	    };
	    StreamResource resource = new StreamResource ( source, inputfile.getName());
	    return resource;
	}
	
	private void messageExportXLSX(String caption, String text) {
		Window confirmDialog = new Window();
		FormLayout content = new FormLayout();
        content.setMargin(true);
		Button bOK = new Button("OK");
		Label lbText = new Label(text);
		confirmDialog.setCaption(caption);
		confirmDialog.setWidth(300.0f, Unit.PIXELS);
		
		 bOK.addClickListener(event -> {
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			addExtension(downloader);
			StreamResource resource = getStream(new File(pathExport + "\\" + fileNameOutput));
			downloader.setFileDownloadResource(resource);
			downloader.download();
         	confirmDialog.close();
         });
		
		VerticalLayout layoutBtn = new VerticalLayout();
		layoutBtn.setSpacing(true);
		layoutBtn.addComponent(lbText);
        layoutBtn.addComponents(bOK);
        layoutBtn.setComponentAlignment(bOK, Alignment.BOTTOM_CENTER);
        content.addComponent(layoutBtn);
        
        confirmDialog.setContent(content);

        getUI().addWindow(confirmDialog);
        // Center it in the browser window
        confirmDialog.center();
        confirmDialog.setResizable(false);
	}
	
	private void exportDataFdspostxnv2ToExcel(List<FdsPosTxnV2> fdsPosTxnV2List, BigDecimal startdate, BigDecimal enddate, String nhtt) {
		//EXPORT LIST TO EXCEL FILE
        XSSFWorkbook workbookExport = new XSSFWorkbook();
        XSSFSheet sheetExport = workbookExport.createSheet("FDS_POS_TXN_V2");
        
        DataFormat format = workbookExport.createDataFormat();
        CellStyle styleNumber;
        styleNumber = workbookExport.createCellStyle();
        styleNumber.setDataFormat(format.getFormat("0.0"));
        
        rowNumExport = 0;
        LOGGER.info("Creating excel");

        if(rowNumExport == 0) {
        	Object[] rowHeader = null;
    		rowHeader = new Object[] {"STT","MA_GD", "MID", "TEN_MID", "TID", "SO_HD", "DIA_CHI_GD", "NGAY_TAO_GD", "NGAY_GIO_GD", "SO_HOA_DON", "MA_CHUAN_CHI", "SO_TIEN_GD_GOC", "LOAI_TIEN", "SO_TIEN_TIP", "SO_BIN", "SO_THE", "LOAI_THE", "KET_QUA_GD", "DAO_HUY", "POS_MDE_2DIGIT", "POS_MODE", "MA_LOI", "BAO_CO", "NGAY_GD", "SCB_CHKS_STAT", "VTB_CHKS_STAT", "MCC", "ON_OFFLINE","USR_ID","CRE_TMS"};
    	
        	int colNum = 0;	 
        	XSSFRow row = sheetExport.createRow(rowNumExport++);         	
        	for (Object field : rowHeader) {
        		Cell cell = row.createCell(colNum++, CellType.STRING);
        		cell.setCellValue((String)field);
        	}      
        	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
        }
        
        try {
	        for(FdsPosTxnV2 item : fdsPosTxnV2List) {
				XSSFRow row = sheetExport.createRow(rowNumExport++);
				
				row.createCell(0).setCellValue(rowNumExport-1);
				row.createCell(1).setCellValue(item.getMaGd());
				row.createCell(2).setCellValue(item.getMid());
				row.createCell(3).setCellValue(item.getTenMid());
				row.createCell(4).setCellValue(item.getTid());
				row.createCell(5).setCellValue(item.getSoHd());
				row.createCell(6).setCellValue(item.getDiaChiGd());
				row.createCell(7).setCellValue(item.getNgayTaoGd()==null ? "" : item.getNgayTaoGd().toString());
				row.createCell(8).setCellValue(item.getNgayGioGd());
				row.createCell(9).setCellValue(item.getSoHoaDon());
				row.createCell(10).setCellValue(item.getMaChuanChi());
				row.createCell(11,CellType.NUMERIC).setCellValue(item.getSoTienGdGoc()==null? 0 : item.getSoTienGdGoc().doubleValue());
				row.createCell(12).setCellValue(item.getLoaiTien());
				row.createCell(13,CellType.NUMERIC).setCellValue(item.getSoTienTip()==null? 0 : item.getSoTienTip().doubleValue());
				row.createCell(14).setCellValue(item.getSoBin());
				row.createCell(15).setCellValue(item.getSoThe());
				row.createCell(16).setCellValue(item.getLoaiThe());
				row.createCell(17).setCellValue(item.getKetQuaGd());
				row.createCell(18).setCellValue(item.getDaoHuy());
				row.createCell(19).setCellValue(item.getPosMde2digit()==null ? null : item.getPosMde2digit());
				row.createCell(20).setCellValue(item.getPosMode()==null ? null : item.getPosMode());
				row.createCell(21).setCellValue(item.getMaLoi());
				row.createCell(22).setCellValue(item.getBaoCo());
				row.createCell(23).setCellValue(item.getNgayGd().toString());
				row.createCell(24).setCellValue(item.getScbChksStat());
				row.createCell(25).setCellValue(item.getVtbChksStat());
				row.createCell(26).setCellValue(item.getMcc()==null ? null : item.getMcc().toString());
				row.createCell(27).setCellValue(item.getOnOffline());
				row.createCell(28).setCellValue(item.getUsrId());
				row.createCell(29).setCellValue(item.getCreTms().toString());
	        }
        
	        sheetExport.createFreezePane(0, 1);
        
        	fileNameOutput = "FDS_TXN_POS_V2_" + nhtt + "_" + startdate + "_" + enddate + ".xlsx";
        	pathExport = Paths.get(configurationHelper.getPathFileRoot() + "\\export");
        	if(Files.notExists(pathExport)) {
        		Files.createDirectories(pathExport);
            }
        	FileOutputStream outputStream = new FileOutputStream(pathExport + "\\" + fileNameOutput);
            LOGGER.info("Created file excel output " + fileNameOutput);
            workbookExport.write(outputStream);
            LOGGER.info("Write data to " + fileNameOutput + " completed");
            workbookExport.close();
            outputStream.close();
	        LOGGER.info("Export excel file " + fileNameOutput);
	        
//	        messageExportXLSX("Info","Export compeleted.");
//	        eventReload();
	        
	        SimpleFileDownloader downloader = new SimpleFileDownloader();
			addExtension(downloader);
			StreamResource resource = getStream(new File(pathExport + "\\" + fileNameOutput));
			downloader.setFileDownloadResource(resource);
			downloader.download();
        } catch (FileNotFoundException e) {
            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
        }
	}
	
	private HorizontalLayout generatePagingLayout() {
		Button btLabelPaging = new Button();
		btLabelPaging.setCaption(reloadLabelPaging());
		btLabelPaging.setEnabled(false);

		final Button btPreviousPage = new Button("Trang trước");
		btPreviousPage.setIcon(FontAwesome.ANGLE_LEFT);
		btPreviousPage.setEnabled(result.hasPrevious());

		final Button btNextPage = new Button("Trang sau");
		btNextPage.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		btNextPage.setIcon(FontAwesome.ANGLE_RIGHT);
		btNextPage.setEnabled(result.hasNext());

		btNextPage.addClickListener(evt -> {
			grid.refreshData(getData(result.nextPageable()));
			btNextPage.setEnabled(result.hasNext());
			btPreviousPage.setEnabled(result.hasPrevious());
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btLabelPaging.setCaption(reloadLabelPaging());
				}
			});

		});

		btPreviousPage.addClickListener(evt -> {
			grid.refreshData(getData(result.previousPageable()));
			btNextPage.setEnabled(result.hasNext());
			btPreviousPage.setEnabled(result.hasPrevious());
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btLabelPaging.setCaption(reloadLabelPaging());
				}
			});
		});

		final HorizontalLayout pageLayout = new HorizontalLayout();
		pageLayout.setSizeUndefined();
		pageLayout.setSpacing(true);
		pageLayout.addComponent(btLabelPaging);
		pageLayout.addComponent(btPreviousPage);
		pageLayout.addComponent(btNextPage);
		pageLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

		return pageLayout;
	}

	private String reloadLabelPaging() {
		final StringBuilder sNumberOfElements = new StringBuilder();
		if (result.getSize() * (result.getNumber() + 1) >= result.getTotalElements()) {
			sNumberOfElements.append(result.getTotalElements());
		} else {
			sNumberOfElements.append(result.getSize() * (result.getNumber() + 1));
		}
		final String sTotalElements = Long.toString(result.getTotalElements());

		return sNumberOfElements.toString() + "/" + sTotalElements;

	}

	private Page<FdsPosTxnV2> getData(Pageable page) {
//		result = new PageImpl<>(fdsPosTxnV2List); 
////		result = new PageImpl<>(fdsPosTxnV2Service.findAll()); 
//		return result;
		if(dfFromDate.getValue()!=null && dfToDate.getValue()!=null && cbbNHTT.getValue()!=null) {
			BigDecimal tungay = new BigDecimal(timeConverter.convertDateTimeToStr(dfFromDate.getValue()).substring(0, 8));
			BigDecimal denngay = new BigDecimal(timeConverter.convertDateTimeToStr(dfToDate.getValue()).substring(0, 8));
			String nhtt = cbbNHTT.getValue().toString();
			fdsPosTxnV2List = fdsPosTxnV2Service.findAllBetweensNgayGd(tungay, denngay, nhtt);
			int start = page.getOffset();
			int end = (start + page.getPageSize()) > fdsPosTxnV2List.size() ? fdsPosTxnV2List.size() : (start + page.getPageSize());
			result = new PageImpl<FdsPosTxnV2>(fdsPosTxnV2List.subList(start, end), page, fdsPosTxnV2List.size());
		}
		return result;
	}

	@Override
	public void eventReload() {
		if(dfFromDate.getValue()!=null && dfToDate.getValue()!=null && cbbNHTT.getValue()!=null) {
			grid.refreshData(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "updTms")));

			// Refresh paging button
			for (Iterator<Component> iterator = mainLayout.iterator(); iterator.hasNext();) {
			   Component comp = (Component) iterator.next();
			   if(comp.equals(pagingLayout))
				   mainLayout.removeComponent(pagingLayout);
			}
			
			pagingLayout = generatePagingLayout();
			pagingLayout.setSpacing(true);
			mainLayout.addComponent(pagingLayout);
			mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
		}
	
	}
}
