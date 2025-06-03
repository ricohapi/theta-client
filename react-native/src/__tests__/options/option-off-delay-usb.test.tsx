import {
  OffDelayUsbEnum,
  offDelayUsbToSeconds,
} from '../../theta-repository/options';

describe('OffDelayUsbEnum', () => {
  const data: string[][] = [
    [OffDelayUsbEnum.DISABLE, 'DISABLE'],
    [OffDelayUsbEnum.OFF_DELAY_10M, 'OFF_DELAY_10M'],
    [OffDelayUsbEnum.OFF_DELAY_1H, 'OFF_DELAY_1H'],
    [OffDelayUsbEnum.OFF_DELAY_2H, 'OFF_DELAY_2H'],
    [OffDelayUsbEnum.OFF_DELAY_4H, 'OFF_DELAY_4H'],
    [OffDelayUsbEnum.OFF_DELAY_8H, 'OFF_DELAY_8H'],
    [OffDelayUsbEnum.OFF_DELAY_12H, 'OFF_DELAY_12H'],
    [OffDelayUsbEnum.OFF_DELAY_18H, 'OFF_DELAY_18H'],
    [OffDelayUsbEnum.OFF_DELAY_24H, 'OFF_DELAY_24H'],
    [OffDelayUsbEnum.OFF_DELAY_2D, 'OFF_DELAY_2D'],
  ];
  const timeData: [OffDelayUsbEnum, number][] = [
    [OffDelayUsbEnum.DISABLE, 0],
    [OffDelayUsbEnum.OFF_DELAY_10M, 60 * 10],
    [OffDelayUsbEnum.OFF_DELAY_1H, 60 * 60],
    [OffDelayUsbEnum.OFF_DELAY_2H, 60 * 60 * 2],
    [OffDelayUsbEnum.OFF_DELAY_4H, 60 * 60 * 4],
    [OffDelayUsbEnum.OFF_DELAY_8H, 60 * 60 * 8],
    [OffDelayUsbEnum.OFF_DELAY_12H, 60 * 60 * 12],
    [OffDelayUsbEnum.OFF_DELAY_18H, 60 * 60 * 18],
    [OffDelayUsbEnum.OFF_DELAY_24H, 60 * 60 * 24],
    [OffDelayUsbEnum.OFF_DELAY_2D, 60 * 60 * 24 * 2],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(OffDelayUsbEnum).length);
    expect(timeData.length).toBe(Object.keys(OffDelayUsbEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
    timeData.forEach((item) => {
      expect(offDelayUsbToSeconds(item[0])).toBe(item[1]);
    });
  });
});
