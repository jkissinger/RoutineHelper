import { NamedEntity } from './named-entity';
import { RoutineUser } from './routine-user';
import { RoutineTask } from './routine-task';

export class PendingTask extends NamedEntity {
  dueTime: string;
  user: RoutineUser;
}