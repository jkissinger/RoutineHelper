package net.peachmonkey.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.peachmonkey.persistence.CompletedTaskRepository;
import net.peachmonkey.persistence.model.CompletedTask;

@RestController
public class CompletedTaskController {

	@Autowired
	private CompletedTaskRepository repo;

	@RequestMapping(value = "/getCompletedTasks", method = RequestMethod.GET)
	public List<CompletedTask> getCompletedTasks() {
		return repo.findAll();
	}

	@RequestMapping(value = "/getTasksCompletedToday", method = RequestMethod.GET)
	public List<CompletedTask> getTasksCompletedToday() {
		LocalDateTime lastMidnight = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		LocalDateTime nextMidnight = lastMidnight.plusDays(1);
		return repo.findByCompletionTimeBetween(lastMidnight, nextMidnight);
	}
}
