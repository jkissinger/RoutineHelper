package net.peachmonkey;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.audio.Announcer;
import net.peachmonkey.controller.RoutineHelperController;
import net.peachmonkey.model.IncompleteTask;
import net.peachmonkey.model.Routine;
import net.peachmonkey.model.RoutineTask;
import net.peachmonkey.properties.ApplicationProperties;

@Component
public class RoutineManager {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private Announcer announcer;
	@Autowired
	private RoutineUtils utils;
	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private RoutineHelperController controller;

	public boolean markNextTaskCompleteForUser(String user, boolean announce) {
		for (IncompleteTask task : utils.getIncompleteTasksUniquePerUser()) {
			if (task.getUser().equalsIgnoreCase(user)) {
				task.getActualTask().setLastCompletedTime(user, LocalDateTime.now());
				if (announce) {
					announcer.announceTaskCompletion(user, task.getStatus());
				}
				controller.completeTask(task);
				LOGGER.trace("Logged completion of task [{}] for [{}].", task.getName(), user.toUpperCase());
				return true;
			}
		}

		LOGGER.debug("No incomplete tasks for user [{}].", user);
		return false;
	}

	public void markAllTasksComplete(String user) {
		while (utils.getNextIncompleteTask(user) != null) {
			markNextTaskCompleteForUser(user, false);
		}
	}

	public void undoLastTask(String user) {
		List<RoutineTask> tasks = utils.getTasksForUser(user);
		LOGGER.debug("Found [{}] tasks for user [{}].", tasks.size(), user);
		Collections.reverse(tasks);
		for (RoutineTask task : tasks) {
			if (utils.isTaskComplete(task, user)) {
				task.setLastCompletedTime(user, null);
				LOGGER.info("Task [{}] marked incomplete for [{}].", task.getName(), user);
				return;
			}
		}
	}

	public void addRoutineDelay(String routineDelay) {
		if (routineDelay.split(" ").length == 2) {
			String routineName = routineDelay.split(" ")[0];
			String delay = routineDelay.split(" ")[1];
			if (NumberUtils.isParsable(delay)) {
				addRoutineDelay(routineName, Integer.parseInt(delay));
			} else {
				LOGGER.warn("The specified delay is non-numeric [{}]", delay);
			}
		} else {
			LOGGER.warn("Could not parse Routine Delay command, expected routine name followed by the delay in seconds, got [{}]", routineDelay);
		}
	}

	private void addRoutineDelay(String routineName, int seconds) {
		Routine routine = null;
		for (Routine r : properties.getRoutines()) {
			if (r.getName().equalsIgnoreCase(routineName)) {
				routine = r;
			}
		}
		if (routine == null) {
			LOGGER.warn("No routine found with name [{}]", routineName);
		} else {
			utils.addRoutineDelay(routine, seconds);
			LOGGER.info("Added temporary delay of [{}] seconds to routine [{}]", seconds, routine.getName());
		}
	}
}
