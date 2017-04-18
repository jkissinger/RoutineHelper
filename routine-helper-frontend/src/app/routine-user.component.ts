import { Component, ViewChild, ViewContainerRef, OnInit } from '@angular/core';
import { MdDialog, MdDialogConfig } from "@angular/material";

import { RoutineUser } from './routine-user';
import { RoutineUserService } from './routine-user.service';
import { CreateRoutineUserComponent } from './create-routine-user.component';

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
      this.getRoutineUsers();
    });
  }

  deleteRoutineUser(user: RoutineUser): void {
    if (confirm(`Are you sure you want to delete user '${user.name}'?`)) {
      this.routineUserService.delete(user).then(result => {
        this.getRoutineUsers();
      });
    }
  }

  getRoutineUsers(): void {
    this.routineUserService.getRoutineUsers().then(routineUsers => this.routineUsers = routineUsers);
  }

  ngOnInit(): void {
    this.getRoutineUsers();
  }
}