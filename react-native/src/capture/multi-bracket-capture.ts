import { CaptureBuilder } from './capture';
import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
import type { BracketSetting } from 'src/theta-repository/options';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_PROGRESS = 'MULTI-BRACKET-PROGRESS';
const NOTIFY_STOP_ERROR = 'MULTI-BRACKET-STOP-ERROR';

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
 * MultiBracketCapture class
 */
export class MultiBracketCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }

  /**
   * start multi bracket shooting
   * @param onProgress the block for multi bracket shooting onProgress
   * @return promise of captured file urls
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
      await ThetaClientReactNative.startMultiBracketCapture()
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
   * cancel multi bracket shooting
   */
  async cancelCapture(): Promise<any> {
    return await ThetaClientReactNative.cancelMultiBracketCapture();
  }
}

/**
 * MultiBracketCaptureBuilder class
 */
export class MultiBracketCaptureBuilder extends CaptureBuilder<MultiBracketCaptureBuilder> {
  interval?: number;

  /** construct MultiBracketCaptureBuilder instance */
  constructor() {
    super();

    this.interval = undefined;
  }

  /**
   * set interval of checking multi bracket shooting status command
   * @param timeMillis interval
   * @returns MultiBracketCaptureBuilder
   */
  setCheckStatusCommandInterval(
    timeMillis: number
  ): MultiBracketCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * Set bracket settings for multi bracket shooting
   *
   * @param params array of bracket setting.
   * Number of settings have to be 2 to 13.
   * For Theta SC2, S and SC, other settings than iso, shutterSpeed and
   * colorTemperature are ignored.
   * For Theta X, exposureProgram and exposureCompensation are ignored
   * so that always manual program.
   * For others than Theta Z1, aperture is ignored.
   * To setting parameters that are not specified, default values are used.
   *
   * @returns MultiBracketCaptureBuilder
   */
  setBracketSettings(settings: BracketSetting[]): MultiBracketCaptureBuilder {
    this.options.autoBracket = settings;
    return this;
  }

  /**
   * Builds an instance of a MultiBracketCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   * @return promise of MultiBracketCapture instance
   */
  build(): Promise<MultiBracketCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<MultiBracketCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getMultiBracketCaptureBuilder();
        await ThetaClientReactNative.buildMultiBracketCapture(params);
        resolve(new MultiBracketCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
