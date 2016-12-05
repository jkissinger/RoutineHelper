package net.peachmonkey;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputMonitor implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String COMPLETE = "complete_all";
	@Autowired
	private RoutineUtilities utils;
	private Scanner scanInput = new Scanner(System.in);

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				String data = scanInput.nextLine().toLowerCase();
				if (data.contains(COMPLETE)) {
					String user = data.replace(COMPLETE, "");
					while (utils.getNextIncompleteTask(user) != null) {
						utils.markNextTaskCompleteForUser(user, false);
					}
				} else {
					utils.markNextTaskCompleteForUser(data, true);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in input thread.", e);
		}
	}
}
