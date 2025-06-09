import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
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

  test('RoamingEnum const', () async {
    List<List<dynamic>> data = [
      [RoamingEnum.unknown, 'UNKNOWN'],
      [RoamingEnum.off, 'OFF'],
      [RoamingEnum.on, 'ON']
    ];
    expect(data.length, RoamingEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('PlanEnum const', () async {
    List<List<dynamic>> data = [
      [PlanEnum.unknown, 'UNKNOWN'],
      [PlanEnum.SORACOM, 'SORACOM'],
      [PlanEnum.SORACOM_PLAN_DU, 'SORACOM_PLAN_DU']
    ];
    expect(data.length, PlanEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['MobileNetworkSetting'] = {"roaming": "OFF", "plan": "SORACOM"};
      return Future.value(optionMap);
    });
    Options options =
        await platform.getOptions([OptionNameEnum.mobileNetworkSetting]);
    expect(options.mobileNetworkSetting,
        MobileNetworkSetting(RoamingEnum.off, PlanEnum.SORACOM),
        reason: 'quality');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['MobileNetworkSetting'],
          {"roaming": "ON", "plan": "SORACOM_PLAN_DU"},
          reason: 'quality');
      return Future.value();
    });
    final options = Options();
    options.mobileNetworkSetting =
        MobileNetworkSetting(RoamingEnum.on, PlanEnum.SORACOM_PLAN_DU);
    await platform.setOptions(options);
  });
}
