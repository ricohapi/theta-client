/// Maximum recordable time (in seconds) of the camera.
enum MaxRecordableTimeEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Maximum recordable time. 180sec for SC2 only.
  time_180('RECORDABLE_TIME_180'),

  /// Maximum recordable time. 300sec for other than SC2.
  time_300('RECORDABLE_TIME_300'),

  /// Maximum recordable time. 1500sec for other than SC2.
  time_1500('RECORDABLE_TIME_1500'),

  /// Maximum recordable time. 3000sec for THETA Z1 Version 3.01.1 or later
  /// only for 3.6K 1/2fps and 2.7K 1/2fps.
  /// If you set 3000 seconds in 3.6K 2fps mode and then set back to 4K 30fps mode,
  /// the max recordable time will be overwritten to 300 seconds automatically.
  time_3000('RECORDABLE_TIME_3000'),

  /// Maximum recordable time. 7200sec for Theta X version 2.00.0 or later,
  /// only for 5.7K 2/5/10fps and 8K 2/5/10fps.
  /// If you set 7200 seconds in 8K 10fps mode and then set back to 4K 30fps mode,
  /// the max recordable time will be overwritten to 1500 seconds automatically.
  time_7200('RECORDABLE_TIME_7200'),

  /// Just used by getMySetting/setMySetting command
  doNotUpdateMySettingCondition('DO_NOT_UPDATE_MY_SETTING_CONDITION');

  final String rawValue;

  const MaxRecordableTimeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static MaxRecordableTimeEnum? getValue(String rawValue) {
    return MaxRecordableTimeEnum.values
        .cast<MaxRecordableTimeEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}
