export class AppGlobal {

  public static handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }

  public static toTwelveHourClock(time: string): string {
    let hourNum = AppGlobal.getHours(time);
    let hour = hourNum > 12 ? hourNum - 12 : hourNum;
    let minuteNum = AppGlobal.getMinutes(time);
    let minute = minuteNum > 9 ? minuteNum : '0' + minuteNum;
    let period = hourNum > 12 ? 'PM' : 'AM';
    return hour + ':' + minute + ' ' + period;
  }

  public static toTimeString(time: string[]): string {
    let hourNum = Number(time[0]);
    if (time[2].toString() === "PM") {
      hourNum = hourNum + 12;
    }
    let hour = hourNum > 9 ? hourNum : '0' + hourNum;
    let minute = Number(time[1]) > 9 ? time[1] : '0' + time[1];
    return hour + ':' + minute;
  }

  public static getHours(time: string): number {
    let timeArray = time.toString().split(':');
    return Number(timeArray[0]);
  }

  public static getMinutes(time: string): number {
    let timeArray = time.toString().split(':');
    return Number(timeArray[1]);
  }

  public static hourValues(): number[] {
    return Array.from(Array(12).keys()).map(x => ++x);
  }

  public static minuteValues(): number[] {
    return Array.from(Array(60).keys());
  }

  public static periodValues(): string[] {
    return ['AM', 'PM'];
  }
}