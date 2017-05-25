package net.peachmonkey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.peachmonkey.persistence.RoutineUserRepository;
import net.peachmonkey.persistence.model.RoutineUser;

@RestController
public class RoutineUserController {

	@Autowired
	private RoutineUserRepository repo;

	@RequestMapping(value = "/getRoutineUsers", method = RequestMethod.GET)
	public List<RoutineUser> getRoutineUsers() {
		return repo.findAll();
	}

	@RequestMapping(value = "/getRoutineUser", method = RequestMethod.GET)
	public RoutineUser getRoutineUser(String name) {
		return repo.findOneByName(name);
	}

	@RequestMapping(value = "/createOrUpdateUser", method = RequestMethod.POST)
	public RoutineUser createOrUpdateUser(@RequestBody RoutineUser routineUser) {
		RoutineUser persisted = null;
		if (routineUser.getId() != null) {
			persisted = repo.findOne(routineUser.getId());
		}
		if (persisted == null && StringUtils.hasText(routineUser.getName())) {
			persisted = repo.findOneByName(routineUser.getName());
		}
		if (persisted == null) {
			return repo.save(routineUser);
		} else {
			if (StringUtils.hasText(routineUser.getName())) {
				persisted.setName(routineUser.getName());
				return repo.save(persisted);
			}
			return persisted;
		}
	}

	@RequestMapping(value = "/deleteRoutineUser", method = RequestMethod.DELETE)
	public void deleteRoutineUser(Long id) {
		repo.delete(id);
	}
}
