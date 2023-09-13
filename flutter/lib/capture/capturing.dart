import '../theta_client_flutter_platform_interface.dart';

/// Capturing
abstract class Capturing {
  /// Stops capture.
  void stopCapture();
}

/// TimeShiftCapturing
class TimeShiftCapturing extends Capturing {
  /// Stops TimeShift capture.
  ///  When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopTimeShiftCapture();
  }
}

/// VideoCapturing
class VideoCapturing extends Capturing {
  /// Stops video capture.
  ///  When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopVideoCapture();
  }
}
