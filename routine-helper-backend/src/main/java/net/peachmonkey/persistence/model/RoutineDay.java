package net.peachmonkey.persistence.model;

import java.time.DayOfWeek;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RoutineDay {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	@Enumerated(EnumType.STRING)
	private DayOfWeek day;

	public DayOfWeek getDay() {
		return day;
	}

	public void setDay(DayOfWeek day) {
		this.day = day;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof RoutineDay)) {
			return false;
		}
		RoutineDay castOther = (RoutineDay) other;
		return Objects.equals(day, castOther.day);
	}

	@Override
	public int hashCode() {
		return Objects.hash(day);
	}

	@Override
	public String toString() {
		return "RoutineDay [day=" + day + "]";
	}
}
