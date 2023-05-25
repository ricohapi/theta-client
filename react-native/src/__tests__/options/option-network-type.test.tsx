import { NetworkTypeEnum } from '../../theta-repository/options/option-network-type';

describe('NetworkTypeEnum', () => {
  const data: string[][] = [
    [NetworkTypeEnum.DIRECT, 'DIRECT'],
    [NetworkTypeEnum.CLIENT, 'CLIENT'],
    [NetworkTypeEnum.ETHERNET, 'ETHERNET'],
    [NetworkTypeEnum.OFF, 'OFF'],
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
