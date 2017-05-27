package net.peachmonkey.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.peachmonkey.TaskUtils;
import net.peachmonkey.persistence.CompletedTaskRepository;
import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.CompletedTask.Cause;
import net.peachmonkey.persistence.model.PendingTask;

@RestController
public class CompletedTaskController {

	@Autowired
	private CompletedTaskRepository repo;
	@Autowired
	private TaskUtils taskUtils;

	@RequestMapping(value = "/getCompletedTasks", method = RequestMethod.GET)
	public List<CompletedTask> getCompletedTasks() {
		return repo.findAll();
	}

	@RequestMapping(value = "/getTasksCompletedToday", method = RequestMethod.GET)
	public List<CompletedTask> getTasksCompletedToday() {
		LocalDateTime lastMidnight = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		LocalDateTime nextMidnight = lastMidnight.plusDays(1);
		List<CompletedTask> tasks = repo.findByCompletionTimeBetweenAndCauseNotIn(lastMidnight, nextMidnight, Arrays.asList(Cause.REASSIGNED, Cause.EXPIRED));
		tasks.sort(Comparator.comparing(CompletedTask::getCompletionTime));
		return tasks;
	}

	@RequestMapping(value = "/getTasksCompletedSince", method = RequestMethod.PUT)
	public List<CompletedTask> getTasksCompletedSince(@RequestBody LocalDateTime cutoff) {
		List<CompletedTask> tasks = repo.findByCompletionTimeAfterAndCauseNotIn(cutoff, Arrays.asList(Cause.REASSIGNED, Cause.EXPIRED));
		tasks.sort(Comparator.comparing(CompletedTask::getCompletionTime));
		return tasks;
	}

	@RequestMapping(value = "/reassignCompletedTask", method = RequestMethod.POST)
	public PendingTask reassignCompletedTask(Long id) {
		return taskUtils.reassignCompletedTask(id);
	}
}
