/// Network type of the camera supported by Theta X, Z1 and V.
enum NetworkTypeEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Direct mode
  direct('DIRECT'),

  /// Client mode via WLAN
  client('CLIENT'),

  /// Client mode via Ethernet cable supported by Theta Z1 and V.
  ethernet('ETHERNET'),

  /// Network is off. This value can be gotten only by plugin.
  off('OFF'),

  /// LTE plan-D
  lteD('LTE_D'),

  /// LTE plan-DU
  lteDU('LTE_DU'),

  /// LTE plan01s
  lte01S('LTE_01S'),

  /// LTE planX3
  lteX3('LTE_X3'),

  /// LTE planP1
  lteP1('LTE_P1'),

  /// LTE plan-K2
  lteK2('LTE_K2'),

  /// LTE plan-K
  lteK('LTE_K');

  final String rawValue;

  const NetworkTypeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static NetworkTypeEnum? getValue(String rawValue) {
    return NetworkTypeEnum.values.cast<NetworkTypeEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
