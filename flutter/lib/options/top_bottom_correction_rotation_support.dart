/// Supported TopBottomCorrectionRotation
class TopBottomCorrectionRotationSupport {
  /// Supported pitch
  final TopBottomCorrectionRotationValueSupport pitch;

  /// Supported roll
  final TopBottomCorrectionRotationValueSupport roll;

  /// Supported yaw
  final TopBottomCorrectionRotationValueSupport yaw;

  TopBottomCorrectionRotationSupport({
    required this.pitch,
    required this.roll,
    required this.yaw,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is TopBottomCorrectionRotationSupport &&
          runtimeType == other.runtimeType &&
          pitch == other.pitch &&
          roll == other.roll &&
          yaw == other.yaw;

  @override
  int get hashCode => Object.hash(pitch, roll, yaw);
}

/// Supported value of TopBottomCorrectionRotation
class TopBottomCorrectionRotationValueSupport {
  /// maximum value
  final double max;

  /// minimum value
  final double min;

  /// step size
  final double stepSize;

  TopBottomCorrectionRotationValueSupport({
    required this.max,
    required this.min,
    required this.stepSize,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is TopBottomCorrectionRotationValueSupport &&
          runtimeType == other.runtimeType &&
          max == other.max &&
          min == other.min &&
          stepSize == other.stepSize;

  @override
  int get hashCode => Object.hash(max, min, stepSize);
}
