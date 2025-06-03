/** Exposure compensation (EV). */
export const ExposureCompensationEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** -4.0 */
  M_4_0: 'M4_0',
  /** -3.7 */
  M_3_7: 'M3_7',
  /** -3.3 */
  M_3_3: 'M3_3',
  /** -3.0 */
  M_3_0: 'M3_0',
  /** -2.7 */
  M_2_7: 'M2_7',
  /** -2.3 */
  M_2_3: 'M2_3',
  /** -2.0 */
  M_2_0: 'M2_0',
  /** -1.7 */
  M_1_7: 'M1_7',
  /** -1.3 */
  M_1_3: 'M1_3',
  /** -1.0 */
  M_1_0: 'M1_0',
  /** -0.7 */
  M_0_7: 'M0_7',
  /** -0.3 */
  M_0_3: 'M0_3',
  /** 0 */
  ZERO: 'ZERO',
  /** 0.3 */
  P_0_3: 'P0_3',
  /** 0.7 */
  P_0_7: 'P0_7',
  /** 1.0 */
  P_1_0: 'P1_0',
  /** 1.3 */
  P_1_3: 'P1_3',
  /** 1.7 */
  P_1_7: 'P1_7',
  /** 2.0 */
  P_2_0: 'P2_0',
  /** 2.3 */
  P_2_3: 'P2_3',
  /** 2.7 */
  P_2_7: 'P2_7',
  /** 3.0 */
  P_3_0: 'P3_0',
  /** 3.3 */
  P_3_3: 'P3_3',
  /** 3.7 */
  P_3_7: 'P3_7',
  /** 4.0 */
  P_4_0: 'P4_0',
} as const;

/** type definition of ExposureCompensationEnum */
export type ExposureCompensationEnum =
  (typeof ExposureCompensationEnum)[keyof typeof ExposureCompensationEnum];
