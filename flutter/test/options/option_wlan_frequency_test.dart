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

  test('WlanFrequencyEnum const', () async {
    List<List<dynamic>> data = [
      [WlanFrequencyEnum.unknown, 'UNKNOWN'],
      [WlanFrequencyEnum.ghz_2_4, 'GHZ_2_4'],
      [WlanFrequencyEnum.ghz_5, 'GHZ_5'],
      [WlanFrequencyEnum.ghz_5_2, 'GHZ_5_2'],
      [WlanFrequencyEnum.ghz_5_8, 'GHZ_5_8'],
    ];
    expect(data.length, WlanFrequencyEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['WlanFrequency'] = 'GHZ_5';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.wlanFrequency]);
    expect(options.wlanFrequency?.rawValue, 'GHZ_5', reason: 'quality');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['WlanFrequency'], 'GHZ_5_8', reason: 'quality');
      return Future.value();
    });
    final options = Options();
    options.wlanFrequency = WlanFrequencyEnum.ghz_5_8;
    await platform.setOptions(options);
  });
}
