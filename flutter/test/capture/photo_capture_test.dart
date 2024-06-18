import 'dart:async';

import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

import '../theta_client_flutter_test.dart';

void main() {
  setUp(() {
    onCallBuildPhotoCapture = (options, interval) => Future.value();
  });

  tearDown(() {});

  test('getPhotoCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetPhotoCaptureBuilder = Future.value;

    var builder = thetaClientPlugin.getPhotoCaptureBuilder();
    expect(builder, isNotNull);
  });

  test('buildPhotoCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetPhotoCaptureBuilder = Future.value;

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
    const fileFormat = [PhotoFileFormatEnum.image_11K, 'PhotoFileFormat'];
    const filter = [FilterEnum.hdr, 'Filter'];
    final gpsInfo = [
      GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'),
      'GpsInfo'
    ];
    const gpsTagRecording = [GpsTagRecordingEnum.on, 'GpsTagRecording'];
    const iso = [IsoEnum.iso100, 'Iso'];
    const isoAutoHighLimit = [IsoAutoHighLimitEnum.iso125, 'IsoAutoHighLimit'];
    const preset = [PresetEnum.face, 'Preset'];
    const whiteBalance = [WhiteBalanceEnum.auto, 'WhiteBalance'];

    onCallBuildPhotoCapture = (options, interval) {
      expect(options[aperture[1]], aperture[0]);
      expect(options[colorTemperature[1]], colorTemperature[0]);
      expect(options[exposureCompensation[1]], exposureCompensation[0]);
      expect(options[exposureDelay[1]], exposureDelay[0]);
      expect(options[exposureProgram[1]], exposureProgram[0]);
      expect(options[fileFormat[1]], fileFormat[0]);
      expect(options[filter[1]], filter[0]);
      expect(options[gpsInfo[1]], gpsInfo[0]);
      expect(options[gpsTagRecording[1]], gpsTagRecording[0]);
      expect(options[iso[1]], iso[0]);
      expect(options[isoAutoHighLimit[1]], isoAutoHighLimit[0]);
      expect(options[preset[1]], preset[0]);
      expect(options[whiteBalance[1]], whiteBalance[0]);
      return Future.value(null);
    };

    final builder = thetaClientPlugin.getPhotoCaptureBuilder();
    builder.setAperture(aperture[0] as ApertureEnum);
    builder.setColorTemperature(colorTemperature[0] as int);
    builder.setExposureCompensation(
        exposureCompensation[0] as ExposureCompensationEnum);
    builder.setExposureDelay(exposureDelay[0] as ExposureDelayEnum);
    builder.setExposureProgram(exposureProgram[0] as ExposureProgramEnum);
    builder.setFileFormat(fileFormat[0] as PhotoFileFormatEnum);
    builder.setFilter(filter[0] as FilterEnum);
    builder.setGpsInfo(gpsInfo[0] as GpsInfo);
    builder.setGpsTagRecording(gpsTagRecording[0] as GpsTagRecordingEnum);
    builder.setIso(iso[0] as IsoEnum);
    builder.setIsoAutoHighLimit(isoAutoHighLimit[0] as IsoAutoHighLimitEnum);
    builder.setPreset(preset[0] as PresetEnum);
    builder.setWhiteBalance(whiteBalance[0] as WhiteBalanceEnum);
    builder.setCheckStatusCommandInterval(100);

    var capture = await builder.build();
    expect(capture, isNotNull);
    expect(capture.getAperture(), aperture[0]);
    expect(capture.getColorTemperature(), colorTemperature[0]);
    expect(capture.getExposureCompensation(), exposureCompensation[0]);
    expect(capture.getExposureDelay(), exposureDelay[0]);
    expect(capture.getExposureProgram(), exposureProgram[0]);
    expect(capture.getFileFormat(), fileFormat[0]);
    expect(capture.getFilter(), filter[0]);
    expect(capture.getGpsInfo(), gpsInfo[0]);
    expect(capture.getGpsTagRecording(), gpsTagRecording[0]);
    expect(capture.getIso(), iso[0]);
    expect(capture.getIsoAutoHighLimit(), isoAutoHighLimit[0]);
    expect(capture.getPreset(), preset[0]);
    expect(capture.getWhiteBalance(), whiteBalance[0]);
    expect(capture.getCheckStatusCommandInterval(), 100);
  });

  test('takePicture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrl = 'http://test.jpg';

    onCallGetPhotoCaptureBuilder = Future.value;
    onCallBuildPhotoCapture = (options, interval) => Future.value();
    onCallTakePicture = (onCapturing) {
      return Future.value(imageUrl);
    };

    var builder = thetaClientPlugin.getPhotoCaptureBuilder();
    var capture = await builder.build();
    String? fileUrl;
    capture.takePicture((value) {
      expect(value, imageUrl);
      fileUrl = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. takePicture');
    });
    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrl, imageUrl);
    expect(capture.getAperture(), isNull);
  });

  test('takePicture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetPhotoCaptureBuilder = Future.value;
    onCallBuildPhotoCapture = (options, interval) => Future.value();
    onCallTakePicture = (onCapturing) {
      return Future.error(Exception('Error. takePicture'));
    };

    var builder = thetaClientPlugin.getPhotoCaptureBuilder();
    var capture = await builder.build();
    String? fileUrl;
    dynamic error;
    capture.takePicture((value) {
      fileUrl = value;
      expect(false, isTrue, reason: 'Error. takePicture');
    }, (exception) {
      error = exception;
      expect(exception, isNotNull);
    });
    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrl, isNull);
    expect(error, isNotNull);
  });

  test('call onCapturing', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var completer = Completer<void>();
    void Function(CapturingStatusEnum status)? paramOnCapturing;

    const imageUrl = 'http://test.jpg';

    onCallGetPhotoCaptureBuilder = Future.value;
    onCallBuildPhotoCapture = (options, interval) => Future.value();
    onCallTakePicture = (onCapturing) {
      paramOnCapturing = onCapturing;
      return Future.value(imageUrl);
    };

    var builder = thetaClientPlugin.getPhotoCaptureBuilder();
    var isOnCapturing = false;
    var capture = await builder.build();
    String? fileUrl;
    capture.takePicture((value) {
      expect(value, imageUrl);
      fileUrl = value;
      completer.complete(null);
    }, (exception) {
      expect(false, isTrue, reason: 'Error. takePicture');
    }, onCapturing: (status) {
      isOnCapturing = true;
      expect(status, CapturingStatusEnum.capturing);
    });
    paramOnCapturing?.call(CapturingStatusEnum.capturing);

    await Future.delayed(const Duration(milliseconds: 10), () {});
    expect(fileUrl, imageUrl);
    expect(isOnCapturing, isTrue);
  });
}
