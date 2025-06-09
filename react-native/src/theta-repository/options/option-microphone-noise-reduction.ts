/**
 * Built-in microphone noise reduction.
 *
 * 1) Video: Ignore changes during recording
 * 2) Live Streaming: Ignore changes during delivery
 */
export const MicrophoneNoiseReductionEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** ON */
  ON: 'ON',
  /** OFF */
  OFF: 'OFF',
} as const;

/** type definition of MicrophoneNoiseReductionEnum */
export type MicrophoneNoiseReductionEnum =
  (typeof MicrophoneNoiseReductionEnum)[keyof typeof MicrophoneNoiseReductionEnum];
