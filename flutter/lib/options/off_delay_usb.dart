/// Auto power off time with USB power supply.
///
/// For RICOH THETA A1
class OffDelayUsbEnum {
  final dynamic rawValue;
  final int sec;

  OffDelayUsbEnum._internal(String rateStr, this.sec) : rawValue = rateStr;

  OffDelayUsbEnum._internalSec(this.sec) : rawValue = sec;

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([sec]);

  @override
  String toString() => rawValue.toString();

  /// Do not turn power off.
  static final disable = OffDelayUsbEnum._internal('DISABLE', 6555);

  /// Power off after 10 minutes.(600sec)
  static final offDelay_10m = OffDelayUsbEnum._internal('OFF_DELAY_10M', 600);

  /// Power off after 1 hour.(3600sec)
  static final offDelay_1h = OffDelayUsbEnum._internal('OFF_DELAY_1H', 3600);

  /// Power off after 2 hour.(7200sec)
  static final offDelay_2h = OffDelayUsbEnum._internal('OFF_DELAY_2H', 7200);

  /// Power off after 4 hour.(14400sec)
  static final offDelay_4h = OffDelayUsbEnum._internal('OFF_DELAY_4H', 14400);

  /// Power off after 8 hour.(28800sec)
  static final offDelay_8h = OffDelayUsbEnum._internal('OFF_DELAY_8H', 28800);

  /// Power off after 12 hour.(43200sec)
  static final offDelay_12h = OffDelayUsbEnum._internal('OFF_DELAY_12H', 43200);

  /// Power off after 18 hour.(64800sec)
  static final offDelay_18h = OffDelayUsbEnum._internal('OFF_DELAY_18H', 64800);

  /// Power off after 24 hour.(86400sec)
  static final offDelay_24h = OffDelayUsbEnum._internal('OFF_DELAY_24H', 86400);

  /// Power off after 2 days.(172800sec)
  static final offDelay_2d = OffDelayUsbEnum._internal('OFF_DELAY_2D', 172800);

  static final values = [
    disable,
    offDelay_10m,
    offDelay_1h,
    offDelay_2h,
    offDelay_4h,
    offDelay_8h,
    offDelay_12h,
    offDelay_18h,
    offDelay_24h,
    offDelay_2d,
  ];

  static OffDelayUsbEnum? getValue(dynamic rawValue) {
    if (rawValue is int) {
      return OffDelayUsbSec(rawValue);
    }
    var list = List<OffDelayUsbEnum?>.of(values);
    list.add(null);
    return list.firstWhere((element) {
      return element?.rawValue == rawValue;
    }, orElse: () => null);
  }
}

/// Auto power off time with USB power supply.
///
/// For RICOH THETA A1
/// 0, or a value that is a multiple of 60 out of 600 or more and 2592000 or less (unit: second), or 65535.
/// Return 0 when 65535 is set and obtained (Do not turn power OFF).
class OffDelayUsbSec extends OffDelayUsbEnum {
  OffDelayUsbSec(super.sec) : super._internalSec();
}
