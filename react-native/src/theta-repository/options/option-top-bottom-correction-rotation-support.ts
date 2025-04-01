/**
 * Supported TopBottomCorrectionRotation
 */
export type TopBottomCorrectionRotationSupport = {
  /**
   * Supported pitch
   */
  pitch: TopBottomCorrectionRotationValueSupport;
  /**
   * Supported roll
   */
  roll: TopBottomCorrectionRotationValueSupport;
  /**
   * Supported yaw
   */
  yaw: TopBottomCorrectionRotationValueSupport;
};

/**
 * Supported value of TopBottomCorrectionRotation
 */
export type TopBottomCorrectionRotationValueSupport = {
  /**
   * maximum value
   */
  max: number;
  /**
   * minimum value
   */
  min: number;
  /**
   * Step size
   */
  stepSize: number;
};
