package net.peachmonkey.routine;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;

import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.RoutineUser;

public class Constants {

	public enum TaskStatus {
		INVALID, NOTIFY, WARNING, ALARM, COMPLETED;
	}

	public static final String FILENAME_SEPARATOR = "-";
	public static final String FILENAME_EXTENSION = ".wav";
	public static final String COMPLETION = "completed";
	public static final String ONLINE_SOUND = "online";

	public static final ParameterizedTypeReference<List<RoutineUser>> userList() {
		return new ParameterizedTypeReference<List<RoutineUser>>() {
		};
	}

	public static final ParameterizedTypeReference<List<CompletedTask>> completedTaskList() {
		return new ParameterizedTypeReference<List<CompletedTask>>() {
		};
	}
}
