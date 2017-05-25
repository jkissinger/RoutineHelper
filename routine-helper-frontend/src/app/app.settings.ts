import { Headers } from '@angular/http';

export class AppSettings {
  public static API_ENDPOINT = 'http://localhost:8079/';
  public static CONTENT_TYPE_HEADERS = new Headers({ 'Content-Type': 'application/json' });

  public static handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}