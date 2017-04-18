package net.peachmonkey.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import net.peachmonkey.persistence.model.Routine;

public interface RoutineRepository extends JpaRepository<Routine, String> {

}
