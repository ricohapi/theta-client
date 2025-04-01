import { CaptureBuilder, CapturingStatusEnum } from './capture';
import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
import {
  ContinuousNumberEnum,
  PhotoFileFormatEnum,
  OptionNameEnum,
} from '../theta-repository/options';
import { getOptions } from '../theta-repository';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_PROGRESS = 'CONTINUOUS-PROGRESS';
const NOTIFY_CAPTURING = 'CONTINUOUS-CAPTURING';

interface CaptureProgressNotify extends BaseNotify {
  params?: {
    completion: number;
  };
}

interface CapturingNotify extends BaseNotify {
  params?: {
    status: CapturingStatusEnum;
  };
}

/**
 * ContinuousCapture class
 */
export class ContinuousCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }
  /**
   * start continuous shooting
   * @param onProgress the block for continuous shooting onProgress
   * @param onCapturing Called when change capture status
   * @return promise of captured file url
   */
  async startCapture(
    onProgress?: (completion?: number) => void,
    onCapturing?: (status: CapturingStatusEnum) => void
  ): Promise<string[] | undefined> {
    if (onProgress) {
      this.notify.addNotify(NOTIFY_PROGRESS, (event: CaptureProgressNotify) => {
        onProgress(event.params?.completion);
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
      await ThetaClientReactNative.startContinuousCapture()
        .then((result?: string[]) => {
          resolve(result ?? undefined);
        })
        .catch((error: any) => {
          reject(error);
        })
        .finally(() => {
          this.notify.removeNotify(NOTIFY_PROGRESS);
          this.notify.removeNotify(NOTIFY_CAPTURING);
        });
    });
  }

  /**
   * Get Number of shots for continuous shooting.
   * @returns ContinuousNumberEnum
   */
  async getContinuousNumber(): Promise<ContinuousNumberEnum> {
    const options = await getOptions([OptionNameEnum.ContinuousNumber]);
    return options?.continuousNumber ?? ContinuousNumberEnum.UNSUPPORTED;
  }
}

/**
 * ContinuousCaptureBuilder class
 */
export class ContinuousCaptureBuilder extends CaptureBuilder<ContinuousCaptureBuilder> {
  interval?: number;

  /** construct ContinuousCaptureBuilder instance */
  constructor() {
    super();

    this.interval = undefined;
  }

  /**
   * set interval of checking continuous shooting status command
   * @param timeMillis interval
   * @returns ContinuousCaptureBuilder
   */
  setCheckStatusCommandInterval(timeMillis: number): ContinuousCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * Set photo file format.
   * Continuous shooting only supports 5.5K and 11K
   * @param {PhotoFileFormatEnum} fileFormat Photo file format to set
   * @return PhotoCaptureBuilder
   */
  setFileFormat(fileFormat: PhotoFileFormatEnum): ContinuousCaptureBuilder {
    this.options.fileFormat = fileFormat;
    return this;
  }

  /**
   * Builds an instance of a ContinuousCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   * @return promise of ContinuousCapture instance
   */
  build(): Promise<ContinuousCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<ContinuousCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getContinuousCaptureBuilder();
        await ThetaClientReactNative.buildContinuousCapture(params);
        resolve(new ContinuousCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
