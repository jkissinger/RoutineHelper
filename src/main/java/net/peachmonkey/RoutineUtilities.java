package net.peachmonkey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.audio.Sound;
import net.peachmonkey.model.IncompleteTask;
import net.peachmonkey.model.Routine;
import net.peachmonkey.model.RoutineTask;
import net.peachmonkey.properties.ApplicationProperties;

@Component
public class RoutineUtilities {

	private static final Logger LOGGER = LogManager.getLogger();
	public static final String FILENAME_SEPARATOR = "-";
	public static final String FILENAME_EXTENSION = ".wav";
	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private Sound sound;
	private Set<String> users = new HashSet<>();

	@PostConstruct
	public void init() {
		for (Routine routine : properties.getRoutines()) {
			for (RoutineTask task : routine.getTasks()) {
				users.addAll(task.getUsers());
			}
		}
	}

	public enum TaskStatus {
		INVALID, NOTIFY, WARNING, ALARM;
	}

	private List<IncompleteTask> getAllIncompleteTasks() {
		List<IncompleteTask> incompleteTasks = new ArrayList<>();
		for (Routine routine : properties.getRoutines()) {
			// TODO: Check if routine is invalid instead here
			if (isRoutineValid(routine)) {
				for (RoutineTask task : routine.getTasks()) {
					incompleteTasks.addAll(getIncompleteTasks(routine, task));
				}
			}
		}
		incompleteTasks.sort((t, o) -> t.getActualTask().getNotifyTime().compareTo(o.getActualTask().getNotifyTime()));
		return incompleteTasks;
	}

	private List<IncompleteTask> getIncompleteTasks(Routine routine, RoutineTask task) {
		List<IncompleteTask> incompleteTasks = new ArrayList<>();
		if (isTaskValid(task)) {
			for (String user : task.getUsers()) {
				IncompleteTask incompleteTask = getIncompleteTask(routine, task, user);
				if (incompleteTask != null) {
					incompleteTasks.add(incompleteTask);
				}
			}
		}
		return incompleteTasks;
	}

	private IncompleteTask getIncompleteTask(Routine routine, RoutineTask task, String user) {
		if (!isTaskComplete(task, user)) {
			IncompleteTask incompleteTask = new IncompleteTask();
			incompleteTask.setActualTask(task);
			incompleteTask.setName(task.getName());
			incompleteTask.setUser(user);
			incompleteTask.setStatus(getTaskStatus(task));
			return incompleteTask;
		}
		return null;
	}

	public IncompleteTask getNextIncompleteTask(String user) {
		List<IncompleteTask> tasks = getAllIncompleteTasks();
		for (IncompleteTask task : tasks) {
			if (task.getUser().equalsIgnoreCase(user)) {
				return task;
			}
		}
		return null;
	}

	public boolean isTaskComplete(RoutineTask task, String user) {
		if (task.getLastCompletedTime(user) == null) {
			return false;
		} else if (task.getLastCompletedTime(user).isBefore(LocalDateTime.now().minusHours(12))) {
			return false;
		}

		return true;
	}

	private boolean isTaskValid(RoutineTask task) {
		if (LocalTime.now().minusSeconds(properties.getMaxTaskAge()).isAfter(task.getNotifyTime())) {
			return false;
		} else if (LocalTime.now().isBefore(task.getNotifyTime())) {
			return false;
		}

		return true;
	}

	public boolean isRoutineValid(Routine routine) {
		if (!routine.getDays().contains(LocalDate.now().getDayOfWeek())) {
			return false;
		}

		return true;
	}

	public TaskStatus getTaskStatus(RoutineTask task) {
		if (isTaskValid(task)) {
			if (task.getAlarmTime().isBefore(LocalTime.now())) {
				return TaskStatus.ALARM;
			} else if (task.getWarningTime().isBefore(LocalTime.now())) {
				return TaskStatus.WARNING;
			} else if (task.getNotifyTime().isBefore(LocalTime.now())) {
				return TaskStatus.NOTIFY;
			}
		}
		return TaskStatus.INVALID;
	}

	public Set<IncompleteTask> getIncompleteTasksUniquePerUser() {
		List<IncompleteTask> tasks = getAllIncompleteTasks();
		Set<IncompleteTask> incompleteTasks = new HashSet<>();
		Set<String> usersWithTasks = new HashSet<>();
		for (IncompleteTask task : tasks) {
			if (!usersWithTasks.contains(task.getUser())) {
				usersWithTasks.add(task.getUser());
				incompleteTasks.add(task);
			}
		}
		return incompleteTasks;
	}

	public void markNextTaskCompleteForUser(String user, boolean announce) {
		for (IncompleteTask task : getIncompleteTasksUniquePerUser()) {
			if (task.getUser().equalsIgnoreCase(user)) {
				LOGGER.debug("Marking [{}] complete for [{}].", task.getName(), user.toUpperCase());
				task.getActualTask().setLastCompletedTime(user, LocalDateTime.now());
				if (announce) {
					sound.announceTaskCompletion(user, task.getStatus());
				}
				return;
			}
		}

		LOGGER.debug("No incomplete tasks for user [{}].", user);
	}
}
