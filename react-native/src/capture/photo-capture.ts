import { CapturingStatusEnum, PhotoCaptureBuilderBase } from './capture';
import type { FilterEnum, PresetEnum } from '../theta-repository/options';

import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_CAPTURING = 'PHOTO-CAPTURING';

interface CapturingNotify extends BaseNotify {
  params?: {
    status: CapturingStatusEnum;
  };
}

/**
 * PhotoCapture class
 */
export class PhotoCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }

  /**
   * Take a picture
   *
   * @param onCapturing Called when change capture status
   * @return promise of token file url
   */
  async takePicture(
    onCapturing?: (status: CapturingStatusEnum) => void
  ): Promise<string | undefined> {
    if (onCapturing) {
      this.notify.addNotify(NOTIFY_CAPTURING, (event: CapturingNotify) => {
        if (event.params?.status) {
          onCapturing(event.params.status);
        }
      });
    }
    try {
      const fileUrl = await ThetaClientReactNative.takePicture();
      return fileUrl ?? undefined;
    } catch (error) {
      throw error;
    } finally {
      this.notify.removeNotify(NOTIFY_CAPTURING);
    }
  }
}

/**
 * PhotoCaptureBaseBuilder class
 */
export class PhotoCaptureBuilder extends PhotoCaptureBuilderBase<PhotoCaptureBuilder> {
  interval?: number;

  /** construct PhotoCaptureBuilder instance */
  constructor() {
    super();
    this.interval = undefined;
  }

  /**
   * set interval of checking take picture status command
   * @param timeMillis interval
   * @returns PhotoCaptureBuilder
   */
  setCheckStatusCommandInterval(timeMillis: number): PhotoCaptureBuilder {
    this.interval = timeMillis;
    return this;
  }

  /**
   * Set image processing filter.
   * @param {FilterEnum} filter Image processing filter to set
   * @return PhotoCaptureBuilder
   */
  setFilter(filter: FilterEnum): PhotoCaptureBuilder {
    this.options.filter = filter;
    return this;
  }

  /**
   * Set preset mode of Theta SC2 and Theta SC2 for business.
   * @param {FilterEnum} preset Preset mode to set
   * @return PhotoCaptureBuilder
   */
  setPreset(preset: PresetEnum): PhotoCaptureBuilder {
    this.options.preset = preset;
    return this;
  }

  /**
   * Builds an instance of a PhotoCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   *
   * @return promise of PhotoCapture instance
   */
  build(): Promise<PhotoCapture> {
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<PhotoCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getPhotoCaptureBuilder();
        await ThetaClientReactNative.buildPhotoCapture(params);
        resolve(new PhotoCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
