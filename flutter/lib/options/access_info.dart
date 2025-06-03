/// Connected network information.
class AccessInfo {
  /// SSID of the wireless LAN access point that THETA connects to
  String ssid;

  /// IP address of access point
  String ipAddress;

  /// subnet mask of access point
  String subnetMask;

  /// default gateway of access point
  String defaultGateway;

  /// Primary DNS server
  String? dns1;

  /// Secondary DNS server
  String? dns2;

  /// proxy URL of access point
  String proxyURL;

  /// Radio frequency
  WlanFrequencyAccessInfoEnum frequency;

  /// WLAN signal strength.
  int wlanSignalStrength;

  /// WLAN signal level.
  int wlanSignalLevel;

  /// LTE signal strength.
  int lteSignalStrength;

  /// LTE signal level.
  int lteSignalLevel;

  /// client devices information
  List<DhcpLeaseAddress>? dhcpLeaseAddress;

  AccessInfo(
      [this.ssid = "",
      this.ipAddress = "",
      this.subnetMask = "",
      this.defaultGateway = "",
      this.dns1,
      this.dns2,
      this.proxyURL = "",
      this.frequency = WlanFrequencyAccessInfoEnum.initialValue,
      this.wlanSignalStrength = 0,
      this.wlanSignalLevel = 0,
      this.lteSignalStrength = 0,
      this.lteSignalLevel = 0,
      this.dhcpLeaseAddress]);

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! AccessInfo) return false;
    return ssid == other.ssid &&
        ipAddress == other.ipAddress &&
        subnetMask == other.subnetMask &&
        defaultGateway == other.defaultGateway &&
        dns1 == other.dns1 &&
        dns2 == other.dns2 &&
        proxyURL == other.proxyURL &&
        frequency == other.frequency &&
        wlanSignalStrength == other.wlanSignalStrength &&
        wlanSignalLevel == other.wlanSignalLevel &&
        lteSignalStrength == other.lteSignalStrength &&
        lteSignalLevel == other.lteSignalLevel &&
        _compareDhcpLeaseAddressLists(dhcpLeaseAddress, other.dhcpLeaseAddress);
  }

  @override
  int get hashCode => Object.hashAll([
        ssid,
        ipAddress,
        subnetMask,
        defaultGateway,
        dns1,
        dns2,
        proxyURL,
        frequency,
        wlanSignalStrength,
        wlanSignalLevel,
        lteSignalStrength,
        lteSignalLevel,
        dhcpLeaseAddress
      ]);

  bool _compareDhcpLeaseAddressLists(
      List<DhcpLeaseAddress>? list1, List<DhcpLeaseAddress>? list2) {
    if (list1 == null && list2 == null) return true;
    if (list1 == null || list2 == null) return false;
    if (list1.length != list2.length) return false;
    for (int i = 0; i < list1.length; i++) {
      if (list1[i] != list2[i]) return false;
    }
    return true;
  }
}

/// WLAN frequency of the access point.
enum WlanFrequencyAccessInfoEnum {
  /// Undefined value
  unknown('UNKNOWN'),

  /// 2.4GHz
  ghz_2_4('GHZ_2_4'),

  /// 5.2GHz
  ghz_5_2('GHZ_5_2'),

  /// 5.8GHz
  ghz_5_8('GHZ_5_8'),

  /// Initial value
  initialValue('INITIAL_VALUE');

  final String rawValue;

  const WlanFrequencyAccessInfoEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static WlanFrequencyAccessInfoEnum? getValue(String rawValue) {
    return WlanFrequencyAccessInfoEnum.values
        .cast<WlanFrequencyAccessInfoEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Client devices information
class DhcpLeaseAddress {
  /// IP address of client device
  final String ipAddress;

  /// MAC address of client device
  final String macAddress;

  /// Host name of client device
  final String hostName;

  DhcpLeaseAddress({
    required this.ipAddress,
    required this.macAddress,
    required this.hostName,
  });

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    if (other is! DhcpLeaseAddress) return false;
    return ipAddress == other.ipAddress &&
        macAddress == other.macAddress &&
        hostName == other.hostName;
  }

  @override
  int get hashCode => Object.hash(ipAddress, macAddress, hostName);
}
