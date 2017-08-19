package net.peachmonkey.persistence.model;

import java.time.LocalDateTime;
import java.util.Objects;

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
	private String name;
	private LocalDateTime dueTime;
	@ManyToOne
	private RoutineUser user;
	private Cause cause;
	private LocalDateTime completionTime;

	public enum Cause {
		COMPLETED, CANCELLED, EXPIRED, REASSIGNED;
	}

	public Long getId() {
		return id;
	}

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

	public Cause getCause() {
		return cause;
	}

	public void setCause(Cause cause) {
		this.cause = cause;
	}

	public LocalDateTime getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(LocalDateTime completionTime) {
		this.completionTime = completionTime;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof CompletedTask)) {
			return false;
		}
		CompletedTask castOther = (CompletedTask) other;
		return Objects.equals(name, castOther.name) && Objects.equals(user, castOther.user) && Objects.equals(completionTime, castOther.completionTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, user, completionTime);
	}

	@Override
	public String toString() {
		return "CompletedTask [id=" + id + ", name=" + name + ", dueTime=" + dueTime + ", user=" + user + ", cause=" + cause + ", completionTime=" + completionTime + "]";
	}

}
