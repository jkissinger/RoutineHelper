package net.peachmonkey.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RoutineTask {

	private String name;
	private LocalTime notifyTime;
	private LocalTime warningTime;
	private LocalTime alarmTime;
	private Map<String, LocalDateTime> completionTimes = Collections.synchronizedMap(new HashMap<>());
	private List<String> users = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalTime getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = LocalTime.parse(notifyTime).plusNanos(1);
	}

	public LocalTime getWarningTime() {
		return warningTime;
	}

	public void setWarningTime(String warningTime) {
		this.warningTime = LocalTime.parse(warningTime).plusNanos(1);
	}

	public LocalTime getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = LocalTime.parse(alarmTime).plusNanos(1);
	}

	public LocalDateTime getLastCompletedTime(String user) {
		return completionTimes.get(user.toUpperCase());
	}

	public void setLastCompletedTime(String user, LocalDateTime lastCompletedTime) {
		completionTimes.put(user.toUpperCase(), lastCompletedTime);
	}

	public List<String> getUsers() {
		return users;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof RoutineTask)) {
			return false;
		}
		RoutineTask castOther = (RoutineTask) other;
		return Objects.equals(name, castOther.name) && Objects.equals(notifyTime, castOther.notifyTime)
				&& Objects.equals(warningTime, castOther.warningTime) && Objects.equals(alarmTime, castOther.alarmTime)
				&& Objects.equals(completionTimes, castOther.completionTimes) && Objects.equals(users, castOther.users);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, notifyTime, warningTime, alarmTime, completionTimes, users);
	}

	@Override
	public String toString() {
		return "RoutineTask [name=" + name + ", notifyTime=" + notifyTime + ", warningTime=" + warningTime
				+ ", alarmTime=" + alarmTime + ", completionTimes=" + completionTimes + ", users=" + users + "]";
	}
}
