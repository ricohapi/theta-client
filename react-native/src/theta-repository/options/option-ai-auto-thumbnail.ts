/** AI auto thumbnail setting. */
export const AiAutoThumbnailEnum = {
  /** ON */
  ON: 'ON',
  /** OFF */
  OFF: 'OFF',
} as const;

/** type definition of AiAutoThumbnailEnum */
export type AiAutoThumbnailEnum =
  (typeof AiAutoThumbnailEnum)[keyof typeof AiAutoThumbnailEnum];
