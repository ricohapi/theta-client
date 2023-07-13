import { ContinuousNumberEnum } from '../../theta-repository/options/option-continuous-number';

describe('BurstModeEnum', () => {
  const data: [ContinuousNumberEnum, string][] = [
    [ContinuousNumberEnum.OFF, 'OFF'],
    [ContinuousNumberEnum.MAX_1, 'MAX_1'],
    [ContinuousNumberEnum.MAX_2, 'MAX_2'],
    [ContinuousNumberEnum.MAX_3, 'MAX_3'],
    [ContinuousNumberEnum.MAX_4, 'MAX_4'],
    [ContinuousNumberEnum.MAX_5, 'MAX_5'],
    [ContinuousNumberEnum.MAX_6, 'MAX_6'],
    [ContinuousNumberEnum.MAX_7, 'MAX_7'],
    [ContinuousNumberEnum.MAX_8, 'MAX_8'],
    [ContinuousNumberEnum.MAX_9, 'MAX_9'],
    [ContinuousNumberEnum.MAX_10, 'MAX_10'],
    [ContinuousNumberEnum.MAX_11, 'MAX_11'],
    [ContinuousNumberEnum.MAX_12, 'MAX_12'],
    [ContinuousNumberEnum.MAX_13, 'MAX_13'],
    [ContinuousNumberEnum.MAX_14, 'MAX_14'],
    [ContinuousNumberEnum.MAX_15, 'MAX_15'],
    [ContinuousNumberEnum.MAX_16, 'MAX_16'],
    [ContinuousNumberEnum.MAX_17, 'MAX_17'],
    [ContinuousNumberEnum.MAX_18, 'MAX_18'],
    [ContinuousNumberEnum.MAX_19, 'MAX_19'],
    [ContinuousNumberEnum.MAX_20, 'MAX_20'],
    [ContinuousNumberEnum.UNSUPPORTED, 'UNSUPPORTED'],
  ];

  test('data', () => {
    expect(data.length).toBe(Object.keys(ContinuousNumberEnum).length);

    data.forEach((item) => {
      expect(item[0].toString()).toBe(item[1]);
    });
  });
});
