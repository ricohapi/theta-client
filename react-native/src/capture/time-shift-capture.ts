import type { EmitterSubscription } from 'react-native';
import { CaptureBuilder } from './capture';
import { NativeModules } from 'react-native';
import { BaseNotify, CaptureProgressNotify, addNotifyListener } from './notify';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

/**
 * TimeShiftCapture class
 */
export class TimeShiftCapture {

  eventListener: EmitterSubscription;
  notifyList = new Map<string, ((notify: BaseNotify) => void) | undefined>();

  constructor() {
    this.eventListener = addNotifyListener((notify: BaseNotify) => {
      this.notifyList.get(notify.name)?.(notify as BaseNotify);
    })
  }

  /**
   * start time-shift
   * @param onProgress the block for time-shift onProgress
   * @return promise of captured file url
   */
  async startCapture(onProgress?: (completion?: number) => void): Promise<string> {
    const promise = await ThetaClientReactNative.startTimeShiftCapture();
    this.notifyList.set('TIME-SHIFT-PROGRESS', onProgress ? (event: CaptureProgressNotify) => {
      onProgress(event.params?.completion);
    } : undefined);
    return promise;
  }

  /**
   * stop time-shift
   */
  async stopCapture(): Promise<any> {
    return await ThetaClientReactNative.stopTimeShiftCapture();
  }

  release() {
    this.eventListener.remove();
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
  setCheckStatusCommandInterval(timeMillis?: number): TimeShiftCaptureBuilder {
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
    return ThetaClientReactNative.buildTimeShiftCapture(this.options, this.interval).then(
      () => new TimeShiftCapture()
    );
  }
}
