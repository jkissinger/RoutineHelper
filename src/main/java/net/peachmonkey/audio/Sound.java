package net.peachmonkey.audio;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.RoutineUtilities.TaskStatus;
import net.peachmonkey.properties.ApplicationProperties;

@Component
public class Sound {

	@Autowired
	private ApplicationProperties properties;
	public static final String FILENAME_SEPARATOR = "-";
	public static final String FILENAME_EXTENSION = ".wav";
	public static final String COMPLETION = "completed";
	private static BlockingQueue<Path> soundQueue = new LinkedBlockingQueue<>();

	public void play(Path soundPath) {
		soundQueue.offer(soundPath);
	}

	Path getNextSound() throws InterruptedException {
		return soundQueue.take();
	}

	public synchronized void announceTask(String user, String taskName, TaskStatus priority) {
		play(getSoundFilePath(user));
		play(getSoundFilePath(taskName));
		play(getSoundFilePath(priority.name()));
	}

	public synchronized void announceTaskCompletion(String user, TaskStatus priority) {
		play(getSoundFilePath(user));
		play(getSoundFilePath(priority.name(), COMPLETION));
	}

	private Path getSoundFilePath(String... strings) {
		return Paths.get(properties.getAudioDirectory().toString(), concatFilename(strings));
	}

	private String concatFilename(String... strings) {
		StringJoiner joiner = new StringJoiner(FILENAME_SEPARATOR);
		for (String string : strings) {
			joiner.add(string.replaceAll(" ", FILENAME_SEPARATOR).toLowerCase());
		}
		return joiner.toString() + FILENAME_EXTENSION;
	}
}