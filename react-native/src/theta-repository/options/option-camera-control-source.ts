/**
 * camera control source
 * Sets whether to lock/unlock the camera UI.
 * The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
 *
 * For RICOH THETA X
 */
export const CameraControlSourceEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',

  /**
   * Operation is possible with the camera. Locks the smartphone
   * application UI (supported app only).
   */
  CAMERA: 'CAMERA',

  /**
   * Operation is possible with the smartphone application. Locks
   * the UI on the shooting screen on the camera.
   */
  APP: 'APP',
} as const;

/** type definition of CameraControlSourceEnum */
export type CameraControlSourceEnum =
  (typeof CameraControlSourceEnum)[keyof typeof CameraControlSourceEnum];
