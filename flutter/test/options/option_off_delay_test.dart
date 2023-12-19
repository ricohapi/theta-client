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

  test('OffDelayEnum const', () async {
    List<List<dynamic>> data = [
      [OffDelayEnum.disable, 'DISABLE', 6555],
      [OffDelayEnum.offDelay_5m, 'OFF_DELAY_5M', 300],
      [OffDelayEnum.offDelay_10m, 'OFF_DELAY_10M', 600],
      [OffDelayEnum.offDelay_15m, 'OFF_DELAY_15M', 900],
      [OffDelayEnum.offDelay_30m, 'OFF_DELAY_30M', 1800],
    ];
    expect(data.length, OffDelayEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      OffDelayEnum enumValue = data[i][0];
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
      expect(enumValue.sec, data[i][2], reason: data[i][1]);
    }
  });

  test('OffDelayEnum number', () async {
    final offDelay = OffDelaySec(300);
    expect(offDelay.rawValue, 300, reason: 'rawValue');
    expect(offDelay.sec, 300, reason: 'number');

    final offDelay2 = OffDelaySec(300);
    expect(offDelay, offDelay2, reason: 'number equal');

    expect(offDelay, OffDelayEnum.offDelay_5m, reason: 'number equal');
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['OffDelay'] = 'OFF_DELAY_5M';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.offDelay]);
    expect(options.offDelay?.rawValue, 'OFF_DELAY_5M',
        reason: 'offDelay const');
  });

  test('getOptions number', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(methodCall.arguments, ['OffDelay']);
      Map<String, dynamic> optionMap = {};
      optionMap['OffDelay'] = 600;
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.offDelay]);
    expect(options.offDelay?.rawValue, 600, reason: 'number');
    expect((options.offDelay as OffDelaySec).sec, 600, reason: 'number');
  });

  test('setOptions const', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['OffDelay'], 'OFF_DELAY_15M', reason: 'const');
      return Future.value();
    });
    final options = Options();
    options.offDelay = OffDelayEnum.offDelay_15m;
    await platform.setOptions(options);
  });

  test('setOptions number', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['OffDelay'], 1800, reason: 'number');
      return Future.value();
    });
    final options = Options();
    options.offDelay = OffDelaySec(1800);
    await platform.setOptions(options);
  });
}
