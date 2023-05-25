/**
 * Shutter speed (sec).
 *
 * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
 * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
 *
 * ### Support value
 * The choice is listed below. There are certain range difference between each models and settings.
 *
 * | captureMode | exposureProgram | X or later | V or Z1 | SC | S |
 * | --- | --- | --- | --- | --- | --- |
 * | Still image shooting mode | Manual | 0.0000625 (1/16000) to 60 | 0.00004 (1/25000) to 60 | 0.000125 (1/8000) to 60 | 0.00015625 (1/6400) to 60 |
 * |                           | Shutter priority  | 0.0000625 (1/16000) to 15 | 0.00004 (1/25000) to 0.125 (1/8) | 0.00004 (1/25000) to 15 `*2`  |  |  |
 * | Video shooting mode `*1`    | Manual or Shutter priority | 0.0000625 (1/16000) to 0.03333333 (1/30) | 0.00004 (1/25000) to 0.03333333 (1/30) |  |  |
 * | Otherwise  |  | 0 (AUTO)  | 0 (AUTO)  | 0 (AUTO)  | 0 (AUTO)  |
 *
 * `*1` RICOH THETA Z1 and RICOH THETA V firmware v3.00.1 or later
 *
 * `*2` RICOH THETA Z1 firmware v1.50.1 or later and RICOH THETA V firmware v3.40.1 or later
 */
