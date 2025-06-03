/**
 * Camera Lock Config
 *
 * - It is possible to enable/disable the function for each HW key.
 * - It is possible to enable/disable the operation of the panel.
 * - When all supported buttons/components are in the "unlock" state, it will be the same as the normal setting.
 * - For THETA models, if there are no wlanKey, fnKey, or panel, it will return "lock". If there are no supported buttons/components, setting to "unlock" or "lock" will not return an error and will not perform any action.
 */
export type CameraLockConfig = {
  /** power key locked or unlocked. */
  isPowerKeyLocked?: boolean;
  /** Shutter key locked or unlocked. */
  isShutterKeyLocked?: boolean;
  /** mode key locked or unlocked. */
  isModeKeyLocked?: boolean;
  /** wlan key locked or unlocked. */
  isWlanKeyLocked?: boolean;
  /** fn key locked or unlocked. */
  isFnKeyLocked?: boolean;
  /**
   * panel locked or unlocked.
   * Fixed to true. Does not cause an error when false, but it is not reflected either.
   */
  isPanelLocked?: boolean;
};
