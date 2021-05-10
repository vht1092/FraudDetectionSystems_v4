package com.fds;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Dung de ho tro get value tu file config cho component khong quan ly boi
 * Spring
 */
@Component
public class SpringConfigurationValueHelper {

	@Value("${path.template.report}")
	private String pathTempReport;

	@Value("${time.refresh.content}")
	private int sTimeRefreshContent;

	@Value("${path.file.root}")
	private String pathFileRoot;
	
	public String getPathTemplateReport() {
		return pathTempReport;
	}

	public int sTimeRefreshContent() {
		return sTimeRefreshContent;
	}

	public String getPathFileRoot() {
		return pathFileRoot;
	}

	
}
