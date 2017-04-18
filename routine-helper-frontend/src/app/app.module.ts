import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MaterialModule } from '@angular/material';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';

import { RoutineUserService } from './routine-user.service';
import { RoutineUserComponent, CreateRoutineUserComponent } from './routine-user.component';

@NgModule({
  declarations: [
    AppComponent,
    RoutineUserComponent,
    CreateRoutineUserComponent
  ],
  entryComponents: [
    RoutineUserComponent,
    CreateRoutineUserComponent
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
      }
    ]),
    HttpModule,
    MaterialModule.forRoot()
  ],
  providers: [
    RoutineUserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
