package net.peachmonkey;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import net.peachmonkey.properties.ApplicationProperties;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfig {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	Environment env;

	@PostConstruct
	public void logProps() {
		for (PropertySource<?> propertySource : ((AbstractEnvironment) env).getPropertySources()) {
			if (propertySource instanceof MapPropertySource) {
				LOGGER.debug("Source [{}]", ((MapPropertySource) propertySource).getSource());
			}
		}
	}
}
