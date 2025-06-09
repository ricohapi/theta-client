import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/options/access_info.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_method_channel.dart';

void main() {
  MethodChannelThetaClientFlutter platform = MethodChannelThetaClientFlutter();
  const MethodChannel channel = MethodChannel('theta_client_flutter');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  test('WlanFrequencyAccessInfoEnum const', () async {
    List<List<dynamic>> data = [
      [WlanFrequencyAccessInfoEnum.unknown, 'UNKNOWN'],
      [WlanFrequencyAccessInfoEnum.ghz_2_4, 'GHZ_2_4'],
      [WlanFrequencyAccessInfoEnum.ghz_5_2, 'GHZ_5_2'],
      [WlanFrequencyAccessInfoEnum.ghz_5_8, 'GHZ_5_8'],
      [WlanFrequencyAccessInfoEnum.initialValue, 'INITIAL_VALUE'],
    ];
    expect(data.length, WlanFrequencyAccessInfoEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['AccessInfo'] = {
        "ssid": "ssid_test",
        "ipAddress": "192.168.1.2",
        "subnetMask": "255.255.0.0",
        "defaultGateway": "192.168.1.12",
        "dns1": "192.168.1.55",
        "dns2": "192.168.1.66",
        "proxyURL": "http://192.168.1.3",
        "frequency": "GHZ_2_4",
        "wlanSignalStrength": -60,
        "wlanSignalLevel": 4,
        "lteSignalStrength": 0,
        "lteSignalLevel": 0,
        "dhcpLeaseAddress": [
          {
            "ipAddress": "192.168.1.5",
            "macAddress": "58:38:79:12:34:56",
            "hostName": "Macbook-Pro"
          }
        ]
      };
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.accessInfo]);
    expect(
        options.accessInfo,
        AccessInfo(
            "ssid_test",
            "192.168.1.2",
            "255.255.0.0",
            "192.168.1.12",
            "192.168.1.55",
            "192.168.1.66",
            "http://192.168.1.3",
            WlanFrequencyAccessInfoEnum.ghz_2_4,
            -60,
            4,
            0,
            0, [
          DhcpLeaseAddress(
              ipAddress: "192.168.1.5",
              macAddress: "58:38:79:12:34:56",
              hostName: "Macbook-Pro")
        ]),
        reason: 'quality');
  });
}
