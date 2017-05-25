import { Entity } from './entity';
import { RoutineUser } from './routine-user';
import { RoutineTask } from './routine-task';

export class PendingTask extends Entity {
  task: RoutineTask;
  user: RoutineUser;
}