import { WhiteBalanceAutoStrengthEnum } from '../../theta-repository/options';

describe('WhiteBalanceAutoStrengthEnum', () => {
  const data: string[][] = [
    [WhiteBalanceAutoStrengthEnum.ON, 'ON'],
    [WhiteBalanceAutoStrengthEnum.OFF, 'OFF'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(WhiteBalanceAutoStrengthEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    })
  });
});
