/**
 * USB connection of the camera.
 *
 * Default value is "MTP".
 * After switching the setting value, reconnect the camera to USB to enable it.
 */
export const UsbConnectionEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** MTP */
  MTP: 'MTP',
  /** MSC */
  MSC: 'MSC',
} as const;

/** type definition of UsbConnectionEnum */
export type UsbConnectionEnum =
  (typeof UsbConnectionEnum)[keyof typeof UsbConnectionEnum];
