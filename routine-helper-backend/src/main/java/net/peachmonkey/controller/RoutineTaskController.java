package net.peachmonkey.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.peachmonkey.persistence.RoutineTaskRepository;
import net.peachmonkey.persistence.RoutineUserRepository;
import net.peachmonkey.persistence.model.RoutineTask;
import net.peachmonkey.persistence.model.RoutineUser;

@RestController
public class RoutineTaskController {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private RoutineTaskRepository repo;
	@Autowired
	private RoutineUserRepository userRepo;

	@RequestMapping(value = "/getRoutineTasks", method = RequestMethod.GET)
	public List<RoutineTask> getRoutineTasks() {
		return repo.findAll();
	}

	@RequestMapping(value = "/getRoutineTask", method = RequestMethod.GET)
	public RoutineTask getRoutine(Long id) {
		return repo.findOne(id);
	}

	@RequestMapping(value = "/addUserToTask", method = RequestMethod.POST)
	public RoutineTask addUserToTask(Long taskId, Long userId) {
		RoutineTask task = repo.findOne(taskId);
		if (task == null) {
			LOGGER.warn("Attempted to add RoutineUser[id={}] to non-existent RoutineTask[id={}].", userId, taskId);
			return null;
		} else {
			RoutineUser user = userRepo.findOne(userId);
			if (user == null) {
				LOGGER.warn("Attempted to add non-existent RoutineUser[id={}] to RoutineTask[id={}].", userId, taskId);
			} else if (!task.getUsers().contains(user)) {
				task.getUsers().add(user);
				return repo.save(task);
			}
			return task;
		}
	}

	@RequestMapping(value = "/removeUserFromTask", method = RequestMethod.POST)
	public RoutineTask removeUserFromTask(Long taskId, Long userId) {
		RoutineTask task = repo.findOne(taskId);
		if (task == null) {
			LOGGER.warn("Attempted to remove RoutineUser[id={}] from non-existent RoutineTask[id={}].", userId, taskId);
			return null;
		} else {
			RoutineUser user = userRepo.findOne(userId);
			if (user == null) {
				LOGGER.warn("Attempted to remove non-existent RoutineUser[id={}] from RoutineTask[id={}].", userId, taskId);
			} else if (task.getUsers().contains(user)) {
				task.getUsers().remove(user);
				return repo.save(task);
			}
			return task;
		}
	}

	@RequestMapping(value = "/createOrUpdateTask", method = RequestMethod.POST)
	public RoutineTask createOrUpdateTask(@RequestBody RoutineTask routineTask) {
		RoutineTask persisted = null;
		if (routineTask.getId() != null) {
			persisted = repo.findOne(routineTask.getId());
		}
		if (persisted == null && StringUtils.hasText(routineTask.getName())) {
			persisted = repo.findOneByName(routineTask.getName());
		}
		if (persisted == null) {
			LOGGER.info("Created new {}.", routineTask);
			return repo.save(routineTask);
		} else {
			if (StringUtils.hasText(routineTask.getName())) {
				persisted.setName(routineTask.getName());
			}
			if (routineTask.getDueTime() != null) {
				persisted.setDueTime(routineTask.getDueTime());
			}
			if (routineTask.getUsers() != null) {
				persisted.setUsers(routineTask.getUsers());
			}
			LOGGER.info("Updated {}.", persisted);
			return repo.save(persisted);
		}
	}

	@RequestMapping(value = "/deleteRoutineTask", method = RequestMethod.DELETE)
	public void deleteRoutineTask(Long id) {
		repo.delete(id);
	}
}
