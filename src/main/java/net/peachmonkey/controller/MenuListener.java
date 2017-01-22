package net.peachmonkey.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import net.peachmonkey.Application;
import net.peachmonkey.RoutineManager;
import net.peachmonkey.RoutineUtils;
import net.peachmonkey.view.Dialog;

@Component
public class MenuListener implements ActionListener {

	@Autowired
	private RoutineManager manager;
	@Autowired
	private RoutineUtils utils;

	public enum Action {
		EXIT {

			@Override
			public boolean doYourThing() {
				System.exit(0);
				return true;
			}

			@Override
			public String toString() {
				return "Exit";
			}
		},
		ABOUT {

			@Override
			public boolean doYourThing() {
				Dialog.displayAbout();
				return true;
			}

			@Override
			public String toString() {
				return "About";
			}
		},
		YO_DAWG {

			@Override
			public boolean doYourThing() {
				Dialog.displayEggs();
				return true;
			}

			@Override
			public String toString() {
				return "Yo Dawg!";
			}
		},
		UNDO {

			@Override
			public boolean doYourThing() {
				RoutineUtils utils = Application.context.getBean(RoutineUtils.class);
				String user = Dialog.getOptions("Who's task would you like to undo?", utils.getAllUsers().toArray(new String[0]));
				if (StringUtils.hasText(user)) {
					RoutineManager manager = Application.context.getBean(RoutineManager.class);
					manager.undoLastTask(user);
				}
				return true;
			}

			@Override
			public String toString() {
				return "Undo Last Task";
			}
		},
		COMPLETE_ALL {

			@Override
			public boolean doYourThing() {
				RoutineUtils utils = Application.context.getBean(RoutineUtils.class);
				String user = Dialog.getOptions("Who's tasks would you like to complete?", utils.getAllUsers().toArray(new String[0]));
				if (StringUtils.hasText(user)) {
					RoutineManager manager = Application.context.getBean(RoutineManager.class);
					manager.markAllTasksComplete(user);
				}
				return true;
			}

			@Override
			public String toString() {
				return "Complete All Tasks";
			}
		};

		public abstract boolean doYourThing();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (Action action : Action.values()) {
			if (action.name().equals(e.getActionCommand())) {
				action.doYourThing();
				return;
			}
		}
		String user = utils.getUserFromLabel(e.getActionCommand());
		String task = utils.getTaskFromLabel(e.getActionCommand());
		if (Dialog.displayConfirm("Are you sure you want to complete " + task + " for " + user + "?")) {
			manager.markNextTaskCompleteForUser(user, true);
		}
	}
}
