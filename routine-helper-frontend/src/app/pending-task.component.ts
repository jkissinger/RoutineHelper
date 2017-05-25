import { Component, ViewChild, ViewContainerRef, OnInit } from '@angular/core';
import { MdDialog, MdDialogConfig, MdDialogRef } from "@angular/material";

import { PendingTask } from './pending-task';
import { PendingTaskService } from './pending-task.service';
import { RoutineUser } from './routine-user';
import { RoutineUserService } from './routine-user.service';

@Component({
  selector: 'pending-task',
  templateUrl: './pending-task.component.html'
})
export class PendingTaskComponent implements OnInit {
  pendingTasks: PendingTask[];

  constructor(public dialog: MdDialog, public vcr: ViewContainerRef, private pendingTaskService: PendingTaskService) { }

  loadTasks(): void {
    this.pendingTaskService.getPendingTasks().then(tasks => this.pendingTasks = tasks);
  }

  completePendingTask(pendingTask: PendingTask): void {
    this.pendingTaskService.completePendingTask(pendingTask.id).then(result => this.loadTasks());
  }

  cancelPendingTask(pendingTask: PendingTask): void {
    this.pendingTaskService.cancelPendingTask(pendingTask.id).then(result => this.loadTasks());;
  }

  ngOnInit(): void {
    this.loadTasks();
  }
}