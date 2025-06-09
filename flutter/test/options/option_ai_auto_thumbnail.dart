import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

void main() {
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

  test('AiAutoThumbnailEnum const', () async {
    List<List<dynamic>> data = [
      [AiAutoThumbnailEnum.unknown, 'UNKNOWN'],
      [AiAutoThumbnailEnum.on, 'ON'],
      [AiAutoThumbnailEnum.off, 'OFF'],
    ];
    expect(data.length, AiAutoThumbnailEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });
}
