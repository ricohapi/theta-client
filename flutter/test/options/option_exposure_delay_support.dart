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

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['ExposureDelaySupport'] = [
        'DELAY_OFF',
        'DELAY_1',
        'DELAY_2',
        'DELAY_3',
        'DELAY_4',
        'DELAY_5',
        'DELAY_6',
        'DELAY_7',
        'DELAY_8',
        'DELAY_9',
        'DELAY_10'
      ];
      return Future.value(optionMap);
    });
    Options options =
        await platform.getOptions([OptionNameEnum.exposureDelaySupport]);
    expect(
        options.exposureDelaySupport,
        [
          ExposureDelayEnum.delayOff,
          ExposureDelayEnum.delay1,
          ExposureDelayEnum.delay2,
          ExposureDelayEnum.delay3,
          ExposureDelayEnum.delay4,
          ExposureDelayEnum.delay5,
          ExposureDelayEnum.delay6,
          ExposureDelayEnum.delay7,
          ExposureDelayEnum.delay8,
          ExposureDelayEnum.delay9,
          ExposureDelayEnum.delay10
        ],
        reason: 'quality');
  });
}
