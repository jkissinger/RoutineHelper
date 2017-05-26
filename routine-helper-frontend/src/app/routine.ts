import { NamedEntity } from './named-entity';
import { RoutineTask } from './routine-task';
import { RoutineDay } from './routine-day';

export class Routine extends NamedEntity {
  tasks: RoutineTask[];
  days: RoutineDay[];
  holidays: any;
}