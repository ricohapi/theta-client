import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

import '../theta_client_flutter_test.dart';

void main() {
  setUp(() {
    onCallGetLimitlessIntervalCaptureBuilder = Future.value;
    onCallBuildLimitlessIntervalCapture = (options, interval) => Future.value();
  });

  tearDown(() {});

  test('getLimitlessIntervalCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetLimitlessIntervalCaptureBuilder = Future.value;

    var builder = thetaClientPlugin.getLimitlessIntervalCaptureBuilder();
    expect(builder, isNotNull);
  });

  test('buildLimitlessIntervalCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetLimitlessIntervalCaptureBuilder = Future.value;

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

    onCallBuildLimitlessIntervalCapture = (options, interval) {
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

    final builder = thetaClientPlugin.getLimitlessIntervalCaptureBuilder();
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

  test('startLimitlessIntervalCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrls = ['http://test.jpeg'];

    onCallStartLimitlessIntervalCapture = (onStopFailed, onCapturing) {
      return Future.value(imageUrls);
    };

    var builder = thetaClientPlugin.getLimitlessIntervalCaptureBuilder();
    var capture = await builder.build();
    List<String>? fileUrls;

    capture.startCapture((value) {
      expect(value, imageUrls);
      fileUrls = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. startCapture');
    }, onStopFailed: (exception) {});

    await Future.delayed(const Duration(milliseconds: 100), () {});
    expect(fileUrls, imageUrls);
    expect(capture.getAperture(), isNull);
  });

  test('startLimitlessIntervalCapture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<List<String>>();
    onCallStartLimitlessIntervalCapture = (onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopLimitlessIntervalCapture = () {
      completer
          .completeError(Exception('Error. startLimitlessIntervalCapture'));
      return Future.value();
    };

    var builder = thetaClientPlugin.getLimitlessIntervalCaptureBuilder();
    var capture = await builder.build();
    var capturing = capture.startCapture((value) {
      expect(false, isTrue, reason: 'startCapture');
    }, (exception) {
      expect(exception, isNotNull, reason: 'Error. startCapture');
    }, onStopFailed: (exception) {});
    capturing.stopCapture();
    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(capture.getAperture(), isNull);
  });

  test('stopLimitlessIntervalCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrls = ['http://test.jpeg'];

    var completer = Completer<List<String>>();
    onCallStartLimitlessIntervalCapture = (onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopLimitlessIntervalCapture = () {
      completer.complete(imageUrls);
      return Future.value();
    };

    var builder = thetaClientPlugin.getLimitlessIntervalCaptureBuilder();
    var capture = await builder.build();
    List<String>? fileUrls;
    var capturing = capture.startCapture((value) {
      expect(value, imageUrls);
      fileUrls = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. startCapture');
    }, onStopFailed: (exception) {});
    capturing.stopCapture();
    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrls, imageUrls);
    expect(capture.getAperture(), isNull);
  });

  test('stopLimitlessIntervalCapture cancel', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<List<String>?>();
    onCallStartLimitlessIntervalCapture = (onStopFailed, onCapturing) {
      return completer.future;
    };
    onCallStopLimitlessIntervalCapture = () {
      completer.complete(null);
      return Future.value();
    };

    var builder = thetaClientPlugin.getLimitlessIntervalCaptureBuilder();
    var capture = await builder.build();
    List<String>? fileUrls;
    var capturing = capture.startCapture((value) {
      expect(value, null);
      fileUrls = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. startCapture');
    }, onStopFailed: (exception) {});
    capturing.stopCapture();
    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrls, null);
  });

  test('call onStopFailed', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
    MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrls = ['http://test.jpeg'];

    onCallStartLimitlessIntervalCapture = (onStopFailed, onCapturing) {
      onStopFailed?.call(Exception("on stop error."));
      return Future.value(imageUrls);
    };

    var builder = thetaClientPlugin.getLimitlessIntervalCaptureBuilder();
    var capture = await builder.build();
    List<String>? fileUrls;

    var isOnStopFailed = false;
    capture.startCapture((value) {
      expect(value, imageUrls);
      fileUrls = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. startCapture');
    }, onStopFailed: (exception) {
      expect(exception, isNotNull, reason: 'Error. stopCapture');
      isOnStopFailed = true;
    });

    await Future.delayed(const Duration(milliseconds: 100), () {});
    expect(fileUrls, imageUrls);
    expect(isOnStopFailed, true);
  });

  test('call onCapturing', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
    MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrls = ['http://test.jpeg'];

    onCallStartLimitlessIntervalCapture = (onStopFailed, onCapturing) {
      onCapturing?.call(CapturingStatusEnum.capturing);
      return Future.value(imageUrls);
    };

    var builder = thetaClientPlugin.getLimitlessIntervalCaptureBuilder();
    var capture = await builder.build();
    List<String>? fileUrls;

    var isOnCapturing = false;
    capture.startCapture((value) {
      expect(value, imageUrls);
      fileUrls = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. startCapture');
    }, onCapturing: (status) {
      isOnCapturing = true;
      expect(status, CapturingStatusEnum.capturing);
    });

    await Future.delayed(const Duration(milliseconds: 100), () {});
    expect(fileUrls, imageUrls);
    expect(isOnCapturing, true);
  });
}
