package net.peachmonkey.audio;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.Constants.Sounds;
import net.peachmonkey.exception.RequiredSoundMissingException;
import net.peachmonkey.properties.ApplicationProperties;

@Component
public class WavPlayer {

	private static final Logger LOGGER = LogManager.getLogger();
	private CountDownLatch latch = new CountDownLatch(1);
	@Autowired
	private AudioUtils audioUtils;
	@Autowired
	private ApplicationProperties props;

	@PostConstruct
	public void validate() {
		if (!audioUtils.getSystemSoundFile(Sounds.MISSING).exists()) {
			throw new RequiredSoundMissingException("Required audio file [" + audioUtils.getSystemSoundFile(Sounds.MISSING).getAbsolutePath() + "] not found.");
		} else if (!audioUtils.getSystemSoundFile(Sounds.ERROR).exists()) {
			throw new RequiredSoundMissingException("Required audio file [" + audioUtils.getSystemSoundFile(Sounds.ERROR).getAbsolutePath() + "] not found.");
		}
	}

	public synchronized void play(File audioFile) {
		if (!audioFile.exists()) {
			LOGGER.warn("Audio file [{}] missing.", audioFile.getAbsolutePath());
			audioFile = audioUtils.getSystemSoundFile(Sounds.MISSING);
		}
		latch = new CountDownLatch(1);
		try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile); Clip clip = AudioSystem.getClip()) {
			listenForEndOf(clip);
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(props.getSoundAnnounceVolumeAdjust());
			clip.start();
			latch.await();
			clip.close();
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
			LOGGER.error("Exception playing sound [{}]", audioFile.getAbsolutePath(), e);
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