/// USB connection of the camera.
///
/// Default value is "MTP".
/// After switching the setting value, reconnect the camera to USB to enable it.
enum UsbConnectionEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// MTP
  mtp('MTP'),

  /// MSC
  msc('MSC');

  final String rawValue;

  const UsbConnectionEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static UsbConnectionEnum? getValue(String rawValue) {
    return UsbConnectionEnum.values.cast<UsbConnectionEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
