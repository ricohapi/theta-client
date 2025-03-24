/// supported color temperature.
class ColorTemperatureSupport {
  /// maximum value
  int maxTemperature;

  /// minimum value
  int minTemperature;

  /// step size
  int stepSize;

  ColorTemperatureSupport(
      this.maxTemperature, this.minTemperature, this.stepSize);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode =>
      Object.hashAll([maxTemperature, minTemperature, stepSize]);
}
