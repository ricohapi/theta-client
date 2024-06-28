import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_method_channel.dart';

void main() {
  MethodChannelThetaClientFlutter platform = MethodChannelThetaClientFlutter();
  const MethodChannel channel = MethodChannel('theta_client_flutter');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    platform = MethodChannelThetaClientFlutter();
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(
      channel,
      null,
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(
      channel,
      null,
    );
  });

  test('buildContinuousCapture', () async {
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
    ];

    Map<String, dynamic> options = {};
    for (int i = 0; i < data.length; i++) {
      options[data[i][0]] = data[i][1];
    }

    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['_capture_interval'], 1);
      for (int i = 0; i < data.length; i++) {
        expect(arguments[data[i][0]], data[i][2], reason: data[i][0]);
      }
      return Future.value();
    });
    await platform.buildContinuousCapture(options, 1);
  });

  test('startContinuousCapture', () async {
    const fileUrls = [
      'http://192.168.1.1/files/150100624436344d4201375fda9dc400/100RICOH/R0013336.MP4'
    ];
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      return fileUrls;
    });
    expect(await platform.startContinuousCapture(null, null), fileUrls);
  });

  test('startContinuousCapture no file', () async {
    const fileUrls = null;
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      return fileUrls;
    });
    expect(await platform.startContinuousCapture(null, null), null);
  });

  test('startContinuousCapture exception', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      throw Exception('test error');
    });
    try {
      await platform.startContinuousCapture(null, null);
      expect(true, false, reason: 'not exception');
    } catch (error) {
      expect(error.toString().contains('test error'), true);
    }
  });

  test('progress of capture', () async {
    const fileUrls = [
      'http://192.168.1.1/files/150100624436344d4201375fda9dc400/100RICOH/R0013336.MP4'
    ];
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10061), true,
          reason: 'add notify progress');

      // native event
      platform.onNotify({
        'id': 10061,
        'params': {
          'completion': 0.1,
        },
      });
      await Future.delayed(const Duration(milliseconds: 10));
      platform.onNotify({
        'id': 10061,
        'params': {
          'completion': 0.2,
        },
      });
      await Future.delayed(const Duration(milliseconds: 10));

      return fileUrls;
    });

    int progressCount = 0;
    var resultCapture = platform.startContinuousCapture((completion) {
      progressCount++;
    }, null);
    var result = await resultCapture.timeout(const Duration(seconds: 5));
    expect(result, fileUrls);
    expect(progressCount, 2);
    expect(platform.notifyList.containsKey(10061), false,
        reason: 'remove notify progress');
  });

  test('call onCapturing', () async {
    const fileUrls = [
      'http://192.168.1.1/files/150100624436344d4201375fda9dc400/100RICOH/R0013336.MP4'
    ];
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10062), true,
          reason: 'add notify capturing status');

      // native event
      platform.onNotify({
        'id': 10062,
        'params': {
          'status': 'SELF_TIMER_COUNTDOWN',
        },
      });
      return fileUrls;
    });

    CapturingStatusEnum? lastStatus;
    var resultCapture =
    platform.startContinuousCapture((completion) {}, (status) {
      lastStatus = status;
    });
    var result = await resultCapture.timeout(const Duration(seconds: 5));
    expect(result, fileUrls);
    expect(lastStatus, CapturingStatusEnum.selfTimerCountdown);
    expect(platform.notifyList.containsKey(10062), false,
        reason: 'remove notify capturing status');
  });
}
