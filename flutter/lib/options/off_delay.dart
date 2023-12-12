/// Length of standby time before the camera automatically powers OFF.
///
/// For RICOH THETA V or later
class OffDelayEnum {
  final dynamic rawValue;
  final int sec;

  OffDelayEnum._internal(String rateStr, this.sec) : rawValue = rateStr;

  OffDelayEnum._internalSec(this.sec) : rawValue = sec;

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([sec]);

  @override
  String toString() => rawValue.toString();

  /// Do not turn power off.
  static final disable = OffDelayEnum._internal('DISABLE', 6555);

  /// Power off after 5 minutes.(300sec)
  static final offDelay_5m = OffDelayEnum._internal('OFF_DELAY_5M', 300);

  /// Power off after 10 minutes.(600sec)
  static final offDelay_10m = OffDelayEnum._internal('OFF_DELAY_10M', 600);

  /// Power off after 15 minutes.(900sec)
  static final offDelay_15m = OffDelayEnum._internal('OFF_DELAY_15M', 900);

  /// Power off after 30 minutes.(1,800sec)
  static final offDelay_30m = OffDelayEnum._internal('OFF_DELAY_30M', 1800);

  static final values = [
    disable,
    offDelay_5m,
    offDelay_10m,
    offDelay_15m,
    offDelay_30m,
  ];

  static OffDelayEnum? getValue(dynamic rawValue) {
    if (rawValue is int) {
      return OffDelaySec(rawValue);
    }
    var list = List<OffDelayEnum?>.of(values);
    list.add(null);
    return list.firstWhere((element) {
      return element?.rawValue == rawValue;
    }, orElse: () => null);
  }
}

/// Length of standby time before the camera automatically powers OFF.
///
/// For RICOH THETA V or later
/// 0, or a value that is a multiple of 60 out of 600 or more and 2592000 or less (unit: second), or 65535.
/// Return 0 when 65535 is set and obtained (Do not turn power OFF).
///
/// For RICOH THETA S or SC
/// 30 or more and 1800 or less (unit: seconds), 65535 (Do not turn power OFF).
class OffDelaySec extends OffDelayEnum {
  OffDelaySec(super.sec) : super._internalSec();
}
