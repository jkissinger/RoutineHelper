package net.peachmonkey.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import net.peachmonkey.persistence.model.CompletedTask;

public interface CompletedTaskRepository extends JpaRepository<CompletedTask, Long> {

}
