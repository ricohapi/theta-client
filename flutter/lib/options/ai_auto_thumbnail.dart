/// AI auto thumbnail setting.
///
/// For RICOH THETA X
enum AiAutoThumbnailEnum {
  /// Undefined value
  unknown('UNKNOWN'),
  
  /// AI auto setting ON.
  on('ON'),

  /// AI auto setting OFF.
  off('OFF');

  final String rawValue;

  const AiAutoThumbnailEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static AiAutoThumbnailEnum? getValue(String rawValue) {
    return AiAutoThumbnailEnum.values.cast<AiAutoThumbnailEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}