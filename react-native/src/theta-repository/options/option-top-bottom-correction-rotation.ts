/**
 * Sets the front position for the top/bottom correction.
 * Enabled only for _topBottomCorrection Manual.
 */
export type TopBottomCorrectionRotation = {
  /**
   * Specifies the pitch.
   * Specified range is -90.0 to +90.0, stepSize is 0.1
   */
  pitch: number;
  /**
   * Specifies the roll.
   * Specified range is -180.0 to +180.0, stepSize is 0.1
   */
  roll: number;
  /**
   * Specifies the yaw.
   * Specified range is -180.0 to +180.0, stepSize is 0.1
   */
  yaw: number;
};
