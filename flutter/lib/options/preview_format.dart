/// Format of live view
enum PreviewFormatEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// For Theta X firmware v2.71.1 or later
  // ignore: constant_identifier_names
  w1920_h960_f30('W1920_H960_F30'),

  /// For Theta Z1 and V
  // ignore: constant_identifier_names
  w1920_h960_f8('W1920_H960_F8'),

  /// width_height_framerate
  /// For Theta X, Z1, V and SC2
  // ignore: constant_identifier_names
  w1024_h512_f30('W1024_H512_F30'),

  /// For Theta X. This value can't set.
  // ignore: constant_identifier_names
  w1024_h512_f15('W1024_H512_F15'),

  /// For Theta A1
  // ignore: constant_identifier_names
  w1024_h512_f10('W1024_H512_F10'),

  /// For Theta Z1 and V
  // ignore: constant_identifier_names
  w1024_h512_f8('W1024_H512_F8'),

  /// For Theta Z1 and V
  // ignore: constant_identifier_names
  w640_h320_f30('W640_H320_F30'),

  /// For Theta S and SC
  // ignore: constant_identifier_names
  w640_h320_f10('W640_H320_F10'),

  /// For Theta Z1 and V
  // ignore: constant_identifier_names
  w640_h320_f8('W640_H320_F8'),

  /// For Theta X
  // ignore: constant_identifier_names
  w512_h512_f30('W512_H512_F30'),

  /// For Theta X
  // ignore: constant_identifier_names
  w3840_h1920_f30('W3840_H1920_F30');

  final String rawValue;

  const PreviewFormatEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static PreviewFormatEnum? getValue(String rawValue) {
    return PreviewFormatEnum.values.cast<PreviewFormatEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
