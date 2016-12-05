package net.peachmonkey.properties;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SelfLoggingProperties {

	private static final Logger LOGGER = LogManager.getLogger();
	private boolean written = false;

	@PostConstruct
	public void writeToLog() throws IllegalArgumentException, IllegalAccessException {
		if (!written) {
			logClass(this, 0);
			written = true;
		}
	}

	private void logClass(Object instance, int iterations) throws IllegalAccessException {
		if (instance instanceof String || instance.getClass().isEnum()) {
			LOGGER.info("{}[{}]", getSpaces(iterations * 2), instance);
		} else {
			for (Field field : instance.getClass().getDeclaredFields()) {
				logField(field, instance, iterations);
			}
		}
	}

	private void logField(Field field, Object instance, int iterations) throws IllegalAccessException {
		if (!field.isSynthetic()) {
			field.setAccessible(true);
			if (List.class.isAssignableFrom(field.getType())) {
				List<?> list = (List<?>) field.get(instance);
				LOGGER.info("{}{}:", getSpaces(iterations * 2), field.getName());
				for (Object o : list) {
					logClass(o, iterations + 1);
				}
			} else {
				LOGGER.info("{}{}=[{}]", getSpaces(iterations * 2), field.getName(), field.get(instance));
			}
		}
	}

	private String getSpaces(int num) {
		return new String(new char[num]).replace('\0', ' ');
	}
}
