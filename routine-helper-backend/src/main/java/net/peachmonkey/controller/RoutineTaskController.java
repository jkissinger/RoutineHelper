package net.peachmonkey.controller;

import java.util.List;

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
		RoutineUser user = userRepo.findOne(userId);
		if (!task.getUsers().contains(user)) {
			task.getUsers().add(user);
			return repo.save(task);
		} else {
			return task;
		}
	}

	@RequestMapping(value = "/removeUserFromTask", method = RequestMethod.POST)
	public RoutineTask removeUserFromTask(Long taskId, Long userId) {
		RoutineTask task = repo.findOne(taskId);
		RoutineUser user = userRepo.findOne(userId);
		if (task.getUsers().contains(user)) {
			task.getUsers().remove(user);
			return repo.save(task);
		} else {
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
			return repo.save(routineTask);
		} else {
			if (StringUtils.hasText(routineTask.getName())) {
				persisted.setName(routineTask.getName());
			}
			if (routineTask.getNotifyTime() != null) {
				persisted.setNotifyTime(routineTask.getNotifyTime());
			}
			if (routineTask.getWarningTime() != null) {
				persisted.setWarningTime(routineTask.getWarningTime());
			}
			if (routineTask.getAlarmTime() != null) {
				persisted.setAlarmTime(routineTask.getAlarmTime());
			}
			if (routineTask.getUsers() != null) {
				persisted.setUsers(routineTask.getUsers());
			}
			return repo.save(persisted);
		}
	}

	@RequestMapping(value = "/deleteRoutineTask", method = RequestMethod.DELETE)
	public void deleteRoutineTask(Long id) {
		repo.delete(id);
	}
}
