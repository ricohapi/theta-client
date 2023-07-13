/// Authentication information used for client mode
class DigestAuth {
  /// User name
  String username;

  /// Password
  ///
  /// If omitted, the default password is used.
  /// Default password is "THETA" + "XX" after the beginning of the serial number.
  String? password;

  DigestAuth(this.username, [this.password]);
}
