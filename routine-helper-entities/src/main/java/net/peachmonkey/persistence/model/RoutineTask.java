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

@Entity
public class RoutineTask {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String name;
	private LocalTime dueTime;
	@ManyToMany
	private List<RoutineUser> users = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalTime getDueTime() {
		return dueTime;
	}

	public void setDueTime(LocalTime dueTime) {
		this.dueTime = dueTime;
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
		return Objects.equals(name, castOther.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "RoutineTask [id=" + id + ", name=" + name + ", dueTime=" + dueTime + ", users=" + users + "]";
	}

}
