import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

import '../theta_client_flutter_test.dart';

void main() {
  setUp(() {});

  tearDown(() {
    onCallGetTimeShiftManualCaptureBuilder = Future.value;
    onCallBuildTimeShiftManualCapture = (options, interval) => Future.value();
  });

  test('getTimeShiftManualCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var builder = thetaClientPlugin.getTimeShiftManualCaptureBuilder();
    expect(builder, isNotNull);
  });

  test('buildTimeShiftManualCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    final timeShift = TimeShift(
        isFrontFirst: true,
        firstInterval: TimeShiftIntervalEnum.interval_1,
        secondInterval: TimeShiftIntervalEnum.interval_2);

    onCallBuildTimeShiftManualCapture = (options, interval) {
      final optionTimeShift = options['TimeShift'];
      expect(optionTimeShift, isNotNull);
      expect(optionTimeShift.isFrontFirst, true);
      expect(optionTimeShift.firstInterval, TimeShiftIntervalEnum.interval_1);
      expect(optionTimeShift.secondInterval, TimeShiftIntervalEnum.interval_2);
      return Future.value(null);
    };

    final builder = thetaClientPlugin.getTimeShiftManualCaptureBuilder();
    builder.setIsFrontFirst(timeShift.isFrontFirst as bool);
    builder.setFirstInterval(timeShift.firstInterval as TimeShiftIntervalEnum);
    builder
        .setSecondInterval(timeShift.secondInterval as TimeShiftIntervalEnum);

    var capture = await builder.build();
    expect(capture, isNotNull);
    final optionTimeShift = capture.getTimeShiftSetting();
    expect(optionTimeShift, isNotNull);
    expect(optionTimeShift?.isFrontFirst, true);
    expect(optionTimeShift?.firstInterval, TimeShiftIntervalEnum.interval_1);
    expect(optionTimeShift?.secondInterval, TimeShiftIntervalEnum.interval_2);
  });

  test('startTimeShiftManualCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrl = 'http://test.JPG';

    onCallStartTimeShiftManualCapture =
        (onProgress, onStopFailed, onCapturing) {
      return Future.value(imageUrl);
    };

    var builder = thetaClientPlugin.getTimeShiftManualCaptureBuilder();
    var capture = await builder.build();
    String? fileUrl;
    capture.startCapture(
        (value) {
          expect(value, imageUrl);
          fileUrl = value;
        },
        (completion) {},
        (exception) {
          expect(false, isTrue, reason: 'Error. startCapture');
        });

    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrl, imageUrl);
  });

  test('startTimeShiftManualCapture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<String>();
    onCallStartTimeShiftManualCapture =
        (onProgress, onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopTimeShiftManualCapture = () {
      completer.completeError(Exception('Error. startTimeShiftManualCapture'));
      return Future.value();
    };

    var builder = thetaClientPlugin.getTimeShiftManualCaptureBuilder();
    var capture = await builder.build();
    var capturing = capture.startCapture(
        (fileUrl) {
          expect(false, isTrue, reason: 'startCapture');
        },
        (completion) {},
        (exception) {
          expect(exception, isNotNull, reason: 'Error. startCapture');
        });

    capturing.stopCapture();
    await Future.delayed(const Duration(milliseconds: 10), () {});
  });

  test('stopTimeShiftManualCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrl = 'http://test.mp4';

    var completer = Completer<String>();
    onCallStartTimeShiftManualCapture =
        (onProgress, onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopTimeShiftManualCapture = () {
      completer.complete(imageUrl);
      return Future.value();
    };

    var builder = thetaClientPlugin.getTimeShiftManualCaptureBuilder();
    var capture = await builder.build();
    String? fileUrl;
    var capturing = capture.startCapture(
        (value) {
          expect(value, imageUrl);
          fileUrl = value;
        },
        (completion) {},
        (exception) {
          expect(false, isTrue, reason: 'Error. startCapture');
        });

    capturing.stopCapture();
    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrl, imageUrl);
  });

  test('call onStopFailed', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<void>();

    void Function(Exception exception)? paramStopFailed;

    onCallStartTimeShiftManualCapture =
        (onProgress, onStopFailed, onCapturing) {
      paramStopFailed = onStopFailed;
      return Completer<String>().future;
    };
    onCallStopTimeShiftManualCapture = () {
      paramStopFailed?.call(Exception("on stop error."));
      return Future.value();
    };

    var builder = thetaClientPlugin.getTimeShiftManualCaptureBuilder();
    var capture = await builder.build();
    var isOnStopFailed = false;
    var capturing = capture.startCapture(
        (fileUrl) {
          expect(false, isTrue, reason: 'startCapture');
        },
        (completion) {},
        (exception) {},
        onStopFailed: (exception) {
          expect(exception, isNotNull, reason: 'Error. stopCapture');
          isOnStopFailed = true;
          completer.complete(null);
        });

    capturing.stopCapture();
    await completer.future.timeout(const Duration(milliseconds: 10));
    expect(isOnStopFailed, true);
  });

  test('call onCapturing', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<void>();

    void Function(CapturingStatusEnum status)? paramOnCapturing;

    onCallStartTimeShiftManualCapture =
        (onProgress, onStopFailed, onCapturing) {
      paramOnCapturing = onCapturing;
      return Completer<String>().future;
    };
    onCallStopTimeShiftManualCapture = () {
      paramOnCapturing?.call(CapturingStatusEnum.capturing);
      return Future.value();
    };

    var builder = thetaClientPlugin.getTimeShiftManualCaptureBuilder();
    var capture = await builder.build();
    var isOnCapturing = false;
    var capturing = capture.startCapture(
        (fileUrl) {
          expect(false, isTrue, reason: 'startCapture');
        },
        (completion) {},
        (exception) {},
        onCapturing: (status) {
          expect(status, CapturingStatusEnum.capturing);
          isOnCapturing = true;
          completer.complete(null);
        });

    capturing.stopCapture();
    await completer.future.timeout(const Duration(milliseconds: 10));
    expect(isOnCapturing, true);
  });
}
