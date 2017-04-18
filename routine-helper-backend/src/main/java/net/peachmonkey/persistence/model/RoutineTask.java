package net.peachmonkey.persistence.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class RoutineTask {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String name;
	@ManyToOne
	private Routine routine;
	private LocalTime notifyTime;
	private LocalTime warningTime;
	private LocalTime alarmTime;
	@ManyToMany
	private List<RoutineUser> users = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Routine getRoutine() {
		return routine;
	}

	public void setRoutine(Routine routine) {
		this.routine = routine;
	}

	public LocalTime getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(LocalTime notifyTime) {
		this.notifyTime = notifyTime;
	}

	public LocalTime getWarningTime() {
		return warningTime;
	}

	public void setWarningTime(LocalTime warningTime) {
		this.warningTime = warningTime;
	}

	public LocalTime getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(LocalTime alarmTime) {
		this.alarmTime = alarmTime;
	}

	public List<RoutineUser> getUsers() {
		return users;
	}

	public void setUsers(List<RoutineUser> users) {
		this.users = users;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof RoutineTask)) {
			return false;
		}
		RoutineTask castOther = (RoutineTask) other;
		return Objects.equals(name, castOther.name) && Objects.equals(routine, castOther.routine) && Objects.equals(notifyTime, castOther.notifyTime)
				&& Objects.equals(warningTime, castOther.warningTime) && Objects.equals(alarmTime, castOther.alarmTime)
				&& Objects.equals(users, castOther.users);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, routine, notifyTime, warningTime, alarmTime, users);
	}

	@Override
	public String toString() {
		return "RoutineTask [name=" + name + ", routine=" + routine + ", notifyTime=" + notifyTime + ", warningTime=" + warningTime + ", alarmTime="
				+ alarmTime + ", users=" + users + "]";
	}

}
