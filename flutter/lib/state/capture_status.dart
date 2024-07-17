/// Capture Status
enum CaptureStatusEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Capture status. Performing continuously shoot
  shooting('SHOOTING'),

  /// Capture status. In standby
  idle('IDLE'),

  /// Capture status. Self-timer is operating
  selfTimerCountdown('SELF_TIMER_COUNTDOWN'),

  /// Capture status. Performing multi bracket shooting
  bracketShooting('BRACKET_SHOOTING'),

  /// Capture status. Converting post file...
  converting('CONVERTING'),

  /// Capture status. Performing timeShift shooting
  timeShiftShooting('TIME_SHIFT_SHOOTING'),

  /// Capture status. Performing continuous shooting
  continuousShooting('CONTINUOUS_SHOOTING'),

  /// Capture status. Waiting for retrospective video...
  retrospectiveImageRecording('RETROSPECTIVE_IMAGE_RECORDING'),
  
  /// Capture status. Performing burst shooting
  burstShooting('BURST_SHOOTING'),
  ;

  final String rawValue;

  const CaptureStatusEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CaptureStatusEnum? getValue(String rawValue) {
    return CaptureStatusEnum.values.cast<CaptureStatusEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
