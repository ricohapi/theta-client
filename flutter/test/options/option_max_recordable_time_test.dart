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

  test('MaxRecordableTimeEnum const', () async {
    List<List<dynamic>> data = [
      [MaxRecordableTimeEnum.unknown, 'UNKNOWN'],
      [MaxRecordableTimeEnum.time_180, 'RECORDABLE_TIME_180'],
      [MaxRecordableTimeEnum.time_300, 'RECORDABLE_TIME_300'],
      [MaxRecordableTimeEnum.time_1500, 'RECORDABLE_TIME_1500'],
      [MaxRecordableTimeEnum.time_3000, 'RECORDABLE_TIME_3000'],
      [MaxRecordableTimeEnum.time_7200, 'RECORDABLE_TIME_7200'],
      [
        MaxRecordableTimeEnum.doNotUpdateMySettingCondition,
        'DO_NOT_UPDATE_MY_SETTING_CONDITION'
      ],
    ];
    expect(data.length, MaxRecordableTimeEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['MaxRecordableTime'] = 'RECORDABLE_TIME_180';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.maxRecordableTime]);
    expect(options.maxRecordableTime?.rawValue, 'RECORDABLE_TIME_180', reason: 'maxRecordableTime');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['MaxRecordableTime'], 'RECORDABLE_TIME_180', reason: 'maxRecordableTime');
      return Future.value();
    });
    final options = Options();
    options.maxRecordableTime = MaxRecordableTimeEnum.time_180;
    await platform.setOptions(options);
  });
}
