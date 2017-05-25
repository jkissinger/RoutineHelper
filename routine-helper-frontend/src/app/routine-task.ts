import { NamedEntity } from './named.entity';
import { RoutineUser } from './routine-user';

export class RoutineTask extends NamedEntity {
  routine: any;
  notifyTime: string;
  warningTime: string;
  alarmTime: string;
  users: RoutineUser[];
}