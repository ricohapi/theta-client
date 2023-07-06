import { TimeShiftIntervalEnum } from '../../theta-repository/options/option-time-shift';

describe('TimeShift', () => {
  const data: string[][] = [
    [TimeShiftIntervalEnum.INTERVAL_0, 'INTERVAL_0'],
    [TimeShiftIntervalEnum.INTERVAL_1, 'INTERVAL_1'],
    [TimeShiftIntervalEnum.INTERVAL_2, 'INTERVAL_2'],
    [TimeShiftIntervalEnum.INTERVAL_3, 'INTERVAL_3'],
    [TimeShiftIntervalEnum.INTERVAL_4, 'INTERVAL_4'],
    [TimeShiftIntervalEnum.INTERVAL_5, 'INTERVAL_5'],
    [TimeShiftIntervalEnum.INTERVAL_6, 'INTERVAL_6'],
    [TimeShiftIntervalEnum.INTERVAL_7, 'INTERVAL_7'],
    [TimeShiftIntervalEnum.INTERVAL_8, 'INTERVAL_8'],
    [TimeShiftIntervalEnum.INTERVAL_9, 'INTERVAL_9'],
    [TimeShiftIntervalEnum.INTERVAL_10, 'INTERVAL_10'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(TimeShiftIntervalEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
