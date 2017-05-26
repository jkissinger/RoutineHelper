import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MaterialModule } from '@angular/material';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';

import { RoutineUserService } from './routine-user.service';
import { RoutineTaskService } from './routine-task.service';
import { RoutineService } from './routine.service';
import { RoutineUserComponent, CreateRoutineUserComponent } from './routine-user.component';
import { RoutineTaskComponent, CreateRoutineTaskComponent } from './routine-task.component';
import { RoutineComponent, CreateRoutineComponent, AddRoutineTaskToRoutineComponent } from './routine.component';
import { PendingTaskComponent, UpdatePendingTaskComponent } from './pending-task.component';
import { PendingTaskService } from './pending-task.service';
import { CompletedTaskService } from './completed-task.service';

@NgModule({
  declarations: [
    AppComponent,
    RoutineUserComponent,
    CreateRoutineUserComponent,
    RoutineTaskComponent,
    CreateRoutineTaskComponent,
    RoutineComponent,
    CreateRoutineComponent,
    AddRoutineTaskToRoutineComponent,
    PendingTaskComponent,
    UpdatePendingTaskComponent
  ],
  entryComponents: [
    CreateRoutineUserComponent,
    CreateRoutineTaskComponent,
    CreateRoutineComponent,
    AddRoutineTaskToRoutineComponent,
    UpdatePendingTaskComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule.forRoot([
      {
        path: '',
        component: PendingTaskComponent
      },
      {
        path: 'routine-users',
        component: RoutineUserComponent
      },
      {
        path: 'routine-tasks',
        component: RoutineTaskComponent
      },
      {
        path: 'routines',
        component: RoutineComponent
      }
    ]),
    HttpModule,
    MaterialModule.forRoot()
  ],
  providers: [
    RoutineUserService,
    RoutineTaskService,
    RoutineService,
    PendingTaskService,
    CompletedTaskService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
