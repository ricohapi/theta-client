import { CaptureBuilder } from './capture';
import type {
  MaxRecordableTimeEnum,
  VideoFileFormatEnum,
} from '../theta-repository/options';

import { NativeModules } from 'react-native';
import {
  BaseNotify,
  NotifyController,
} from '../theta-repository/notify-controller';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

const NOTIFY_NAME = 'VIDEO-CAPTURE-STOP-ERROR';

interface CaptureStopErrorNotify extends BaseNotify {
  params?: {
    message: string;
  };
}

/**
 * VideoCapture class
 */
export class VideoCapture {
  notify: NotifyController;
  constructor(notify: NotifyController) {
    this.notify = notify;
  }

  /**
   * start video capture
   * @param onStopFailed the block for error of stopCapture
   * @return promise of captured file url
   */
  startCapture(
    onStopFailed?: (error: any) => void
  ): Promise<string | undefined> {
    if (onStopFailed) {
      this.notify.addNotify(NOTIFY_NAME, (event: CaptureStopErrorNotify) => {
        onStopFailed(event.params);
      });
    }
    return new Promise<string | undefined>(async (resolve, reject) => {
      await ThetaClientReactNative.startVideoCapture()
        .then((result?: string) => {
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
   * stop video capture
   */
  stopCapture() {
    ThetaClientReactNative.stopVideoCapture();
  }
}

/**
 * VideoCaptureBuilder class
 */
export class VideoCaptureBuilder extends CaptureBuilder<VideoCaptureBuilder> {
  /** construct VideoCaptureBuilder instance */
  constructor() {
    super();
  }

  /**
   * Set video file format.
   * @param {VideoFileFormatEnum} fileFormat Video file format to set
   * @return VideoCaptureBuilder
   */
  setFileFormat(fileFormat: VideoFileFormatEnum): VideoCaptureBuilder {
    this.options.fileFormat = fileFormat;
    return this;
  }

  /**
   * Set maximum recordable time (in seconds) of the camera.
   * @param {MaxRecordableTimeEnum} time Maximum recordable time to set
   * @return VideoCaptureBuilder
   */
  setMaxRecordableTime(time: MaxRecordableTimeEnum): VideoCaptureBuilder {
    this.options.maxRecordableTime = time;
    return this;
  }

  /**
   * Builds an instance of a VideoCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   *
   * @return promise of VideoCapture instance
   */
  build(): Promise<VideoCapture> {
    return new Promise<VideoCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getVideoCaptureBuilder();
        await ThetaClientReactNative.buildVideoCapture(this.options);
        resolve(new VideoCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
