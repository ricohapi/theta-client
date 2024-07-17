import type { ShootingFunctionEnum } from '../options/option-function';
import type { CameraErrorEnum } from './camera-error';
import type { CaptureStatusEnum } from './capture-status';
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
  fingerprint?: string;
  /** Battery level between 0.0 and 1.0 */
  batteryLevel?: number;
  /** Storage URI */
  storageUri?: string;
  /** Storage ID */
  storageID?: string;
  /** Continuously shoots state */
  captureStatus?: CaptureStatusEnum;
  /** Recorded time of movie (seconds) */
  recordedTime?: number;
  /** Recordable time of movie (seconds) */
  recordableTime?: number;
  /** Number of still images captured during continuous shooting, Unit: images */
  capturedPictures?: number;
  /** Elapsed time for interval composite shooting (sec) */
  compositeShootingElapsedTime?: number;
  /** URL of the last saved file */
  latestFileUrl?: string;
  /** Charging state */
  chargingState?: ChargingStateEnum;
  /** API version currently set (1: v2.0, 2: v2.1) */
  apiVersion?: number;
  /**  Plugin running state (true: running, false: stop) */
  isPluginRunning?: boolean;
  /** Plugin web server state (true: enabled, false: disabled) */
  isPluginWebServer?: boolean;
  /** Shooting function status*/
  function?: ShootingFunctionEnum;
  /** My setting changed state */
  isMySettingChanged?: boolean;
  /** Identifies the microphone used while recording video */
  currentMicrophone?: MicrophoneOptionEnum;
  /** true if record to SD card */
  isSdCard?: boolean;
  /** Error information of the camera */
  cameraError?: Array<CameraErrorEnum>;
  /** true: Battery inserted; false: Battery not inserted */
  isBatteryInsert?: boolean;
  /** Location data is obtained through an external device using WebAPI or BLE-API. */
  externalGpsInfo?: StateGpsInfo;
  /** Location data is obtained through an internal GPS module. RICOH THETA Z1 does not have a built-in GPS module. */
  internalGpsInfo?: StateGpsInfo;
  /** This represents the current temperature inside the camera as an integer value, ranging from -10°C to 100°C with a precision of 1°C. */
  boardTemp?: number;
  /** This represents the current temperature inside the battery as an integer value, ranging from -10°C to 100°C with a precision of 1°C. */
  batteryTemp?: number;
};
