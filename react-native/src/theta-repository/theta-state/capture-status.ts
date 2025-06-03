/** Capture Status constants */
export const CaptureStatusEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** Performing continuously shoot */
  SHOOTING: 'SHOOTING',
  /** In standby */
  IDLE: 'IDLE',
  /** Self-timer is operating */
  SELF_TIMER_COUNTDOWN: 'SELF_TIMER_COUNTDOWN',
  /** Performing multi bracket shooting */
  BRACKET_SHOOTING: 'BRACKET_SHOOTING',
  /** Converting post file... */
  CONVERTING: 'CONVERTING',
  /** Performing timeShift shooting */
  TIME_SHIFT_SHOOTING: 'TIME_SHIFT_SHOOTING',
  /** Performing continuous shooting */
  CONTINUOUS_SHOOTING: 'CONTINUOUS_SHOOTING',
  /** Waiting for retrospective video... */
  RETROSPECTIVE_IMAGE_RECORDING: 'RETROSPECTIVE_IMAGE_RECORDING',
  /** Performing burst shooting */
  BURST_SHOOTING: 'BURST_SHOOTING',
  /**
   * In the case of time-lag shooting by manual lens,
   * set while waiting for the second shot to be taken after the first shot is completed.
   */
  TIME_SHIFT_SHOOTING_IDLE: 'TIME_SHIFT_SHOOTING_IDLE',
} as const;

/** type definition of CaptureStatusEnum */
export type CaptureStatusEnum =
  (typeof CaptureStatusEnum)[keyof typeof CaptureStatusEnum];
