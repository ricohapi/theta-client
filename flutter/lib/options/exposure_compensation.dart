/// Exposure compensation (EV).
enum ExposureCompensationEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Exposure compensation -4.0
  m4_0('M4_0'),

  /// Exposure compensation -3.7
  m3_7('M3_7'),

  /// Exposure compensation -3.3
  m3_3('M3_3'),

  /// Exposure compensation -3.0
  m3_0('M3_0'),

  /// Exposure compensation -2.7
  m2_7('M2_7'),

  /// Exposure compensation -2.3
  m2_3('M2_3'),

  /// Exposure compensation -2.0
  m2_0('M2_0'),

  /// Exposure compensation -1.7
  m1_7('M1_7'),

  /// Exposure compensation -1.3
  m1_3('M1_3'),

  /// Exposure compensation -1.0
  m1_0('M1_0'),

  /// Exposure compensation -0.7
  m0_7('M0_7'),

  /// Exposure compensation -0.3
  m0_3('M0_3'),

  /// Exposure compensation 0.0
  zero('ZERO'),

  /// Exposure compensation 0.3
  p0_3('P0_3'),

  /// Exposure compensation 0.7
  p0_7('P0_7'),

  /// Exposure compensation 1.0
  p1_0('P1_0'),

  /// Exposure compensation 1.3
  p1_3('P1_3'),

  /// Exposure compensation 1.7
  p1_7('P1_7'),

  /// Exposure compensation 2.0
  p2_0('P2_0'),

  /// Exposure compensation 2.3
  p2_3('P2_3'),

  /// Exposure compensation 2.7
  p2_7('P2_7'),

  /// Exposure compensation 3.0
  p3_0('P3_0'),

  /// Exposure compensation 3.3
  p3_3('P3_3'),

  /// Exposure compensation 3.7
  p3_7('P3_7'),

  /// Exposure compensation 4.0
  p4_0('P4_0');

  final String rawValue;

  const ExposureCompensationEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ExposureCompensationEnum? getValue(String rawValue) {
    return ExposureCompensationEnum.values
        .cast<ExposureCompensationEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}
