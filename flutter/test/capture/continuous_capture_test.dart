import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

import '../theta_client_flutter_test.dart';

void main() {
  setUp(() {});

  tearDown(() {
    onCallGetContinuousCaptureBuilder = Future.value;
    onCallBuildContinuousCapture = (options, interval) => Future.value();
  });

  test('getContinuousCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var builder = thetaClientPlugin.getContinuousCaptureBuilder();
    expect(builder, isNotNull);
  });

  test('buildContinuousCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const format = PhotoFileFormatEnum.image_5_5K;
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

    onCallBuildContinuousCapture = (options, interval) {
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

    final builder = thetaClientPlugin.getContinuousCaptureBuilder();
    builder.setFileFormat(format);
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
    expect(capture.getFileFormat(), format);
  });

  test('startContinuousCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrls = ['http://test.jpeg'];

    onCallStartContinuousCapture = (onProgress, onCapturing) {
      return Future.value(imageUrls);
    };

    final builder = thetaClientPlugin.getContinuousCaptureBuilder();
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

  test('startContinuousCapture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<List<String>>();
    onCallStartContinuousCapture = (onProgress, onCapturing) {
      return completer.future;
    };

    final builder = thetaClientPlugin.getContinuousCaptureBuilder();
    var capture = await builder.build();
    capture.startCapture(
        (fileUrl) {
          expect(false, isTrue, reason: 'startCapture');
        },
        (completion) {},
        (exception) {
          expect(exception, isNotNull, reason: 'Error. startCapture');
        });
    await Future.delayed(const Duration(milliseconds: 10), () {});
  });

  test('call onProgress', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<void>();

    void Function(double completion)? paramOnProgress;

    onCallStartContinuousCapture = (onProgress, onCapturing) {
      paramOnProgress = onProgress;
      return Completer<List<String>>().future;
    };

    final builder = thetaClientPlugin.getContinuousCaptureBuilder();
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

  test('call getContinuousNumber', () async {
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetOptions = (optionNames) {
      expect(optionNames.length, 1);
      expect(optionNames[0], OptionNameEnum.continuousNumber);
      final options = Options();
      options.continuousNumber = ContinuousNumberEnum.max10;
      return Future.value(options);
    };

    var capture = ContinuousCapture({}, 1);
    final continuousNumber = await capture.getContinuousNumber();
    expect(continuousNumber, ContinuousNumberEnum.max10);
  });

  test('call getContinuousNumber unsupported', () async {
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetOptions = (optionNames) {
      expect(optionNames.length, 1);
      expect(optionNames[0], OptionNameEnum.continuousNumber);
      final options = Options();
      return Future.value(options);
    };

    var capture = ContinuousCapture({}, 1);
    final continuousNumber = await capture.getContinuousNumber();
    expect(continuousNumber, ContinuousNumberEnum.unsupported);
  });

  test('call getContinuousNumber exception', () async {
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetOptions = (optionNames) {
      throw Exception('test error');
    };

    var capture = ContinuousCapture({}, 1);
    try {
      await capture.getContinuousNumber();
    } catch (error) {
      expect(error.toString().contains('test error'), true);
    }
  });

  test('call onCapturing', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
    MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<void>();

    void Function(CapturingStatusEnum status)? paramOnCapturing;

    onCallStartContinuousCapture = (onProgress, onCapturing) {
      paramOnCapturing = onCapturing;
      return Completer<List<String>>().future;
    };

    final builder = thetaClientPlugin.getContinuousCaptureBuilder();
    var capture = await builder.build();
    var isOnCapturing = false;
    capture.startCapture((fileUrl) {
      expect(false, isTrue, reason: 'startCapture');
    }, (completion) {}, (exception) {},
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
