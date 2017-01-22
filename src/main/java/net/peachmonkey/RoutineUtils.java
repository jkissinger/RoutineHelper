package net.peachmonkey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.model.IncompleteTask;
import net.peachmonkey.model.Routine;
import net.peachmonkey.model.RoutineTask;
import net.peachmonkey.properties.ApplicationProperties;

@Component
public class RoutineUtils {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private ApplicationProperties properties;
	private Set<String> users = new HashSet<>();
	private Map<String, Integer> routineDelays = Collections.synchronizedMap(new HashMap<>());

	@PostConstruct
	public void init() {
		for (Routine routine : properties.getRoutines()) {
			for (RoutineTask task : routine.getTasks()) {
				users.addAll(task.getUsers());
				task.setRoutine(routine);
			}
			addRoutineDelay(routine, 0);
		}
	}

	public enum TaskStatus {
		INVALID, NOTIFY, WARNING, ALARM;
	}

	public void addRoutineDelay(Routine routine, int seconds) {
		routineDelays.put(routine.getName(), seconds);
	}

	private List<RoutineTask> getAllTasks(boolean validRoutinesOnly) {
		List<RoutineTask> tasks = new ArrayList<>();
		for (Routine routine : properties.getRoutines()) {
			if (!validRoutinesOnly || isRoutineValid(routine)) {
				tasks.addAll(routine.getTasks());
			}
		}
		tasks.sort(Comparator.comparing(RoutineTask::getNotifyTime));
		return tasks;
	}

	private List<IncompleteTask> getAllIncompleteTasks() {
		List<IncompleteTask> incompleteTasks = new ArrayList<>();
		for (RoutineTask task : getAllTasks(true)) {
			incompleteTasks.addAll(getIncompleteTasks(task));
		}
		incompleteTasks.sort((t, o) -> t.getActualTask().getNotifyTime().compareTo(o.getActualTask().getNotifyTime()));
		return incompleteTasks;
	}

	private List<IncompleteTask> getIncompleteTasks(RoutineTask task) {
		List<IncompleteTask> incompleteTasks = new ArrayList<>();
		if (isTaskValid(task)) {
			for (String user : task.getUsers()) {
				IncompleteTask incompleteTask = getIncompleteTask(task, user);
				if (incompleteTask != null) {
					incompleteTasks.add(incompleteTask);
				}
			}
		}
		return incompleteTasks;
	}

	private IncompleteTask getIncompleteTask(RoutineTask task, String user) {
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
		if (task.getNotifyTime() == null) {
			LOGGER.warn("[{}] notify time is null", task);
		} else if (routineDelays == null) {
			LOGGER.warn("Routine delays is null", task);
		} else if (task.getRoutine() == null) {
			LOGGER.warn("[{}] routine is null", task);
		} else if (task.getRoutine().getName() == null) {
			LOGGER.warn("[{}] routine name is null", task.getRoutine());
		} else if (routineDelays.get(task.getRoutine().getName()) == null) {
			LOGGER.warn("routine delays is null for routine", task.getRoutine());
		}
		if (LocalTime.now().minusSeconds(properties.getMaxTaskAge()).isAfter(task.getNotifyTime())) {
			return false;
		} else if (LocalTime.now().isBefore(task.getNotifyTime().plusSeconds(routineDelays.get(task.getRoutine().getName())))) {
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

	public List<String> getUsersLowerCase(RoutineTask task) {
		List<String> lowerCaseUsers = new ArrayList<>();
		task.getUsers().forEach(u -> lowerCaseUsers.add(u.toLowerCase()));
		return lowerCaseUsers;
	}

	public List<RoutineTask> getTasksForUser(String user) {
		List<RoutineTask> tasks = new ArrayList<>(getAllTasks(true));
		tasks.removeIf(t -> !getUsersLowerCase(t).contains(user.toLowerCase()));
		return tasks;
	}

	public Set<String> getAllUsers() {
		Set<String> users = new HashSet<>();
		List<RoutineTask> tasks = getAllTasks(true);
		for (RoutineTask task : tasks) {
			users.addAll(task.getUsers());
		}
		return users;
	}

	public String getLabel(IncompleteTask task) {
		return task.getUser() + " - " + task.getName();
	}

	public String getUserFromLabel(String label) {
		return label.split("-")[0].trim();
	}

	public String getTaskFromLabel(String label) {
		return label.split("-")[1].trim();
	}
}
