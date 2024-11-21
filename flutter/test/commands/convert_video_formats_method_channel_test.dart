import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter_method_channel.dart';

void main() {
  MethodChannelThetaClientFlutter platform = MethodChannelThetaClientFlutter();
  const MethodChannel channel = MethodChannel('theta_client_flutter');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    platform = MethodChannelThetaClientFlutter();
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, null);
  });

  test('convertVideoFormats', () async {
    String fileUrl = 'http://dummy.MP4';
    String result = 'http://dummy_result.MP4';
    bool toLowResolution = true;
    bool applyTopBottomCorrection = true;
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(methodCall.method, 'convertVideoFormats');
      expect(platform.notifyList.containsKey(10091), true,
          reason: 'add notify progress');

      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['fileUrl'], fileUrl);
      expect(arguments['toLowResolution'], toLowResolution);
      expect(arguments['applyTopBottomCorrection'], applyTopBottomCorrection);

      // native event
      platform.onNotify({
        'id': 10091,
        'params': {
          'completion': 0.1,
        },
      });
      await Future.delayed(const Duration(milliseconds: 10));
      platform.onNotify({
        'id': 10091,
        'params': {
          'completion': 0.2,
        },
      });
      await Future.delayed(const Duration(milliseconds: 10));

      return Future.value(result);
    });

    int progressCount = 0;
    expect(
        await platform.convertVideoFormats(
            fileUrl, toLowResolution, applyTopBottomCorrection, (completion) {
          progressCount += 1;
        }),
        result);
    expect(progressCount, 2);
    expect(platform.notifyList.length, 0);

    expect(
        await platform.convertVideoFormats(
            fileUrl, toLowResolution, applyTopBottomCorrection, null),
        result);
  });

  test('error call in running', () async {
    String fileUrl = 'http://dummy.MP4';
    String result = 'http://dummy_result.MP4';
    bool toLowResolution = true;
    bool applyTopBottomCorrection = true;
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(methodCall.method, 'convertVideoFormats');
      await Future.delayed(const Duration(milliseconds: 10));

      return Future.value(result);
    });

    var future = platform.convertVideoFormats(
        fileUrl, toLowResolution, applyTopBottomCorrection, (completion) {});

    try {
      await platform.convertVideoFormats(
          fileUrl, toLowResolution, applyTopBottomCorrection, (completion) {});
      expect(true, false, reason: 'not exception');
    } catch (error) {
      expect(
          error.toString().contains('convertVideoFormats is running.'), true);
    }
    await future.then((value) {
      expect(value, result);
    });
    expect(platform.notifyList.length, 0);
  });

  test('exception', () async {
    String fileUrl = 'http://dummy.MP4';
    bool toLowResolution = true;
    bool applyTopBottomCorrection = true;
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(methodCall.method, 'convertVideoFormats');
      throw Exception('test error');
    });

    try {
      await platform.convertVideoFormats(
          fileUrl, toLowResolution, applyTopBottomCorrection, (completion) {});
      expect(true, false, reason: 'not exception');
    } catch (error) {
      expect(error.toString().contains('test error'), true);
    }
    expect(platform.notifyList.length, 0);
  });
}
