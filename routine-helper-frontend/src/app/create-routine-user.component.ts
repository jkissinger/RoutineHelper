import { Injectable, Component, ViewContainerRef } from '@angular/core';
import { MdDialog, MdDialogConfig, MdDialogRef } from "@angular/material";

import { RoutineUserService } from './routine-user.service';
import { RoutineUserComponent } from './routine-user.component';

@Injectable()
export class CreateRoutineUserDialog {

  constructor(public dialog: MdDialog, public vcr: ViewContainerRef, public routineUserComponent: RoutineUserComponent) { }

  openDialog() {
    const config = new MdDialogConfig();
    config.viewContainerRef = this.vcr;
    let dialogRef = this.dialog.open(CreateRoutineUserComponent, config);
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`)
      this.routineUserComponent.getRoutineUsers();
    });
  }
}

@Component({
  selector: 'create-routine-user',
  template: `
    <div>
      <md-input-container>
        <input md-input placeholder="User Name" value="King Arthur"  #name>
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