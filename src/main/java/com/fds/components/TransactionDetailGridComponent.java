package com.fds.components;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.FdsPosTxnV2;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SpringComponent
@Scope("prototype")
public class TransactionDetailGridComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDetailGridComponent.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	private final transient Grid grid;
	private final transient Page<FdsPosTxnV2> dataSource;
	private final transient Label lbNoDataFound;
	private final transient IndexedContainer container;

	/*
	 * @color: De to mau so the theo rule mac dinh la false
	 */

	public TransactionDetailGridComponent(final Page<FdsPosTxnV2> dataSource) {
		setSizeFull();
		this.dataSource = dataSource;

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
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
		container.addContainerProperty("usrId", String.class, "");
		container.addContainerProperty("magd", String.class, "");
		container.addContainerProperty("mid", String.class, "");
		container.addContainerProperty("tenMid", String.class, "");
		container.addContainerProperty("tid", String.class, "");
		container.addContainerProperty("soHd", String.class, "");
		container.addContainerProperty("diaChiGd", String.class, "");
		container.addContainerProperty("ngayTaoGd", String.class, "");
		container.addContainerProperty("ngayGioGd", String.class, "");
		container.addContainerProperty("soHoaDon", String.class, "");
		container.addContainerProperty("maChuanChi", String.class, "");
		container.addContainerProperty("soTienGdGoc", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("loaiTien", String.class, "");
		container.addContainerProperty("soTienTip", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("soBin", String.class, "");
		container.addContainerProperty("soThe", String.class, "");
		container.addContainerProperty("loaiThe", String.class, "");
		container.addContainerProperty("ketQuaGd", String.class, "");
		container.addContainerProperty("daoHuy", String.class, "");
		container.addContainerProperty("posMode2Digit", String.class, "");
		container.addContainerProperty("posMode", String.class, "");
		container.addContainerProperty("maLoi", String.class, "");
		container.addContainerProperty("baoCo", String.class, "");
		container.addContainerProperty("ngayGd", String.class, "");
//		container.addContainerProperty("scbCheckStat", String.class, "");
//		container.addContainerProperty("vtbCheckStat", String.class, "");
//		container.addContainerProperty("eimCheckStat", String.class, "");
		container.addContainerProperty("mcc", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("onOffline", String.class, "");
		
		initGrid();
	}

	private void initGrid() {
		if (createDataForContainer(this.dataSource) == false) {
//			if (!lbNoDataFound.isVisible() && this.dataSource != null) {
//				lbNoDataFound.setVisible(true);
//			}
			{
				lbNoDataFound.setVisible(true);
				grid.setVisible(false);
			}
		} else {
//			if (!grid.isVisible()) {
//				grid.setVisible(true);
//			}
			{
				lbNoDataFound.setVisible(false);
				grid.setVisible(true);
			}
		}

		grid.setContainerDataSource(container);
		
		grid.getColumn("usrId").setHeaderCaption("USER ID");
		grid.getColumn("magd").setHeaderCaption("MÃ GD");
		grid.getColumn("mid").setHeaderCaption("MID");
		grid.getColumn("tenMid").setHeaderCaption("TÊN MID");
		grid.getColumn("tid").setHeaderCaption("TID");
		grid.getColumn("soHd").setHeaderCaption("SỐ HD");
		grid.getColumn("diaChiGd").setHeaderCaption("ĐỊA CHỈ GD");
		grid.getColumn("ngayTaoGd").setHeaderCaption("NGÀY TẠO GD");
		grid.getColumn("ngayGioGd").setHeaderCaption("NGÀY GIỜ GD");
		grid.getColumn("soHoaDon").setHeaderCaption("SỐ HÓA ĐƠN");
		grid.getColumn("maChuanChi").setHeaderCaption("MÃ CHUẨN CHI");
		grid.getColumn("soTienGdGoc").setHeaderCaption("SỐ TIỀN GD GỐC");
		grid.getColumn("loaiTien").setHeaderCaption("LOẠI TIỀN");
		grid.getColumn("soTienTip").setHeaderCaption("SỐ TIỀN TIP");
		grid.getColumn("soBin").setHeaderCaption("SỐ BIN");
		grid.getColumn("soThe").setHeaderCaption("SỐ THẺ");
		grid.getColumn("loaiThe").setHeaderCaption("LOẠI THẺ");
		grid.getColumn("ketQuaGd").setHeaderCaption("KẾT QUẢ GD");
		grid.getColumn("daoHuy").setHeaderCaption("ĐẢO HỦY");
		grid.getColumn("posMode2Digit").setHeaderCaption("POS MODE 2 DIGIT");
		grid.getColumn("posMode").setHeaderCaption("POS MODE");
		grid.getColumn("maLoi").setHeaderCaption("MÃ LỖI");
		grid.getColumn("baoCo").setHeaderCaption("BÁO CÓ");
		grid.getColumn("ngayGd").setHeaderCaption("NGÀY GD");
//		grid.getColumn("scbCheckStat").setHeaderCaption("SCB STATUS");
//		grid.getColumn("vtbCheckStat").setHeaderCaption("VTB STATUS");
//		grid.getColumn("eimCheckStat").setHeaderCaption("EIM STATUS");
		grid.getColumn("mcc").setHeaderCaption("MCC");
		grid.getColumn("onOffline").setHeaderCaption("ON-OFFLINE");
		
		grid.setCellStyleGenerator(cell -> {
			if (cell.getPropertyId().equals("soTienGdGoc")) {
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("soTienTip")) {
				return "v-align-right";
			}
			return "";
		});

		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);

		// mainLayout.addComponentAsFirst(label_nodatafound);
		// mainLayout.addComponentAsFirst(grid);

	}


	public void refreshData(Page<FdsPosTxnV2> dataSource) {
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
	private boolean createDataForContainer(final Page<FdsPosTxnV2> listTxnDetail) {
		if (listTxnDetail != null && listTxnDetail.hasContent()) {

			container.removeAllItems();
			listTxnDetail.forEach(s -> {
				Item item = container.getItem(container.addItem());
				item.getItemProperty("usrId").setValue(s.getUsrId());
				item.getItemProperty("magd").setValue(s.getMaGd()); 
				item.getItemProperty("mid").setValue(s.getMid()); 
				item.getItemProperty("tenMid").setValue(s.getTenMid());
				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("soHd").setValue(s.getSoHd());
				item.getItemProperty("diaChiGd").setValue(s.getDiaChiGd());
				item.getItemProperty("ngayTaoGd").setValue(s.getNgayTaoGd().toString());
				item.getItemProperty("ngayGioGd").setValue(s.getNgayGioGd());
				item.getItemProperty("soHoaDon").setValue(s.getSoHoaDon());
				item.getItemProperty("maChuanChi").setValue(s.getMaChuanChi());
				item.getItemProperty("soTienGdGoc").setValue(s.getSoTienGdGoc());
				item.getItemProperty("loaiTien").setValue(s.getLoaiTien());
				item.getItemProperty("soTienTip").setValue(s.getSoTienTip());
				item.getItemProperty("soBin").setValue(s.getSoBin());
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("loaiThe").setValue(s.getLoaiThe());
				item.getItemProperty("ketQuaGd").setValue(s.getKetQuaGd());
				item.getItemProperty("daoHuy").setValue(s.getDaoHuy());
				item.getItemProperty("posMode2Digit").setValue(s.getPosMde2digit());
				item.getItemProperty("posMode").setValue(s.getPosMode());
				item.getItemProperty("maLoi").setValue(s.getMaLoi());
				item.getItemProperty("baoCo").setValue(s.getBaoCo());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd().toString());
//				item.getItemProperty("scbCheckStat").setValue(s.getScbChksStat());
//				item.getItemProperty("vtbCheckStat").setValue(s.getVtbChksStat());
//				item.getItemProperty("eimCheckStat").setValue(s.geteim);
				item.getItemProperty("mcc").setValue(s.getMcc());
				item.getItemProperty("onOffline").setValue(s.getOnOffline());
				
				
			});
		} else {
			return false;
		}
		return true;
	}


}
