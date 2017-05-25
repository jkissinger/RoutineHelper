package net.peachmonkey;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import net.peachmonkey.persistence.RoutineDayRepository;
import net.peachmonkey.persistence.model.RoutineDay;

@Component
public class Initializer implements CommandLineRunner {

	@Autowired
	private RoutineDayRepository repo;

	@Override
	public void run(String... args) {
		List<RoutineDay> days = repo.findAll();
		List<DayOfWeek> daysOfWeek = days.stream().map(RoutineDay::getDay).collect(Collectors.toList());
		for (DayOfWeek dow : DayOfWeek.values()) {
			if (!daysOfWeek.contains(dow)) {
				RoutineDay day = new RoutineDay();
				day.setDay(dow);
				repo.save(day);
			}
		}
	}

}