package net.peachmonkey.controller;

import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private PendingTaskRepository repo;
	@Autowired
	private TaskUtils taskUtils;

	@RequestMapping(value = "/getPendingTasks", method = RequestMethod.GET)
	public List<PendingTask> getPendingTasks() {
		List<PendingTask> tasks = repo.findAll();
		tasks.sort(Comparator.comparing(PendingTask::getDueTime));
		return tasks;
	}

	@RequestMapping(value = "/getPendingTask", method = RequestMethod.GET)
	public PendingTask getPendingTask(String userName, String taskName) {
		return repo.findOneByUserNameAndName(userName, taskName);
	}

	@RequestMapping(value = "/generatePendingTasks", method = RequestMethod.POST)
	public void generatePendingTasks() {
		taskUtils.generatePendingTasks();
	}

	@RequestMapping(value = "/completePendingTask", method = RequestMethod.POST)
	public CompletedTask completePendingTask(Long id, Cause cause) {
		return taskUtils.completePendingTask(id, cause);
	}

	@RequestMapping(value = "/updatePendingTask", method = RequestMethod.POST)
	public PendingTask updatePendingTask(@RequestBody PendingTask task) {
		if (task == null || task.getId() == null) {
			LOGGER.warn("Could not update {}.", task);
			return null;
		}

		PendingTask persisted = repo.findOne(task.getId());
		if (persisted == null) {
			LOGGER.warn("Attempted to update non-existent PendingTask[id={}].", task.getId());
			return null;
		}

		persisted.setDueTime(task.getDueTime());
		return repo.save(persisted);
	}
}
