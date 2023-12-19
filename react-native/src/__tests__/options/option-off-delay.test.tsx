import {
  OffDelayEnum,
  offDelayToSeconds,
} from '../../theta-repository/options';

describe('OffDelayEnum', () => {
  const data: string[][] = [
    [OffDelayEnum.DISABLE, 'DISABLE'],
    [OffDelayEnum.OFF_DELAY_5M, 'OFF_DELAY_5M'],
    [OffDelayEnum.OFF_DELAY_10M, 'OFF_DELAY_10M'],
    [OffDelayEnum.OFF_DELAY_15M, 'OFF_DELAY_15M'],
    [OffDelayEnum.OFF_DELAY_30M, 'OFF_DELAY_30M'],
  ];
  const timeData: [OffDelayEnum, number][] = [
    [OffDelayEnum.DISABLE, 0],
    [OffDelayEnum.OFF_DELAY_5M, 300],
    [OffDelayEnum.OFF_DELAY_10M, 600],
    [OffDelayEnum.OFF_DELAY_15M, 900],
    [OffDelayEnum.OFF_DELAY_30M, 1800],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(OffDelayEnum).length);
    expect(timeData.length).toBe(Object.keys(OffDelayEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
    timeData.forEach((item) => {
      expect(offDelayToSeconds(item[0])).toBe(item[1]);
    });
  });
});
