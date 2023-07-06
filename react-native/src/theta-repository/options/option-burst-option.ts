/**
 * Burst shooting setting.
 *
 * only For RICOH THETA Z1 firmware v2.10.1 or later
 */
export type BurstOption = {
  /**
   * @see BurstCaptureNumEnum
   */
  burstCaptureNum?: BurstCaptureNumEnum;

  /**
   * @see BurstBracketStepEnum
   */
  burstBracketStep?: BurstBracketStepEnum;

  /**
   * @see BurstCompensationEnum
   */
  burstCompensation?: BurstCompensationEnum;

  /**
   * @see BurstMaxExposureTimeEnum
   */
  burstMaxExposureTime?: BurstMaxExposureTimeEnum;

  /**
   * @see BurstEnableIsoControlEnum
   */
  burstEnableIsoControl?: BurstEnableIsoControlEnum;

  /**
   * @see BurstOrderEnum
   */
  burstOrder?: BurstOrderEnum;
};

/**
 * Number of shots for burst shooting
 * 1, 3, 5, 7, 9
 */
export const BurstCaptureNumEnum = {
  BURST_CAPTURE_NUM_1: 'BURST_CAPTURE_NUM_1',
  BURST_CAPTURE_NUM_3: 'BURST_CAPTURE_NUM_3',
  BURST_CAPTURE_NUM_5: 'BURST_CAPTURE_NUM_5',
  BURST_CAPTURE_NUM_7: 'BURST_CAPTURE_NUM_7',
  BURST_CAPTURE_NUM_9: 'BURST_CAPTURE_NUM_9',
} as const;

/** Type definition of BurstCaptureNumEnum */
export type BurstCaptureNumEnum =
  (typeof BurstCaptureNumEnum)[keyof typeof BurstCaptureNumEnum];

/**
 * Bracket value range between each shot for burst shooting
 * 0.0, 0.3, 0.7, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0
 */
export const BurstBracketStepEnum = {
  BRACKET_STEP_0_0: 'BRACKET_STEP_0_0',
  BRACKET_STEP_0_3: 'BRACKET_STEP_0_3',
  BRACKET_STEP_0_7: 'BRACKET_STEP_0_7',
  BRACKET_STEP_1_0: 'BRACKET_STEP_1_0',
  BRACKET_STEP_1_3: 'BRACKET_STEP_1_3',
  BRACKET_STEP_1_7: 'BRACKET_STEP_1_7',
  BRACKET_STEP_2_0: 'BRACKET_STEP_2_0',
  BRACKET_STEP_2_3: 'BRACKET_STEP_2_3',
  BRACKET_STEP_2_7: 'BRACKET_STEP_2_7',
  BRACKET_STEP_3_0: 'BRACKET_STEP_3_0',
} as const;

/** Type definition of BurstBracketStepEnum */
export type BurstBracketStepEnum =
  (typeof BurstBracketStepEnum)[keyof typeof BurstBracketStepEnum];

/**
 * Exposure compensation for the base image and entire shooting for burst shooting
 * -5.0, -4.7, -4,3, -4.0, -3.7, -3,3, -3.0, -2.7, -2,3, -2.0, -1.7, -1,3, -1.0, -0.7, -0,3,
 * 0.0, 0.3, 0.7, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 4.3, 4.7, 5.0
 */
export const BurstCompensationEnum = {
  BURST_COMPENSATION_DOWN_5_0: 'BURST_COMPENSATION_DOWN_5_0',
  BURST_COMPENSATION_DOWN_4_7: 'BURST_COMPENSATION_DOWN_4_7',
  BURST_COMPENSATION_DOWN_4_3: 'BURST_COMPENSATION_DOWN_4_3',
  BURST_COMPENSATION_DOWN_4_0: 'BURST_COMPENSATION_DOWN_4_0',
  BURST_COMPENSATION_DOWN_3_7: 'BURST_COMPENSATION_DOWN_3_7',
  BURST_COMPENSATION_DOWN_3_3: 'BURST_COMPENSATION_DOWN_3_3',
  BURST_COMPENSATION_DOWN_3_0: 'BURST_COMPENSATION_DOWN_3_0',
  BURST_COMPENSATION_DOWN_2_7: 'BURST_COMPENSATION_DOWN_2_7',
  BURST_COMPENSATION_DOWN_2_3: 'BURST_COMPENSATION_DOWN_2_3',
  BURST_COMPENSATION_DOWN_2_0: 'BURST_COMPENSATION_DOWN_2_0',
  BURST_COMPENSATION_DOWN_1_7: 'BURST_COMPENSATION_DOWN_1_7',
  BURST_COMPENSATION_DOWN_1_3: 'BURST_COMPENSATION_DOWN_1_3',
  BURST_COMPENSATION_DOWN_1_0: 'BURST_COMPENSATION_DOWN_1_0',
  BURST_COMPENSATION_DOWN_0_7: 'BURST_COMPENSATION_DOWN_0_7',
  BURST_COMPENSATION_DOWN_0_3: 'BURST_COMPENSATION_DOWN_0_3',
  BURST_COMPENSATION_0_0: 'BURST_COMPENSATION_0_0',
  BURST_COMPENSATION_UP_0_3: 'BURST_COMPENSATION_UP_0_3',
  BURST_COMPENSATION_UP_0_7: 'BURST_COMPENSATION_UP_0_7',
  BURST_COMPENSATION_UP_1_0: 'BURST_COMPENSATION_UP_1_0',
  BURST_COMPENSATION_UP_1_3: 'BURST_COMPENSATION_UP_1_3',
  BURST_COMPENSATION_UP_1_7: 'BURST_COMPENSATION_UP_1_7',
  BURST_COMPENSATION_UP_2_0: 'BURST_COMPENSATION_UP_2_0',
  BURST_COMPENSATION_UP_2_3: 'BURST_COMPENSATION_UP_2_3',
  BURST_COMPENSATION_UP_2_7: 'BURST_COMPENSATION_UP_2_7',
  BURST_COMPENSATION_UP_3_0: 'BURST_COMPENSATION_UP_3_0',
  BURST_COMPENSATION_UP_3_3: 'BURST_COMPENSATION_UP_3_3',
  BURST_COMPENSATION_UP_3_7: 'BURST_COMPENSATION_UP_3_7',
  BURST_COMPENSATION_UP_4_0: 'BURST_COMPENSATION_UP_4_0',
  BURST_COMPENSATION_UP_4_3: 'BURST_COMPENSATION_UP_4_3',
  BURST_COMPENSATION_UP_4_7: 'BURST_COMPENSATION_UP_4_7',
  BURST_COMPENSATION_UP_5_0: 'BURST_COMPENSATION_UP_5_0',
} as const;

