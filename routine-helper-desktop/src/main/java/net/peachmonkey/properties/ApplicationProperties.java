package net.peachmonkey.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ApplicationProperties extends SelfLoggingProperties {

	@Value("${spring.application.name}")
	private String applicationName;
	@Value("${application.api.host:localhost}")
	private String apiHost;
	@Value("${application.api.port:8079}")
	private int apiPort;
	@Value("${sound.announce.seconds.beforeDue:300}")
	private int soundAnnounceSecondsBeforeDue;
	@Value("${sound.announce.seconds.afterDue:3600}")
	private int soundAnnounceSecondsAfterDue;
	@Value("${sound.files.location:Sounds}")
	private String soundFilesLocation;
	@Value("${icon.tray:icon.png}")
	private String trayIconImage;

	public String getApplicationName() {
		return applicationName;
	}

	public String getApiHost() {
		return apiHost;
	}

	public int getApiPort() {
		return apiPort;
	}

	public int getSoundAnnounceSecondsBeforeDue() {
		return soundAnnounceSecondsBeforeDue;
	}

	public void setSoundAnnounceSecondsBeforeDue(int soundAnnounceSecondsBeforeDue) {
		this.soundAnnounceSecondsBeforeDue = soundAnnounceSecondsBeforeDue;
	}

	public int getSoundAnnounceSecondsAfterDue() {
		return soundAnnounceSecondsAfterDue;
	}

	public void setSoundAnnounceSecondsAfterDue(int soundAnnounceSecondsAfterDue) {
		this.soundAnnounceSecondsAfterDue = soundAnnounceSecondsAfterDue;
	}

	public String getSoundFilesLocation() {
		return soundFilesLocation;
	}

	public String getTrayIconImage() {
		return trayIconImage;
	}

}