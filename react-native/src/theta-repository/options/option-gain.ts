/**
 * Microphone gain.
 *
 * For
 * - RICOH THETA X
 * - RICOH THETA Z1
 * - RICOH THETA V
 */
export const GainEnum = {
  /** Normal mode */
  NORMAL: 'NORMAL',
  /** Loud volume mode */
  MEGA_VOLUME: 'MEGA_VOLUME',
  /** Mute mode (RICOH THETA V firmware v2.50.1 or later, RICOH THETA X is not supported.) */
  MUTE: 'MUTE',
} as const;

/** type definition of GainEnum */
export type GainEnum = (typeof GainEnum)[keyof typeof GainEnum];
