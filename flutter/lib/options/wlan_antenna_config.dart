/// Configure SISO or MIMO for Wireless LAN.
enum WlanAntennaConfigEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// SISO
  siso('SISO'),

  /// MIMO
  mimo('MIMO');

  final String rawValue;

  const WlanAntennaConfigEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static WlanAntennaConfigEnum? getValue(String rawValue) {
    return WlanAntennaConfigEnum.values
        .cast<WlanAntennaConfigEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}
