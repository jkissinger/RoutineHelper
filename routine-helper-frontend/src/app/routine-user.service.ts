import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';

import { RoutineUser } from './routine-user';

@Injectable()
export class RoutineUserService {
    private headers = new Headers({ 'Content-Type': 'application/json' });
    private routineUsersUrl = 'http://localhost:8079/routineUsers';

    constructor(private http: Http) { }

    getRoutineUsers(): Promise<RoutineUser[]> {
        return this.http.get(this.routineUsersUrl)
            .toPromise()
            .then(response => response.json()._embedded.routineUsers as RoutineUser[])
            .catch(this.handleError);
    }

    getRoutineUser(name: string): Promise<RoutineUser> {
        const url = `${this.routineUsersUrl}/${name}`;
        return this.http.get(url)
            .toPromise()
            .then(response => response.json().data as RoutineUser)
            .catch(this.handleError);
    }

    delete(user: RoutineUser): Promise<void> {
        const url = `${user._links.self.href}`;
        return this.http.delete(url, { headers: this.headers })
            .toPromise()
            .then(() => null)
            .catch(this.handleError);
    }

    create(name: string): Promise<RoutineUser> {
        return this.http
            .post(this.routineUsersUrl, JSON.stringify({ name: name }), { headers: this.headers })
            .toPromise()
            .then(res => res.json().data)
            .catch(this.handleError);
    }

    update(routineUser: RoutineUser): Promise<RoutineUser> {
        const url = `${this.routineUsersUrl}/${routineUser.name}`;
        return this.http
            .put(url, JSON.stringify(routineUser), { headers: this.headers })
            .toPromise()
            .then(() => routineUser)
            .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    }
}
