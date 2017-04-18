package net.peachmonkey.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import net.peachmonkey.persistence.model.RoutineTask;

public interface RoutineTaskRepository extends JpaRepository<RoutineTask, Long> {

}
