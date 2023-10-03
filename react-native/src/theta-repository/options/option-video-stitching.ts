/**
 * Video Stitching
 *
 * For
 * - RICOH THETA X
 * - RICOH THETA Z1
 * - RICOH THETA V
 */
export const VideoStitchingEnum = {
  /** Stitching is OFF */
  NONE: 'NONE',
  /** Stitching by the camera is ON */
  ONDEVICE: 'ONDEVICE',
} as const;

/** type definition of VideoStitchingEnum */
export type VideoStitchingEnum =
  (typeof VideoStitchingEnum)[keyof typeof VideoStitchingEnum];
