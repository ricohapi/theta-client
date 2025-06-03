/// Whether the camera's WLAN CL mode uses 2.4 GHz, 5.2 GHz, or 5.8 GHz frequencies.
///
/// For RICOH THETA A1
class WlanFrequencyClMode {
  /// 2.4GHz
  bool enable2_4;

  /// 5.2GHz
  bool enable5_2;

  /// 5.8GHz
  bool enable5_8;

  WlanFrequencyClMode(this.enable2_4, this.enable5_2, this.enable5_8);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([enable2_4, enable5_2, enable5_8]);
}
