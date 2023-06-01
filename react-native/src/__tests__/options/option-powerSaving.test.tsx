import { PowerSavingEnum } from '../../theta-repository/options/option-powerSaving';

describe('PowerSavingEnum', () => {
  const data: string[][] = [
    [PowerSavingEnum.ON, 'ON'],
    [PowerSavingEnum.OFF, 'OFF'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(PowerSavingEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
})
