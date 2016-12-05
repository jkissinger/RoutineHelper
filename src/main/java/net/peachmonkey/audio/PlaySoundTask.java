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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlaySoundTask implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private Sound sound;

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				play(sound.getNextSound());
			}
		} catch (InterruptedException e) {
			LOGGER.error(e);
		}
	}

	private void play(Path wavPath) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(wavPath.toFile());
				Clip clip = AudioSystem.getClip()) {
			listenForEndOf(clip, latch);
			clip.open(audioIn);
			clip.start();
			latch.await();
			clip.close();
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			LOGGER.error("Exception playing sound [{}]", wavPath.toAbsolutePath(), e);
		}
	}

	private void listenForEndOf(Clip clip, CountDownLatch latch) {
		clip.addLineListener(event -> {
			if (event.getType() == LineEvent.Type.STOP) {
				latch.countDown();
			}
		});
	}
}
