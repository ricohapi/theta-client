/**
 * Time shift shooting settings.
 *
 * For Theta X, Z1 and V.
 **/

export type TimeShift = {
  /**
   * Shooting order.
   * if true, first shoot the front side (side with Theta logo) then shoot the rear side (side with monitor).
   * if false, first shoot the rear side then shoot the front side.
   * Default is front first.
   */
  isFrontFirst?: boolean;

  /**
   * Time (sec) before 1st lens shooting.
   * For V or Z1, default is 5. For X, default is 2.
   */
  firstInterval?: TimeShiftIntervalEnum;

  /**
   * Time (sec) from 1st lens shooting until start of 2nd lens shooting.
   * Default is 5.
   */
  secondInterval?: TimeShiftIntervalEnum;
};

/**
 * Time shift interval in seconds
 */
export const TimeShiftIntervalEnum = {
  /** 0 second */
  INTERVAL_0: 'INTERVAL_0',
  /** 1 second */
  INTERVAL_1: 'INTERVAL_1',
  /** 2 seconds */
  INTERVAL_2: 'INTERVAL_2',
  /** 3 seconds */
  INTERVAL_3: 'INTERVAL_3',
  /** 4 seconds */
  INTERVAL_4: 'INTERVAL_4',
  /** 5 seconds */
  INTERVAL_5: 'INTERVAL_5',
  /** 6 seconds */
  INTERVAL_6: 'INTERVAL_6',
  /** 7 seconds */
  INTERVAL_7: 'INTERVAL_7',
  /** 8 seconds */
  INTERVAL_8: 'INTERVAL_8',
  /** 9 seconds */
  INTERVAL_9: 'INTERVAL_9',
  /** 10 seconds */
  INTERVAL_10: 'INTERVAL_10',
} as const;

/** Type definition of TimeShiftIntervalEnum */
export type TimeShiftIntervalEnum =
  (typeof TimeShiftIntervalEnum)[keyof typeof TimeShiftIntervalEnum];
