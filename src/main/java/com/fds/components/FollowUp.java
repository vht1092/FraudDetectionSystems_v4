package com.fds.components;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fds.ReloadAutoComponent;
import com.fds.SpringContextHelper;
import com.fds.entities.FdsPosCasesDetail;
import com.fds.services.CaseDetailService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

/**
 * Man hinh danh sach case theo doi
 * 
 */
@SpringComponent
@ViewScope
public class FollowUp extends CustomComponent implements ReloadAutoComponent {
	private static final long serialVersionUID = 8808990975324145765L;
	public static final String CAPTION = "CASE GIÁM SÁT";
	private final static String STATUS = "MON";
	private final transient CaseDetailService caseDetailService;
	private final CaseDetailGridComponent grid;
	private final VerticalLayout mainLayout = new VerticalLayout();
	private Page<FdsPosCasesDetail> result;
	// Paging
	private final static int SIZE_OF_PAGE = 50;
	private final static int FIRST_OF_PAGE = 0;

	// private final Logger logger
	// =LoggerFactory.getLogger(CaseDistribution.class);

	public FollowUp() {
		setCaption(CAPTION);

		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");

		mainLayout.setSpacing(true);
		grid = new CaseDetailGridComponent(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "checkDt")), true, "");
		mainLayout.addComponentAsFirst(grid);

		setCompositionRoot(mainLayout);
	}

	private Page<FdsPosCasesDetail> getData(Pageable page) {
		result = caseDetailService.findAllByStatus(page, STATUS);
		return result;
	}

	@Override
	public void eventReloadAuto() {
		grid.refreshData(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "checkDt")));
	}

}
