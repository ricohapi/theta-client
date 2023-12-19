import '../theta_client_flutter_platform_interface.dart';

/// Capturing
abstract class Capturing {
  /// Stops capture.
  void stopCapture();
}

/// TimeShiftCapturing
class TimeShiftCapturing extends Capturing {
  /// Stops TimeShift capture.
  /// When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopTimeShiftCapture();
  }
}

/// VideoCapturing
class VideoCapturing extends Capturing {
  /// Stops video capture.
  /// When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopVideoCapture();
  }
}

/// LimitlessIntervalCapturing
class LimitlessIntervalCapturing extends Capturing {
  /// Stops limitless interval capture.
  /// When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopLimitlessIntervalCapture();
  }
}

/// ShotCountSpecifiedIntervalCapturing
class ShotCountSpecifiedIntervalCapturing extends Capturing {
  /// Stops interval shooting with the shot count specified.
  /// When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopShotCountSpecifiedIntervalCapture();
  }
}

/// CompositeIntervalCapturing
class CompositeIntervalCapturing extends Capturing {
  /// Stops interval composite shooting.
  /// When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopCompositeIntervalCapture();
  }
}

/// BurstCapturing
class BurstCapturing extends Capturing {
  /// Stops burst shooting
  /// When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopBurstCapture();
  }
}

/// MultiBracketCapturing
class MultiBracketCapturing extends Capturing {
  /// Stops multi bracket shooting.
  /// When call stopCapture() then call property callback.
  @override
  void stopCapture() {
    ThetaClientFlutterPlatform.instance.stopMultiBracketCapture();
  }
}
