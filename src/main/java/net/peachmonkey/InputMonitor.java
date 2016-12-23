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
	private static final String UNDO = "undo";
	private static final String DELAY_ROUTINE = "delay routine";
	@Autowired
	private RoutineManager manager;
	private Scanner scanInput = new Scanner(System.in);

	@Override
	public void run() {
		try {
			System.out.println("Commands:");
			System.out.println("'username' - Completes the next valid incomplete task for the given user.");
			System.out.println("'username complete_all' - Completes all valid incomplete tasks for the given user.");
			System.out.println("'undo username' - Marks the most recently completed valid task as incomplete for the given user.");
			System.out.println("'routine delay routine-name delay-in-seconds' - Completes all valid incomplete tasks for the given user.");
			while (!Thread.currentThread().isInterrupted()) {
				String data = scanInput.nextLine().toLowerCase();
				if (data.contains(COMPLETE)) {
					String user = data.replace(COMPLETE, "").trim();
					manager.markAllTasksComplete(user);
				} else if (data.contains(UNDO)) {
					String user = data.replace(UNDO, "").trim();
					manager.undoLastTask(user);
				} else if (data.contains(DELAY_ROUTINE)) {
					manager.addRoutineDelay(data.replace(DELAY_ROUTINE, "").trim());
				} else {
					manager.markNextTaskCompleteForUser(data, true);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in input thread.", e);
		}
	}
}
