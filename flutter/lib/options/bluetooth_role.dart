/// Role of the Bluetooth module.
enum BluetoothRoleEnum {
  /// Central: ON, Peripheral: OFF
  central('CENTRAL'),

  /// Central: OFF, Peripheral: ON
  peripheral('PERIPHERAL'),

  /// Central: ON, Peripheral: ON
  centralPeripheral('CENTRAL_PERIPHERAL');

  final String rawValue;

  const BluetoothRoleEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BluetoothRoleEnum? getValue(String rawValue) {
    return BluetoothRoleEnum.values.cast<BluetoothRoleEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
