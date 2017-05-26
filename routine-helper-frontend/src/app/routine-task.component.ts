import { Component, ViewChild, ViewContainerRef, OnInit } from '@angular/core';
import { MdDialog, MdDialogConfig, MdDialogRef } from "@angular/material";

import { RoutineTask } from './routine-task';
import { RoutineTaskService } from './routine-task.service';
import { RoutineUser } from './routine-user';
import { RoutineUserService } from './routine-user.service';
import { AppGlobal } from './app.global';

@Component({
  selector: 'routine-task',
  templateUrl: './routine-task.component.html'
})
export class RoutineTaskComponent implements OnInit {
  routineTasks: RoutineTask[];

  constructor(public dialog: MdDialog, public vcr: ViewContainerRef, private routineTaskService: RoutineTaskService) { }

  ngOnInit(): void {
    this.loadRoutineTasks();
  }

  loadRoutineTasks(): void {
    this.routineTaskService.getRoutineTasks().then(routineTasks => this.routineTasks = routineTasks);
  }

  parseTime(time: string): string {
    return AppGlobal.toTwelveHourClock(time);
  }

  addRoutineTask(): void {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(CreateRoutineTaskComponent, config);
    dialogRef.afterClosed().subscribe(result => this.loadRoutineTasks());
  }

  deleteRoutineTask(task: RoutineTask): void {
    if (confirm(`Are you sure you want to delete task '${task.name}'?`)) {
      this.routineTaskService.delete(task.id).then(result => this.loadRoutineTasks());
    }
  }
}

@Component({
  selector: 'create-routine-task',
  templateUrl: './routine-task.create.component.html'
})
export class CreateRoutineTaskComponent {
  users: RoutineUser[];

  constructor(private routineTaskService: RoutineTaskService, private routineUserService: RoutineUserService, private dialogRef: MdDialogRef<CreateRoutineTaskComponent>) {
    routineUserService.getRoutineUsers().then((users) => this.users = users);
  }

  createRoutineTask(name: string, dueTime: string[]) {
    let task = new RoutineTask();
    task.name = name;
    task.dueTime = AppGlobal.toTimeString(dueTime);
    this.routineTaskService.createOrUpdate(task).then(result => this.dialogRef.close(`${result}`));
  }
}