/**
 * Still image stitching setting during shooting.
 *
 * For Theta X, Z1 and V.
 **/

export const ImageStitchingEnum = {
  /** Refer to stitching when shooting with "auto" */
  AUTO: 'AUTO',
  /** Performs static stitching */
  STATIC: 'STATIC',
  /** Performs dynamic stitching(RICOH THETA X or later) */
  DYNAMIC: 'DYNAMIC',
  /** For Normal shooting, performs dynamic stitching,
   * for Interval shooting, saves dynamic distortion correction parameters for the first image
   * and then uses them for the 2nd and subsequent images(RICOH THETA X is not supported) */
  DYNAMIC_AUTO: 'DYNAMIC_AUTO',
  /** Performs semi-dynamic stitching
   * Saves dynamic distortion correction parameters for the first image
   * and then uses them for the 2nd and subsequent images(RICOH THETA X or later) */
  DYNAMIC_SEMI_AUTO: 'DYNAMIC_SEMI_AUTO',
  /** Performs dynamic stitching and then saves distortion correction parameters */
  DYNAMIC_SAVE: 'DYNAMIC_SAVE',
  /** Performs stitching using the saved distortion correction parameters */
  DYNAMIC_LOAD: 'DYNAMIC_LOAD',
  /** Does not perform stitching */
  NONE: 'NONE',
} as const;

/** Type definition of ImageStitchingEnum */
export type ImageStitchingEnum =
  (typeof ImageStitchingEnum)[keyof typeof ImageStitchingEnum];
