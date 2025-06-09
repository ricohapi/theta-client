/// Camera Lock Config
///
/// - It is possible to enable/disable the function for each HW key.
/// - It is possible to enable/disable the operation of the panel.
/// - When all supported buttons/components are in the "unlock" state, it will be the same as the normal setting.
/// - For THETA models, if there are no wlanKey, fnKey, or panel, it will return "lock". If there are no supported buttons/components, setting to "unlock" or "lock" will not return an error and will not perform any action.
class CameraLockConfig {
  /// power key locked or unlocked.
  bool? isPowerKeyLocked;

  /// Shutter key locked or unlocked.
  bool? isShutterKeyLocked;

  /// mode key locked or unlocked.
  bool? isModeKeyLocked;

  /// wlan key locked or unlocked.
  bool? isWlanKeyLocked;

  /// fn key locked or unlocked.
  bool? isFnKeyLocked;

  /// panel locked or unlocked.
  /// Fixed to true. Does not cause an error when false, but it is not reflected either.
  bool? isPanelLocked;

  CameraLockConfig(
      [this.isPowerKeyLocked,
      this.isShutterKeyLocked,
      this.isModeKeyLocked,
      this.isWlanKeyLocked,
      this.isFnKeyLocked,
      this.isPanelLocked]);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([
        isPowerKeyLocked,
        isShutterKeyLocked,
        isModeKeyLocked,
        isWlanKeyLocked,
        isFnKeyLocked,
        isPanelLocked
      ]);
}
