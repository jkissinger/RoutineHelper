package net.peachmonkey;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.persistence.CompletedTaskRepository;
import net.peachmonkey.persistence.PendingTaskRepository;
import net.peachmonkey.persistence.RoutineRepository;
import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.CompletedTask.Cause;
import net.peachmonkey.persistence.model.PendingTask;
import net.peachmonkey.persistence.model.Routine;
import net.peachmonkey.persistence.model.RoutineDay;
import net.peachmonkey.persistence.model.RoutineHoliday;
import net.peachmonkey.persistence.model.RoutineTask;
import net.peachmonkey.persistence.model.RoutineUser;

@Component
public class TaskUtils {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private RoutineRepository routineRepo;
	@Autowired
	private PendingTaskRepository pendingRepo;
	@Autowired
	private CompletedTaskRepository completedRepo;

	@Transactional
	public void generatePendingTasks() {
		for (Routine routine : routineRepo.findAll()) {
			LocalDate today = LocalDate.now();
			List<DayOfWeek> routineDays = routine.getDays().stream().map(RoutineDay::getDay).collect(Collectors.toList());
			List<LocalDate> routineHolidays = routine.getHolidays().stream().map(RoutineHoliday::getDate).collect(Collectors.toList());
			if (routineDays.contains(today.getDayOfWeek()) && !routineHolidays.contains(today)) {
				generateTasksForRoutine(routine);
			}
		}
	}

	private void generateTasksForRoutine(Routine routine) {
		// TODO: Add check for already completed tasks
		for (RoutineTask task : routine.getTasks()) {
			for (RoutineUser user : task.getUsers()) {
				PendingTask pendingTask = new PendingTask();
				pendingTask.setTask(task);
				pendingTask.setUser(user);
				pendingRepo.save(pendingTask);
			}
		}
	}

	public CompletedTask completePendingTask(Long id, Cause cause) {
		PendingTask pendingTask = pendingRepo.findOne(id);
		CompletedTask completed = new CompletedTask();
		completed.setCause(cause);
		completed.setCompletionTime(LocalDateTime.now());
		completed.setTask(pendingTask.getTask());
		completed.setUser(pendingTask.getUser());
		pendingRepo.delete(pendingTask);
		LOGGER.debug("Deleted {}.", pendingTask);
		return completedRepo.save(completed);
	}

	public PendingTask reassignCompletedTask(Long id) {
		CompletedTask completed = completedRepo.findOne(id);
		completed.setCause(Cause.REASSIGNED);
		PendingTask pending = new PendingTask();
		pending.setTask(completed.getTask());
		pending.setUser(completed.getUser());
		completedRepo.save(completed);
		LOGGER.debug("Reassigned {}.", completed);
		return pendingRepo.save(pending);
	}
}
