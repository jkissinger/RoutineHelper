package net.peachmonkey.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Routine {

	private String name;
	private List<RoutineTask> tasks = new ArrayList<>();
	private List<DayOfWeek> days = new ArrayList<>();
	private List<LocalDate> holidays = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RoutineTask> getTasks() {
		return tasks;
	}

	public List<DayOfWeek> getDays() {
		return days;
	}

	public List<LocalDate> getHolidays() {
		return holidays;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Routine)) {
			return false;
		}
		Routine castOther = (Routine) other;
		return Objects.equals(name, castOther.name) && Objects.equals(tasks, castOther.tasks) && Objects.equals(days, castOther.days)
				&& Objects.equals(holidays, castOther.holidays);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, tasks, days, holidays);
	}

	@Override
	public String toString() {
		return "Routine [name=" + name + ", tasks=" + tasks + ", days=" + days + ", holidays=" + holidays + "]";
	}
}
