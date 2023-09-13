import { CaptureBuilder } from './capture';
import { NativeModules } from 'react-native';
import type { TimeShiftIntervalEnum } from '../theta-repository/options';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_NAME = 'TIME-SHIFT-PROGRESS';

interface CaptureProgressNotify extends BaseNotify {
  params?: {
    completion: number;
  };
}

/**
 * TimeShiftCapture class
 */
export class TimeShiftCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }
  /**
   * start time-shift
   * @param onProgress the block for time-shift onProgress
   * @return promise of captured file url
   */
  async startCapture(
    onProgress?: (completion?: number) => void
  ): Promise<string | undefined> {
    if (onProgress) {
      this.notify.addNotify(NOTIFY_NAME, (event: CaptureProgressNotify) => {
        onProgress(event.params?.completion);
      });
    }

    return new Promise<string | undefined>(async (resolve, reject) => {
      await ThetaClientReactNative.startTimeShiftCapture()
        .then((result?: string) => {
          resolve(result ?? undefined);
        })
        .catch((error: any) => {
          reject(error);
        })
        .finally(() => {
          this.notify.removeNotify(NOTIFY_NAME);
        });
    });
  }

  /**
   * cancel time-shift
   */
  async cancelCapture(): Promise<any> {
    return await ThetaClientReactNative.cancelTimeShiftCapture();
  }
}

/**
 * TimeShiftCaptureBuilder class
 */
export class TimeShiftCaptureBuilder extends CaptureBuilder<TimeShiftCaptureBuilder> {
  interval?: number;

  /** construct TimeShiftCaptureBuilder instance */
  constructor() {
    super();

    this.interval = undefined;
  }

  /**
   * set interval of checking time-shift status command
   * @param timeMillis interval
   * @returns TimeShiftCaptureBuilder
   */
  setCheckStatusCommandInterval(timeMillis: number): TimeShiftCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * Set is front first.
   * @param isFrontFirst is front first
   * @returns TimeShiftCaptureBuilder
   */
  setIsFrontFirst(isFrontFirst: boolean): TimeShiftCaptureBuilder {
    this.checkAndInitTimeShiftSetting();
    if (this.options.timeShift != null) {
      this.options.timeShift.isFrontFirst = isFrontFirst;
    }
    return this;
  }

  /**
   * set time (sec) before 1st lens shooting
   * @param interval 1st interval
   * @returns TimeShiftCaptureBuilder
   */
  setFirstInterval(interval: TimeShiftIntervalEnum): TimeShiftCaptureBuilder {
    this.checkAndInitTimeShiftSetting();
    if (this.options.timeShift != null) {
      this.options.timeShift.firstInterval = interval;
    }
    return this;
  }

  /**
   * set time (sec) from 1st lens shooting until start of 2nd lens shooting.
   * @param interval 2nd interval
   * @returns TimeShiftCaptureBuilder
   */
  setSecondInterval(interval: TimeShiftIntervalEnum): TimeShiftCaptureBuilder {
    this.checkAndInitTimeShiftSetting();
    if (this.options.timeShift != null) {
      this.options.timeShift.secondInterval = interval;
    }
    return this;
  }

  private checkAndInitTimeShiftSetting() {
    if (this.options.timeShift == null) {
      this.options.timeShift = {};
    }
  }

  /**
   * Builds an instance of a TimeShiftCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   *
   * @return promise of TimeShiftCapture instance
   */
  build(): Promise<TimeShiftCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<TimeShiftCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getTimeShiftCaptureBuilder();
        await ThetaClientReactNative.buildTimeShiftCapture(params);
        resolve(new TimeShiftCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
