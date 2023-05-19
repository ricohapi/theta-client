/**
 * Camera mode.
 * The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
 *
 * For RICOH THETA X
 */
export const CameraModeEnum = {
  /** shooting screen */
  CAPTURE: 'CAPTURE',
  /** playback screen */
  PLAYBACK: 'PLAYBACK',
  /** shooting setting screen */
  SETTING: 'SETTING',
  /** plugin selection screen */
  PLUGIN: 'PLUGIN',
} as const;

/** type definition of CameraModeEnum */
export type CameraModeEnum =
  (typeof CameraModeEnum)[keyof typeof CameraModeEnum];
