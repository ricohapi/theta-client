/// Control camera lock/unlock
enum CameraLockEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Camera is unlocked
  unlock('UNLOCK'),

  /// Camera basic lock state
  /// (Mode button, WLAN button, and Fn button presses are inhibited)
  basicLock('BASIC_LOCK'),

  /// Lock according to the parameters set in _cameraLockConfig.
  customLock('CUSTOM_LOCK'),
  ;

  final String rawValue;

  const CameraLockEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CameraLockEnum? getValue(String rawValue) {
    return CameraLockEnum.values.cast<CameraLockEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
