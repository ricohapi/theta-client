/**
 * Control camera lock/unlock
 */
export const CameraLockEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** Camera is unlocked */
  UNLOCK: 'UNLOCK',
  /**
   * Camera basic lock state
   * (Mode button, WLAN button, and Fn button presses are inhibited)
   */
  BASIC_LOCK: 'BASIC_LOCK',
  /**
   * Lock according to the parameters set in _cameraLockConfig.
   */
  CUSTOM_LOCK: 'CUSTOM_LOCK',
} as const;

/** type definition of CameraLockEnum */
export type CameraLockEnum =
  (typeof CameraLockEnum)[keyof typeof CameraLockEnum];
