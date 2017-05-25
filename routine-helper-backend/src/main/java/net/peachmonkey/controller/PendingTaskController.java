package net.peachmonkey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.peachmonkey.TaskUtils;
import net.peachmonkey.persistence.PendingTaskRepository;
import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.CompletedTask.Cause;
import net.peachmonkey.persistence.model.PendingTask;

@RestController
public class PendingTaskController {

	@Autowired
	private PendingTaskRepository repo;
	@Autowired
	private TaskUtils taskUtils;

	@RequestMapping(value = "/getPendingTasks", method = RequestMethod.GET)
	public List<PendingTask> getPendingTasks() {
		return repo.findAll();
	}

	@RequestMapping(value = "/getPendingTask", method = RequestMethod.GET)
	public PendingTask getPendingTask(String userName, String taskName) {
		return repo.findOneByUserNameAndTaskName(userName, taskName);
	}

	@RequestMapping(value = "/generatePendingTasks", method = RequestMethod.POST)
	public void generatePendingTasks() {
		taskUtils.generatePendingTasks();
	}

	@RequestMapping(value = "/completePendingTask", method = RequestMethod.POST)
	public CompletedTask completePendingTask(Long id, Cause cause) {
		return taskUtils.completePendingTask(id, cause);
	}

	@RequestMapping(value = "/reassignCompletedTask", method = RequestMethod.POST)
	public PendingTask reassignCompletedTask(Long id) {
		return taskUtils.reassignCompletedTask(id);
	}
}
