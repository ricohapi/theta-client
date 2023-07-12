/**
 * Number of shots for continuous shooting.
 * It can be acquired by camera.getOptions.
 *
 * For RICOH THETA X
 * - 11k image: Maximum value 8
 * - 5.5k image: Maximum value 20
 *
 * Depending on available storage capacity, the value may be less than maximum.
 */
export const ContinuousNumberEnum = {
  /** Disable continuous shooting. */
  OFF: 'OFF',
  /** Maximum value 1 */
  MAX_1: 'MAX_1',
  /** Maximum value 2 */
  MAX_2: 'MAX_2',
  /** Maximum value 3 */
  MAX_3: 'MAX_3',
  /** Maximum value 4 */
  MAX_4: 'MAX_4',
  /** Maximum value 5 */
  MAX_5: 'MAX_5',
  /** Maximum value 6 */
  MAX_6: 'MAX_6',
  /** Maximum value 7 */
  MAX_7: 'MAX_7',
  /** Maximum value 8 */
  MAX_8: 'MAX_8',
  /** Maximum value 9 */
  MAX_9: 'MAX_9',
  /** Maximum value 10 */
  MAX_10: 'MAX_10',
  /** Maximum value 10 */
  MAX_11: 'MAX_11',
  /** Maximum value 10 */
  MAX_12: 'MAX_12',
  /** Maximum value 10 */
  MAX_13: 'MAX_13',
  /** Maximum value 10 */
  MAX_14: 'MAX_14',
  /** Maximum value 10 */
  MAX_15: 'MAX_15',
  /** Maximum value 10 */
  MAX_16: 'MAX_16',
  /** Maximum value 10 */
  MAX_17: 'MAX_17',
  /** Maximum value 10 */
  MAX_18: 'MAX_18',
  /** Maximum value 10 */
  MAX_19: 'MAX_19',
  /** Maximum value 20 */
  MAX_20: 'MAX_20',
  /**
   * Unsupported value
   * 
   * If camera.getOptions returns the number other than 0 to 20, this value is set.
   * Do not use this value to setOptions().
   */
  UNSUPPORTED: 'UNSUPPORTED',
} as const;

/** type definition of ContinuousNumberEnum */
export type ContinuousNumberEnum =
  (typeof ContinuousNumberEnum)[keyof typeof ContinuousNumberEnum];
