/// Turns position information assigning ON/OFF.
///
/// For RICOH THETA X
enum GpsTagRecordingEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Position information assigning ON.
  on('ON'),

  /// Position information assigning OFF.
  off('OFF');

  final String rawValue;

  const GpsTagRecordingEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }
}
