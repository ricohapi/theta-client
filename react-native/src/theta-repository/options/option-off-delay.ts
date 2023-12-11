/** Length of standby time before the camera automatically powers OFF. */
export const OffDelayEnum = {
  /** Do not turn power off. */
  DISABLE: 'DISABLE',
  /** Power off after 5 minutes.(300sec) */
  OFF_DELAY_5M: 'OFF_DELAY_5M',
  /** Power off after 10 minutes.(600sec) */
  OFF_DELAY_10M: 'OFF_DELAY_10M',
  /** Power off after 15 minutes.(900sec) */
  OFF_DELAY_15M: 'OFF_DELAY_15M',
  /** Power off after 30 minutes.(1,800sec) */
  OFF_DELAY_30M: 'OFF_DELAY_30M',
} as const;

/** type definition of OffDelayEnum */
export type OffDelayEnum =
  | (typeof OffDelayEnum)[keyof typeof OffDelayEnum]
  | number;

/**
 * Get time(seconds) from OffDelayEnum
 * @param offDelay enum of OffDelay
 * @returns time(seconds)
 */
export function offDelayToSeconds(offDelay: OffDelayEnum): number {
  if (typeof offDelay === 'number') {
    return offDelay;
  }

  const table: Record<OffDelayEnum, number> = {
    DISABLE: 0,
    OFF_DELAY_5M: 300,
    OFF_DELAY_10M: 600,
    OFF_DELAY_15M: 900,
    OFF_DELAY_30M: 1800,
  };
  return table[offDelay];
}
