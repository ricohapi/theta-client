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

  test('ExposureCompensationEnum const', () async {
    List<List<dynamic>> data = [
      [ExposureCompensationEnum.unknown, 'UNKNOWN'],
      [ExposureCompensationEnum.m4_0, 'M4_0'],
      [ExposureCompensationEnum.m3_7, 'M3_7'],
      [ExposureCompensationEnum.m3_3, 'M3_3'],
      [ExposureCompensationEnum.m3_0, 'M3_0'],
      [ExposureCompensationEnum.m2_7, 'M2_7'],
      [ExposureCompensationEnum.m2_3, 'M2_3'],
      [ExposureCompensationEnum.m2_0, 'M2_0'],
      [ExposureCompensationEnum.m1_7, 'M1_7'],
      [ExposureCompensationEnum.m1_3, 'M1_3'],
      [ExposureCompensationEnum.m1_0, 'M1_0'],
      [ExposureCompensationEnum.m0_7, 'M0_7'],
      [ExposureCompensationEnum.m0_3, 'M0_3'],
      [ExposureCompensationEnum.zero, 'ZERO'],
      [ExposureCompensationEnum.p0_3, 'P0_3'],
      [ExposureCompensationEnum.p0_7, 'P0_7'],
      [ExposureCompensationEnum.p1_0, 'P1_0'],
      [ExposureCompensationEnum.p1_3, 'P1_3'],
      [ExposureCompensationEnum.p1_7, 'P1_7'],
      [ExposureCompensationEnum.p2_0, 'P2_0'],
      [ExposureCompensationEnum.p2_3, 'P2_3'],
      [ExposureCompensationEnum.p2_7, 'P2_7'],
      [ExposureCompensationEnum.p3_0, 'P3_0'],
      [ExposureCompensationEnum.p3_3, 'P3_3'],
      [ExposureCompensationEnum.p3_7, 'P3_7'],
      [ExposureCompensationEnum.p4_0, 'P4_0'],
    ];
    expect(data.length, ExposureCompensationEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['ExposureCompensation'] = 'ZERO';
      return Future.value(optionMap);
    });
    Options options =
        await platform.getOptions([OptionNameEnum.exposureCompensation]);
    expect(options.exposureCompensation?.rawValue, 'ZERO', reason: 'quality');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['ExposureCompensation'], 'P4_0', reason: 'quality');
      return Future.value();
    });
    final options = Options();
    options.exposureCompensation = ExposureCompensationEnum.p4_0;
    await platform.setOptions(options);
  });
}
