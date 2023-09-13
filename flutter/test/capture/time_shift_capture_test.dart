import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

import '../theta_client_flutter_test.dart';

void main() {
  setUp(() {});

  tearDown(() {
    onCallGetTimeShiftCaptureBuilder = Future.value;
    onCallBuildTimeShiftCapture = (options, interval) => Future.value();
  });

  test('getTimeShiftCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var builder = thetaClientPlugin.getTimeShiftCaptureBuilder();
    expect(builder, isNotNull);
  });

  test('buildTimeShiftCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    final timeShift = TimeShift(
        isFrontFirst: true,
        firstInterval: TimeShiftIntervalEnum.interval_1,
        secondInterval: TimeShiftIntervalEnum.interval_2);

    onCallBuildTimeShiftCapture = (options, interval) {
      final optionTimeShift = options['TimeShift'];
      expect(optionTimeShift, isNotNull);
      expect(optionTimeShift.isFrontFirst, true);
      expect(optionTimeShift.firstInterval, TimeShiftIntervalEnum.interval_1);
      expect(optionTimeShift.secondInterval, TimeShiftIntervalEnum.interval_2);
      return Future.value(null);
    };

    final builder = thetaClientPlugin.getTimeShiftCaptureBuilder();
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

  test('startTimeShiftCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrl = 'http://test.JPG';

    onCallStartTimeShiftCapture = (onProgress) {
      return Future.value(imageUrl);
    };

    var builder = thetaClientPlugin.getTimeShiftCaptureBuilder();
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

  test('startTimeShiftCapture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<String>();
    onCallStartTimeShiftCapture = (onProgress) {
      return completer.future;
    };
    onCallStopTimeShiftCapture = () {
      completer.completeError(Exception('Error. startTimeShiftCapture'));
      return Future.value();
    };

    var builder = thetaClientPlugin.getTimeShiftCaptureBuilder();
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

  test('stopTimeShiftCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrl = 'http://test.mp4';

    var completer = Completer<String>();
    onCallStartTimeShiftCapture = (onProgress) {
      return completer.future;
    };
    onCallStopTimeShiftCapture = () {
      completer.complete(imageUrl);
      return Future.value();
    };

    var builder = thetaClientPlugin.getTimeShiftCaptureBuilder();
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
}
