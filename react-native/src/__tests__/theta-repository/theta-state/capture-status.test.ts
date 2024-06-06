import { CaptureStatusEnum } from '../../../theta-repository/theta-state';

describe('CaptureStatusEnum', () => {
  const data: [CaptureStatusEnum, string][] = [
    [CaptureStatusEnum.UNKNOWN, 'UNKNOWN'],
    [CaptureStatusEnum.SHOOTING, 'SHOOTING'],
    [CaptureStatusEnum.IDLE, 'IDLE'],
    [CaptureStatusEnum.SELF_TIMER_COUNTDOWN, 'SELF_TIMER_COUNTDOWN'],
    [CaptureStatusEnum.BRACKET_SHOOTING, 'BRACKET_SHOOTING'],
    [CaptureStatusEnum.CONVERTING, 'CONVERTING'],
    [CaptureStatusEnum.TIME_SHIFT_SHOOTING, 'TIME_SHIFT_SHOOTING'],
    [CaptureStatusEnum.CONTINUOUS_SHOOTING, 'CONTINUOUS_SHOOTING'],
    [
      CaptureStatusEnum.RETROSPECTIVE_IMAGE_RECORDING,
      'RETROSPECTIVE_IMAGE_RECORDING',
    ],
    [CaptureStatusEnum.BURST_SHOOTING, 'BURST_SHOOTING'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(CaptureStatusEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
