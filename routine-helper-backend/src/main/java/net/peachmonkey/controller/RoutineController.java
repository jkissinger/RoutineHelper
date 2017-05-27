package net.peachmonkey.controller;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger LOGGER = LogManager.getLogger();
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
		if (routine == null) {
			LOGGER.warn("Attempted to get tasks for non-existent Routine[id={}].", id);
			return new ArrayList<>();
		} else {
			routine.getTasks().size();
			return routine.getTasks();
		}
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
		if (routine == null) {
			LOGGER.warn("Attempted to add RoutineTask to non-existent Routine[id={}].", routineId);
			return null;
		} else {
			RoutineTask task = taskRepo.findOne(taskId);
			if (task == null) {
				LOGGER.warn("Attempted to add non-existent RoutineTask[id={}] to Routine[id={}].", taskId, routineId);
			} else if (!routine.getTasks().contains(task)) {
				routine.getTasks().add(task);
				return repo.save(routine);
			}
			return routine;
		}
	}

	@RequestMapping(value = "/removeTaskFromRoutine", method = RequestMethod.POST)
	public Routine removeTaskFromRoutine(Long routineId, Long taskId) {
		Routine routine = repo.findOne(routineId);
		if (routine == null) {
			LOGGER.warn("Attempted to remove RoutineTask[id={}] from non-existent Routine[id={}].", taskId, routineId);
			return null;
		} else {
			RoutineTask task = taskRepo.findOne(taskId);
			if (task == null) {
				LOGGER.warn("Attempted to remove non-existent RoutineTask[id={}] from Routine[id={}].", taskId, routineId);
			} else if (routine.getTasks().contains(task)) {
				routine.getTasks().remove(task);
				return repo.save(routine);
			}
			return routine;
		}
	}

	@RequestMapping(value = "/createOrUpdateRoutine", method = RequestMethod.POST)
	public Routine createOrUpdateRoutine(@RequestBody Routine routine) {
		Routine persisted = null;
		if (routine.getId() != null) {
			persisted = repo.findOne(routine.getId());
		}
		if (persisted == null && StringUtils.hasText(routine.getName())) {
			persisted = repo.findOneByName(routine.getName());
		}
		if (persisted == null) {
			LOGGER.info("Created new {}.", routine);
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
			LOGGER.info("Updated {}.", persisted);
			return repo.save(persisted);
		}
	}

	@RequestMapping(value = "/deleteRoutine", method = RequestMethod.DELETE)
	public void deleteRoutine(Long id) {
		repo.delete(id);
	}
}
