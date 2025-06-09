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

  test('NetworkTypeEnum const', () async {
    List<List<dynamic>> data = [
      [NetworkTypeEnum.unknown, 'UNKNOWN'],
      [NetworkTypeEnum.direct, 'DIRECT'],
      [NetworkTypeEnum.client, 'CLIENT'],
      [NetworkTypeEnum.ethernet, 'ETHERNET'],
      [NetworkTypeEnum.off, 'OFF'],
      [NetworkTypeEnum.lteD, 'LTE_D'],
      [NetworkTypeEnum.lteDU, 'LTE_DU'],
      [NetworkTypeEnum.lte01S, 'LTE_01S'],
      [NetworkTypeEnum.lteX3, 'LTE_X3'],
      [NetworkTypeEnum.lteP1, 'LTE_P1'],
      [NetworkTypeEnum.lteK2, 'LTE_K2'],
      [NetworkTypeEnum.lteK, 'LTE_K'],
    ];
    expect(data.length, NetworkTypeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['NetworkType'] = 'ETHERNET';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.networkType]);
    expect(options.networkType?.rawValue, 'ETHERNET', reason: 'quality');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['NetworkType'], 'LTE_X3', reason: 'quality');
      return Future.value();
    });
    final options = Options();
    options.networkType = NetworkTypeEnum.lteX3;
    await platform.setOptions(options);
  });
}
