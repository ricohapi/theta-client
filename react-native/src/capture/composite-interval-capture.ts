import { CaptureBuilder } from './capture';
import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_PROGRESS = 'COMPOSITE-INTERVAL-PROGRESS';
const NOTIFY_STOP_ERROR = 'COMPOSITE-INTERVAL-STOP-ERROR';

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
 * CompositeIntervalCapture class
 */
export class CompositeIntervalCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }
  /**
   * start interval composite shooting
   * @param onProgress the block for interval composite shooting onProgress
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
      await ThetaClientReactNative.startCompositeIntervalCapture()
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
   * cancel interval composite shooting
   */
  async cancelCapture(): Promise<any> {
    return await ThetaClientReactNative.cancelCompositeIntervalCapture();
  }
}

/**
 * CompositeIntervalCaptureBuilder class
 */
export class CompositeIntervalCaptureBuilder extends CaptureBuilder<CompositeIntervalCaptureBuilder> {
  interval?: number;
  readonly shootingTimeSec: number;

  /** construct CompositeIntervalCaptureBuilder instance */
  constructor(shootingTimeSec: number) {
    super();

    this.interval = undefined;
    this.shootingTimeSec = shootingTimeSec;
  }

  /**
   * set interval of checking interval composite shooting status command
   * @param timeMillis interval
   * @returns CompositeIntervalCaptureBuilder
   */
  setCheckStatusCommandInterval(
    timeMillis: number
  ): CompositeIntervalCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * Set In-progress save interval for interval composite shooting (sec).
   * @param {interval} sec sec.
   * @return CompositeIntervalCaptureBuilder
   */
  setCompositeShootingOutputInterval(
    sec: number
  ): CompositeIntervalCaptureBuilder {
    this.options.compositeShootingOutputInterval = sec;
    return this;
  }

  /**
   * Builds an instance of a CompositeIntervalCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   * @return promise of CompositeIntervalCapture instance
   */
  build(): Promise<CompositeIntervalCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<CompositeIntervalCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getCompositeIntervalCaptureBuilder(
          this.shootingTimeSec
        );
        await ThetaClientReactNative.buildCompositeIntervalCapture(params);
        resolve(new CompositeIntervalCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
