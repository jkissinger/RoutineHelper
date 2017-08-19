package net.peachmonkey.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.peachmonkey.TaskUtils;
import net.peachmonkey.persistence.CompletedTaskRepository;
import net.peachmonkey.persistence.RoutineUserRepository;
import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.CompletedTask.Cause;
import net.peachmonkey.persistence.model.PendingTask;
import net.peachmonkey.persistence.model.RoutineUser;

@RestController
public class CompletedTaskController {

	@Autowired
	private CompletedTaskRepository repo;
	@Autowired
	private RoutineUserRepository userRepo;
	@Autowired
	private TaskUtils taskUtils;
	private static final Comparator<CompletedTask> COMPLETED_COMPARATOR = Comparator.comparing(CompletedTask::getCompletionTime).reversed();

	@RequestMapping(value = "/getCompletedTasks", method = RequestMethod.GET)
	public List<CompletedTask> getCompletedTasks() {
		List<CompletedTask> tasks = repo.findAll();
		tasks.sort(COMPLETED_COMPARATOR);
		return tasks;
	}

	@RequestMapping(value = "/getTasksCompletedToday", method = RequestMethod.GET)
	public List<CompletedTask> getTasksCompletedToday() {
		LocalDateTime lastMidnight = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		LocalDateTime nextMidnight = lastMidnight.plusDays(1);
		List<CompletedTask> tasks = repo.findByCompletionTimeBetweenAndCauseNotIn(lastMidnight, nextMidnight, Arrays.asList(Cause.REASSIGNED, Cause.EXPIRED));
		tasks.sort(COMPLETED_COMPARATOR);
		return tasks;
	}

	@RequestMapping(value = "/getTasksCompletedSince", method = RequestMethod.PUT)
	public List<CompletedTask> getTasksCompletedSince(@RequestBody LocalDateTime cutoff) {
		List<CompletedTask> tasks = repo.findByCompletionTimeAfterAndCauseNotIn(cutoff, Arrays.asList(Cause.REASSIGNED, Cause.EXPIRED));
		tasks.sort(COMPLETED_COMPARATOR);
		return tasks;
	}

	@RequestMapping(value = "/reassignCompletedTask", method = RequestMethod.POST)
	public PendingTask reassignCompletedTask(Long id) {
		return taskUtils.reassignCompletedTask(id);
	}

	@RequestMapping(value = "/getCompletedTasksByCauseAndName", method = RequestMethod.GET)
	public List<CompletedTask> getCompletedTasksByCauseAndName(@RequestParam Cause cause, @RequestParam String name) {
		List<CompletedTask> tasks = repo.findByCauseAndName(cause, name);
		tasks.sort(COMPLETED_COMPARATOR);
		return tasks;
	}

	@RequestMapping(value = "/getCompletedTasksByCauseAndNameAndUserName", method = RequestMethod.GET)
	public List<CompletedTask> getCompletedTasksByCauseAndNameAndUserName(@RequestParam Cause cause, @RequestParam String name, @RequestParam String userName) {
		List<CompletedTask> tasks = repo.findByCauseAndNameAndUserName(cause, name, userName);
		tasks.sort(COMPLETED_COMPARATOR);
		return tasks;
	}

	@RequestMapping(value = "/getAverageCompletionTime", method = RequestMethod.GET)
	public LocalTime getAverageCompletionTime(@RequestParam Cause cause, @RequestParam String name, @RequestParam String userName) {
		List<CompletedTask> tasks = repo.findByCauseAndNameAndUserName(cause, name, userName);
		Long totalSeconds = 0L;
		for (CompletedTask task : tasks) {
			totalSeconds += task.getCompletionTime().toLocalTime().toSecondOfDay();
		}
		if (totalSeconds > 0L) {
			return LocalTime.ofSecondOfDay(totalSeconds / tasks.size());
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/getAverageCompletionTimes", method = RequestMethod.GET)
	public Map<String, LocalTime> getAverageCompletionTimes(@RequestParam Cause cause, @RequestParam String name) {
		Map<String, LocalTime> times = new HashMap<>();
		for (RoutineUser user : userRepo.findAll()) {
			LocalTime avgTime = getAverageCompletionTime(cause, name, user.getName());
			if (avgTime != null) {
				times.put(user.getName(), avgTime);
			}
		}
		return times;
	}
}
