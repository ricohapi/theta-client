import { BurstModeEnum } from '../../theta-repository/options/option-burst-mode';

describe('BurstModeEnum', () => {
  const data: string[][] = [
    [BurstModeEnum.ON, 'ON'],
    [BurstModeEnum.OFF, 'OFF'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BurstModeEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
