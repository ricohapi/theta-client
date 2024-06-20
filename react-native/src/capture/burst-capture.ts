import { CaptureBuilder, CapturingStatusEnum } from './capture';
import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
import type {
  BurstBracketStepEnum,
  BurstCaptureNumEnum,
  BurstCompensationEnum,
  BurstEnableIsoControlEnum,
  BurstMaxExposureTimeEnum,
  BurstModeEnum,
  BurstOrderEnum,
} from '../theta-repository/options';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_PROGRESS = 'BURST-PROGRESS';
const NOTIFY_STOP_ERROR = 'BURST-STOP-ERROR';
const NOTIFY_CAPTURING = 'BURST-CAPTURING';

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
 * BurstCapture class
 */
export class BurstCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }
  /**
   * start burst shooting
   * @param onProgress the block for burst shooting onProgress
   * @param onStopFailed Called when stopCapture error occurs
   * @param onCapturing Called when change capture status
   * @return promise of captured file url
   */
  async startCapture(
    onProgress?: (completion?: number) => void,
    onStopFailed?: (error: any) => void,
    onCapturing?: (status: CapturingStatusEnum) => void
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
    if (onCapturing) {
      this.notify.addNotify(NOTIFY_CAPTURING, (event: CapturingNotify) => {
        if (event.params?.status) {
          onCapturing(event.params.status);
        }
      });
    }

    return new Promise<string[] | undefined>(async (resolve, reject) => {
      await ThetaClientReactNative.startBurstCapture()
        .then((result?: string[]) => {
          resolve(result ?? undefined);
        })
        .catch((error: any) => {
          reject(error);
        })
        .finally(() => {
          this.notify.removeNotify(NOTIFY_PROGRESS);
          this.notify.removeNotify(NOTIFY_STOP_ERROR);
          this.notify.removeNotify(NOTIFY_CAPTURING);
        });
    });
  }

  /**
   * cancel burst shooting
   */
  async cancelCapture(): Promise<any> {
    return await ThetaClientReactNative.cancelBurstCapture();
  }
}

/**
 * BurstCaptureBuilder class
 */
export class BurstCaptureBuilder extends CaptureBuilder<BurstCaptureBuilder> {
  interval?: number;
  readonly burstCaptureNum: BurstCaptureNumEnum;
  readonly burstBracketStep: BurstBracketStepEnum;
  readonly burstCompensation: BurstCompensationEnum;
  readonly burstMaxExposureTime: BurstMaxExposureTimeEnum;
  readonly burstEnableIsoControl: BurstEnableIsoControlEnum;
  readonly burstOrder: BurstOrderEnum;

  /** construct BurstCaptureBuilder instance */
  constructor(
    burstCaptureNum: BurstCaptureNumEnum,
    burstBracketStep: BurstBracketStepEnum,
    burstCompensation: BurstCompensationEnum,
    burstMaxExposureTime: BurstMaxExposureTimeEnum,
    burstEnableIsoControl: BurstEnableIsoControlEnum,
    burstOrder: BurstOrderEnum
  ) {
    super();

    this.interval = undefined;
    this.burstCaptureNum = burstCaptureNum;
    this.burstBracketStep = burstBracketStep;
    this.burstCompensation = burstCompensation;
    this.burstMaxExposureTime = burstMaxExposureTime;
    this.burstEnableIsoControl = burstEnableIsoControl;
    this.burstOrder = burstOrder;
  }

  /**
   * set interval of checking burst shooting status command
   * @param timeMillis interval
   * @returns BurstCaptureBuilder
   */
  setCheckStatusCommandInterval(timeMillis: number): BurstCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * BurstMode setting.
   * When this is set to ON, burst shooting is enabled,
   * and a screen dedicated to burst shooting is displayed in Live View.
   * @param mode BurstMode
   * @return BurstCaptureBuilder
   */
  setBurstMode(mode: BurstModeEnum): BurstCaptureBuilder {
    this.options.burstMode = mode;
    return this;
  }

  /**
   * Builds an instance of a BurstCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   * @return promise of BurstCapture instance
   */
  build(): Promise<BurstCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<BurstCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getBurstCaptureBuilder(
          this.burstCaptureNum,
          this.burstBracketStep,
          this.burstCompensation,
          this.burstMaxExposureTime,
          this.burstEnableIsoControl,
          this.burstOrder
        );
        await ThetaClientReactNative.buildBurstCapture(params);
        resolve(new BurstCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
