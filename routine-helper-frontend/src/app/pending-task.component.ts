import { Component, ViewChild, ViewContainerRef, OnInit } from '@angular/core';
import { MdDialog, MdDialogConfig, MdDialogRef } from "@angular/material";

import { PendingTask } from './pending-task';
import { PendingTaskService } from './pending-task.service';
import { CompletedTask } from './completed-task';
import { CompletedTaskService } from './completed-task.service';
import { AppGlobal } from './app.global';

@Component({
  selector: 'pending-task',
  templateUrl: './pending-task.component.html'
})
export class PendingTaskComponent implements OnInit {
  pendingTasks: PendingTask[];
  completedTasks: CompletedTask[];

  constructor(public dialog: MdDialog, public vcr: ViewContainerRef, private pendingTaskService: PendingTaskService, private completedTaskService: CompletedTaskService) { }

  ngOnInit(): void {
    this.loadTasks();
  }

  parseTime(time: string): string {
    return AppGlobal.toTwelveHourClock(time);
  }

  getPendingTaskIcon(pendingTask: PendingTask): string {
    if (this.isTaskOverdue(pendingTask)) {
      return 'assignment_late';
    } else {
      return 'assignment';
    }
  }

  isTaskOverdue(pendingTask: PendingTask): boolean {
    let taskTime = new Date();
    taskTime.setHours(AppGlobal.getHours(pendingTask.dueTime));
    taskTime.setMinutes(AppGlobal.getMinutes(pendingTask.dueTime));
    taskTime.setSeconds(0);
    let currentTime = new Date();
    return currentTime > taskTime;
  }

  loadTasks(): void {
    this.pendingTaskService.getPendingTasks().then(tasks => this.pendingTasks = tasks);
    this.completedTaskService.getCompletedTasks().then(tasks => this.completedTasks = tasks);
  }

  completePendingTask(pendingTask: PendingTask): void {
    this.pendingTaskService.completePendingTask(pendingTask.id).then(result => this.loadTasks());
  }

  updatePendingTask(task: PendingTask): void {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(UpdatePendingTaskComponent, config);
    dialogRef.componentInstance.task = task;
    dialogRef.afterClosed().subscribe(result => this.loadTasks());
  }

  cancelPendingTask(pendingTask: PendingTask): void {
    this.pendingTaskService.cancelPendingTask(pendingTask.id).then(result => this.loadTasks());
  }

  reassignCompletedTask(completedTask: CompletedTask): void {
    this.completedTaskService.reassignCompletedTask(completedTask.id).then(result => this.loadTasks());
  }
}

@Component({
  selector: 'update-pending-task',
  templateUrl: './pending-task.update.component.html'
})
export class UpdatePendingTaskComponent {
  task: PendingTask;

  constructor(private pendingTaskService: PendingTaskService, private dialogRef: MdDialogRef<UpdatePendingTaskComponent>) { }

  global(): AppGlobal {
    return AppGlobal;
  }

  updatePendingTask(dueTime: string[]) {
    let task = new PendingTask();
    task.id = this.task.id;
    task.name = this.task.name;
    task.dueTime = AppGlobal.toTimeTodayString(dueTime);
    this.pendingTaskService.updatePendingTask(task).then(result => this.dialogRef.close(`${result}`));
  }
}