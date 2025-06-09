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

  test('UsbConnectionEnum const', () async {
    List<List<dynamic>> data = [
      [UsbConnectionEnum.unknown, 'UNKNOWN'],
      [UsbConnectionEnum.mtp, 'MTP'],
      [UsbConnectionEnum.msc, 'MSC']
    ];
    expect(data.length, UsbConnectionEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['UsbConnection'] = 'MTP';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.usbConnection]);
    expect(options.usbConnection?.rawValue, 'MTP', reason: 'quality');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['UsbConnection'], 'MSC', reason: 'quality');
      return Future.value();
    });
    final options = Options();
    options.usbConnection = UsbConnectionEnum.msc;
    await platform.setOptions(options);
  });
}
