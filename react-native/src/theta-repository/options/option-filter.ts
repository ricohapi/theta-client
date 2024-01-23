/** Image processing filter. */
export const FilterEnum = {
  /** No filter. */
  OFF: 'OFF',
  /**
   * DR compensation.
   *
   * RICOH THETA X is not supported.
   */
  DR_COMP: 'DR_COMP',
  /** Noise reduction. */
  NOISE_REDUCTION: 'NOISE_REDUCTION',
  /** HDR. */
  HDR: 'HDR',
  /**
   * Handheld HDR.
   *
   * RICOH THETA X firmware v2.40.0 or later,
   * RICOH THETA Z1 firmware v1.20.1 or later,
   * and RICOH THETA V firmware v3.10.1 or later.
   */
  HH_HDR: 'HH_HDR',
} as const;

/** type definition of FilterEnum */
export type FilterEnum = (typeof FilterEnum)[keyof typeof FilterEnum];
