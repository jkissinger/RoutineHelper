package net.peachmonkey.persistence.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Routine {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String name;
	@OneToMany
	private List<RoutineTask> tasks = new ArrayList<>();
	@ManyToMany
	private List<RoutineDay> days = new ArrayList<>();
	@ManyToMany
	private List<RoutineHoliday> holidays = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RoutineTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<RoutineTask> tasks) {
		this.tasks = tasks;
	}

	public List<RoutineDay> getDays() {
		return days;
	}

	public void setDays(List<RoutineDay> days) {
		this.days = days;
	}

	public List<RoutineHoliday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<RoutineHoliday> holidays) {
		this.holidays = holidays;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Routine)) {
			return false;
		}
		Routine castOther = (Routine) other;
		return Objects.equals(name, castOther.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "Routine [id=" + id + ", name=" + name + ", tasks=" + tasks + ", days=" + days + ", holidays=" + holidays + "]";
	}
}
