import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

void main() {
  setUp(() {});

  tearDown(() {});

  test('CaptureStatusEnum', () async {
    List<List<dynamic>> data = [
      [CaptureStatusEnum.unknown, 'UNKNOWN'],
      [CaptureStatusEnum.shooting, 'SHOOTING'],
      [CaptureStatusEnum.idle, 'IDLE'],
      [CaptureStatusEnum.selfTimerCountdown, 'SELF_TIMER_COUNTDOWN'],
      [CaptureStatusEnum.bracketShooting, 'BRACKET_SHOOTING'],
      [CaptureStatusEnum.converting, 'CONVERTING'],
      [CaptureStatusEnum.timeShiftShooting, 'TIME_SHIFT_SHOOTING'],
      [CaptureStatusEnum.continuousShooting, 'CONTINUOUS_SHOOTING'],
      [
        CaptureStatusEnum.retrospectiveImageRecording,
        'RETROSPECTIVE_IMAGE_RECORDING'
      ],
      [CaptureStatusEnum.burstShooting, 'BURST_SHOOTING'],
    ];
    expect(data.length, CaptureStatusEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });
}
