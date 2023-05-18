import { CameraModeEnum } from '../../theta-repository/options/option-camera-mode';

describe('CameraModeEnum', () => {
  const data: string[][] = [
    [CameraModeEnum.CAPTURE, 'CAPTURE'],
    [CameraModeEnum.PLAYBACK, 'PLAYBACK'],
    [CameraModeEnum.SETTING, 'SETTING'],
    [CameraModeEnum.PLUGIN, 'PLUGIN'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(CameraModeEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
