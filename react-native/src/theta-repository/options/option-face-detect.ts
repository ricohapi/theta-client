/**
 * Face detection
 *
 * For
 * - RICOH THETA X
 */
export const FaceDetectEnum = {
  /** Face detection ON */
  ON: 'ON',
  /** Face detection OFF */
  OFF: 'OFF',
} as const;

/** type definition of FaceDetectEnum */
export type FaceDetectEnum =
  (typeof FaceDetectEnum)[keyof typeof FaceDetectEnum];
