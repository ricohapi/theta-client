/// Sets the front position for the top/bottom correction.
/// Enabled only for _topBottomCorrection Manual.
class TopBottomCorrectionRotation {
  /// Specifies the pitch.
  /// Specified range is -90.0 to +90.0, stepSize is 0.1
  double pitch;

  /// Specifies the roll.
  /// Specified range is -180.0 to +180.0, stepSize is 0.1
  double roll;

  /// Specifies the yaw.
  /// Specified range is -180.0 to +180.0, stepSize is 0.1
  double yaw;

  TopBottomCorrectionRotation(this.pitch, this.roll, this.yaw);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([pitch, roll, yaw]);
}
