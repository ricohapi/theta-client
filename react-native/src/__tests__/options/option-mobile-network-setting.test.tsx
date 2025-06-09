import { PlanEnum, RoamingEnum } from '../../theta-repository/options';

describe('RoamingEnum', () => {
  const data: string[][] = [
    [RoamingEnum.UNKNOWN, 'UNKNOWN'],
    [RoamingEnum.OFF, 'OFF'],
    [RoamingEnum.ON, 'ON'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(RoamingEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});

describe('PlanEnum', () => {
  const data: string[][] = [
    [PlanEnum.UNKNOWN, 'UNKNOWN'],
    [PlanEnum.SORACOM, 'SORACOM'],
    [PlanEnum.SORACOM_PLAN_DU, 'SORACOM_PLAN_DU'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(PlanEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
