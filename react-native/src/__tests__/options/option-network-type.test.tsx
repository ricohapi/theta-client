import { NetworkTypeEnum } from '../../theta-repository/options/option-network-type';

describe('NetworkTypeEnum', () => {
  const data: string[][] = [
    [NetworkTypeEnum.UNKNOWN, 'UNKNOWN'],
    [NetworkTypeEnum.DIRECT, 'DIRECT'],
    [NetworkTypeEnum.CLIENT, 'CLIENT'],
    [NetworkTypeEnum.ETHERNET, 'ETHERNET'],
    [NetworkTypeEnum.OFF, 'OFF'],
    [NetworkTypeEnum.LTE_D, 'LTE_D'],
    [NetworkTypeEnum.LTE_DU, 'LTE_DU'],
    [NetworkTypeEnum.LTE_01S, 'LTE_01S'],
    [NetworkTypeEnum.LTE_X3, 'LTE_X3'],
    [NetworkTypeEnum.LTE_P1, 'LTE_P1'],
    [NetworkTypeEnum.LTE_K2, 'LTE_K2'],
    [NetworkTypeEnum.LTE_K, 'LTE_K'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(NetworkTypeEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
