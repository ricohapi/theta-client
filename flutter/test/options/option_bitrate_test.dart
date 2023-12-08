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

  test('Bitrate const', () async {
    List<List<dynamic>> data = [
      [Bitrate.auto, 'AUTO'],
      [Bitrate.fine, 'FINE'],
      [Bitrate.normal, 'NORMAL'],
      [Bitrate.economy, 'ECONOMY'],
    ];
    expect(data.length, Bitrate.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('Bitrate number', () async {
    final bitrate = BitrateNumber(10);
    expect(bitrate.rawValue, '10', reason: 'rawValue');
    expect(bitrate.value, 10, reason: 'number');

    final bitrate2 = BitrateNumber(10);
    expect(bitrate, bitrate2, reason: 'number equal');
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['Bitrate'] = 'FINE';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.bitrate]);
    expect(options.bitrate?.rawValue, 'FINE', reason: 'quality');
  });

  test('getOptions number', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['Bitrate'] = 10;
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.bitrate]);
    expect(options.bitrate?.rawValue, "10", reason: 'number');
    expect((options.bitrate as BitrateNumber).value, 10, reason: 'number');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['Bitrate'], 'AUTO', reason: 'quality');
      return Future.value();
    });
    final options = Options();
    options.bitrate = Bitrate.auto;
    await platform.setOptions(options);
  });

  test('setOptions number', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['Bitrate'], 10, reason: 'number');
      return Future.value();
    });
    final options = Options();
    options.bitrate = BitrateNumber(10);
    await platform.setOptions(options);
  });
}
