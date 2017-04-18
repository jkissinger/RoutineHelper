import { Component, Input } from '@angular/core';

import { RoutineUser } from './routine-user';

@Component({
  selector: 'routine-user',
  template: `
    <h1>{{title}}</h1>
    <h2>Users</h2>
    <ul class="routineUser">
      <li *ngFor="let routineUser of routineUsers">
        {{routineUser.name}}
      </li>
    </ul>
  `
})
export class RoutineUserComponent {
  @Input() routineUsers: RoutineUser[];
}