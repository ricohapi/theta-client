import { CaptureBuilder } from './capture';
import type {
  MaxRecordableTimeEnum,
  VideoFileFormatEnum,
} from '../theta-repository/options';

import { NativeModules } from 'react-native';
const ThetaClientReactNative = NativeModules.ThetaClientBleReactNative;

/**
 * VideoCapture class
 */
export class VideoCapture {
  /**
   * start video capture
   * @return promise of captured file url
   */
  startCapture(): Promise<string> {
    return ThetaClientReactNative.startCapture();
  }
  /**
   * stop video capture
   */
  stopCapture() {
    ThetaClientReactNative.stopCapture();
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
    return ThetaClientReactNative.buildVideoCapture(this.options).then(
      () => new VideoCapture()
    );
  }
}
