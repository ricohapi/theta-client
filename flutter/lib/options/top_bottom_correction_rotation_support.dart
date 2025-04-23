import 'package:theta_client_flutter/theta_client_flutter.dart';

/// Supported TopBottomCorrectionRotation
class TopBottomCorrectionRotationSupport {
  /// Supported pitch
  final ValueRange<double> pitch;

  /// Supported roll
  final ValueRange<double> roll;

  /// Supported yaw
  final ValueRange<double> yaw;

  TopBottomCorrectionRotationSupport({
    required this.pitch,
    required this.roll,
    required this.yaw,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is TopBottomCorrectionRotationSupport &&
          runtimeType == other.runtimeType &&
          pitch == other.pitch &&
          roll == other.roll &&
          yaw == other.yaw;

  @override
  int get hashCode => Object.hash(pitch, roll, yaw);
}
