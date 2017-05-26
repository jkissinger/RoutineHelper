package net.peachmonkey.persistence.model;

import java.time.LocalTime;

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
	private LocalTime dueTime;
	@ManyToOne
	private RoutineUser user;

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
	public String toString() {
		return "PendingTask [id=" + id + ", name=" + name + ", dueTime=" + dueTime + ", user=" + user + "]";
	}

}
