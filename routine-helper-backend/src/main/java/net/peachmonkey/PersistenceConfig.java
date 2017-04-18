package net.peachmonkey;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.peachmonkey.persistence.RoutineDayRepository;
import net.peachmonkey.persistence.model.RoutineDay;

@Configuration
public class PersistenceConfig {

	@Autowired
	private RoutineDayRepository dayRepo;

	@PostConstruct
	private void initDatabase() {
		List<DayOfWeek> days = new ArrayList<>(Arrays.asList(DayOfWeek.values()));
		for (RoutineDay routineDay : dayRepo.findAll()) {
			days.remove(routineDay.getDay());
		}
		for (DayOfWeek day : days) {
			RoutineDay routineDay = new RoutineDay();
			routineDay.setDay(day);
			dayRepo.save(routineDay);
		}
	}

	@Bean
	public ServletRegistrationBean h2servletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
		registration.addUrlMappings("/console/*");
		return registration;
	}

}
