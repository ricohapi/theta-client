import 'dart:async';

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

  test('getLivePreview', () async {
    bool isCallStop = false;
    var completer = Completer();

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      switch (methodCall.method) {
        case 'stopLivePreview':
          expect(platform.notifyList.containsKey(10001), false,
              reason: 'remove notify livePreview');
          isCallStop = true;
          completer.complete();
          return;
        case 'getLivePreview':
          expect(platform.notifyList.containsKey(10001), true,
              reason: 'add notify livePreview');
          break;
        default:
          expect(false, true, reason: 'bad method: ${methodCall.method}');
          break;
      }

      // native event
      platform.onNotify({
        'id': 10001,
        'params': {
          'image': Uint8List(10),
        },
      });
      await Future.delayed(const Duration(milliseconds: 10));
      platform.onNotify({
        'id': 10001,
        'params': {
          'image': Uint8List(10),
        },
      });
      await Future.delayed(const Duration(milliseconds: 10));
      return;
    });

    int previewCount = 0;
    var resultCapture = platform.getLivePreview((data) {
      expect(data.length, 10);
      previewCount++;
      return previewCount != 2;
    });
    await resultCapture.timeout(const Duration(seconds: 5));
    expect(previewCount, 2);
    expect(platform.notifyList.containsKey(10001), false,
        reason: 'remove notify livePreview');
    await completer.future.timeout(const Duration(seconds: 1));
    expect(isCallStop, true, reason: 'stop called');
  });

  test('getLivePreview exception', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10001), true,
          reason: 'add notify livePreview');
      throw Exception('test error');
    });

    int previewCount = 0;
    var resultCapture = platform.getLivePreview((data) {
      previewCount++;
      return true;
    });
    try {
      await resultCapture.timeout(const Duration(seconds: 5));
      expect(true, false, reason: 'not exception');
    } catch (error) {
      expect(error.toString().contains('test error'), true);
    }
    expect(previewCount, 0);
    expect(platform.notifyList.containsKey(10002), false,
        reason: 'remove notify progress');
  });
}
