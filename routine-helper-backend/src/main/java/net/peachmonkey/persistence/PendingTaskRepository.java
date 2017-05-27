package net.peachmonkey.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import net.peachmonkey.persistence.model.PendingTask;

public interface PendingTaskRepository extends JpaRepository<PendingTask, Long> {

	public PendingTask findOneByUserNameAndName(String userName, String taskName);

	public PendingTask findFirstByUserNameOrderByDueTime(String userName);
}
