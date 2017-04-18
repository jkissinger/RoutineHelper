import { Component, ViewChild, ViewContainerRef, OnInit } from '@angular/core';
import { MdDialog, MdDialogConfig, MdDialogRef } from "@angular/material";

import { RoutineUser } from './routine-user';
import { RoutineUserService } from './routine-user.service';

@Component({
  selector: 'routine-user',
  templateUrl: './routine-user.component.html'
})
export class RoutineUserComponent implements OnInit {
  routineUsers: RoutineUser[];
  constructor(public dialog: MdDialog, public vcr: ViewContainerRef, private routineUserService: RoutineUserService) { }

  addRoutineUser(): void {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(CreateRoutineUserComponent, config);
    dialogRef.afterClosed().subscribe(result => {
      this.loadRoutineUsers();
    });
  }

  deleteRoutineUser(user: RoutineUser): void {
    if (confirm(`Are you sure you want to delete user '${user.name}'?`)) {
      this.routineUserService.delete(user).then(result => {
        this.loadRoutineUsers();
      });
    }
  }

  loadRoutineUsers(): void {
    this.routineUserService.getRoutineUsers().then(routineUsers => this.routineUsers = routineUsers);
  }

  ngOnInit(): void {
    this.loadRoutineUsers();
  }
}

@Component({
  selector: 'create-routine-user',
  template: `
    <div>
      <md-input-container>
        <input md-input placeholder="User Name" value=""  #name>
      </md-input-container>
      <button md-button type="submit" (click)="createRoutineUser(name.value)">Add User</button>
    </div>
  `
})
export class CreateRoutineUserComponent {

  constructor(private routineUserService: RoutineUserService, private dialogRef: MdDialogRef<CreateRoutineUserComponent>) { }

  createRoutineUser(name: string) {
    this.routineUserService.create(name).then(result => this.dialogRef.close(`${result}`));
  }
}