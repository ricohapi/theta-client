import { CaptureBuilder } from './capture';
import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_PROGRESS = 'SHOT-COUNT-SPECIFIED-INTERVAL-PROGRESS';
const NOTIFY_STOP_ERROR = 'SHOT-COUNT-SPECIFIED-INTERVAL-STOP-ERROR';

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

/**
 * ShotCountSpecifiedIntervalCapture class
 */
export class ShotCountSpecifiedIntervalCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }
  /**
   * start interval shooting with the shot count specified
   * @param onProgress the block for interval shooting with the shot count specified onProgress
   * @param onStopFailed the block for error of cancelCapture
   * @return promise of captured file url
   */
  async startCapture(
    onProgress?: (completion?: number) => void,
    onStopFailed?: (error: any) => void
  ): Promise<string[] | undefined> {
    if (onProgress) {
      this.notify.addNotify(NOTIFY_PROGRESS, (event: CaptureProgressNotify) => {
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

    return new Promise<string[] | undefined>(async (resolve, reject) => {
      await ThetaClientReactNative.startShotCountSpecifiedIntervalCapture()
        .then((result?: string[]) => {
          resolve(result ?? undefined);
        })
        .catch((error: any) => {
          reject(error);
        })
        .finally(() => {
          this.notify.removeNotify(NOTIFY_PROGRESS);
          this.notify.removeNotify(NOTIFY_STOP_ERROR);
        });
    });
  }

  /**
   * cancel interval shooting with the shot count specified
   */
  async cancelCapture(): Promise<any> {
    return await ThetaClientReactNative.cancelShotCountSpecifiedIntervalCapture();
  }
}

/**
 * ShotCountSpecifiedIntervalCaptureBuilder class
 */
export class ShotCountSpecifiedIntervalCaptureBuilder extends CaptureBuilder<ShotCountSpecifiedIntervalCaptureBuilder> {
  interval?: number;
  readonly shotCount: number;

  /** construct ShotCountSpecifiedIntervalCaptureBuilder instance */
  constructor(shotCount: number) {
    super();

    this.interval = undefined;
    this.shotCount = shotCount;
  }

  /**
   * set interval of checking interval shooting with the shot count specified status command
   * @param timeMillis interval
   * @returns ShotCountSpecifiedIntervalCaptureBuilder
   */
  setCheckStatusCommandInterval(
    timeMillis: number
  ): ShotCountSpecifiedIntervalCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * Set shooting interval (sec.) for interval shooting.
   * @param {interval} interval sec.
   * @return ShotCountSpecifiedIntervalCaptureBuilder
   */
  setCaptureInterval(
    interval: number
  ): ShotCountSpecifiedIntervalCaptureBuilder {
    this.options.captureInterval = interval;
    return this;
  }

  /**
   * Builds an instance of a ShotCountSpecifiedIntervalCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   * @return promise of ShotCountSpecifiedIntervalCapture instance
   */
  build(): Promise<ShotCountSpecifiedIntervalCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<ShotCountSpecifiedIntervalCapture>(
      async (resolve, reject) => {
        try {
          await ThetaClientReactNative.getShotCountSpecifiedIntervalCaptureBuilder(
            this.shotCount
          );
          await ThetaClientReactNative.buildShotCountSpecifiedIntervalCapture(
            params
          );
          resolve(
            new ShotCountSpecifiedIntervalCapture(NotifyController.instance)
          );
        } catch (error) {
          reject(error);
        }
      }
    );
  }
}
