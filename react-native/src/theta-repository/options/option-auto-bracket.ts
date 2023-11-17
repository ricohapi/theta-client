import type {
  ApertureEnum,
  ExposureCompensationEnum,
  ExposureProgramEnum,
  IsoEnum,
  WhiteBalanceEnum,
} from './options';
import type { ShutterSpeedEnum } from './option-shutter-speed';

/**
 * Multi bracket shooting setting
 */
export type BracketSetting = {
  /**
   * @see ApertureEnum
   */
  aperture?: ApertureEnum;

  /**
   * Color temperature of the camera (Kelvin), 2,500 to 10,000 in 100-Kelvin units.
   */
  colorTemperature?: number;

  /**
   * @see ExposureCompensationEnum
   */
  exposureCompensation?: ExposureCompensationEnum;

  /**
   * @see ExposureProgramEnum
   */
  exposureProgram?: ExposureProgramEnum;

  /**
   * @see IsoEnum
   */
  iso?: IsoEnum;

  /**
   * @see ShutterSpeedEnum
   */
  shutterSpeed?: ShutterSpeedEnum;

  /**
   * @see WhiteBalanceEnum
   */
  whiteBalance?: WhiteBalanceEnum;
};
