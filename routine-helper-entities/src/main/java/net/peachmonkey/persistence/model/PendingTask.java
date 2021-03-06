package net.peachmonkey.persistence.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PendingTask {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private LocalDateTime dueTime;
	@ManyToOne
	private RoutineUser user;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDueTime() {
		return dueTime;
	}

	public void setDueTime(LocalDateTime dueTime) {
		this.dueTime = dueTime;
	}

	public RoutineUser getUser() {
		return user;
	}

	public void setUser(RoutineUser user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PendingTask)) {
			return false;
		}
		PendingTask castOther = (PendingTask) other;
		return Objects.equals(name, castOther.name) && Objects.equals(user, castOther.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, user);
	}

	@Override
	public String toString() {
		return "PendingTask [id=" + id + ", name=" + name + ", dueTime=" + dueTime + ", user=" + user + "]";
	}

}
