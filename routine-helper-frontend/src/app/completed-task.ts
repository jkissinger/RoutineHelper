import { NamedEntity } from './named-entity';
import { RoutineUser } from './routine-user';
import { RoutineTask } from './routine-task';

export class CompletedTask extends NamedEntity {
  dueTime: string;
  user: RoutineUser;
  cause: string;
  completionTime: string;
}