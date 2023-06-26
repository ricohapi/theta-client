import { FilterEnum } from '../../theta-repository/options/option-filter';

describe('FilterEnum', () => {
  const data: [FilterEnum, string][] = [
    [FilterEnum.OFF, 'OFF'],
    [FilterEnum.DR_COMP, 'DR_COMP'],
    [FilterEnum.NOISE_REDUCTION, 'NOISE_REDUCTION'],
    [FilterEnum.HDR, 'HDR'],
    [FilterEnum.HH_HDR, 'HH_HDR'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(FilterEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
