import {
  SleepDelayEnum,
  sleepDelayToSeconds,
} from '../../theta-repository/options';

describe('SleepDelayEnum', () => {
  const data: string[][] = [
    [SleepDelayEnum.DISABLE, 'DISABLE'],
    [SleepDelayEnum.SLEEP_DELAY_3M, 'SLEEP_DELAY_3M'],
    [SleepDelayEnum.SLEEP_DELAY_5M, 'SLEEP_DELAY_5M'],
    [SleepDelayEnum.SLEEP_DELAY_7M, 'SLEEP_DELAY_7M'],
    [SleepDelayEnum.SLEEP_DELAY_10M, 'SLEEP_DELAY_10M'],
  ];
  const timeData: [SleepDelayEnum, number][] = [
    [SleepDelayEnum.DISABLE, 0],
    [SleepDelayEnum.SLEEP_DELAY_3M, 180],
    [SleepDelayEnum.SLEEP_DELAY_5M, 300],
    [SleepDelayEnum.SLEEP_DELAY_7M, 420],
    [SleepDelayEnum.SLEEP_DELAY_10M, 600],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(SleepDelayEnum).length);
    expect(timeData.length).toBe(Object.keys(SleepDelayEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
    timeData.forEach((item) => {
      expect(sleepDelayToSeconds(item[0])).toBe(item[1]);
    });
  });
});
