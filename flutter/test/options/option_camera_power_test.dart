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

  test('CameraPowerEnum const', () async {
    List<List<dynamic>> data = [
      [CameraPowerEnum.unknown, 'UNKNOWN'],
      [CameraPowerEnum.on, 'ON'],
      [CameraPowerEnum.off, 'OFF'],
      [CameraPowerEnum.powerSaving, 'POWER_SAVING'],
      [CameraPowerEnum.silentMode, 'SILENT_MODE']
    ];
    expect(data.length, CameraPowerEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['CameraPower'] = 'SILENT_MODE';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.cameraPower]);
    expect(options.cameraPower?.rawValue, 'SILENT_MODE', reason: 'quality');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['CameraPower'], 'POWER_SAVING', reason: 'quality');
      return Future.value();
    });
    final options = Options();
    options.cameraPower = CameraPowerEnum.powerSaving;
    await platform.setOptions(options);
  });
}
