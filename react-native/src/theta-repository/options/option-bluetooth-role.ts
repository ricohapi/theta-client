/**
 * Role of the Bluetooth module.
 */
export const BluetoothRoleEnum = {
  /** Central: ON, Peripheral: OFF */
  CENTRAL: 'CENTRAL',
  /** Central: OFF, Peripheral: ON */
  PERIPHERAL: 'PERIPHERAL',
  /** Central: ON, Peripheral: ON */
  CENTRAL_PERIPHERAL: 'CENTRAL_PERIPHERAL',
} as const;

/** Type definition of BluetoothRoleEnum */
export type BluetoothRoleEnum =
  (typeof BluetoothRoleEnum)[keyof typeof BluetoothRoleEnum];
