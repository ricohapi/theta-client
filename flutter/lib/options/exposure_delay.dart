/// Operating time (sec.) of the self-timer.
enum ExposureDelayEnum {
  /// Disable self-timer.
  delayOff('DELAY_OFF'),

  /// Self-timer time. 1sec.
  delay1('DELAY_1'),

  /// Self-timer time. 2sec.
  delay2('DELAY_2'),

  /// Self-timer time. 3sec.
  delay3('DELAY_3'),

  /// Self-timer time. 4sec.
  delay4('DELAY_4'),

  /// Self-timer time. 5sec.
  delay5('DELAY_5'),

  /// Self-timer time. 6sec.
  delay6('DELAY_6'),

  /// Self-timer time. 7sec.
  delay7('DELAY_7'),

  /// Self-timer time. 8sec.
  delay8('DELAY_8'),

  /// Self-timer time. 9sec.
  delay9('DELAY_9'),

  /// Self-timer time. 10sec.
  delay10('DELAY_10');

  final String rawValue;

  const ExposureDelayEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ExposureDelayEnum? getValue(String rawValue) {
    return ExposureDelayEnum.values.cast<ExposureDelayEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
