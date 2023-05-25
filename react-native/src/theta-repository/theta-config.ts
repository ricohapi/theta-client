import type { DigestAuth } from './digest-auth';
import type { LanguageEnum, OffDelayEnum, SleepDelayEnum } from './options';

/**
 * Configuration of THETA
 */
export type ThetaConfig = {
  /** Location information acquisition time */
  dateTime?: string;
  /** Language used in camera OS */
  language?: LanguageEnum;
  /** Length of standby time before the camera automatically power OFF. */
  offDelay?: OffDelayEnum;
  /** Length of standby time before the camera enters the sleep mode. */
  sleepDelay?: SleepDelayEnum;
  /** Shutter volume. */
  shutterVolume?: number;
  /** Authentication information used for client mode connections */
  clientMode?: DigestAuth;
};
