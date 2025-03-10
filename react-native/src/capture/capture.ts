import type {
  ApertureEnum,
  ExposureCompensationEnum,
  ExposureDelayEnum,
  ExposureProgramEnum,
  GpsInfo,
  GpsTagRecordingEnum,
  IsoAutoHighLimitEnum,
  IsoEnum,
  Options,
  PhotoFileFormatEnum,
  WhiteBalanceEnum,
} from '../theta-repository/options';

/**
 * Capture Builder class
 */
export abstract class CaptureBuilder<T extends CaptureBuilder<T>> {
  /** capture options */
  options: Options;

  /** construct capture builder instance */
  constructor() {
    this.options = {};
  }

  /**
   * Set aperture value.
   * @param {ApertureEnum} aperture aperture value to set
   * @return CaptureBuilder
   */
  setAperture(aperture: ApertureEnum): T {
    this.options.aperture = aperture;
    return this as unknown as T;
  }

  /**
   * Set color temperature of the camera (Kelvin).
   * @param {number} kelvin Color temperature to set
   * @return CaptureBuilder
   */
  setColorTemperature(kelvin: number): T {
    this.options.colorTemperature = kelvin;
    return this as unknown as T;
  }

  /**
   * Set exposure compensation (EV).
   * @param {ExposureCompensationEnum} value Exposure compensation to set
   * @return CaptureBuilder
   */
  setExposureCompensation(value: ExposureCompensationEnum): T {
    this.options.exposureCompensation = value;
    return this as unknown as T;
  }

  /**
   * Set operating time (sec.) of the self-timer.
   * @param {ExposureDelayEnum} delay Operating time to set
   * @return CaptureBuilder
   */
  setExposureDelay(delay: ExposureDelayEnum): T {
    this.options.exposureDelay = delay;
    return this as unknown as T;
  }

  /**
   * Set exposure program. The exposure settings that take priority can be selected.
   * @param {ExposureProgramEnum} program Exposure program to set
   * @return CaptureBuilder
   */
  setExposureProgram(program: ExposureProgramEnum): T {
    this.options.exposureProgram = program;
    return this as unknown as T;
  }
  /**
   * Set GPS information.
   * @param {GpsInfo} gpsInfo GPS information to set
   * @return CaptureBuilder
   */
  setGpsInfo(gpsInfo: GpsInfo): T {
    this.options.gpsInfo = gpsInfo;
    return this as unknown as T;
  }

  /**
   * Set turns position information assigning ON/OFF.
   * @param {GpsTagRecordingEnum} value Turns position information assigning ON/OFF to set
   * @return CaptureBuilder
   */
  setGpsTagRecording(value: GpsTagRecordingEnum): T {
    this.options._gpsTagRecording = value;
    return this as unknown as T;
  }

  /**
   * Set ISO sensitivity.
   * @param {IsoEnum} iso ISO sensitivity to set
   * @return CaptureBuilder
   */
  setIso(iso: IsoEnum): T {
    this.options.iso = iso;
    return this as unknown as T;
  }

  /**
   * Set ISO sensitivity upper limit when ISO sensitivity is set to automatic.
   * @param {IsoAutoHighLimitEnum} iso ISO sensitivity upper limit to set
   * @return CaptureBuilder
   */
  setIsoAutoHighLimit(iso: IsoAutoHighLimitEnum): T {
    this.options.isoAutoHighLimit = iso;
    return this as unknown as T;
  }

  /**
   * Set white balance.
   * @param {WhiteBalanceEnum} whiteBalance White balance to set
   * @return CaptureBuilder
   */
  setWhiteBalance(whiteBalance: WhiteBalanceEnum): T {
    this.options.whiteBalance = whiteBalance;
    return this as unknown as T;
  }
}

/** Capturing status */
export const CapturingStatusEnum = {
  /** The process is starting */
  STARTING: 'STARTING',
  /** Capture in progress */
  CAPTURING: 'CAPTURING',
  /** Self-timer in progress */
  SELF_TIMER_COUNTDOWN: 'SELF_TIMER_COUNTDOWN',
} as const;

/** type definition of CapturingStatusEnum */
export type CapturingStatusEnum =
  (typeof CapturingStatusEnum)[keyof typeof CapturingStatusEnum];

export class PhotoCaptureBuilderBase<
  T extends PhotoCaptureBuilderBase<T>
> extends CaptureBuilder<T> {
  /**
   * Set photo file format.
   * @param {PhotoFileFormatEnum} fileFormat Photo file format to set
   * @return PhotoCaptureBuilder
   */
  setFileFormat(fileFormat: PhotoFileFormatEnum): T {
    this.options.fileFormat = fileFormat;
    return this as unknown as T;
  }
}
