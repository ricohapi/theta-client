import { CaptureModeEnum } from '../../theta-repository/options/option-capture-mode';

describe('CaptureModeEnum', () => {
  const data: string[][] = [
    [CaptureModeEnum.IMAGE, 'IMAGE'],
    [CaptureModeEnum.VIDEO, 'VIDEO'],
    [CaptureModeEnum.LIVE_STREAMING, 'LIVE_STREAMING'],
    [CaptureModeEnum.INTERVAL, 'INTERVAL'],
    [CaptureModeEnum.PRESET, 'PRESET'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(CaptureModeEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
})
