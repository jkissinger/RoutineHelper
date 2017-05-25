package net.peachmonkey.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import net.peachmonkey.persistence.model.RoutineUser;

public interface RoutineUserRepository extends JpaRepository<RoutineUser, Long> {

	public RoutineUser findOneByName(String name);

}
