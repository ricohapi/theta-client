import { CaptureBuilder, CapturingStatusEnum } from './capture';
import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_NAME = 'LIMITLESS-INTERVAL-CAPTURE-STOP-ERROR';
const NOTIFY_CAPTURING = 'LIMITLESS-INTERVAL-CAPTURE-CAPTURING';

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
 * LimitlessIntervalCapture class
 */
export class LimitlessIntervalCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }

  /**
   * start limitless interval capture
   * @param onStopFailed the block for error of stopCapture
   * @param onCapturing Called when change capture status
   * @return promise of captured file url
   */
  startCapture(
    onStopFailed?: (error: any) => void,
    onCapturing?: (status: CapturingStatusEnum) => void
  ): Promise<string[] | undefined> {
    if (onStopFailed) {
      this.notify.addNotify(NOTIFY_NAME, (event: CaptureStopErrorNotify) => {
        onStopFailed(event.params);
      });
    }
    if (onCapturing) {
      this.notify.addNotify(NOTIFY_CAPTURING, (event: CapturingNotify) => {
        if (event.params?.status) {
          onCapturing(event.params.status);
        }
      });
    }
    return new Promise<string[] | undefined>(async (resolve, reject) => {
      await ThetaClientReactNative.startLimitlessIntervalCapture()
        .then((result?: string[]) => {
          resolve(result ?? undefined);
        })
        .catch((error: any) => {
          reject(error);
        })
        .finally(() => {
          this.notify.removeNotify(NOTIFY_NAME);
          this.notify.removeNotify(NOTIFY_CAPTURING);
        });
    });
  }

  /**
   * stop limitless interval capture
   */
  stopCapture() {
    ThetaClientReactNative.stopLimitlessIntervalCapture();
  }
}

/**
 * LimitlessIntervalCaptureBuilder class
 */
export class LimitlessIntervalCaptureBuilder extends CaptureBuilder<LimitlessIntervalCaptureBuilder> {
  interval?: number;

  /** construct LimitlessIntervalCaptureBuilder instance */
  constructor() {
    super();
    this.interval = undefined;
  }

  /**
   * set interval of checking continuous shooting status command
   * @param timeMillis interval
   * @returns LimitlessIntervalCaptureBuilder
   */
  setCheckStatusCommandInterval(
    timeMillis: number
  ): LimitlessIntervalCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * Set shooting interval (sec.) for interval shooting.
   * @param {interval} interval sec.
   * @return LimitlessIntervalCaptureBuilder
   */
  setCaptureInterval(interval: number): LimitlessIntervalCaptureBuilder {
    this.options.captureInterval = interval;
    return this;
  }

  /**
   * Builds an instance of a LimitlessIntervalCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   *
   * @return promise of LimitlessIntervalCapture instance
   */
  build(): Promise<LimitlessIntervalCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<LimitlessIntervalCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getLimitlessIntervalCaptureBuilder();
        await ThetaClientReactNative.buildLimitlessIntervalCapture(params);
        resolve(new LimitlessIntervalCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
