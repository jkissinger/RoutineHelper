package net.peachmonkey.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.peachmonkey.persistence.model.CompletedTask;
import net.peachmonkey.persistence.model.PendingTask;
import net.peachmonkey.persistence.model.RoutineUser;
import net.peachmonkey.properties.ApplicationProperties;
import net.peachmonkey.routine.Constants;

@Component
public class RoutineTemplate {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ApplicationProperties props;

	private String getRootUrl() {
		return "http://" + props.getApiHost() + ":" + props.getApiPort() + "/";
	}

	public List<RoutineUser> getRoutineUsers() {
		return restTemplate.exchange(getRootUrl() + "getRoutineUsers", HttpMethod.GET, null, Constants.userList()).getBody();
	}

	public PendingTask getNextPendingTask(String userName) {
		return restTemplate.getForObject(getRootUrl() + "getNextPendingTask?userName={userName}", PendingTask.class, userName);
	}

	public List<CompletedTask> getCompletedTasksSince(LocalDateTime cutoff) {
		return restTemplate.exchange(getRootUrl() + "getTasksCompletedSince", HttpMethod.PUT, new HttpEntity<>(cutoff), Constants.completedTaskList()).getBody();
	}

}
