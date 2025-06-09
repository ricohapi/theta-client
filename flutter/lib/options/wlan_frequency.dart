/// Wireless LAN frequency of the camera supported by Theta X, Z1 and V.
enum WlanFrequencyEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// 2.4GHz
  ghz_2_4('GHZ_2_4'),

  /// 5GHz
  ghz_5('GHZ_5'),

  /// 5.2GHz
  ghz_5_2('GHZ_5_2'),

  /// 5.8GHz
  ghz_5_8('GHZ_5_8');

  final String rawValue;

  const WlanFrequencyEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static WlanFrequencyEnum? getValue(String rawValue) {
    return WlanFrequencyEnum.values.cast<WlanFrequencyEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
