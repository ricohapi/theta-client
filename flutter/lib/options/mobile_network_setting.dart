/// Mobile Network Settings
class MobileNetworkSetting {
  /// roaming
  RoamingEnum? roaming;

  /// plan
  PlanEnum? plan;

  MobileNetworkSetting(this.roaming, this.plan);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([roaming, plan]);
}

/// Roaming of MobileNetworkSetting
enum RoamingEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// OFF
  off('OFF'),

  /// ON
  on('ON');

  final String rawValue;

  const RoamingEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static RoamingEnum? getValue(String rawValue) {
    return RoamingEnum.values.cast<RoamingEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Plan of MobileNetworkSetting
enum PlanEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Communicate with APN settings for plan-D
  // ignore: constant_identifier_names
  SORACOM("SORACOM"),

  /// Communicate with APN settings for plan-DU
  // ignore: constant_identifier_names
  SORACOM_PLAN_DU('SORACOM_PLAN_DU');

  final String rawValue;

  const PlanEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static PlanEnum? getValue(String rawValue) {
    return PlanEnum.values.cast<PlanEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
