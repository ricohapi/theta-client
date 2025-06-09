import { WlanFrequencyEnum } from '../../theta-repository/options/option-wlan-frequency';

describe('WlanFrequencyEnum', () => {
  const data: string[][] = [
    [WlanFrequencyEnum.UNKNOWN, 'UNKNOWN'],
    [WlanFrequencyEnum.GHZ_2_4, 'GHZ_2_4'],
    [WlanFrequencyEnum.GHZ_5, 'GHZ_5'],
    [WlanFrequencyEnum.GHZ_5_2, 'GHZ_5_2'],
    [WlanFrequencyEnum.GHZ_5_8, 'GHZ_5_8'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(WlanFrequencyEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
