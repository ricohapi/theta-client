import type { ExposureProgramEnum, IsoEnum, WhiteBalanceEnum } from './options';
import type { ApertureEnum } from './option-aperture';
import type { ShutterSpeedEnum } from './option-shutter-speed';
import type { ExposureCompensationEnum } from './option-exposure-compensation';

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
