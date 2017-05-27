package net.peachmonkey.audio;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.properties.ApplicationProperties;
import net.peachmonkey.routine.Constants;
import net.peachmonkey.routine.Constants.TaskStatus;

@Component
public class Announcer {

	@Autowired
	private ApplicationProperties props;
	@Autowired
	private WavPlayer playSound;

	@PostConstruct
	public void init() {
		playSound.play(getSoundFilePath(Constants.ONLINE_SOUND));
	}

	public void announceTask(String user, String taskName, TaskStatus priority) {
		playSound.play(getSoundFilePath(user));
		playSound.play(getSoundFilePath(taskName));
		playSound.play(getSoundFilePath(priority.name()));
	}

	public void announceTaskCompletion(String user, TaskStatus priority) {
		playSound.play(getSoundFilePath(user));
		playSound.play(getSoundFilePath(priority.name(), Constants.COMPLETION));
	}

	private Path getSoundFilePath(String... strings) {
		return Paths.get(props.getSoundFilesLocation(), concatFilename(strings));
	}

	private String concatFilename(String... strings) {
		StringJoiner joiner = new StringJoiner(Constants.FILENAME_SEPARATOR);
		for (String string : strings) {
			joiner.add(string.replaceAll(" ", Constants.FILENAME_SEPARATOR).toLowerCase());
		}
		return joiner.toString() + Constants.FILENAME_EXTENSION;
	}
}