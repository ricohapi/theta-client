import { CameraPowerEnum } from '../../theta-repository/options/option-camera-power';

describe('CameraPowerEnum', () => {
  const data: string[][] = [
    [CameraPowerEnum.UNKNOWN, 'UNKNOWN'],
    [CameraPowerEnum.ON, 'ON'],
    [CameraPowerEnum.OFF, 'OFF'],
    [CameraPowerEnum.POWER_SAVING, 'POWER_SAVING'],
    [CameraPowerEnum.SILENT_MODE, 'SILENT_MODE'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(CameraPowerEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
