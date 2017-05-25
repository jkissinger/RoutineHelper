package net.peachmonkey.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.peachmonkey.persistence.RoutineRepository;
import net.peachmonkey.persistence.RoutineTaskRepository;
import net.peachmonkey.persistence.model.Routine;
import net.peachmonkey.persistence.model.RoutineTask;

@RestController
public class RoutineController {

	@Autowired
	private RoutineRepository repo;
	@Autowired
	private RoutineTaskRepository taskRepo;

	@RequestMapping(value = "/getRoutines", method = RequestMethod.GET)
	public List<Routine> getRoutines() {
		return repo.findAll();
	}

	@RequestMapping(value = "/getRoutine", method = RequestMethod.GET)
	public Routine getRoutine(Long id) {
		return repo.findOne(id);
	}

	@Transactional
	@RequestMapping(value = "/getTasksForRoutine", method = RequestMethod.GET)
	public List<RoutineTask> getTasksForRoutine(Long id) {
		Routine routine = repo.findOne(id);
		routine.getTasks().size();
		return routine.getTasks();
	}

	@RequestMapping(value = "/getTasksNotForRoutine", method = RequestMethod.GET)
	public List<RoutineTask> getTasksNotForRoutine(Long id) {
		List<RoutineTask> tasks = taskRepo.findAll();
		tasks.removeAll(getTasksForRoutine(id));
		return tasks;
	}

	@RequestMapping(value = "/addTaskToRoutine", method = RequestMethod.POST)
	public Routine addTaskToRoutine(Long routineId, Long taskId) {
		Routine routine = repo.findOne(routineId);
		RoutineTask task = taskRepo.findOne(taskId);
		if (!routine.getTasks().contains(task)) {
			routine.getTasks().add(task);
			return repo.save(routine);
		} else {
			return routine;
		}
	}

	@RequestMapping(value = "/removeTaskFromRoutine", method = RequestMethod.POST)
	public Routine removeTaskFromRoutine(Long routineId, Long taskId) {
		Routine routine = repo.findOne(routineId);
		RoutineTask task = taskRepo.findOne(taskId);
		if (routine.getTasks().contains(task)) {
			routine.getTasks().remove(task);
			return repo.save(routine);
		} else {
			return routine;
		}
	}

	@RequestMapping(value = "/createOrUpdateRoutine", method = RequestMethod.POST)
	public Routine createOrUpdateUser(@RequestBody Routine routine) {
		Routine persisted = null;
		if (routine.getId() != null) {
			persisted = repo.findOne(routine.getId());
		}
		if (persisted == null && StringUtils.hasText(routine.getName())) {
			persisted = repo.findOneByName(routine.getName());
		}
		if (persisted == null) {
			return repo.save(routine);
		} else {
			if (StringUtils.hasText(routine.getName())) {
				persisted.setName(routine.getName());
			}
			if (routine.getDays() != null) {
				persisted.setDays(routine.getDays());
			}
			if (routine.getHolidays() != null) {
				persisted.setHolidays(routine.getHolidays());
			}
			if (routine.getTasks() != null) {
				persisted.setTasks(routine.getTasks());
			}
			return repo.save(persisted);
		}
	}

	@RequestMapping(value = "/deleteRoutine", method = RequestMethod.DELETE)
	public void deleteRoutine(Long id) {
		repo.delete(id);
	}
}
