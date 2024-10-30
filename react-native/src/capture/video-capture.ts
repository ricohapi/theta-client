import { CaptureBuilder, CapturingStatusEnum } from './capture';
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
const NOTIFY_CAPTURING = 'VIDEO-CAPTURE-CAPTURING';
const NOTIFY_STARTED = 'VIDEO-CAPTURE-STARTED';

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

interface CaptureStartedNotify extends BaseNotify {
  params?: {
    fileUrl: string;
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
   * @param onCapturing Called when change capture status
   * @param onCaptureStarted Called when capture started
   * @return promise of captured file url
   */
  startCapture(
    onStopFailed?: (error: any) => void,
    onCapturing?: (status: CapturingStatusEnum) => void,
    onCaptureStarted?: (fileUrl?: string) => void
  ): Promise<string | undefined> {
    if (onStopFailed) {
      this.notify.addNotify(NOTIFY_NAME, (event: CaptureStopErrorNotify) => {
        onStopFailed(event.params);
      });
    }
    if (onCapturing) {
      this.notify.addNotify(NOTIFY_CAPTURING, (event: CapturingNotify) => {
        if (event.params?.status) {
          onCapturing(event.params.status);
        }
      });
    }
    if (onCaptureStarted) {
      this.notify.addNotify(NOTIFY_STARTED, (event: CaptureStartedNotify) => {
        onCaptureStarted(
          event.params?.fileUrl !== '' ? event.params?.fileUrl : undefined
        );
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
          this.notify.removeNotify(NOTIFY_CAPTURING);
          this.notify.removeNotify(NOTIFY_STARTED);
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
  interval?: number;

  /** construct VideoCaptureBuilder instance */
  constructor() {
    super();
    this.interval = undefined;
  }

  /**
   * set interval of checking continuous shooting status command
   * @param timeMillis interval
   * @returns VideoCaptureBuilder
   */
  setCheckStatusCommandInterval(timeMillis: number): VideoCaptureBuilder {
    this.interval = timeMillis;
    return this;
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
    let params = {
      ...this.options,
      // Cannot pass negative values in IOS, use objects
      _capture_interval: this.interval ?? -1,
    };
    return new Promise<VideoCapture>(async (resolve, reject) => {
      try {
        await ThetaClientReactNative.getVideoCaptureBuilder();
        await ThetaClientReactNative.buildVideoCapture(params);
        resolve(new VideoCapture(NotifyController.instance));
      } catch (error) {
        reject(error);
      }
    });
  }
}
