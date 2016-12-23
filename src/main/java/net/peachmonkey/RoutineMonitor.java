package net.peachmonkey;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.peachmonkey.RoutineUtils.TaskStatus;
import net.peachmonkey.audio.Announcer;
import net.peachmonkey.model.IncompleteTask;
import net.peachmonkey.properties.ApplicationProperties;

@Component
@Scope("prototype")
public class RoutineMonitor implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private RoutineUtils utils;
	@Autowired
	private Announcer announcer;
	private int iterations = 0;

	@Override
	public void run() {
		try {
			Set<IncompleteTask> tasks = utils.getIncompleteTasksUniquePerUser();
			for (IncompleteTask task : tasks) {
				if (iterations % getInterval(task.getStatus()) == 0) {
					LOGGER.info(task.getStatus() + ": Task [{}] for [{}]", task.getName(), task.getUser());
					announcer.announceTask(task.getUser(), task.getName(), task.getStatus());
				}
			}

			if (++iterations >= properties.getNotifyInterval()) {
				iterations = 0;
			}
		} catch (Exception e) {
			LOGGER.error("Caught exception running task.", e);
		}
	}

	public int getInterval(TaskStatus status) {
		if (status == TaskStatus.NOTIFY) {
			return properties.getNotifyInterval();
		} else if (status == TaskStatus.WARNING) {
			return properties.getWarnInterval();
		} else if (status == TaskStatus.ALARM) {
			return properties.getAlarmInterval();
		} else {
			return 0;
		}
	}
}
