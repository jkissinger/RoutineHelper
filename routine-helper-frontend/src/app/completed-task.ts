import { Entity } from './entity';
import { RoutineUser } from './routine-user';
import { RoutineTask } from './routine-task';

export class CompletedTask extends Entity {
  task: RoutineTask;
  user: RoutineUser;
  cause: string;
  completionTime: string;
}