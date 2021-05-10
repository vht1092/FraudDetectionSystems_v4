package com.fds.components;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fds.ReloadComponent;
import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.entities.FdsPosCasesDetail;
import com.fds.services.CaseDetailService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Man hinh danh sach case dang xu ly
 * 
 */
@SpringComponent
@Scope("prototype")
public class Inbox extends CustomComponent implements ReloadComponent {
	private static final long serialVersionUID = 1L;
	private transient String sUserId;
	private transient CaseDetailGridComponent grid;
	private final transient CaseDetailService caseDetailService;
	public static final String CAPTION = "CASE ĐANG XỬ LÝ";
	private final transient VerticalLayout mainLayout;
	private transient Page<FdsPosCasesDetail> result;

	private static final int SIZE_OF_PAGE = 50;
	private static final int FIRST_OF_PAGE = 0;
	private transient HorizontalLayout pagingLayout;

	public Inbox() {
		setCaption(CAPTION);

		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		this.sUserId = SecurityUtils.getUserId();

		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);

		grid = new CaseDetailGridComponent(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "checkDt")), true, "");

		final Label lbNumberOfElements = new Label();
		lbNumberOfElements.setStyleName(ValoTheme.LABEL_BOLD);
		mainLayout.addComponent(grid);

		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);
		mainLayout.addComponent(pagingLayout);
		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);

		setCompositionRoot(mainLayout);
	}

	private HorizontalLayout generatePagingLayout() {
		final Button btPaging = new Button();
		btPaging.setCaption(reloadLabelPaging());
		btPaging.setEnabled(false);

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
					btPaging.setCaption(reloadLabelPaging());
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
					btPaging.setCaption(reloadLabelPaging());
				}
			});
		});

		final HorizontalLayout pagingLayout = new HorizontalLayout();
		pagingLayout.setSizeUndefined();
		pagingLayout.setSpacing(true);
		pagingLayout.addComponent(btPaging);
		pagingLayout.addComponent(btPreviousPage);
		pagingLayout.addComponent(btNextPage);
		pagingLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

		return pagingLayout;
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

	private Page<FdsPosCasesDetail> getData(final Pageable page) {
		result = caseDetailService.findAllProcessingByUser(page, sUserId);
		return result;
	}

	@Override
	public void eventReload() {
		grid.refreshData(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "checkDt")));
		// Refresh paging button
		mainLayout.removeComponent(pagingLayout);
		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);
		mainLayout.addComponent(pagingLayout);
		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
	}

}
