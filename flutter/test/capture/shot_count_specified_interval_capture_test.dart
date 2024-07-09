import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

import '../theta_client_flutter_test.dart';

void main() {
  setUp(() {});

  tearDown(() {
    onCallGetShotCountSpecifiedIntervalCaptureBuilder = Future.value;
    onCallBuildShotCountSpecifiedIntervalCapture =
        (options, interval) => Future.value();
  });

  test('getShotCountSpecifiedIntervalCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const shotCount = 2;
    var builder = thetaClientPlugin
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
    expect(builder, isNotNull);
  });

  test('buildShotCountSpecifiedIntervalCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const shotCount = 2;

    const aperture = [ApertureEnum.aperture_2_0, 'Aperture'];
    const colorTemperature = [2, 'ColorTemperature'];
    const exposureCompensation = [
      ExposureCompensationEnum.m0_3,
      'ExposureCompensation'
    ];
    const exposureDelay = [ExposureDelayEnum.delay1, 'ExposureDelay'];
    const exposureProgram = [
      ExposureProgramEnum.aperturePriority,
      'ExposureProgram'
    ];
    final gpsInfo = [
      GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'),
      'GpsInfo'
    ];
    const gpsTagRecording = [GpsTagRecordingEnum.on, 'GpsTagRecording'];
    const iso = [IsoEnum.iso100, 'Iso'];
    const isoAutoHighLimit = [IsoAutoHighLimitEnum.iso125, 'IsoAutoHighLimit'];
    const whiteBalance = [WhiteBalanceEnum.auto, 'WhiteBalance'];

    onCallBuildShotCountSpecifiedIntervalCapture = (options, interval) {
      expect(options[aperture[1]], aperture[0]);
      expect(options[colorTemperature[1]], colorTemperature[0]);
      expect(options[exposureCompensation[1]], exposureCompensation[0]);
      expect(options[exposureDelay[1]], exposureDelay[0]);
      expect(options[exposureProgram[1]], exposureProgram[0]);
      expect(options[gpsInfo[1]], gpsInfo[0]);
      expect(options[gpsTagRecording[1]], gpsTagRecording[0]);
      expect(options[iso[1]], iso[0]);
      expect(options[isoAutoHighLimit[1]], isoAutoHighLimit[0]);
      expect(options[whiteBalance[1]], whiteBalance[0]);
      return Future.value(null);
    };

    final builder = thetaClientPlugin
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
    builder.setAperture(aperture[0] as ApertureEnum);
    builder.setColorTemperature(colorTemperature[0] as int);
    builder.setExposureCompensation(
        exposureCompensation[0] as ExposureCompensationEnum);
    builder.setExposureDelay(exposureDelay[0] as ExposureDelayEnum);
    builder.setExposureProgram(exposureProgram[0] as ExposureProgramEnum);
    builder.setGpsInfo(gpsInfo[0] as GpsInfo);
    builder.setGpsTagRecording(gpsTagRecording[0] as GpsTagRecordingEnum);
    builder.setIso(iso[0] as IsoEnum);
    builder.setIsoAutoHighLimit(isoAutoHighLimit[0] as IsoAutoHighLimitEnum);
    builder.setWhiteBalance(whiteBalance[0] as WhiteBalanceEnum);

    var capture = await builder.build();
    expect(capture, isNotNull);
    expect(capture.getAperture(), aperture[0]);
    expect(capture.getColorTemperature(), colorTemperature[0]);
    expect(capture.getExposureCompensation(), exposureCompensation[0]);
    expect(capture.getExposureDelay(), exposureDelay[0]);
    expect(capture.getExposureProgram(), exposureProgram[0]);
    expect(capture.getGpsInfo(), gpsInfo[0]);
    expect(capture.getGpsTagRecording(), gpsTagRecording[0]);
    expect(capture.getIso(), iso[0]);
    expect(capture.getIsoAutoHighLimit(), isoAutoHighLimit[0]);
    expect(capture.getWhiteBalance(), whiteBalance[0]);
  });

  test('startShotCountSpecifiedIntervalCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const shotCount = 2;
    const imageUrls = ['http://test.jpeg'];

    onCallStartShotCountSpecifiedIntervalCapture =
        (onProgress, onStopFailed, onCapturing) {
      return Future.value(imageUrls);
    };

    var builder = thetaClientPlugin
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
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

  test('startShotCountSpecifiedIntervalCapture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<List<String>>();
    onCallStartShotCountSpecifiedIntervalCapture =
        (onProgress, onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopShotCountSpecifiedIntervalCapture = () {
      completer.completeError(
          Exception('Error. startShotCountSpecifiedIntervalCapture'));
      return Future.value();
    };

    const shotCount = 2;
    var builder = thetaClientPlugin
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
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

  test('stopShotCountSpecifiedIntervalCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const shotCount = 2;
    const imageUrls = ['http://test.jpeg'];

    var completer = Completer<List<String>>();
    onCallStartShotCountSpecifiedIntervalCapture =
        (onProgress, onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopShotCountSpecifiedIntervalCapture = () {
      completer.complete(imageUrls);
      return Future.value();
    };

    var builder = thetaClientPlugin
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
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

    onCallStartShotCountSpecifiedIntervalCapture =
        (onProgress, onStopFailed, onCapturing) {
      paramStopFailed = onStopFailed;
      return Completer<List<String>>().future;
    };
    onCallStopShotCountSpecifiedIntervalCapture = () {
      paramStopFailed?.call(Exception("on stop error."));
      return Future.value();
    };

    const shotCount = 2;
    var builder = thetaClientPlugin
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
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

    onCallStartShotCountSpecifiedIntervalCapture =
        (onProgress, onStopFailed, onCapturing) {
      paramOnProgress = onProgress;
      return Completer<List<String>>().future;
    };

    const shotCount = 2;
    var builder = thetaClientPlugin
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
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

    onCallStartShotCountSpecifiedIntervalCapture =
        (onProgress, onStopFailed, onCapturing) {
      paramOnCapturing = onCapturing;
      return Completer<List<String>>().future;
    };

    const shotCount = 2;
    var builder = thetaClientPlugin
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
    var capture = await builder.build();
    var isOnCapturing = false;
    capture.startCapture((fileUrl) {
      expect(false, isTrue, reason: 'startCapture');
    }, (completion) {}, (exception) {},
        onCapturing: (status) {
          isOnCapturing = true;
          completer.complete(null);
        });

    paramOnCapturing?.call(CapturingStatusEnum.capturing);
    await completer.future.timeout(const Duration(milliseconds: 10));
    expect(isOnCapturing, true);
  });
}
