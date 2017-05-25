import { NamedEntity } from './named.entity';
import { RoutineTask } from './routine-task';

export class Routine extends NamedEntity {
  tasks: RoutineTask[];
  days: any;
  holidays: any;
}