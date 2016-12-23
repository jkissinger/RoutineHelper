package net.peachmonkey.model;

import net.peachmonkey.RoutineUtils.TaskStatus;

public class IncompleteTask {

	private String name;
	private String user;
	private TaskStatus status;
	private RoutineTask actualTask;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public RoutineTask getActualTask() {
		return actualTask;
	}

	public void setActualTask(RoutineTask actualTask) {
		this.actualTask = actualTask;
	}

	@Override
	public String toString() {
		return "IncompleteTask [name=" + name + ", user=" + user + ", status=" + status + ", actualTask=" + actualTask + "]";
	}
}
