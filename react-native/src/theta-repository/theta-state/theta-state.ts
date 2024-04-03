import type { ShootingFunctionEnum } from '../options/option-function';
import type { CameraErrorEnum } from './camera-error';
import type { StateGpsInfo } from './state-gps-info';

/** Battery charging state constants */
export const ChargingStateEnum = {
  /** Charging */
  CHARGING: 'CHARGING',
  /** Charging completed */
  COMPLETED: 'COMPLETED',
  /** Not charging */
  NOT_CHARGING: 'NOT_CHARGING',
} as const;

/** type definition of ChargingStateEnum */
export type ChargingStateEnum =
  (typeof ChargingStateEnum)[keyof typeof ChargingStateEnum];

/** Capture Status constants */
export const CaptureStatusEnum = {
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
} as const;

/** type definition of CaptureStatusEnum */
export type CaptureStatusEnum =
  (typeof CaptureStatusEnum)[keyof typeof CaptureStatusEnum];

/** Microphone option constants */
export const MicrophoneOptionEnum = {
  /** auto */
  AUTO: 'AUTO',
  /** built-in microphone */
  INTERNAL: 'INTERNAL',
  /** external microphone */
  EXTERNAL: 'EXTERNAL',
} as const;

/** type definition of MicrophoneOptionEnum */
export type MicrophoneOptionEnum =
  (typeof MicrophoneOptionEnum)[keyof typeof MicrophoneOptionEnum];

/** Mutable values representing Theta status. */
export type ThetaState = {
  /** Fingerprint (unique identifier) of the current camera state */
  fingerprint: string;
  /** Battery level between 0.0 and 1.0 */
  batteryLevel: number;
  /** Storage URI */
  storageUri: string | null;
  /** Storage ID */
  storageID: string | null;
  /** Continuously shoots state */
  captureStatus: CaptureStatusEnum;
  /** Recorded time of movie (seconds) */
  recordedTime: number;
  /** Recordable time of movie (seconds) */
  recordableTime: number;
  /** Number of still images captured during continuous shooting, Unit: images */
  capturedPictures: number | null;
  /** Elapsed time for interval composite shooting (sec) */
  compositeShootingElapsedTime: number | null;
  /** URL of the last saved file */
  latestFileUrl: string;
  /** Charging state */
  chargingState: ChargingStateEnum;
  /** API version currently set (1: v2.0, 2: v2.1) */
  apiVersion: number;
  /**  Plugin running state (true: running, false: stop) */
  isPluginRunning: boolean | null;
  /** Plugin web server state (true: enabled, false: disabled) */
  isPluginWebServer: boolean | null;
  /** Shooting function status*/
  function: ShootingFunctionEnum | null;
  /** My setting changed state */
  isMySettingChanged: boolean | null;
  /** Identifies the microphone used while recording video */
  currentMicrophone: MicrophoneOptionEnum | null;
  /** true if record to SD card */
  isSdCard: boolean;
  /** Error information of the camera */
  cameraError: Array<CameraErrorEnum> | null;
  /** true: Battery inserted; false: Battery not inserted */
  isBatteryInsert: boolean | null;
  /** Location data is obtained through an external device using WebAPI or BLE-API. */
  externalGpsInfo: StateGpsInfo | null;
  /** Location data is obtained through an internal GPS module. RICOH THETA Z1 does not have a built-in GPS module. */
  internalGpsInfo: StateGpsInfo | null;
  /** This represents the current temperature inside the camera as an integer value, ranging from -10°C to 100°C with a precision of 1°C. */
  boardTemp: number | null;
  /** This represents the current temperature inside the battery as an integer value, ranging from -10°C to 100°C with a precision of 1°C. */
  batteryTemp: number | null;
};
