import type { EmitterSubscription } from 'react-native';
import { CaptureBuilder } from './capture';
import { NativeModules } from 'react-native';
import { BaseNotify, CaptureProgressNotify, addNotifyListener } from './notify';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

/**
 * TimeShiftCapture class
 */
export class TimeShiftCapture {
  eventListener?: EmitterSubscription = undefined;
  notifyList = new Map<string, ((notify: BaseNotify) => void) | undefined>();

  /**
   * start time-shift
   * @param onProgress the block for time-shift onProgress
   * @return promise of captured file url
   */
  async startCapture(
    onProgress?: (completion?: number) => void
  ): Promise<string> {
    await this.initNotify();
    this.notifyList.set(
      'TIME-SHIFT-PROGRESS',
      onProgress
        ? (event: CaptureProgressNotify) => {
            onProgress(event.params?.completion);
          }
        : undefined
    );

    return new Promise<string>(async (resolve, reject) => {
      await ThetaClientReactNative.startTimeShiftCapture()
        .then((result: string) => {
          resolve(result);
        })
        .catch((error: any) => {
          reject(error);
        })
        .finally(() => {
          this.releaseNotify();
        });
    });
  }

  /**
   * cancel time-shift
   */
  async cancelCapture(): Promise<any> {
    return await ThetaClientReactNative.cancelTimeShiftCapture();
  }

  async initNotify() {
    this.eventListener = addNotifyListener((notify: BaseNotify) => {
      this.notifyList.get(notify.name)?.(notify as BaseNotify);
    });
  }

  releaseNotify() {
    this.eventListener?.remove();
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
   * Builds an instance of a TimeShiftCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   *
   * @return promise of TimeShiftCapture instance
   */
  build(): Promise<TimeShiftCapture> {
    return ThetaClientReactNative.buildTimeShiftCapture(
      this.options,
      this.interval ?? -1
    ).then(() => new TimeShiftCapture());
  }
}
