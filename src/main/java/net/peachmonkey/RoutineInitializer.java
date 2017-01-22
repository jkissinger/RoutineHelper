package net.peachmonkey;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import net.peachmonkey.audio.PlaySoundTask;
import net.peachmonkey.controller.ConsoleController;
import net.peachmonkey.properties.ApplicationProperties;
import net.peachmonkey.view.RoutineHelperTrayIcon;

@Component
public class RoutineInitializer implements SmartLifecycle {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private RoutineHelperTrayIcon trayIcon;
	@Autowired
	private ConsoleController consoleController;
	private boolean running = false;
	private ScheduledExecutorService scheduledExecutors;
	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void start() {
		LOGGER.info("Initialized Routine Monitor");
		if (consoleController.isConsoleAvailable()) {
			executorService.submit(consoleController);
		}
		try {
			trayIcon.init();
		} catch (Exception e) {
			LOGGER.error("Failed to initialize TrayIcon.", e);
			throw new RuntimeException("Failed to initialize TrayIcon");
		}
		executorService.submit(context.getBean(PlaySoundTask.class));
		scheduledExecutors = Executors.newSingleThreadScheduledExecutor();
		RoutineMonitor monitor = context.getBean(RoutineMonitor.class);
		scheduledExecutors.scheduleAtFixedRate(monitor, properties.getCheckInterval(), properties.getCheckInterval(), TimeUnit.SECONDS);
		running = true;
	}

	@Override
	public void stop() {
		scheduledExecutors.shutdownNow();
		running = false;
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		new Thread(callback).start();
	}
}
