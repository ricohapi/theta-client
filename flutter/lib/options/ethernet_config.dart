import '../theta_client_flutter.dart';

/// IP address allocation to be used when wired LAN is enabled.
///
/// For
/// - RICOH THETA X firmware v2.40.0 or later
class EthernetConfig {
  /// Using DHCP or not.
  bool usingDhcp;

  /// (optional) IPv4 for IP address
  String? ipAddress;

  /// (optional) IPv4 for subnet mask
  String? subnetMask;

  /// (optional) IPv4 for default gateway
  String? defaultGateway;

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
      [this.ipAddress, this.subnetMask, this.defaultGateway, this.proxy]);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode =>
      Object.hashAll([usingDhcp, ipAddress, subnetMask, defaultGateway, proxy]);
}
