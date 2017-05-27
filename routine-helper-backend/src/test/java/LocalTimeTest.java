import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.peachmonkey.persistence.model.RoutineTask;

public class LocalTimeTest {

	@Test
	public void test() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		LocalTime time = LocalTime.NOON;
		System.out.println(mapper.writeValueAsString(time));
		// {"name":"asdfdasf","notifyTime":{"hour":2,"minute":3}}
	}

	@Test
	public void test1() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		LocalDateTime time = LocalDateTime.now();
		System.out.println(mapper.writeValueAsString(time));
		// {"name":"asdfdasf","notifyTime":{"hour":2,"minute":3}}
	}

	@Test
	public void test2() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		LocalTime time = LocalTime.NOON;
		RoutineTask task = new RoutineTask();
		task.setName("TestName");
		task.setDueTime(time);
		System.out.println(mapper.writeValueAsString(task));

		try {
			mapper.readerFor(RoutineTask.class).readValue("{\"name\":\"asdfdasf\",\"dueTime\":[2,3]}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
