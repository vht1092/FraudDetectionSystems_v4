package com.fds.components.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.fds.AdvancedFileDownloader;
import com.fds.AdvancedFileDownloader.AdvancedDownloaderListener;
import com.fds.AdvancedFileDownloader.DownloaderEvent;
import com.fds.SpringConfigurationValueHelper;
import com.fds.SpringContextHelper;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@SpringComponent
@Scope("prototype")
public class ReportForm extends CustomComponent {

	private static final long serialVersionUID = 7496781413585717785L;
	public DateField dffromDate;
	public DateField dfToDate;
	
	public ComboBox cbboxRuleId;
	public TextField tfTid;
	public TextField tfMid;
	public TextField tfCase;
	public TextField tfCardType;
	public TextField tfPan;
	public TextField tfMcc;
	public TextField tfResultTxn;
	public TextField tfPosMode;
	public TextField tfContent;
	public ComboBox cbboxOnOffline;
	
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private SpringConfigurationValueHelper configurationHelper;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportForm.class);
	public String filename;

	protected DataSource localDataSource;

	public ReportForm() {
		FormLayout mainLayout = new FormLayout();
		mainLayout.setMargin(true);

		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		localDataSource = (DataSource) helper.getBean("dataSource");

		dffromDate = new DateField("Từ ngày");
		dffromDate.setDateFormat("dd/MM/yyyy");
		dffromDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dffromDate.setValidationVisible(false);

		dfToDate = new DateField("Đến ngày");
		dfToDate.setDateFormat("dd/MM/yyyy");
		dfToDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfToDate.setValidationVisible(false);

		

		cbboxRuleId = new ComboBox("Rule id");
		cbboxRuleId.setVisible(false);
		
		tfTid = new TextField("TID");
		tfTid.setVisible(false);
		
		tfMid = new TextField("MID");
		tfMid.setVisible(false);
		
		tfCase = new TextField("Case No");
		tfCase.setVisible(false);
		
		tfCardType = new TextField("Loại thẻ");
		tfCardType.setVisible(false);
		
		tfPan = new TextField("Số thẻ");
		tfPan.setVisible(false);
		
		tfMcc = new TextField("MCC");
		tfMcc.setVisible(false);
		
		tfResultTxn = new TextField("Kết quả giao dịch");
		tfResultTxn.setVisible(false);
		
		tfPosMode = new TextField("Pos Entry Mode");
		tfPosMode.setVisible(false);
		
		tfContent = new TextField("Nội dung xử lý");
		tfContent.setVisible(false);
		
		cbboxOnOffline = new ComboBox("Online/Offline");
		cbboxOnOffline.addItems("Online");
		cbboxOnOffline.addItems("Offline");
		cbboxOnOffline.setVisible(false);
		
		final HorizontalLayout exportLayout = new HorizontalLayout();
		exportLayout.setSpacing(true);

		final Button btPDFDowload = new Button("PDF");
		btPDFDowload.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btPDFDowload.setIcon(FontAwesome.DOWNLOAD);

		final Button btXLSXDowload = new Button("XLSX");
		btXLSXDowload.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btXLSXDowload.setIcon(FontAwesome.DOWNLOAD);

		SimpleFileDownloader downloader = new SimpleFileDownloader();
		addExtension(downloader);

		// Dowload file pdf
		final StreamResource myResourcePDF = createTransMKResourcePDF();
		btPDFDowload.addClickListener(evt -> {
			if (checkValidator()) {
				downloader.setFileDownloadResource(myResourcePDF);
				downloader.download();
			}
		});
		// Dowload file xlsx
		final StreamResource myResourceXLSX = createTransMKResourceXLS();
		btXLSXDowload.addClickListener(evt -> {

			if (checkValidator()) {
				downloader.setFileDownloadResource(myResourceXLSX);
				downloader.download();
			}
		});
		
				

		mainLayout.addComponent(dffromDate);
		mainLayout.addComponent(dfToDate);
		
		mainLayout.addComponent(cbboxRuleId);
		mainLayout.addComponent(tfTid);
		mainLayout.addComponent(tfMid);
		mainLayout.addComponent(tfCase);
		mainLayout.addComponent(tfCardType);
		mainLayout.addComponent(tfPan);
		mainLayout.addComponent(tfMcc);
		mainLayout.addComponent(tfResultTxn);
		mainLayout.addComponent(tfPosMode);
		mainLayout.addComponent(tfContent);
		mainLayout.addComponent(cbboxOnOffline);
		
		exportLayout.addComponent(btPDFDowload);
		exportLayout.addComponent(btXLSXDowload);

		mainLayout.addComponent(exportLayout);

		setCompositionRoot(mainLayout);
	}

	public Map<String, Object> getParameter() {
		return null;
	}

	@SuppressWarnings("serial")
	private StreamResource createTransMKResourcePDF() {
		return new StreamResource(new StreamSource() {
			@Override
			public InputStream getStream() {
				try {
					final ByteArrayOutputStream outpuf = makeFileForDownLoad(filename, "PDF");
					return new ByteArrayInputStream(outpuf.toByteArray());
				} catch (Exception e) {
					LOGGER.error("createTransMKResourcePDF - Message: " + e.getMessage());
				}
				return null;

			}
		}, "fds_baocao.pdf");
	}

	@SuppressWarnings("serial")
	private StreamResource createTransMKResourceXLS() {
		return new StreamResource(new StreamSource() {
			@Override
			public InputStream getStream() {

				try {
					final ByteArrayOutputStream outpuf = makeFileForDownLoad(filename, "XLSX");
					return new ByteArrayInputStream(outpuf.toByteArray());
				} catch (Exception e) {
					LOGGER.error("createTransMKResourceXLS - Message: " + e.getMessage());
				}
				return null;

			}
		}, "fds_baocao.xlsx");
	}

	private ByteArrayOutputStream makeFileForDownLoad(String filename, String extension) throws JRException, SQLException {

		final Connection con = localDataSource.getConnection();
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		if (this.getParameter() != null) {
			// Tham so truyen vao cho bao cao
			final Map<String, Object> parameters = this.getParameter();

			final JasperReport jasperReport = (JasperReport) JRLoader
					.loadObjectFromFile(configurationHelper.getPathTemplateReport() + "/" + filename);
			final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);

			// Xuat file Excel
			if (extension.equals("XLSX")) {
				final JRXlsxExporter xlsx = new JRXlsxExporter();
				xlsx.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsx.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
				xlsx.exportReport();
			} else if (extension.equals("PDF")) { // File PDF
				JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			}
			return output;
		} else {
			return null;
		}

	}

	public boolean checkValidator() {
		return false;
	}

}
