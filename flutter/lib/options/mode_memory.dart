/// Mode Memory
///
/// The default setting is "off", preserving the previous behavior.
/// When set to "on", the following shooting parameters will be retained even after power-off or sleep:
/// - Still image stitching process
/// - Exposure program / Aperture / Shutter speed / ISO sensitivity / Exposure compensation
/// - White balance mode / Color temperature setting
/// - Filters
/// - Interval shooting: number of shots / interval
/// - Multi-bracket shooting: number of shots / parameters
/// - Interval composite shooting: duration / intermediate saving
/// - Time-shift shooting settings (excluding shooting order)
/// - Burst shooting options
/// - Geotag data
enum ModeMemoryEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// ON
  on('ON'),

  /// OFF
  off('OFF');

  final String rawValue;

  const ModeMemoryEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ModeMemoryEnum? getValue(String rawValue) {
    return ModeMemoryEnum.values.cast<ModeMemoryEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
