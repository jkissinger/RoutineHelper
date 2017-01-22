package net.peachmonkey.properties;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import net.peachmonkey.model.Routine;

@ConfigurationProperties
public class ApplicationProperties extends SelfLoggingProperties {

	@Value("${interval.check:1}")
	private int checkInterval;
	@Value("${interval.notify:1}")
	private int notifyInterval;
	@Value("${interval.warn:1}")
	private int warnInterval;
	@Value("${interval.alarm:1}")
	private int alarmInterval;
	private Path audioDirectory;
	@Value("${maxTaskAge:3600}")
	private int maxTaskAge;
	@Value("${trayIconImage:icon.png}")
	private String trayIconImage;
	@Value("${spring.application.name}")
	private String applicationName;
	private List<Routine> routines = new ArrayList<>();

	public int getCheckInterval() {
		return checkInterval;
	}

	public int getNotifyInterval() {
		return notifyInterval;
	}

	public int getWarnInterval() {
		return warnInterval;
	}

	public int getAlarmInterval() {
		return alarmInterval;
	}

	public void setAudioDirectory(String audioDirectory) {
		this.audioDirectory = Paths.get(audioDirectory);
	}

	public Path getAudioDirectory() {
		return audioDirectory;
	}

	public int getMaxTaskAge() {
		return maxTaskAge;
	}

	public String getTrayIconImage() {
		return trayIconImage;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public List<Routine> getRoutines() {
		return routines;
	}
}