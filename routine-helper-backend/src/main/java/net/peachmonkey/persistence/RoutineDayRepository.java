package net.peachmonkey.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import net.peachmonkey.persistence.model.RoutineDay;

public interface RoutineDayRepository extends JpaRepository<RoutineDay, Long> {

}
