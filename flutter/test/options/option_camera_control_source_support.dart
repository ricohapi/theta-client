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
      optionMap['CameraControlSourceSupport'] = ['CAMERA', 'APP', 'UNKNOWN'];
      return Future.value(optionMap);
    });
    Options options =
        await platform.getOptions([OptionNameEnum.cameraControlSourceSupport]);
    expect(
        options.cameraControlSourceSupport,
        [
          CameraControlSourceEnum.camera,
          CameraControlSourceEnum.app,
          CameraControlSourceEnum.unknown
        ],
        reason: 'quality');
  });
}
