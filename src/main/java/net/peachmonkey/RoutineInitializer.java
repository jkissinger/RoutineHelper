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
import net.peachmonkey.properties.ApplicationProperties;

@Component
public class RoutineInitializer implements SmartLifecycle {

	private static final Logger LOGGER = LogManager.getLogger();
	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private ApplicationContext context;
	private boolean running = false;
	private ScheduledExecutorService scheduledExecutors;
	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void start() {
		scheduledExecutors = Executors.newSingleThreadScheduledExecutor();
		RoutineMonitor monitor = context.getBean(RoutineMonitor.class);
		scheduledExecutors.scheduleAtFixedRate(monitor, properties.getCheckInterval(), properties.getCheckInterval(), TimeUnit.SECONDS);
		LOGGER.info("Initialized Routine Monitor");
		executorService.submit(context.getBean(InputMonitor.class));
		executorService.submit(context.getBean(PlaySoundTask.class));
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
