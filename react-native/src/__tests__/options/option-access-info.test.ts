import { WlanFrequencyAccessInfoEnum } from '../../theta-repository/options/option-access-info';

describe('WlanFrequencyAccessInfoEnum', () => {
  const data: string[][] = [
    [WlanFrequencyAccessInfoEnum.UNKNOWN, 'UNKNOWN'],
    [WlanFrequencyAccessInfoEnum.GHZ_2_4, 'GHZ_2_4'],
    [WlanFrequencyAccessInfoEnum.GHZ_5_2, 'GHZ_5_2'],
    [WlanFrequencyAccessInfoEnum.GHZ_5_8, 'GHZ_5_8'],
    [WlanFrequencyAccessInfoEnum.INITIAL_VALUE, 'INITIAL_VALUE'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(WlanFrequencyAccessInfoEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
