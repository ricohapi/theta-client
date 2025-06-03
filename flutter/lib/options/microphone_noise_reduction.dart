/// Built-in microphone noise reduction.
///
/// 1) Video: Ignore changes during recording
/// 2) Live Streaming: Ignore changes during delivery
enum MicrophoneNoiseReductionEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// ON
  on('ON'),

  /// OFF
  off('OFF');

  final String rawValue;

  const MicrophoneNoiseReductionEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static MicrophoneNoiseReductionEnum? getValue(String rawValue) {
    return MicrophoneNoiseReductionEnum.values
        .cast<MicrophoneNoiseReductionEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}
