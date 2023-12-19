/// Length of standby time before the camera enters the sleep mode.
class SleepDelayEnum {
  final dynamic rawValue;
  final int sec;

  SleepDelayEnum._internal(String rateStr, this.sec) : rawValue = rateStr;

  SleepDelayEnum._internalSec(this.sec) : rawValue = sec;

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([sec]);

  @override
  String toString() => rawValue.toString();

  /// Sleep mode after 3 minutes.(180sec)
  static final disable = SleepDelayEnum._internal('DISABLE', 6555);

  /// Sleep mode after 3 minutes.(180sec)
  static final sleepDelay_3m = SleepDelayEnum._internal('SLEEP_DELAY_3M', 180);

  /// Sleep mode after 5 minutes.(300sec)
  static final sleepDelay_5m = SleepDelayEnum._internal('SLEEP_DELAY_5M', 300);

  /// Sleep mode after 7 minutes.(420sec)
  static final sleepDelay_7m = SleepDelayEnum._internal('SLEEP_DELAY_7M', 420);

  /// Sleep mode after 10 minutes.(600sec)
  static final sleepDelay_10m =
      SleepDelayEnum._internal('SLEEP_DELAY_10M', 600);

  static final values = [
    disable,
    sleepDelay_3m,
    sleepDelay_5m,
    sleepDelay_7m,
    sleepDelay_10m,
  ];

  static SleepDelayEnum? getValue(dynamic rawValue) {
    if (rawValue is int) {
      return SleepDelaySec(rawValue);
    }
    var list = List<SleepDelayEnum?>.of(values);
    list.add(null);
    return list.firstWhere((element) {
      return element?.rawValue == rawValue;
    }, orElse: () => null);
  }
}

/// Length of standby time before the camera enters the sleep mode.
///
/// For RICOH THETA V or later
/// 60 to 65534, or 65535 (to disable the sleep mode).
/// If a value from "0" to "59" is specified, and error (invalidParameterValue) is returned.
///
/// For RICOH THETA S or SC
/// 30 to 1800, or 65535 (to disable the sleep mode)
class SleepDelaySec extends SleepDelayEnum {
  SleepDelaySec(super.sec) : super._internalSec();
}
