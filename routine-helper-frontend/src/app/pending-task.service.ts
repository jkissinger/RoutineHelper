import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { AppSettings } from './app.settings';

import { PendingTask } from './pending-task';
import { CompletedTask } from './completed-task';

@Injectable()
export class PendingTaskService {

    constructor(private http: Http) { }

    getPendingTasks(): Promise<PendingTask[]> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getPendingTasks')
            .toPromise()
            .then(response => response.json() as PendingTask[])
            .catch(AppSettings.handleError);
    }

    completePendingTask(id: number): Promise<CompletedTask> {
        return this.resolvePendingTask(id, 'COMPLETED');

    }

    cancelPendingTask(id: number) {
        return this.resolvePendingTask(id, 'CANCELLED');
    }

    private resolvePendingTask(id: number, cause: string): Promise<CompletedTask> {
        return this.http
            .post(AppSettings.API_ENDPOINT + 'completePendingTask?id=' + id + '&cause=' + cause, { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(res => res.json())
            .catch(AppSettings.handleError);
    }
}
