package net.peachmonkey.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PendingTask {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private RoutineTask task;
	@ManyToOne
	private RoutineUser user;

	public RoutineTask getTask() {
		return task;
	}

	public void setTask(RoutineTask task) {
		this.task = task;
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
		return "PendingTask [id=" + id + ", task=" + task + ", user=" + user + "]";
	}

}
