import { GainEnum } from '../../theta-repository/options/option-gain';

describe('GainEnum', () => {
  const data: string[][] = [
    [GainEnum.NORMAL, 'NORMAL'],
    [GainEnum.MEGA_VOLUME, 'MEGA_VOLUME'],
    [GainEnum.MUTE, 'MUTE'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(GainEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
