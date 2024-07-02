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

  test('FileFormatEnum const', () async {
    List<List<dynamic>> data = [
      [FileFormatEnum.unknown, 'UNKNOWN'],
      [FileFormatEnum.image_2K, 'IMAGE_2K'],
      [FileFormatEnum.image_5K, 'IMAGE_5K'],
      [FileFormatEnum.image_6_7K, 'IMAGE_6_7K'],
      [FileFormatEnum.rawP_6_7K, 'RAW_P_6_7K'],
      [FileFormatEnum.image_5_5K, 'IMAGE_5_5K'],
      [FileFormatEnum.image_11K, 'IMAGE_11K'],
      [FileFormatEnum.videoHD, 'VIDEO_HD'],
      [FileFormatEnum.videoFullHD, 'VIDEO_FULL_HD'],
      [FileFormatEnum.video_2K, 'VIDEO_2K'],
      [FileFormatEnum.video_2KnoCodec, 'VIDEO_2K_NO_CODEC'],
      [FileFormatEnum.video_2_7K_1F, 'VIDEO_2_7K_1F'],
      [FileFormatEnum.video_2_7K_2F, 'VIDEO_2_7K_2F'],
      [FileFormatEnum.video_3_6K_1F, 'VIDEO_3_6K_1F'],
      [FileFormatEnum.video_3_6K_2F, 'VIDEO_3_6K_2F'],
      [FileFormatEnum.video_4K, 'VIDEO_4K'],
      [FileFormatEnum.video_4KnoCodec, 'VIDEO_4K_NO_CODEC'],
      [FileFormatEnum.video_2K_30F, 'VIDEO_2K_30F'],
      [FileFormatEnum.video_2K_60F, 'VIDEO_2K_60F'],
      [FileFormatEnum.video_2_7K_2752_2F, 'VIDEO_2_7K_2752_2F'],
      [FileFormatEnum.video_2_7K_2752_5F, 'VIDEO_2_7K_2752_5F'],
      [FileFormatEnum.video_2_7K_2752_10F, 'VIDEO_2_7K_2752_10F'],
      [FileFormatEnum.video_2_7K_2752_30F, 'VIDEO_2_7K_2752_30F'],
      [FileFormatEnum.video_4K_10F, 'VIDEO_4K_10F'],
      [FileFormatEnum.video_4K_15F, 'VIDEO_4K_15F'],
      [FileFormatEnum.video_4K_30F, 'VIDEO_4K_30F'],
      [FileFormatEnum.video_4K_60F, 'VIDEO_4K_60F'],
      [FileFormatEnum.video_5_7K_2F, 'VIDEO_5_7K_2F'],
      [FileFormatEnum.video_5_7K_5F, 'VIDEO_5_7K_5F'],
      [FileFormatEnum.video_5_7K_10F, 'VIDEO_5_7K_10F'],
      [FileFormatEnum.video_5_7K_15F, 'VIDEO_5_7K_15F'],
      [FileFormatEnum.video_5_7K_30F, 'VIDEO_5_7K_30F'],
      [FileFormatEnum.video_7K_2F, 'VIDEO_7K_2F'],
      [FileFormatEnum.video_7K_5F, 'VIDEO_7K_5F'],
      [FileFormatEnum.video_7K_10F, 'VIDEO_7K_10F'],
    ];
    expect(data.length, FileFormatEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('getOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      Map<String, dynamic> optionMap = {};
      optionMap['FileFormat'] = 'IMAGE_2K';
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions([OptionNameEnum.bitrate]);
    expect(options.fileFormat?.rawValue, 'IMAGE_2K', reason: 'fileFormat');
  });

  test('setOptions', () async {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger
        .setMockMethodCallHandler(channel, (MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['FileFormat'], 'VIDEO_2K', reason: 'fileFormat');
      return Future.value();
    });
    final options = Options();
    options.fileFormat = FileFormatEnum.video_2K;
    await platform.setOptions(options);
  });
}
