/**
 * Shooting mode.
 * 
 * The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
 * Swithcing modes may take time. Wait a while to send the command that takes place after switching the mode.
 *
 * Live streaming mode is supported by only RICOH THETA S.
 */ 
export const CaptureModeEnum = {
    /** Still image capture mode */
    IMAGE: 'IMAGE',
    /** Video capture mode */
    VIDEO: 'VIDEO',
    /** Live preview mode. This mode can not be set. */
    LIVE_STREAMING: 'LIVE_STREAMING',
    /** Interval still image capture mode just for Theta SC2 and Theta SC2 for business */
    INTERVAL: 'INTERVAL',
    /** Preset mode just for Theta SC2 and Theta SC2 for business */
    PRESET: 'PRESET',
  } as const;
  
  /** type definition of CaptureModeEnum */
  export type CaptureModeEnum =
    typeof CaptureModeEnum[keyof typeof CaptureModeEnum];