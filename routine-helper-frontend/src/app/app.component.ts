import { Component, ViewChild, ViewContainerRef, OnInit } from '@angular/core';
import { MdSidenav, MdDialog, MdDialogConfig } from "@angular/material";

import { RoutineUser } from './routine-user';
import { RoutineUserService } from './routine-user.service';
import { CreateRoutineUserComponent } from './create-routine-user.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  @ViewChild('sidenav') sidenav: MdSidenav;
  routineUsers: RoutineUser[];
  constructor(public dialog: MdDialog, public vcr: ViewContainerRef, private routineUserService: RoutineUserService) { }

  addRoutineUser(): void {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(CreateRoutineUserComponent, config);
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`)
      this.getRoutineUsers();
    });
  }

  getRoutineUsers(): void {
    this.routineUserService.getRoutineUsers().then(routineUsers => this.routineUsers = routineUsers);
  }

  ngOnInit(): void {
    this.getRoutineUsers();
  }
}