import { WlanAntennaConfigEnum } from '../../theta-repository/options/option-wlan-antenna-config';

describe('WlanAntennaConfigEnum', () => {
  const data: string[][] = [
    [WlanAntennaConfigEnum.UNKNOWN, 'UNKNOWN'],
    [WlanAntennaConfigEnum.SISO, 'SISO'],
    [WlanAntennaConfigEnum.MIMO, 'MIMO'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(WlanAntennaConfigEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
