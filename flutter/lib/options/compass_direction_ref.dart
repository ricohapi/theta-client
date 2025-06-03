/// _compassDirectionRef
enum CompassDirectionRefEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// If GPS positioning is available, record in true north;
  /// if GPS is off or not available, record in magnetic north.
  auto('AUTO'),

  /// If the azimuth is set to true north, GPS is turned off, or positioning is not possible,
  /// the azimuth information is not recorded (positioning information is required for conversion).
  trueNorth('TRUE_NORTH'),

  /// Do not set azimuth to true north, set azimuth to magnetic north
  magnetic('MAGNETIC');

  final String rawValue;

  const CompassDirectionRefEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CompassDirectionRefEnum? getValue(String rawValue) {
    return CompassDirectionRefEnum.values
        .cast<CompassDirectionRefEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}
