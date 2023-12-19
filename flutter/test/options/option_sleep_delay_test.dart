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

  test('SleepDelayEnum const', () async {
    List<List<dynamic>> data = [
      [SleepDelayEnum.disable, 'DISABLE', 6555],
      [SleepDelayEnum.sleepDelay_3m, 'SLEEP_DELAY_3M', 180],
      [SleepDelayEnum.sleepDelay_5m, 'SLEEP_DELAY_5M', 300],
      [SleepDelayEnum.sleepDelay_7m, 'SLEEP_DELAY_7M', 420],
      [SleepDelayEnum.sleepDelay_10m, 'SLEEP_DELAY_10M', 600],
    ];
    expect(data.length, SleepDelayEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      SleepDelayEnum enumValue = data[i][0];
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
      expect(enumValue.sec, data[i][2], reason: data[i][1]);
    }
  });

  test('SleepDelayEnum number', () async {
    final sleepDelay = SleepDelaySec(300);
    expect(sleepDelay.rawValue, 300, reason: 'rawValue');
    expect(sleepDelay.sec, 300, reason: 'number');

    final sleepDelay2 = SleepDelaySec(300);
    expect(sleepDelay, sleepDelay2, reason: 'number equal');

    expect(sleepDelay, SleepDelayEnum.sleepDelay_5m, reason: 'number equal');
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['SleepDelay'] = 'SLEEP_DELAY_5M';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.sleepDelay]);
    expect(options.sleepDelay?.rawValue, 'SLEEP_DELAY_5M',
        reason: 'sleepDelay const');
  });

  test('getOptions number', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(methodCall.arguments, ['SleepDelay']);
      Map<String, dynamic> optionMap = {};
      optionMap['SleepDelay'] = 600;
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.sleepDelay]);
    expect(options.sleepDelay?.rawValue, 600, reason: 'number');
    expect((options.sleepDelay as SleepDelaySec).sec, 600, reason: 'number');
  });

  test('setOptions const', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['SleepDelay'], 'SLEEP_DELAY_3M', reason: 'const');
      return Future.value();
    });
    final options = Options();
    options.sleepDelay = SleepDelayEnum.sleepDelay_3m;
    await platform.setOptions(options);
  });

  test('setOptions number', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['SleepDelay'], 420, reason: 'number');
      return Future.value();
    });
    final options = Options();
    options.sleepDelay = SleepDelaySec(420);
    await platform.setOptions(options);
  });
}
