/// Camera control source.
enum CameraControlSourceEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// Operation is possible with the camera. Locks the smartphone
  /// application UI (supported app only).
  camera('CAMERA'),

  /// Operation is possible with the smartphone application. Locks
  /// the UI on the shooting screen on the camera.
  app('APP');

  final String rawValue;

  const CameraControlSourceEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CameraControlSourceEnum? getValue(String rawValue) {
    return CameraControlSourceEnum.values
        .cast<CameraControlSourceEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}
