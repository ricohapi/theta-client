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

  test('buildVideoCapture', () async {
    Map<String, dynamic> gpsInfoMap = {
      'latitude': 1.0,
      'longitude': 2.0,
      'altitude': 3.0,
      'dateTimeZone': '2022:01:01 00:01:00+09:00'
    };
    List<List<dynamic>> data = [
      ['Aperture', ApertureEnum.aperture_2_0, 'APERTURE_2_0'],
      ['ColorTemperature', 2, 2],
      ['ExposureCompensation', ExposureCompensationEnum.m0_3, 'M0_3'],
      ['ExposureDelay', ExposureDelayEnum.delay1, 'DELAY_1'],
      [
        'ExposureProgram',
        ExposureProgramEnum.aperturePriority,
        'APERTURE_PRIORITY'
      ],
      [
        'GpsInfo',
        GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'),
        gpsInfoMap
      ],
      ['GpsTagRecording', GpsTagRecordingEnum.on, 'ON'],
      ['Iso', IsoEnum.iso50, 'ISO_50'],
      ['IsoAutoHighLimit', IsoAutoHighLimitEnum.iso200, 'ISO_200'],
      ['WhiteBalance', WhiteBalanceEnum.bulbFluorescent, 'BULB_FLUORESCENT'],
      [
        'MaxRecordableTime',
        MaxRecordableTimeEnum.time_1500,
        'RECORDABLE_TIME_1500'
      ],
      ['VideoFileFormat', VideoFileFormatEnum.videoFullHD, 'VIDEO_FULL_HD'],
    ];

    Map<String, dynamic> options = {};
    for (int i = 0; i < data.length; i++) {
      options[data[i][0]] = data[i][1];
    }

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      for (int i = 0; i < data.length; i++) {
        expect(arguments[data[i][0]], data[i][2], reason: data[i][0]);
      }

      return Future.value();
    });
    await platform.buildVideoCapture(options, 1);
  });

  test('startVideoCapture', () async {
    const fileUrl =
        'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4';
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      return fileUrl;
    });
    expect(await platform.startVideoCapture(null, null, null), fileUrl);
  });

  test('call onStopFailed', () async {
    const fileUrl =
        'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4';
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10081), true,
          reason: 'add notify stop error');

      // native event
      platform.onNotify({
        'id': 10081,
        'params': {
          'message': "stop error",
        },
      });

      return fileUrl;
    });

    var isOnStopFailed = false;
    expect(
        await platform.startVideoCapture((exception) {
          isOnStopFailed = true;
        }, null, null),
        fileUrl);
    expect(platform.notifyList.containsKey(10081), false,
        reason: 'remove notify stop error');
    expect(isOnStopFailed, true);
  });

  test('call onCapturing', () async {
    const fileUrl =
        'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4';
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10082), true,
          reason: 'add notify capturing status');

      // native event
      platform.onNotify({
        'id': 10082,
        'params': {
          'status': 'SELF_TIMER_COUNTDOWN',
        },
      });

      return fileUrl;
    });

    CapturingStatusEnum? lastStatus;
    expect(
        await platform.startVideoCapture(null, (status) {
          lastStatus = status;
        }, null),
        fileUrl);
    expect(platform.notifyList.containsKey(10082), false,
        reason: 'remove notify capturing status');
    expect(lastStatus, CapturingStatusEnum.selfTimerCountdown);
  });

  test('call onCaptureStarted', () async {
    const fileUrl =
        'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4';
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10083), true,
          reason: 'add notify stop error');

      // native event
      platform.onNotify({
        'id': 10083,
        'params': {
          'fileUrl': "xxx.mp4",
        },
      });

      return fileUrl;
    });

    String? thisTimeFileUrl;
    expect(
        await platform.startVideoCapture(null, null, (fileUrl) {
          thisTimeFileUrl = fileUrl;
        }),
        fileUrl);
    expect(platform.notifyList.containsKey(10083), true,
        reason: 'capture started');
    expect(thisTimeFileUrl, "xxx.mp4");
  });
}
