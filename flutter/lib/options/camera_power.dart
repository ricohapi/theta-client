/// _cameraPower is the power status of camera.
///
/// For RICOH THETA X v2.61.0 or later
enum CameraPowerEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Power ON
  on('ON'),

  /// Power OFF
  off('OFF'),

  /// Power on, power saving mode. Camera is closed.
  /// Unavailable parameter when plugin is running. In this case, invalidParameterValue error will be returned.
  powerSaving('POWER_SAVING'),

  /// Power on, silent mode. LCD/LED is turned off.
  /// Unavailable parameter when plugin is running. In this case, invalidParameterValue error will be returned.
  silentMode('SILENT_MODE');

  final String rawValue;

  const CameraPowerEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CameraPowerEnum? getValue(String rawValue) {
    return CameraPowerEnum.values.cast<CameraPowerEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
