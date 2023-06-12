import { ShootingMethodEnum } from '../../theta-repository/options/option-shooting-method';

describe('ShootingMethodEnum', () => {
  const data: string[][] = [
    [ShootingMethodEnum.NORMAL, 'NORMAL'],
    [ShootingMethodEnum.INTERVAL, 'INTERVAL'],
    [ShootingMethodEnum.MOVE_INTERVAL, 'MOVE_INTERVAL'],
    [ShootingMethodEnum.FIXED_INTERVAL, 'FIXED_INTERVAL'],
    [ShootingMethodEnum.BRACKET, 'BRACKET'],
    [ShootingMethodEnum.COMPOSITE, 'COMPOSITE'],
    [ShootingMethodEnum.CONTINUOUS, 'CONTINUOUS'],
    [ShootingMethodEnum.TIME_SHIFT, 'TIME_SHIFT'],
    [ShootingMethodEnum.BURST, 'BURST'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(ShootingMethodEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
})
