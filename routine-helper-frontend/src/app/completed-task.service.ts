import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { AppSettings } from './app.settings';

import { CompletedTask } from './completed-task';
import { PendingTask } from './pending-task';

@Injectable()
export class CompletedTaskService {

    constructor(private http: Http) { }

    getCompletedTasks(): Promise<CompletedTask[]> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getTasksCompletedToday')
            .toPromise()
            .then(response => response.json() as CompletedTask[])
            .catch(AppSettings.handleError);
    }

    reassignCompletedTask(id: number): Promise<PendingTask> {
        return this.http
            .post(AppSettings.API_ENDPOINT + 'reassignCompletedTask?id=' + id, { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(res => res.json())
            .catch(AppSettings.handleError);
    }
}
