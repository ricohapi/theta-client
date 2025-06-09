import { CaptureBuilder, CapturingStatusEnum } from './capture';
import { NativeModules } from 'react-native';
import type { TimeShiftIntervalEnum } from '../theta-repository/options';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_NAME = 'TIME-SHIFT-MANUAL-PROGRESS';
const NOTIFY_STOP_ERROR = 'TIME-SHIFT-MANUAL-STOP-ERROR';
const NOTIFY_CAPTURING = 'TIME-SHIFT-MANUAL-CAPTURING';

interface CaptureProgressNotify extends BaseNotify {
  params?: {
    completion: number;
  };
}

interface CaptureStopErrorNotify extends BaseNotify {
  params?: {
    message: string;
  };
}

interface CapturingNotify extends BaseNotify {
  params?: {
    status: CapturingStatusEnum;
  };
}

/**
 * TimeShiftManualCapture class
 */
export class TimeShiftManualCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }
  /**
   * start manual time-shift
   *
   * Later, need to call startSecondCapture() or cancelCapture()
   *
   * @param onProgress the block for time-shift onProgress
   * @param onStopFailed the block for error of cancelCapture
   * @param onCapturing Called when change capture status
   * @return promise of captured file url
   */
  async startCapture(
    onProgress?: (completion?: number) => void,
    onStopFailed?: (error: any) => void,
    onCapturing?: (status: CapturingStatusEnum) => void
  ): Promise<string | undefined> {
    if (onProgress) {
      this.notify.addNotify(NOTIFY_NAME, (event: CaptureProgressNotify) => {
        onProgress(event.params?.completion);
      });
    }
    if (onStopFailed) {
      this.notify.addNotify(
        NOTIFY_STOP_ERROR,
        (event: CaptureStopErrorNotify) => {
          onStopFailed(event.params);
        }
      );
    }
    if (onCapturing) {
      this.notify.addNotify(NOTIFY_CAPTURING, (event: CapturingNotify) => {
        if (event.params?.status) {
          onCapturing(event.params.status);
        }
      });
    }

    return new Promise<string | undefined>(async (resolve, reject) => {
      await ThetaClientReactNative.startTimeShiftManualCapture()
        .then((result?: string) => {
          resolve(result ?? undefined);
        })
        .catch((error: any) => {
          reject(error);
        })
        .finally(() => {
          this.notify.removeNotify(NOTIFY_NAME);
          this.notify.removeNotify(NOTIFY_STOP_ERROR);
          this.notify.removeNotify(NOTIFY_CAPTURING);
        });
    });
  }

  /**
   * cancel manual time-shift
   */
  async startSecondCapture(): Promise<any> {
    return await ThetaClientReactNative.startTimeShiftManualSecondCapture();
  }

  /**
   * cancel manual time-shift
   */
  async cancelCapture(): Promise<any> {
    return await ThetaClientReactNative.cancelTimeShiftManualCapture();
  }
}

/**
 * TimeShiftManualCaptureBuilder class
 */
export class TimeShiftManualCaptureBuilder extends CaptureBuilder<TimeShiftManualCaptureBuilder> {
  interval?: number;

  /** construct TimeShiftManualCaptureBuilder instance */
  constructor() {
    super();

    this.interval = undefined;
  }

  /**
   * set interval of checking time-shift status command
   * @param timeMillis interval
   * @returns TimeShiftManualCaptureBuilder
   */
  setCheckStatusCommandInterval(
    timeMillis: number
  ): TimeShiftManualCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * Set is front first.
   * @param isFrontFirst is front first
   * @returns TimeShiftManualCaptureBuilder
   */
  setIsFrontFirst(isFrontFirst: boolean): TimeShiftManualCaptureBuilder {
    this.checkAndInitTimeShiftSetting();
    if (this.options.timeShift != null) {
      this.options.timeShift.isFrontFirst = isFrontFirst;
    }
    return this;
  }

  /**
   * set time (sec) before 1st lens shooting
   * @param interval 1st interval
   * @returns TimeShiftManualCaptureBuilder
   */
  setFirstInterval(
    interval: TimeShiftIntervalEnum
  ): TimeShiftManualCaptureBuilder {
    this.checkAndInitTimeShiftSetting();
    if (this.options.timeShift != null) {
      this.options.timeShift.firstInterval = interval;
    }
    return this;
  }

  /**
   * set time (sec) from 1st lens shooting until start of 2nd lens shooting.
   * @param interval 2nd interval
   * @returns TimeShiftManualCaptureBuilder
   */
  setSecondInterval(
    interval: TimeShiftIntervalEnum
  ): TimeShiftManualCaptureBuilder {
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
   * Builds an instance of a TimeShiftManualCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   *
   * @return promise of TimeShiftManualCapture instance
   */
  build(): Promise<TimeShiftManualCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<TimeShiftManualCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getTimeShiftManualCaptureBuilder();
        await ThetaClientReactNative.buildTimeShiftManualCapture(params);
        resolve(new TimeShiftManualCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
