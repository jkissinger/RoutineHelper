package net.peachmonkey.persistence.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RoutineHoliday {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String name;
	private LocalDate date;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof RoutineHoliday)) {
			return false;
		}
		RoutineHoliday castOther = (RoutineHoliday) other;
		return Objects.equals(name, castOther.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "RoutineHoliday [id=" + id + ", name=" + name + ", date=" + date + "]";
	}
}
