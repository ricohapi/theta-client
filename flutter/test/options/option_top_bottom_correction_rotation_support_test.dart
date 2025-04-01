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
    final pitchData = {'max': '10.0', 'min': '-15.0', 'stepSize': '1.0'};
    final rollData = {'max': '20.0', 'min': '-5.0', 'stepSize': '0.5'};
    final yawData = {'max': '30.0', 'min': '-10.0', 'stepSize': '2.0'};

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['TopBottomCorrectionRotationSupport'] = {
        "pitch": pitchData,
        "roll": rollData,
        "yaw": yawData
      };
      return Future.value(optionMap);
    });
    Options options = await platform
        .getOptions([OptionNameEnum.topBottomCorrectionRotationSupport]);
    expect(
        options.topBottomCorrectionRotationSupport,
        TopBottomCorrectionRotationSupport(
            pitch: TopBottomCorrectionRotationValueSupport(
              max: double.parse(pitchData['max'] ?? '0.0'),
              min: double.parse(pitchData['min'] ?? '0.0'),
              stepSize: double.parse(pitchData['stepSize'] ?? '0.0'),
            ),
            roll: TopBottomCorrectionRotationValueSupport(
              max: double.parse(rollData['max'] ?? '0.0'),
              min: double.parse(rollData['min'] ?? '0.0'),
              stepSize: double.parse(rollData['stepSize'] ?? '0.0'),
            ),
            yaw: TopBottomCorrectionRotationValueSupport(
              max: double.parse(yawData['max'] ?? '0.0'),
              min: double.parse(yawData['min'] ?? '0.0'),
              stepSize: double.parse(yawData['stepSize'] ?? '0.0'),
            )),
        reason: 'quality');
  });

  test('getOptionsError', () async {
    const pitchData = null;
    final rollData = {'max': '20.0', 'min': '-5.0', 'stepSize': '0.5'};
    final yawData = {'max': 'abc'};

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['TopBottomCorrectionRotationSupport'] = {
        "pitch": pitchData,
        "roll": rollData,
        "yaw": yawData
      };
      return Future.value(optionMap);
    });
    try {
      await platform
          .getOptions([OptionNameEnum.topBottomCorrectionRotationSupport]);
      fail('Expected an exception due to invalid yaw data');
    } on NoSuchMethodError catch (e) {
      expect(e, isA<NoSuchMethodError>(),
          reason: 'Expecting NoSuchMethodError due to invalid yaw data');
    } catch (e) {
      fail('Unexpected exception type: $e');
    }
  });
}
