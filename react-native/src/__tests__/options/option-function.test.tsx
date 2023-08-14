import { ShootingFunctionEnum } from '../../theta-repository/options/option-function';

describe('ShootingFunctionEnum', () => {
  const data: string[][] = [
    [ShootingFunctionEnum.NORMAL, 'NORMAL'],
    [ShootingFunctionEnum.SELF_TIMER, 'SELF_TIMER'],
    [ShootingFunctionEnum.MY_SETTING, 'MY_SETTING'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(ShootingFunctionEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
