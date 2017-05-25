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
import { RoutineComponent, CreateRoutineComponent, AddRoutineTaskToRoutineComponent, RemoveRoutineTaskFromRoutineComponent } from './routine.component';
import { PendingTaskComponent } from './pending-task.component';
import { PendingTaskService } from './pending-task.service';

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
    RemoveRoutineTaskFromRoutineComponent,
    PendingTaskComponent
  ],
  entryComponents: [
    CreateRoutineUserComponent,
    CreateRoutineTaskComponent,
    CreateRoutineComponent,
    AddRoutineTaskToRoutineComponent,
    RemoveRoutineTaskFromRoutineComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule.forRoot([
      {
        path: 'app-root',
        component: AppComponent
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
      },
      {
        path: 'pending-tasks',
        component: PendingTaskComponent
      }
    ]),
    HttpModule,
    MaterialModule.forRoot()
  ],
  providers: [
    RoutineUserService,
    RoutineTaskService,
    RoutineService,
    PendingTaskService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
