package net.peachmonkey;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;

import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.RoutineUser;

public class Constants {

	public enum TaskStatus {
		INVALID, PENDING, DUE, OVERDUE, COMPLETED;
	}

	public static final String FILENAME_SEPARATOR = "-";
	public static final String AUDIO_FILE_EXTENSION = ".wav";

	public static final class Sounds {

		public static final String TASKS_DIR = "tasks";
		public static final String USERS_DIR = "users";
		public static final String SYSTEM_DIR = "system";
		public static final String COMPLETION = "completed";
		public static final String ONLINE = "online";
		public static final String MISSING = "missing";
		public static final String ERROR = "error";

	}

	public static final ParameterizedTypeReference<List<RoutineUser>> userList() {
		return new ParameterizedTypeReference<List<RoutineUser>>() {
		};
	}

	public static final ParameterizedTypeReference<List<CompletedTask>> completedTaskList() {
		return new ParameterizedTypeReference<List<CompletedTask>>() {
		};
	}
}
