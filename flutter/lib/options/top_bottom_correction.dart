/// top bottom correction
///
/// Sets the top/bottom correction.  For RICOH THETA V and RICOH
/// THETA Z1, the top/bottom correction can be set only for still
/// images.  For RICOH THETA X, the top/bottom correction can be
/// set for both still images and videos.
enum TopBottomCorrectionOptionEnum {
  /// Top/bottom correction is performed.
  apply('APPLY'),

  /// Refer to top/bottom correction when shooting with "ApplyAuto"
  applyAuto('APPLY_AUTO'),

  /// Top/bottom correction is performed. The parameters used for
  /// top/bottom correction for the first image are saved and used
  /// for the 2nd and subsequent images.(RICOH THETA X or later)
  applySemiauto('APPLY_SEMIAUTO'),

  /// Performs top/bottom correction and then saves the parameters.
  applySave('APPLY_SAVE'),

  /// Performs top/bottom correction using the saved parameters.
  applyLoad('APPLY_LOAD'),

  /// Does not perform top/bottom correction.
  disapply('DISAPPLY'),

  /// Performs the top/bottom correction with the specified front
  /// position. The front position can be specified with
  /// _topBottomCorrectionRotation.
  manual('MANUAL');

  final String rawValue;

  const TopBottomCorrectionOptionEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static TopBottomCorrectionOptionEnum? getValue(String rawValue) {
    return TopBottomCorrectionOptionEnum.values
        .cast<TopBottomCorrectionOptionEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}
