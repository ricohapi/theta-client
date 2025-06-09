import '../theta_client_flutter.dart';

/// IP address allocation to be used when wired LAN is enabled.
///
/// For
/// - RICOH THETA X firmware v2.40.0 or later
class EthernetConfig {
  /// Using DHCP or not.
  bool usingDhcp;

  /// (optional) IPv4 for IP address
  /// Do not specify this property when usingDhcp is true.
  String? ipAddress;

  /// (optional) IPv4 for subnet mask
  /// Do not specify this property when usingDhcp is true.
  String? subnetMask;

  /// (optional) IPv4 for default gateway
  /// Do not specify this property when usingDhcp is true.
  String? defaultGateway;

  /// (optional) IPv4 for Primary DNS server
  /// Do not specify this property when usingDhcp is true.
  String? dns1;

  /// (optional) IPv4 for Secondary DNS server
  /// Do not specify this property when usingDhcp is true.
  String? dns2;

  /// (optional) refer to _proxy for detail
  ///
  /// If "use" is set to true, "url" and "port" must be set.
  /// "userid" and "password" must be set together.
  /// It is recommended to set proxy as this three patterns:
  /// - (use = false)
  /// - (use = true, url = {url}, port = {port})
  /// - (use = true, url = {url}, port = {port}, userid = {userid}, password = {password})
  Proxy? proxy;

  EthernetConfig(this.usingDhcp,
      [this.ipAddress,
      this.subnetMask,
      this.defaultGateway,
      this.dns1,
      this.dns2,
      this.proxy]);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll(
      [usingDhcp, ipAddress, subnetMask, defaultGateway, dns1, dns2, proxy]);
}
