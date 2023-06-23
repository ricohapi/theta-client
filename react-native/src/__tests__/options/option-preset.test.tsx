import { PresetEnum } from '../../theta-repository/options/option-preset';

describe('PresetEnum', () => {
  const data: string[][] = [
    [PresetEnum.FACE, 'FACE'],
    [PresetEnum.NIGHT_VIEW, 'NIGHT_VIEW'],
    [PresetEnum.LENS_BY_LENS_EXPOSURE, 'LENS_BY_LENS_EXPOSURE'],
    [PresetEnum.ROOM, 'ROOM'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(PresetEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
})
