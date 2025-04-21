/// Aperture value.
enum ApertureEnum {
  /// Aperture AUTO(0).
  apertureAuto('APERTURE_AUTO'),

  /// Aperture 2.0F.
  ///
  /// RICOH THETA V or prior
  aperture_2_0('APERTURE_2_0'),

  /// Aperture 2.1F.
  ///
  /// RICOH THETA Z1 and the exposure program [exposureProgram] is set to Manual or Aperture Priority
  aperture_2_1('APERTURE_2_1'),

  /// Aperture 2.4F.
  ///
  /// RICOH THETA X or later
  aperture_2_4('APERTURE_2_4'),

  /// Aperture 3.5F.
  ///
  /// RICOH THETA Z1 and the exposure program [exposureProgram] is set to Manual or Aperture Priority
  aperture_3_5('APERTURE_3_5'),

  /// Aperture 5.6F.
  ///
  /// RICOH THETA Z1 and the exposure program [exposureProgram] is set to Manual or Aperture Priority
  aperture_5_6('APERTURE_5_6');

  final String rawValue;

  const ApertureEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ApertureEnum? getValue(String rawValue) {
    return ApertureEnum.values.cast<ApertureEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
