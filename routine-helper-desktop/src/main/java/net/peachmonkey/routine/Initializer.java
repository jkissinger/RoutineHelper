package net.peachmonkey.routine;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.peachmonkey.audio.Announcer;
import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.PendingTask;
import net.peachmonkey.persistence.model.RoutineUser;
import net.peachmonkey.properties.ApplicationProperties;
import net.peachmonkey.rest.RoutineTemplate;
import net.peachmonkey.routine.Constants.TaskStatus;

@Component
public class Initializer {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private ApplicationProperties props;
	@Autowired
	private RoutineTemplate routineTemplate;
	@Autowired
	private Announcer announcer;
	private LocalDateTime lastCompletionAnnouncement = LocalDateTime.now();

	@Scheduled(fixedDelay = 5000)
	public void init() {
		try {
			announcePendingTasks();
		} catch (RuntimeException e) {
			LOGGER.error("Exception announcing pending tasks.", e);
		}
		try {
			announceCompletedTasks();
		} catch (RuntimeException e) {
			LOGGER.error("Exception announcing completed tasks.", e);
		}
	}

	private void announcePendingTasks() {
		List<RoutineUser> users = routineTemplate.getRoutineUsers();
		for (RoutineUser user : users) {
			PendingTask nextTask = routineTemplate.getNextPendingTask(user.getName());
			if (nextTask != null) {
				announcePendingTask(nextTask);
			}
		}
	}

	private void announcePendingTask(PendingTask nextTask) {
		long secondsUntilDue = Duration.between(Instant.now(), nextTask.getDueTime().toInstant(ZoneOffset.UTC)).getSeconds();
		if (secondsUntilDue < props.getSoundAnnounceSecondsBeforeDue() && secondsUntilDue < props.getSoundAnnounceSecondsAfterDue() * -1) {
			LOGGER.debug("Announcing [{}] for [{}].", nextTask.getName(), nextTask.getUser().getName());
			announcer.announceTask(nextTask.getUser().getName(), nextTask.getName(), TaskStatus.NOTIFY);
		} else {
			LOGGER.trace("Skipping announcement of pending task [{}] for [{}].", nextTask.getName(), nextTask.getUser().getName());
		}
	}

	private void announceCompletedTasks() {
		List<CompletedTask> tasks = routineTemplate.getCompletedTasksSince(lastCompletionAnnouncement);
		lastCompletionAnnouncement = LocalDateTime.now();
		for (CompletedTask task : tasks) {
			// Announce the actual task as well?
			// TODO: Announce cancelled tasks differently
			announcer.announceTaskCompletion(task.getUser().getName(), TaskStatus.NOTIFY);
		}
	}
}