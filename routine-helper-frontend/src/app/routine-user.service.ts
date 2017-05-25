import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';

import { RoutineUser } from './routine-user';
import { AppSettings } from './app.settings';

@Injectable()
export class RoutineUserService {

    constructor(private http: Http) { }

    getRoutineUsers(): Promise<RoutineUser[]> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getRoutineUsers')
            .toPromise()
            .then(response => response.json() as RoutineUser[])
            .catch(AppSettings.handleError);
    }

    getRoutineUser(name: string): Promise<RoutineUser> {
        return this.http.get(AppSettings.API_ENDPOINT + 'getRoutineUser?name=' + name)
            .toPromise()
            .then(response => response.json() as RoutineUser)
            .catch(AppSettings.handleError);
    }

    delete(user: RoutineUser): Promise<void> {
        return this.http.delete(AppSettings.API_ENDPOINT + 'deleteRoutineUser?id=' + user.id, { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(() => null)
            .catch(AppSettings.handleError);
    }

    createOrUpdate(user: RoutineUser): Promise<RoutineUser> {
        return this.http
            .post(AppSettings.API_ENDPOINT + 'createOrUpdateUser', JSON.stringify(user), { headers: AppSettings.CONTENT_TYPE_HEADERS })
            .toPromise()
            .then(res => res.json())
            .catch(AppSettings.handleError);
    }
}
