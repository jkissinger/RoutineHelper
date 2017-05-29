package net.peachmonkey.audio;

import java.io.File;
import java.nio.file.Paths;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.Constants;
import net.peachmonkey.Constants.Sounds;
import net.peachmonkey.properties.ApplicationProperties;

@Component
public class AudioUtils {

	@Autowired
	private ApplicationProperties props;

	public File getUserSoundFile(String name) {
		return Paths.get(props.getSoundFilesLocation(), Sounds.USERS_DIR, concatFilename(name)).toFile();
	}

	public File getTaskSoundFile(String name) {
		return Paths.get(props.getSoundFilesLocation(), Sounds.TASKS_DIR, concatFilename(name)).toFile();
	}

	public File getSystemSoundFile(String... name) {
		return Paths.get(props.getSoundFilesLocation(), Sounds.SYSTEM_DIR, concatFilename(name)).toFile();
	}

	private String concatFilename(String... strings) {
		StringJoiner joiner = new StringJoiner(Constants.FILENAME_SEPARATOR);
		for (String string : strings) {
			joiner.add(string.replaceAll(" ", Constants.FILENAME_SEPARATOR).toLowerCase());
		}
		return joiner.toString() + Constants.AUDIO_FILE_EXTENSION;
	}
}
