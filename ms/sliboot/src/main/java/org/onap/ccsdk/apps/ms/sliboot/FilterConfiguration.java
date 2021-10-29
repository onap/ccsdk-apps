package org.onap.ccsdk.apps.ms.sliboot;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.onap.aaf.cadi.filter.CadiFilter;
import org.onap.ccsdk.sli.core.utils.common.EnvProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConditionalOnProperty("cadi.properties.path")
public class FilterConfiguration {
    private static final Logger log = LoggerFactory.getLogger(FilterConfiguration.class);

	@Value( "${cadi.properties.path:none}" )
	private String cadiPropFile;

	@Bean
	@Order(1)
	public FilterRegistrationBean<CadiFilter> cadiFilter() {
		CadiFilter filter = new CadiFilter();
		
		FilterRegistrationBean<CadiFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		if ("none".equals(cadiPropFile)) {
            log.info("cadi.properties.path undefined, AAF CADI disabled");
			registrationBean.setEnabled(false);
			registrationBean.addUrlPatterns("/xxxx/*");
		} else {
			// Note: assume that cadi.properties.path specifies full path to properties file
			File cadiFile = new File(cadiPropFile);
			if (!cadiFile.exists()) {
				log.info("cadi properties file {} not found, AAF CADI disabled", cadiPropFile);
				registrationBean.setEnabled(false);
				registrationBean.addUrlPatterns("/xxxx/*");
			} else {
				Properties cadiProperties = new EnvProperties();
				try {
					cadiProperties.load(new FileReader(cadiFile));
					cadiProperties.forEach((k, v) -> {
						registrationBean.addInitParameter((String) k, cadiProperties.getProperty((String) k));
					});
					registrationBean.addUrlPatterns("/*");
					log.info("Installed and configured CADI filter");
				} catch (IOException e) {
					log.info("Caught exception loading cadi properties file {}, AAF CADI disabled", cadiPropFile, e);
					registrationBean.setEnabled(false);
					registrationBean.addUrlPatterns("/xxxx/*");
				}
			}

		}

		return registrationBean;
	}

}
