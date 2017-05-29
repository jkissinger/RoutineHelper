import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { AppSettings } from './app.settings';

import { RoutineTask } from './routine-task';
import { RoutineDay } from './routine-day';
import { Routine } from './routine';

@Injectable()
export class RoutineService {

    constructor(private http: Http) { }

    getRoutines(): Promise<Routine[]> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getRoutines')
            .toPromise()
            .then(response => response.json() as Routine[])
            .catch(AppSettings.handleError);
    }

    getRoutine(id: number): Promise<Routine> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getRoutine?id=' + id)
            .toPromise()
            .then(response => response.json() as Routine)
            .catch(AppSettings.handleError);
    }

    delete(id: number): Promise<void> {
        return this.http.delete(AppSettings.API_ENDPOINT + 'deleteRoutine?id=' + id, { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(() => null)
            .catch(AppSettings.handleError);
    }

    createOrUpdate(routine: Routine): Promise<Routine> {
        return this.http
            .post(AppSettings.API_ENDPOINT + 'createOrUpdate', JSON.stringify(routine), { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(res => res.json())
            .catch(AppSettings.handleError);
    }

    addTask(routine: Routine, task: RoutineTask): Promise<Routine> {
        return this.http
            .post(AppSettings.API_ENDPOINT + 'addTaskToRoutine?routineId=' + routine.id + '&taskId=' + task.id, JSON.stringify(routine), { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(res => res.json())
            .catch(AppSettings.handleError);
    }

    removeDay(routine: Routine, day: RoutineDay): Promise<Routine> {
        return this.http
            .post(AppSettings.API_ENDPOINT + 'removeDayFromRoutine?routineId=' + routine.id + '&dayId=' + day.id, JSON.stringify(routine), { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(res => res.json())
            .catch(AppSettings.handleError);
    }

    removeTask(routine: Routine, task: RoutineTask): Promise<Routine> {
        return this.http
            .post(AppSettings.API_ENDPOINT + 'removeTaskFromRoutine?routineId=' + routine.id + '&taskId=' + task.id, JSON.stringify(routine), { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(res => res.json())
            .catch(AppSettings.handleError);
    }
}
