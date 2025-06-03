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

  test('OffDelayUsbEnum const', () async {
    List<List<dynamic>> data = [
      [OffDelayUsbEnum.disable, 'DISABLE', 6555],
      [OffDelayUsbEnum.offDelay_10m, 'OFF_DELAY_10M', 60 * 10],
      [OffDelayUsbEnum.offDelay_1h, 'OFF_DELAY_1H', 60 * 60],
      [OffDelayUsbEnum.offDelay_2h, 'OFF_DELAY_2H', 60 * 60 * 2],
      [OffDelayUsbEnum.offDelay_4h, 'OFF_DELAY_4H', 60 * 60 * 4],
      [OffDelayUsbEnum.offDelay_8h, 'OFF_DELAY_8H', 60 * 60 * 8],
      [OffDelayUsbEnum.offDelay_12h, 'OFF_DELAY_12H', 60 * 60 * 12],
      [OffDelayUsbEnum.offDelay_18h, 'OFF_DELAY_18H', 60 * 60 * 18],
      [OffDelayUsbEnum.offDelay_24h, 'OFF_DELAY_24H', 60 * 60 * 24],
      [OffDelayUsbEnum.offDelay_2d, 'OFF_DELAY_2D', 60 * 60 * 24 * 2],
    ];
    expect(data.length, OffDelayUsbEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      OffDelayUsbEnum enumValue = data[i][0];
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
      expect(enumValue.sec, data[i][2], reason: data[i][1]);
    }
  });

  test('OffDelayUsbEnum number', () async {
    final offDelayUsb = OffDelayUsbSec(600);
    expect(offDelayUsb.rawValue, 600, reason: 'rawValue');
    expect(offDelayUsb.sec, 600, reason: 'number');

    final offDelayUsb2 = OffDelaySec(600);
    expect(offDelayUsb, offDelayUsb2, reason: 'number equal');

    expect(offDelayUsb, OffDelayUsbEnum.offDelay_10m, reason: 'number equal');
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['OffDelayUsb'] = 'OFF_DELAY_10M';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.offDelayUsb]);
    expect(options.offDelayUsb?.rawValue, 'OFF_DELAY_10M',
        reason: 'offDelayUsb const');
  });

  test('getOptions number', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(methodCall.arguments, ['OffDelayUsb']);
      Map<String, dynamic> optionMap = {};
      optionMap['OffDelayUsb'] = 600;
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.offDelayUsb]);
    expect(options.offDelayUsb?.rawValue, 600, reason: 'number');
    expect((options.offDelayUsb as OffDelayUsbSec).sec, 600, reason: 'number');
  });

  test('setOptions const', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['OffDelayUsb'], 'OFF_DELAY_10M', reason: 'const');
      return Future.value();
    });
    final options = Options();
    options.offDelayUsb = OffDelayUsbEnum.offDelay_10m;
    await platform.setOptions(options);
  });

  test('setOptions number', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['OffDelayUsb'], 1800, reason: 'number');
      return Future.value();
    });
    final options = Options();
    options.offDelayUsb = OffDelayUsbSec(1800);
    await platform.setOptions(options);
  });
}
