import type { ValueRange } from './options';

/**
 * Supported TopBottomCorrectionRotation
 */
export type TopBottomCorrectionRotationSupport = {
  /**
   * Supported pitch
   */
  pitch: ValueRange;
  /**
   * Supported roll
   */
  roll: ValueRange;
  /**
   * Supported yaw
   */
  yaw: ValueRange;
};
