import { CaptureBuilder } from './capture';
import type {
  FilterEnum,
  PhotoFileFormatEnum,
  PresetEnum,
} from '../theta-repository/options';

import { NativeModules } from 'react-native';
const ThetaClientReactNative = NativeModules.ThetaClientReactNative;

/**
 * PhotoCapture class
 */
export class PhotoCapture {
  /**
   * @return promise of token file url
   */
  takePicture(): Promise<string> {
    return ThetaClientReactNative.takePicture();
  }
}

/**
 * PhotoCaptureBaseBuilder class
 */
export class PhotoCaptureBuilder extends CaptureBuilder<PhotoCaptureBuilder> {
  /** construct PhotoCaptureBuilder instance */
  constructor() {
    super();
  }

  /**
   * Set photo file format.
   * @param {PhotoFileFormatEnum} fileFormat Photo file format to set
   * @return PhotoCaptureBuilder
   */
  setFileFormat(fileFormat: PhotoFileFormatEnum): PhotoCaptureBuilder {
    this.options.fileFormat = fileFormat;
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
    return ThetaClientReactNative.buildPhotoCapture(this.options).then(
      () => new PhotoCapture()
    );
  }
}