/** Type definition of BurstCompensationEnum */
export type BurstCompensationEnum =
  (typeof BurstCompensationEnum)[keyof typeof BurstCompensationEnum];

/**
 * Maximum exposure time for burst shooting
 * 0.5, 0.625, 0.76923076, 1, 1.3, 1.6, 2, 2.5, 3.2, 4, 5, 6, 8, 10, 13, 15, 20, 25, 30, 40, 50, 60
 */
export const BurstMaxExposureTimeEnum = {
  MAX_EXPOSURE_TIME_0_5: 'MAX_EXPOSURE_TIME_0_5',
  MAX_EXPOSURE_TIME_0_625: 'MAX_EXPOSURE_TIME_0_625',
  MAX_EXPOSURE_TIME_0_76923076: 'MAX_EXPOSURE_TIME_0_76923076',
  MAX_EXPOSURE_TIME_1: 'MAX_EXPOSURE_TIME_1',
  MAX_EXPOSURE_TIME_1_3: 'MAX_EXPOSURE_TIME_1_3',
  MAX_EXPOSURE_TIME_1_6: 'MAX_EXPOSURE_TIME_1_6',
  MAX_EXPOSURE_TIME_2: 'MAX_EXPOSURE_TIME_2',
  MAX_EXPOSURE_TIME_2_5: 'MAX_EXPOSURE_TIME_2_5',
  MAX_EXPOSURE_TIME_3_2: 'MAX_EXPOSURE_TIME_3_2',
  MAX_EXPOSURE_TIME_4: 'MAX_EXPOSURE_TIME_4',
  MAX_EXPOSURE_TIME_5: 'MAX_EXPOSURE_TIME_5',
  MAX_EXPOSURE_TIME_6: 'MAX_EXPOSURE_TIME_6',
  MAX_EXPOSURE_TIME_8: 'MAX_EXPOSURE_TIME_8',
  MAX_EXPOSURE_TIME_10: 'MAX_EXPOSURE_TIME_10',
  MAX_EXPOSURE_TIME_13: 'MAX_EXPOSURE_TIME_13',
  MAX_EXPOSURE_TIME_15: 'MAX_EXPOSURE_TIME_15',
  MAX_EXPOSURE_TIME_20: 'MAX_EXPOSURE_TIME_20',
  MAX_EXPOSURE_TIME_25: 'MAX_EXPOSURE_TIME_25',
  MAX_EXPOSURE_TIME_30: 'MAX_EXPOSURE_TIME_30',
  MAX_EXPOSURE_TIME_40: 'MAX_EXPOSURE_TIME_40',
  MAX_EXPOSURE_TIME_50: 'MAX_EXPOSURE_TIME_50',
  MAX_EXPOSURE_TIME_60: 'MAX_EXPOSURE_TIME_60',
} as const;

/** Type definition of BurstMaxExposureTimeEnum */
export type BurstMaxExposureTimeEnum =
  (typeof BurstMaxExposureTimeEnum)[keyof typeof BurstMaxExposureTimeEnum];

/**
 * Adjustment with ISO sensitivity for burst shooting
 * 0: Do not adjust with ISO sensitivity, 1: Adjust with ISO sensitivity
 */
export const BurstEnableIsoControlEnum = {
  OFF: 'OFF',
  ON: 'ON',
} as const;

/** Type definition of BurstCaptureNumEnum */
export type BurstEnableIsoControlEnum =
  (typeof BurstEnableIsoControlEnum)[keyof typeof BurstEnableIsoControlEnum];

/**
 * Shooting order for burst shooting
 * 0: '0' → '-' → '+', 1: '-' → '0' → '+'
 */
export const BurstOrderEnum = {
  BURST_BRACKET_ORDER_0: 'BURST_BRACKET_ORDER_0',
  BURST_BRACKET_ORDER_1: 'BURST_BRACKET_ORDER_1',
} as const;

/** Type definition of BurstOrderEnum */
export type BurstOrderEnum =
  (typeof BurstOrderEnum)[keyof typeof BurstOrderEnum];
