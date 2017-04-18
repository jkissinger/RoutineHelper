package net.peachmonkey.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ApplicationProperties extends SelfLoggingProperties {

	@Value("${spring.application.name}")
	private String applicationName;

	public String getApplicationName() {
		return applicationName;
	}

}