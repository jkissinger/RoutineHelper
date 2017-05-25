package net.peachmonkey.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import net.peachmonkey.persistence.model.Routine;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

	public Routine findOneByName(String name);
}
