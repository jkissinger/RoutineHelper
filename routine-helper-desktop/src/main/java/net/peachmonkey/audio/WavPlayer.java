package net.peachmonkey.audio;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class WavPlayer {

	private static final Logger LOGGER = LogManager.getLogger();
	private CountDownLatch latch = new CountDownLatch(1);

	public synchronized void play(Path wavPath) {
		latch = new CountDownLatch(1);
		try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(wavPath.toFile()); Clip clip = AudioSystem.getClip()) {
			listenForEndOf(clip);
			clip.open(audioIn);
			clip.start();
			latch.await();
			clip.close();
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
			LOGGER.error("Exception playing sound [{}]", wavPath.toAbsolutePath(), e);
			throw new AudioException(e);
		}
	}

	private void listenForEndOf(Clip clip) {
		clip.addLineListener(event -> {
			if (event.getType() == LineEvent.Type.STOP) {
				latch.countDown();
			}
		});
	}
}