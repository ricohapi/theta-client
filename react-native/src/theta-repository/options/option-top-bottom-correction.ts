/**
 * top bottom correction
 *
 * Sets the top/bottom correction.  For RICOH THETA V and RICOH
 * THETA Z1, the top/bottom correction can be set only for still
 * images.  For RICOH THETA X, the top/bottom correction can be
 * set for both still images and videos.
 */
export const TopBottomCorrectionOptionEnum = {
  /**
   * Top/bottom correction is performed.
   */
  APPLY: 'APPLY',
  /**
   * Refer to top/bottom correction when shooting with "ApplyAuto"
   */
  APPLY_AUTO: 'APPLY_AUTO',
  /**
   * Top/bottom correction is performed. The parameters used for
   * top/bottom correction for the first image are saved and used
   * for the 2nd and subsequent images.(RICOH THETA X or later)
   */
  APPLY_SEMIAUTO: 'APPLY_SEMIAUTO',
  /**
   * Performs top/bottom correction and then saves the parameters.
   */
  APPLY_SAVE: 'APPLY_SAVE',
  /**
   * Performs top/bottom correction using the saved parameters.
   */
  APPLY_LOAD: 'APPLY_LOAD',
  /**
   * Does not perform top/bottom correction.
   */
  DISAPPLY: 'DISAPPLY',
  /**
   * Performs the top/bottom correction with the specified front
   * position. The front position can be specified with
   * _topBottomCorrectionRotation.
   */
  MANUAL: 'MANUAL',
} as const;

/** type definition of TopBottomCorrectionOptionEnum */
export type TopBottomCorrectionOptionEnum =
  (typeof TopBottomCorrectionOptionEnum)[keyof typeof TopBottomCorrectionOptionEnum];
