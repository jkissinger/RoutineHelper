import { Component, ViewChild, ViewContainerRef, OnInit } from '@angular/core';
import { MdDialog, MdDialogConfig, MdDialogRef } from "@angular/material";

import { RoutineTask } from './routine-task';
import { RoutineTaskService } from './routine-task.service';
import { RoutineUser } from './routine-user';
import { RoutineUserService } from './routine-user.service';

@Component({
  selector: 'routine-task',
  templateUrl: './routine-task.component.html'
})
export class RoutineTaskComponent implements OnInit {
  routineTasks: RoutineTask[];
  hours: number[];

  constructor(public dialog: MdDialog, public vcr: ViewContainerRef, private routineTaskService: RoutineTaskService) {
    this.hours = Array(12).fill(1, 1, 12);
  }

  addRoutineTask(): void {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(CreateRoutineTaskComponent, config);
    dialogRef.afterClosed().subscribe(result => {
      this.loadRoutineTasks();
    });
  }

  deleteRoutineTask(task: RoutineTask): void {
    if (confirm(`Are you sure you want to delete task '${task.name}'?`)) {
      this.routineTaskService.delete(task).then(result => {
        this.loadRoutineTasks();
      });
    }
  }

  loadRoutineTasks(): void {
    this.routineTaskService.getRoutineTasks().then(routineTasks => this.routineTasks = routineTasks);
  }

  ngOnInit(): void {
    this.loadRoutineTasks();
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

  createRoutineTask(name: string, notifyTime: number[]) {
    this.routineTaskService.create(name, notifyTime).then(result => this.dialogRef.close(`${result}`));
  }
}