import { Component, ViewChild, ViewContainerRef, OnInit } from '@angular/core';
import { MdDialog, MdDialogConfig, MdDialogRef } from "@angular/material";

import { Routine } from './routine';
import { RoutineService } from './routine.service';
import { RoutineTask } from './routine-task';
import { RoutineTaskService } from './routine-task.service';

@Component({
  selector: 'routine',
  templateUrl: './routine.component.html'
})
export class RoutineComponent implements OnInit {
  routines: Routine[];

  constructor(public dialog: MdDialog, public vcr: ViewContainerRef, private routineService: RoutineService, private routineTaskService: RoutineTaskService) { }

  createRoutine(): void {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(CreateRoutineComponent, config);
    dialogRef.afterClosed().subscribe(result => {
      this.loadRoutines();
    });
  }

  addRoutineTask(routine: Routine): void {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(AddRoutineTaskToRoutineComponent, config);
    dialogRef.componentInstance.routine = routine;
    this.routineTaskService.getTasksNotForRoutine(routine.name).then(result => dialogRef.componentInstance.routineTasks = result);
    dialogRef.afterClosed().subscribe(result => {
      this.loadRoutines();
    });
  }

  removeRoutineTask(routine: Routine): void {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(RemoveRoutineTaskFromRoutineComponent, config);
    dialogRef.componentInstance.routine = routine;
    dialogRef.afterClosed().subscribe(result => {
      this.loadRoutines();
    });
  }

  deleteRoutine(routine: Routine): void {
    if (confirm(`Are you sure you want to delete routine '${routine.name}'?`)) {
      this.routineService.delete(routine).then(result => {
        this.loadRoutines();
      });
    }
  }

  loadRoutines(): void {
    this.routineService.getRoutines().then(routines => this.routines = routines);
  }

  ngOnInit(): void {
    this.loadRoutines();
  }
}

@Component({
  selector: 'create-routine',
  template: `
  <div>
    <md-input-container>
      <input md-input placeholder="Routine Name" value="" #name>
    </md-input-container>
    <button md-button type="submit" (click)="createRoutine(name.value, [chosenHour, chosenMinute])">Add Routine</button>
  </div>
`
})
export class CreateRoutineComponent {

  constructor(private routineService: RoutineService, private dialogRef: MdDialogRef<CreateRoutineComponent>) { }

  createRoutine(name: string) {
    this.routineService.create(name).then(result => this.dialogRef.close(`${result}`));
  }
}

@Component({
  selector: 'add-routine-task-to-routine',
  template: `
  <div>
    <md-select placeholder="Routine Task" [(ngModel)]="chosenTask">
      <md-option *ngFor="let task of routineTasks" [value]="task">{{ task.name }}</md-option>
    </md-select>
    <br />
    <button md-button type="submit" (click)="addRoutineTask(chosenTask)">Add Task</button>
  </div>
`
})
export class AddRoutineTaskToRoutineComponent {
  routine: Routine;
  routineTasks: RoutineTask[];

  constructor(private routineService: RoutineService, private dialogRef: MdDialogRef<AddRoutineTaskToRoutineComponent>) { }

  addRoutineTask(routineTask: RoutineTask) {
    this.routineService.addTask(this.routine, routineTask).then(result => this.dialogRef.close(`${result}`));
  }
}

@Component({
  selector: 'remove-routine-task-from-routine',
  template: `
  <div>
    <md-input-container>
      <input md-input placeholder="Routine Name" value="" #name>
    </md-input-container>
    <button md-button type="submit" (click)="createRoutine(name.value, [chosenHour, chosenMinute])">Add Routine</button>
  </div>
`
})
export class RemoveRoutineTaskFromRoutineComponent {
  routine: Routine;

  constructor(private routineService: RoutineService, private dialogRef: MdDialogRef<AddRoutineTaskToRoutineComponent>) { }

  removeRoutineTask(routineTask: RoutineTask) {
    this.routineService.removeTask(this.routine, routineTask).then(result => this.dialogRef.close(`${result}`));
  }
}