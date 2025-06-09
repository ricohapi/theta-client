/**
 * Mobile Network Settings
 */
export type MobileNetworkSetting = {
  /**
   * @see RoamingEnum
   */
  roaming?: RoamingEnum;

  /**
   * @see PlanEnum
   */
  plan?: PlanEnum;
};

/**
 * Roaming of MobileNetworkSetting
 */
export const RoamingEnum = {
  UNKNOWN: 'UNKNOWN',
  OFF: 'OFF',
  ON: 'ON',
} as const;

/** Type definition of RoamingEnum */
export type RoamingEnum = (typeof RoamingEnum)[keyof typeof RoamingEnum];

/**
 * Plan of MobileNetworkSetting
 */
export const PlanEnum = {
  UNKNOWN: 'UNKNOWN',
  SORACOM: 'SORACOM',
  SORACOM_PLAN_DU: 'SORACOM_PLAN_DU',
} as const;

/** Type definition of PlanEnum */
export type PlanEnum = (typeof PlanEnum)[keyof typeof PlanEnum];