export const ShutterSpeedEnum = {
  /**
   * Shutter speed. auto
   */
  SHUTTER_SPEED_AUTO: 'SHUTTER_SPEED_AUTO',

  /**
   * Shutter speed. 60 sec
   */
  SHUTTER_SPEED_60: 'SHUTTER_SPEED_60',

  /**
   * Shutter speed. 50 sec
   *
   * RICOH THETA Z1 firmware v2.10.1 or later and RICOH THETA V firmware v3.80.1 or later.
   * For RICOH THETA X, all versions are supported.
   */
  SHUTTER_SPEED_50: 'SHUTTER_SPEED_50',

  /**
   * Shutter speed. 40 sec
   *
   * RICOH THETA Z1 firmware v2.10.1 or later and RICOH THETA V firmware v3.80.1 or later.
   * For RICOH THETA X, all versions are supported.
   */
  SHUTTER_SPEED_40: 'SHUTTER_SPEED_40',

  /**
   * Shutter speed. 30 sec
   */
  SHUTTER_SPEED_30: 'SHUTTER_SPEED_30',

  /**
   * Shutter speed. 25 sec
   */
  SHUTTER_SPEED_25: 'SHUTTER_SPEED_25',

  /**
   * Shutter speed. 20 sec
   */
  SHUTTER_SPEED_20: 'SHUTTER_SPEED_20',

  /**
   * Shutter speed. 15 sec
   */
  SHUTTER_SPEED_15: 'SHUTTER_SPEED_15',

  /**
   * Shutter speed. 13 sec
   */
  SHUTTER_SPEED_13: 'SHUTTER_SPEED_13',

  /**
   * Shutter speed. 10 sec
   */
  SHUTTER_SPEED_10: 'SHUTTER_SPEED_10',

  /**
   * Shutter speed. 8 sec
   */
  SHUTTER_SPEED_8: 'SHUTTER_SPEED_8',

  /**
   * Shutter speed. 6 sec
   */
  SHUTTER_SPEED_6: 'SHUTTER_SPEED_6',

  /**
   * Shutter speed. 5 sec
   */
  SHUTTER_SPEED_5: 'SHUTTER_SPEED_5',

  /**
   * Shutter speed. 4 sec
   */
  SHUTTER_SPEED_4: 'SHUTTER_SPEED_4',

  /**
   * Shutter speed. 3.2 sec
   */
  SHUTTER_SPEED_3_2: 'SHUTTER_SPEED_3_2',

  /**
   * Shutter speed. 2.5 sec
   */
  SHUTTER_SPEED_2_5: 'SHUTTER_SPEED_2_5',

  /**
   * Shutter speed. 2 sec
   */
  SHUTTER_SPEED_2: 'SHUTTER_SPEED_2',

  /**
   * Shutter speed. 1.6 sec
   */
  SHUTTER_SPEED_1_6: 'SHUTTER_SPEED_1_6',

  /**
   * Shutter speed. 1.3 sec
   */
  SHUTTER_SPEED_1_3: 'SHUTTER_SPEED_1_3',

  /**
   * Shutter speed. 1 sec
   */
  SHUTTER_SPEED_1: 'SHUTTER_SPEED_1',

  /**
   * Shutter speed. 1/3 sec(0.76923076)
   */
  SHUTTER_SPEED_ONE_OVER_1_3: 'SHUTTER_SPEED_ONE_OVER_1_3',

  /**
   * Shutter speed. 1/6 sec(0.625)
   */
  SHUTTER_SPEED_ONE_OVER_1_6: 'SHUTTER_SPEED_ONE_OVER_1_6',

  /**
   * Shutter speed. 1/2 sec(0.5)
   */
  SHUTTER_SPEED_ONE_OVER_2: 'SHUTTER_SPEED_ONE_OVER_2',

  /**
   * Shutter speed. 1/2.5 sec(0.4)
   */
  SHUTTER_SPEED_ONE_OVER_2_5: 'SHUTTER_SPEED_ONE_OVER_2_5',

  /**
   * Shutter speed. 1/3 sec(0.33333333)
   */
  SHUTTER_SPEED_ONE_OVER_3: 'SHUTTER_SPEED_ONE_OVER_3',

  /**
   * Shutter speed. 1/4 sec(0.25)
   */
  SHUTTER_SPEED_ONE_OVER_4: 'SHUTTER_SPEED_ONE_OVER_4',

  /**
   * Shutter speed. 1/5 sec(0.2)
   */
  SHUTTER_SPEED_ONE_OVER_5: 'SHUTTER_SPEED_ONE_OVER_5',

  /**
   * Shutter speed. 1/6 sec(0.16666666)
   */
  SHUTTER_SPEED_ONE_OVER_6: 'SHUTTER_SPEED_ONE_OVER_6',

  /**
   * Shutter speed. 1/8 sec(0.125)
   */
  SHUTTER_SPEED_ONE_OVER_8: 'SHUTTER_SPEED_ONE_OVER_8',

  /**
   * Shutter speed. 1/10 sec(0.1)
   */
  SHUTTER_SPEED_ONE_OVER_10: 'SHUTTER_SPEED_ONE_OVER_10',

  /**
   * Shutter speed. 1/13 sec(0.07692307)
   */
  SHUTTER_SPEED_ONE_OVER_13: 'SHUTTER_SPEED_ONE_OVER_13',

  /**
   * Shutter speed. 1/15 sec(0.06666666)
   */
  SHUTTER_SPEED_ONE_OVER_15: 'SHUTTER_SPEED_ONE_OVER_15',

  /**
   * Shutter speed. 1/20 sec(0.05)
   */
  SHUTTER_SPEED_ONE_OVER_20: 'SHUTTER_SPEED_ONE_OVER_20',

  /**
   * Shutter speed. 1/25 sec(0.04)
   */
  SHUTTER_SPEED_ONE_OVER_25: 'SHUTTER_SPEED_ONE_OVER_25',

  /**
   * Shutter speed. 1/30 sec(0.03333333)
   */
  SHUTTER_SPEED_ONE_OVER_30: 'SHUTTER_SPEED_ONE_OVER_30',

  /**
   * Shutter speed. 1/40 sec(0.025)
   */
  SHUTTER_SPEED_ONE_OVER_40: 'SHUTTER_SPEED_ONE_OVER_40',

  /**
   * Shutter speed. 1/50 sec(0.02)
   */
  SHUTTER_SPEED_ONE_OVER_50: 'SHUTTER_SPEED_ONE_OVER_50',

  /**
   * Shutter speed. 1/60 sec(0.01666666)
   */
  SHUTTER_SPEED_ONE_OVER_60: 'SHUTTER_SPEED_ONE_OVER_60',

  /**
   * Shutter speed. 1/80 sec(0.0125)
   */
  SHUTTER_SPEED_ONE_OVER_80: 'SHUTTER_SPEED_ONE_OVER_80',

  /**
   * Shutter speed. 1/100 sec(0.01)
   */
  SHUTTER_SPEED_ONE_OVER_100: 'SHUTTER_SPEED_ONE_OVER_100',

  /**
   * Shutter speed. 1/125 sec(0.008)
   */
  SHUTTER_SPEED_ONE_OVER_125: 'SHUTTER_SPEED_ONE_OVER_125',

  /**
   * Shutter speed. 1/160 sec(0.00625)
   */
  SHUTTER_SPEED_ONE_OVER_160: 'SHUTTER_SPEED_ONE_OVER_160',

  /**
   * Shutter speed. 1/200 sec(0.005)
   */
  SHUTTER_SPEED_ONE_OVER_200: 'SHUTTER_SPEED_ONE_OVER_200',

  /**
   * Shutter speed. 1/250 sec(0.004)
   */
  SHUTTER_SPEED_ONE_OVER_250: 'SHUTTER_SPEED_ONE_OVER_250',

  /**
   * Shutter speed. 1/320 sec(0.003125)
   */
  SHUTTER_SPEED_ONE_OVER_320: 'SHUTTER_SPEED_ONE_OVER_320',

  /**
   * Shutter speed. 1/400 sec(0.0025)
   */
  SHUTTER_SPEED_ONE_OVER_400: 'SHUTTER_SPEED_ONE_OVER_400',

  /**
   * Shutter speed. 1/500 sec(0.002)
   */
  SHUTTER_SPEED_ONE_OVER_500: 'SHUTTER_SPEED_ONE_OVER_500',

  /**
   * Shutter speed. 1/640 sec(0.0015625)
   */
  SHUTTER_SPEED_ONE_OVER_640: 'SHUTTER_SPEED_ONE_OVER_640',

  /**
   * Shutter speed. 1/800 sec(0.00125)
   */
  SHUTTER_SPEED_ONE_OVER_800: 'SHUTTER_SPEED_ONE_OVER_800',

  /**
   * Shutter speed. 1/1000 sec(0.001)
   */
  SHUTTER_SPEED_ONE_OVER_1000: 'SHUTTER_SPEED_ONE_OVER_1000',

  /**
   * Shutter speed. 1/1250 sec(0.0008)
   */
  SHUTTER_SPEED_ONE_OVER_1250: 'SHUTTER_SPEED_ONE_OVER_1250',

  /**
   * Shutter speed. 1/1600 sec(0.000625)
   */
  SHUTTER_SPEED_ONE_OVER_1600: 'SHUTTER_SPEED_ONE_OVER_1600',

  /**
   * Shutter speed. 1/2000 sec(0.0005)
   */
  SHUTTER_SPEED_ONE_OVER_2000: 'SHUTTER_SPEED_ONE_OVER_2000',

  /**
   * Shutter speed. 1/2500 sec(0.0004)
   */
  SHUTTER_SPEED_ONE_OVER_2500: 'SHUTTER_SPEED_ONE_OVER_2500',

  /**
   * Shutter speed. 1/3200 sec(0.0003125)
   */
  SHUTTER_SPEED_ONE_OVER_3200: 'SHUTTER_SPEED_ONE_OVER_3200',

  /**
   * Shutter speed. 1/4000 sec(0.00025)
   */
  SHUTTER_SPEED_ONE_OVER_4000: 'SHUTTER_SPEED_ONE_OVER_4000',

  /**
   * Shutter speed. 1/5000 sec(0.0002)
   */
  SHUTTER_SPEED_ONE_OVER_5000: 'SHUTTER_SPEED_ONE_OVER_5000',

  /**
   * Shutter speed. 1/6400 sec(0.00015625)
   */
  SHUTTER_SPEED_ONE_OVER_6400: 'SHUTTER_SPEED_ONE_OVER_6400',

  /**
   * Shutter speed. 1/8000 sec(0.000125)
   */
  SHUTTER_SPEED_ONE_OVER_8000: 'SHUTTER_SPEED_ONE_OVER_8000',

  /**
   * Shutter speed. 1/10000 sec(0.0001)
   */
  SHUTTER_SPEED_ONE_OVER_10000: 'SHUTTER_SPEED_ONE_OVER_10000',

  /**
   * Shutter speed. 1/12500 sec(0.00008)
   *
   * No support for RICOH THETA X.
   */
  SHUTTER_SPEED_ONE_OVER_12500: 'SHUTTER_SPEED_ONE_OVER_12500',

  /**
   * Shutter speed. 1/12800 sec(0.00007812)
   *
   * Enabled only for RICOH THETA X.
   */
  SHUTTER_SPEED_ONE_OVER_12800: 'SHUTTER_SPEED_ONE_OVER_12800',

  /**
   * Shutter speed. 1/16000 sec(0.0000625)
   */
  SHUTTER_SPEED_ONE_OVER_16000: 'SHUTTER_SPEED_ONE_OVER_16000',

  /**
   * Shutter speed. 1/20000 sec(0.00005)
   */
  SHUTTER_SPEED_ONE_OVER_20000: 'SHUTTER_SPEED_ONE_OVER_20000',

  /**
   * Shutter speed. 1/25000 sec(0.00004)
   */
  SHUTTER_SPEED_ONE_OVER_25000: 'SHUTTER_SPEED_ONE_OVER_25000',
} as const;

/** type definition of ShutterSpeedEnum */
export type ShutterSpeedEnum =
  (typeof ShutterSpeedEnum)[keyof typeof ShutterSpeedEnum];
