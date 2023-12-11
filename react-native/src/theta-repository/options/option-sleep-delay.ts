/** Length of standby time before the camera enters the sleep mode. */
export const SleepDelayEnum = {
  /** Do not turn sleep mode. */
  DISABLE: 'DISABLE',
  /** sleep mode after 3 minutes.(180seconds) */
  SLEEP_DELAY_3M: 'SLEEP_DELAY_3M',
  /** sleep mode after 5 minutes.(300seconds) */
  SLEEP_DELAY_5M: 'SLEEP_DELAY_5M',
  /** sleep mode after 7 minutes.(420seconds) */
  SLEEP_DELAY_7M: 'SLEEP_DELAY_7M',
  /** sleep mode after 10 minutes.(600seconds) */
  SLEEP_DELAY_10M: 'SLEEP_DELAY_10M',
} as const;

/** type definition of SleepDelayEnum */
export type SleepDelayEnum =
  | (typeof SleepDelayEnum)[keyof typeof SleepDelayEnum]
  | number;

/**
 * Get time(seconds) from SleepDelayEnum
 * @param offDelay enum of SleepDelay
 * @returns time(seconds)
 */
export function sleepDelayToSeconds(sleepDelay: SleepDelayEnum): number {
  if (typeof sleepDelay === 'number') {
    return sleepDelay;
  }
  const table: Record<SleepDelayEnum, number> = {
    DISABLE: 0,
    SLEEP_DELAY_3M: 180,
    SLEEP_DELAY_5M: 300,
    SLEEP_DELAY_7M: 420,
    SLEEP_DELAY_10M: 600,
  };
  return table[sleepDelay];
}
