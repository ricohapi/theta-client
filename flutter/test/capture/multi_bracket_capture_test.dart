import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

import '../theta_client_flutter_test.dart';

void main() {
  setUp(() {});

  tearDown(() {
    onCallGetMultiBracketCaptureBuilder = Future.value;
    onCallBuildMultiBracketCapture = (options, interval) => Future.value();
  });

  test('getMultiBracketCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var builder = thetaClientPlugin.getMultiBracketCaptureBuilder();
    expect(builder, isNotNull);
  });

  test('buildMultiBracketCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    List<BracketSetting> settings = [
      BracketSetting(
          aperture: ApertureEnum.apertureAuto,
          colorTemperature: 5000,
          exposureCompensation: ExposureCompensationEnum.p0_7,
          exposureProgram: ExposureProgramEnum.normalProgram,
          iso: IsoEnum.iso100,
          shutterSpeed: ShutterSpeedEnum.shutterSpeedAuto,
          whiteBalance: WhiteBalanceEnum.daylight),
      BracketSetting(colorTemperature: 6000)
    ];

    onCallBuildMultiBracketCapture = (options, interval) {
      expect(options['AutoBracket'].length, 2);
      return Future.value(null);
    };

    final builder = thetaClientPlugin.getMultiBracketCaptureBuilder();
    builder.setBracketSettings(settings);
    var capture = await builder.build();
    expect(capture, isNotNull);
  });

  test('startMultiBracketCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrls = ['http://test1.jpeg', 'http://test2.jpeg'];

    onCallStartMultiBracketCapture = (onProgress, onStopFailed, onCapturing) {
      return Future.value(imageUrls);
    };

    var builder = thetaClientPlugin.getMultiBracketCaptureBuilder();
    var capture = await builder.build();
    List<String>? fileUrls;
    capture.startCapture(
        (value) {
          expect(value, imageUrls);
          fileUrls = value;
        },
        (completion) {},
        (exception) {
          expect(false, isTrue, reason: 'Error. startCapture');
        });

    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrls, imageUrls);
    expect(capture.getAperture(), isNull);
  });

  test('startMultiBracketCapture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<List<String>>();
    onCallStartMultiBracketCapture = (onProgress, onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopMultiBracketCapture = () {
      completer.completeError(Exception('Error. startMultiBracketCapture'));
      return Future.value();
    };

    var builder = thetaClientPlugin.getMultiBracketCaptureBuilder();
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

  test('stopMultiBracketCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrls = ['http://test1.jpeg', 'http://test2.jpeg'];

    var completer = Completer<List<String>>();
    onCallStartMultiBracketCapture = (onProgress, onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopMultiBracketCapture = () {
      completer.complete(imageUrls);
      return Future.value();
    };

    var builder = thetaClientPlugin.getMultiBracketCaptureBuilder();
    var capture = await builder.build();
    List<String>? fileUrls;
    var capturing = capture.startCapture(
        (value) {
          expect(value, imageUrls);
          fileUrls = value;
        },
        (completion) {},
        (exception) {
          expect(false, isTrue, reason: 'Error. startCapture');
        });

    capturing.stopCapture();
    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrls, imageUrls);
  });

  test('call onStopFailed', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<void>();

    void Function(Exception exception)? paramStopFailed;

    onCallStartMultiBracketCapture = (onProgress, onStopFailed, onCapturing) {
      paramStopFailed = onStopFailed;
      return Completer<List<String>>().future;
    };
    onCallStopMultiBracketCapture = () {
      paramStopFailed?.call(Exception("on stop error."));
      return Future.value();
    };

    var builder = thetaClientPlugin.getMultiBracketCaptureBuilder();
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

  test('call onProgress', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<void>();

    void Function(double completion)? paramOnProgress;

    onCallStartMultiBracketCapture = (onProgress, onStopFailed, onCapturing) {
      paramOnProgress = onProgress;
      return Completer<List<String>>().future;
    };

    var builder = thetaClientPlugin.getMultiBracketCaptureBuilder();
    var capture = await builder.build();
    var isOnProgress = false;
    capture.startCapture((fileUrl) {
      expect(false, isTrue, reason: 'startCapture');
    }, (completion) {
      isOnProgress = true;
      completer.complete(null);
    }, (exception) {});

    paramOnProgress?.call(0.1);
    await completer.future.timeout(const Duration(milliseconds: 10));
    expect(isOnProgress, true);
  });

  test('call onCapturing', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<void>();

    void Function(CapturingStatusEnum status)? paramOnCapturing;

    onCallStartMultiBracketCapture = (onProgress, onStopFailed, onCapturing) {
      paramOnCapturing = onCapturing;
      return Completer<List<String>>().future;
    };

    var builder = thetaClientPlugin.getMultiBracketCaptureBuilder();
    var capture = await builder.build();
    var isOnCapturing = false;
    capture.startCapture(
        (fileUrl) {
          expect(false, isTrue, reason: 'startCapture');
        },
        (completion) {},
        (exception) {},
        onCapturing: (status) {
          isOnCapturing = true;
          expect(status, CapturingStatusEnum.capturing);
          completer.complete(null);
        });

    paramOnCapturing?.call(CapturingStatusEnum.capturing);
    await completer.future.timeout(const Duration(milliseconds: 10));
    expect(isOnCapturing, true);
  });
}
