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
import org.springframework.scheduling.annotation.Scheduled;
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

	@Scheduled(cron = "0 0 23 * * *")
	public void expirePendingTasks() {
		for (PendingTask task : pendingRepo.findAll()) {
			completePendingTask(task, Cause.EXPIRED);
		}
	}

	@Scheduled(cron = "0 0 1 * * *")
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
				pendingTask.setName(task.getName());
				pendingTask.setDueTime(task.getDueTime().atDate(LocalDate.now()));
				pendingTask.setUser(user);
				pendingRepo.save(pendingTask);
				LOGGER.info("Generated PendingTask for Routine=[{}], Task=[{}], User=[{}] due @ [{}].", routine.getName(), task.getName(), user.getName(), task.getDueTime());
			}
		}
	}

	public CompletedTask completePendingTask(Long id, Cause cause) {
		PendingTask pendingTask = pendingRepo.findOne(id);
		if (pendingTask == null) {
			LOGGER.warn("Attempted to complete non-existent PendingTask[id={}].", id);
			return null;
		} else {
			return completePendingTask(pendingTask, cause);
		}
	}

	public CompletedTask completePendingTask(PendingTask pendingTask, Cause cause) {
		CompletedTask completed = new CompletedTask();
		completed.setCause(cause);
		completed.setCompletionTime(LocalDateTime.now());
		completed.setName(pendingTask.getName());
		completed.setDueTime(pendingTask.getDueTime());
		completed.setUser(pendingTask.getUser());
		pendingRepo.delete(pendingTask);
		LOGGER.debug("Completed {}.", pendingTask);
		return completedRepo.save(completed);
	}

	public PendingTask reassignCompletedTask(Long id) {
		CompletedTask completed = completedRepo.findOne(id);
		if (completed == null) {
			LOGGER.warn("Attempted to reassign non-existent CompletedTask[id={}].", id);
			return null;
		} else {
			completed.setCause(Cause.REASSIGNED);
			PendingTask pending = new PendingTask();
			pending.setName(completed.getName());
			pending.setDueTime(completed.getDueTime());
			pending.setUser(completed.getUser());
			completedRepo.save(completed);
			LOGGER.debug("Reassigned {}.", completed);
			return pendingRepo.save(pending);
		}
	}
}
