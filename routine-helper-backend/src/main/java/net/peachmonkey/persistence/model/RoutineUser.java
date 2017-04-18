package net.peachmonkey.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RoutineUser {

	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RoutineUser [name=" + name + "]";
	}
}
