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

  test('buildMultiBracketCapture', () async {
    Map<String, dynamic> settingMap1 = {
      'aperture': 'APERTURE_AUTO',
      'colorTemperature': 5000,
      'exposureCompensation': 'P0_7',
      'exposureProgram': 'NORMAL_PROGRAM',
      'iso': 'ISO_100',
      'shutterSpeed': 'SHUTTER_SPEED_AUTO',
      'whiteBalance': 'DAYLIGHT'
    };
    Map<String, dynamic> settingMap2 = {
      'colorTemperature': 6000,
    };
    List<Map<String, dynamic>> settingList = [settingMap1, settingMap2];
    List<List<dynamic>> data = [
      [
        'AutoBracket',
        [
          BracketSetting(
              aperture: ApertureEnum.apertureAuto,
              colorTemperature: 5000,
              exposureCompensation: ExposureCompensationEnum.p0_7,
              exposureProgram: ExposureProgramEnum.normalProgram,
              iso: IsoEnum.iso100,
              shutterSpeed: ShutterSpeedEnum.shutterSpeedAuto,
              whiteBalance: WhiteBalanceEnum.daylight),
          BracketSetting(colorTemperature: 6000)
        ],
        settingList
      ]
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
    await platform.buildMultiBracketCapture(options, 1);
  });

  test('startMultiBracketCapture', () async {
    const fileUrls = [
      'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4'
    ];
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      return fileUrls;
    });
    expect(await platform.startMultiBracketCapture(null, null, null), fileUrls);
  });

  test('startMultiBracketCapture no file', () async {
    const fileUrls = null;
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      return fileUrls;
    });
    expect(await platform.startMultiBracketCapture(null, null, null), null);
  });

  test('startMultiBracketCapture exception', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      throw Exception('test error');
    });
    try {
      await platform.startMultiBracketCapture(null, null, null);
      expect(true, false, reason: 'not exception');
    } catch (error) {
      expect(error.toString().contains('test error'), true);
    }
  });

  test('progress of capture', () async {
    const fileUrls = [
      'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4'
    ];
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10041), true,
          reason: 'add notify progress');

      // native event
      platform.onNotify({
        'id': 10041,
        'params': {
          'completion': 0.1,
        },
      });
      await Future.delayed(const Duration(milliseconds: 10));
      platform.onNotify({
        'id': 10041,
        'params': {
          'completion': 0.2,
        },
      });
      await Future.delayed(const Duration(milliseconds: 10));

      return fileUrls;
    });

    int progressCount = 0;
    var resultCapture = platform.startMultiBracketCapture((completion) {
      progressCount++;
    }, null, null);
    var result = await resultCapture.timeout(const Duration(seconds: 5));
    expect(result, fileUrls);
    expect(progressCount, 2);
    expect(platform.notifyList.containsKey(10041), false,
        reason: 'remove notify progress');
  });

  test('call onStopFailed', () async {
    const fileUrls = [
      'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4'
    ];
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10042), true,
          reason: 'add notify stop error');

      await Future.delayed(const Duration(milliseconds: 1));
      // native event
      platform.onNotify({
        'id': 10042,
        'params': {
          'message': "stop error",
        },
      });
      return fileUrls;
    });

    var isOnStopFailed = false;
    var resultCapture = platform.startMultiBracketCapture(null, (exception) {
      isOnStopFailed = true;
    }, null);
    var result = await resultCapture.timeout(const Duration(seconds: 5));
    expect(result, fileUrls);
    expect(platform.notifyList.containsKey(10042), false,
        reason: 'remove notify stop error');
    expect(isOnStopFailed, true);
  });

  test('call onCapturing', () async {
    const fileUrls = [
      'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4'
    ];
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      expect(platform.notifyList.containsKey(10043), true,
          reason: 'add notify capturing status');

      await Future.delayed(const Duration(milliseconds: 1));
      // native event
      platform.onNotify({
        'id': 10043,
        'params': {
          'status': 'SELF_TIMER_COUNTDOWN',
        },
      });
      return fileUrls;
    });

    CapturingStatusEnum? lastStatus;
    var resultCapture = platform.startMultiBracketCapture(null, null, (status) {
      lastStatus = status;
    });
    var result = await resultCapture.timeout(const Duration(seconds: 5));
    expect(result, fileUrls);
    expect(platform.notifyList.containsKey(10043), false,
        reason: 'remove notify stop error');
    expect(lastStatus, CapturingStatusEnum.selfTimerCountdown);
  });
}
