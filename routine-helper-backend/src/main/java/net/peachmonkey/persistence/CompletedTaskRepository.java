package net.peachmonkey.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.CompletedTask.Cause;

public interface CompletedTaskRepository extends JpaRepository<CompletedTask, Long> {

	public List<CompletedTask> findByCompletionTimeBetweenAndCauseNotIn(LocalDateTime lastMidnight, LocalDateTime nextMidnight, List<Cause> causes);

	public List<CompletedTask> findByCompletionTimeAfterAndCauseNotIn(LocalDateTime time, List<Cause> causes);
}
