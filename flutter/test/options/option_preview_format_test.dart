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

  test('PreviewFormatEnum const', () async {
    List<List<dynamic>> data = [
      [PreviewFormatEnum.unknown, 'UNKNOWN'],
      [PreviewFormatEnum.w1024_h512_f30, 'W1024_H512_F30'],
      [PreviewFormatEnum.w1920_h960_f8, 'W1920_H960_F8'],
      [PreviewFormatEnum.w1024_h512_f30, 'W1024_H512_F30'],
      [PreviewFormatEnum.w1024_h512_f15, 'W1024_H512_F15'],
      [PreviewFormatEnum.w1024_h512_f10, 'W1024_H512_F10'],
      [PreviewFormatEnum.w1024_h512_f8, 'W1024_H512_F8'],
      [PreviewFormatEnum.w640_h320_f30, 'W640_H320_F30'],
      [PreviewFormatEnum.w640_h320_f10, 'W640_H320_F10'],
      [PreviewFormatEnum.w640_h320_f8, 'W640_H320_F8'],
      [PreviewFormatEnum.w512_h512_f30, 'W512_H512_F30'],
      [PreviewFormatEnum.w3840_h1920_f30, 'W3840_H1920_F30'],
    ];
    expect(data.length, PreviewFormatEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['PreviewFormat'] = 'W1024_H512_F10';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.previewFormat]);
    expect(options.previewFormat?.rawValue, 'W1024_H512_F10',
        reason: 'quality');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['PreviewFormat'], 'W1920_H960_F8', reason: 'quality');
      return Future.value();
    });
    final options = Options();
    options.previewFormat = PreviewFormatEnum.w1920_h960_f8;
    await platform.setOptions(options);
  });
}
