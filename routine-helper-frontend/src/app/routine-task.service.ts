import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { AppSettings } from './app.settings';

import { RoutineTask } from './routine-task';
import { Routine } from './routine';
import { RoutineService } from './routine.service';

@Injectable()
export class RoutineTaskService {

    constructor(private http: Http, private routineService: RoutineService) { }

    getRoutineTasks(): Promise<RoutineTask[]> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getRoutineTasks')
            .toPromise()
            .then(response => response.json() as RoutineTask[])
            .catch(AppSettings.handleError);
    }

    getTasksForRoutine(id: number): Promise<RoutineTask[]> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getTasksForRoutine?id=' + id)
            .toPromise()
            .then(response => {
                return response.json() as RoutineTask[];
            })
            .catch(AppSettings.handleError);
    }

    getTasksNotForRoutine(id: number): Promise<RoutineTask[]> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getTasksNotForRoutine?id=' + id)
            .toPromise()
            .then(response => {
                return response.json() as RoutineTask[];
            })
            .catch(AppSettings.handleError);
    }

    getRoutineTask(id: number): Promise<RoutineTask> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getRoutineTask?id=' + id)
            .toPromise()
            .then(response => response.json() as RoutineTask)
            .catch(AppSettings.handleError);
    }

    delete(id: number): Promise<void> {
        return this.http.delete(AppSettings.API_ENDPOINT + 'deleteRoutineTask?id=' + id, { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(() => null)
            .catch(AppSettings.handleError);
    }

    createOrUpdate(routineTask: RoutineTask): Promise<RoutineTask> {
        console.log("createOrUpdate: " + JSON.stringify(routineTask)); // TODO Remove, debugging only
        return this.http
            .post(AppSettings.API_ENDPOINT + 'createOrUpdateTask', JSON.stringify(routineTask), { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(res => res.json())
            .catch(AppSettings.handleError);
    }
}
