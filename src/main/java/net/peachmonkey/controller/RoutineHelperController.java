package net.peachmonkey.controller;

import java.awt.TrayIcon.MessageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.peachmonkey.RoutineUtils;
import net.peachmonkey.RoutineUtils.TaskStatus;
import net.peachmonkey.model.IncompleteTask;
import net.peachmonkey.view.RoutineHelperTrayIcon;

@Component
public class RoutineHelperController {

	@Autowired
	private RoutineHelperTrayIcon trayIcon;
	@Autowired
	private RoutineUtils utils;

	public void displayTask(IncompleteTask task) {
		String taskDescription = task.getUser() + " " + task.getName();
		MessageType messageType = MessageType.WARNING;
		if (task.getStatus() == TaskStatus.NOTIFY) {
			messageType = MessageType.INFO;
		}
		trayIcon.showMessage("Task - " + task.getStatus(), taskDescription, messageType);
		addMenuItem(task);
	}

	private void addMenuItem(IncompleteTask task) {
		String label = utils.getLabel(task);
		if (!trayIcon.hasMenuItem(label)) {
			trayIcon.addMenuItem(label, label);
		}
	}

	public void completeTask(IncompleteTask task) {
		String label = utils.getLabel(task);
		trayIcon.removeMenuItem(label);
	}
}
