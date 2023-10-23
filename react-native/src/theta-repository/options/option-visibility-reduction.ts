/**
 * Visibility reduction.
 * Reduction visibility of camera body to still image when stitching.
 *
 * For
 * - RICOH THETA Z1 v1.11.1 or later
 */
export const VisibilityReductionEnum = {
  /** ON */
  ON: 'ON',
  /** OFF */
  OFF: 'OFF',
} as const;

/** type definition of visibilityReductionEnum */
export type VisibilityReductionEnum =
  (typeof VisibilityReductionEnum)[keyof typeof VisibilityReductionEnum];
