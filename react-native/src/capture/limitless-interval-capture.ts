import { CaptureBuilder } from './capture';
import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_NAME = 'LIMITLESS-INTERVAL-CAPTURE-STOP-ERROR';

interface CaptureStopErrorNotify extends BaseNotify {
  params?: {
    message: string;
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
   * @return promise of captured file url
   */
  startCapture(
    onStopFailed?: (error: any) => void
  ): Promise<string[] | undefined> {
    if (onStopFailed) {
      this.notify.addNotify(NOTIFY_NAME, (event: CaptureStopErrorNotify) => {
        onStopFailed(event.params);
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
  /** construct LimitlessIntervalCaptureBuilder instance */
  constructor() {
    super();
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
    return new Promise<LimitlessIntervalCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getLimitlessIntervalCaptureBuilder();
        await ThetaClientReactNative.buildLimitlessIntervalCapture(
          this.options
        );
        resolve(new LimitlessIntervalCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
