/**
 * Auto power off time with USB power supply.
 *
 * For RICOH THETA A1
 */
export const OffDelayUsbEnum = {
  /** Do not turn power off. */
  DISABLE: 'DISABLE',
  /** Power off after 10 minutes.(600sec) */
  OFF_DELAY_10M: 'OFF_DELAY_10M',
  /** Power off after 1 hour.(3,600sec) */
  OFF_DELAY_1H: 'OFF_DELAY_1H',
  /** Power off after 2 hour.(7,200sec) */
  OFF_DELAY_2H: 'OFF_DELAY_2H',
  /** Power off after 4 hour.(14,400sec) */
  OFF_DELAY_4H: 'OFF_DELAY_4H',
  /** Power off after 8 hour.(28,800sec) */
  OFF_DELAY_8H: 'OFF_DELAY_8H',
  /** Power off after 12 hour.(43,200sec) */
  OFF_DELAY_12H: 'OFF_DELAY_12H',
  /** Power off after 18 hour.(64,800sec) */
  OFF_DELAY_18H: 'OFF_DELAY_18H',
  /** Power off after 24 hour.(86,400sec) */
  OFF_DELAY_24H: 'OFF_DELAY_24H',
  /** Power off after 2 days.(172,800sec) */
  OFF_DELAY_2D: 'OFF_DELAY_2D',
} as const;

/** type definition of OffDelayEnum */
export type OffDelayUsbEnum =
  | (typeof OffDelayUsbEnum)[keyof typeof OffDelayUsbEnum]
  | number;

/**
 * Get time(seconds) from OffDelayEnum
 * @param offDelayUsb enum of OffDelayUsb
 * @returns time(seconds)
 */
export function offDelayUsbToSeconds(offDelayUsb: OffDelayUsbEnum): number {
  if (typeof offDelayUsb === 'number') {
    return offDelayUsb;
  }

  const table: Record<OffDelayUsbEnum, number> = {
    DISABLE: 0,
    OFF_DELAY_10M: 600,
    OFF_DELAY_1H: 3600,
    OFF_DELAY_2H: 7200,
    OFF_DELAY_4H: 14400,
    OFF_DELAY_8H: 28800,
    OFF_DELAY_12H: 43200,
    OFF_DELAY_18H: 64800,
    OFF_DELAY_24H: 86400,
    OFF_DELAY_2D: 172800,
  };
  return table[offDelayUsb];
}
