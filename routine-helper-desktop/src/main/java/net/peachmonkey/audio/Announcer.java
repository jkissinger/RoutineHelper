package net.peachmonkey.audio;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import net.peachmonkey.Constants.Sounds;
import net.peachmonkey.Constants.TaskStatus;
import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.PendingTask;
import net.peachmonkey.properties.ApplicationProperties;

@Component
public class Announcer {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private ApplicationProperties props;
	@Autowired
	private AudioUtils audioUtils;
	@Autowired
	private WavPlayer wavPlayer;
	private Map<PendingTask, Instant> announceTimes = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		Assert.notNull(props.getSoundQuietTimeStart(), "Start quiet time is not defined");
		Assert.notNull(props.getSoundQuietTimeEnd(), "End quiet time is not defined");

		wavPlayer.play(audioUtils.getSystemSoundFile(Sounds.ONLINE));
	}

	public void announcePendingTask(PendingTask task) {
		long secondsUntilDue = getSecondsUntilDue(task.getDueTime());
		TaskStatus status = getTaskStatus(secondsUntilDue);
		if (isAnnounceable(task, status, secondsUntilDue)) {
			announceTask(task, status);
		}
	}

	private long getSecondsUntilDue(LocalDateTime dueTime) {
		return Duration.between(LocalDateTime.now(), dueTime).getSeconds();
	}

	private boolean isAnnounceable(PendingTask task, TaskStatus status, long secondsUntilDue) {
		if (props.getSoundQuietTimeStart().isBefore(LocalTime.now()) || props.getSoundQuietTimeEnd().isAfter(LocalTime.now())) {
			// In the quiet time, nothing is announceable.
			LOGGER.trace("Task is not announceable because it is quiet time. {}", task);
			return false;
		}

		if (secondsUntilDue > props.getSoundAnnounceSecondsBeforeDue() || secondsUntilDue * -1 > props.getSoundAnnounceSecondsAfterDue()) {
			// Not ready to announce yet, or too old to still be announced.
			LOGGER.trace("Task is not announceable because it is not ready to be announced yet or too old to be announced. {}, seconds until due={}", task, secondsUntilDue);
			return false;
		}

		long secondsSinceLastAnnouncement = Integer.MAX_VALUE;
		if (announceTimes.containsKey(task)) {
			secondsSinceLastAnnouncement = Duration.between(announceTimes.get(task), Instant.now()).getSeconds();
		}
		long maxSeconds = 0;
		if (status == TaskStatus.PENDING) {
			maxSeconds = props.getSoundDelayPending();
		} else if (status == TaskStatus.DUE) {
			maxSeconds = props.getSoundDelayDue();
		} else if (status == TaskStatus.OVERDUE) {
			maxSeconds = props.getSoundDelayOverdue();
		}
		return secondsSinceLastAnnouncement >= maxSeconds;
	}

	private TaskStatus getTaskStatus(long secondsUntilDue) {
		if (secondsUntilDue > 0) {
			return TaskStatus.PENDING;
		} else if (secondsUntilDue * -1 > props.getTaskStatusOverdueSeconds()) {
			return TaskStatus.OVERDUE;
		} else {
			return TaskStatus.DUE;
		}
	}

	public void announceTask(PendingTask task, TaskStatus priority) {
		LOGGER.trace("Announcing [{}] for [{}].", task.getName(), task.getUser().getName());
		wavPlayer.play(audioUtils.getUserSoundFile(task.getUser().getName()));
		wavPlayer.play(audioUtils.getTaskSoundFile(task.getName()));
		wavPlayer.play(audioUtils.getSystemSoundFile(priority.name()));
		announceTimes.put(task, Instant.now());
	}

	public void announceTaskCompletion(CompletedTask task) {
		LOGGER.trace("Announcing Completion of [{}] for [{}]", task.getName(), task.getUser().getName());
		TaskStatus status = getTaskStatus(getSecondsUntilDue(task.getDueTime()));
		wavPlayer.play(audioUtils.getUserSoundFile(task.getUser().getName()));
		wavPlayer.play(audioUtils.getSystemSoundFile(status.name(), Sounds.COMPLETION));
	}

}