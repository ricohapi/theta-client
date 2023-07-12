import { BitrateEnum } from '../../theta-repository/options/option-bitrate';

describe('BitrateEnum', () => {
  const data: [BitrateEnum, string][] = [
    [BitrateEnum.AUTO, 'AUTO'],
    [BitrateEnum.FINE, 'FINE'],
    [BitrateEnum.NORMAL, 'NORMAL'],
    [BitrateEnum.ECONOMY, 'ECONOMY'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BitrateEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
