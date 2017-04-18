package net.peachmonkey.persistence.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CompletedTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private RoutineUser user;
	@ManyToOne
	private RoutineTask task;
	private LocalDateTime completionTime;

	public long getId() {
		return id;
	}

	public RoutineUser getUser() {
		return user;
	}

	public void setUser(RoutineUser user) {
		this.user = user;
	}

	public RoutineTask getTask() {
		return task;
	}

	public void setTask(RoutineTask task) {
		this.task = task;
	}

	public LocalDateTime getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(LocalDateTime completionTime) {
		this.completionTime = completionTime;
	}

	@Override
	public String toString() {
		return "CompletedTask [id=" + id + ", user=" + user + ", task=" + task + ", completionTime=" + completionTime + "]";
	}

}