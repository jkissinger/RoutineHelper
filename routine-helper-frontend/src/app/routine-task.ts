import { NamedEntity } from './named-entity';
import { RoutineUser } from './routine-user';

export class RoutineTask extends NamedEntity {
  dueTime: string;
  users: RoutineUser[];
}