package net.peachmonkey.persistence.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RoutineUser {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof RoutineUser)) {
			return false;
		}
		RoutineUser castOther = (RoutineUser) other;
		return Objects.equals(name, castOther.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "RoutineUser [id=" + id + ", name=" + name + "]";
	}
}
