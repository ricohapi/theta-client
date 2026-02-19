import { ModeMemoryEnum } from '../../theta-repository/options/option-mode-memory';

describe('ModeMemoryEnum', () => {
  const data: [ModeMemoryEnum, string][] = [
    [ModeMemoryEnum.UNKNOWN, 'UNKNOWN'],
    [ModeMemoryEnum.ON, 'ON'],
    [ModeMemoryEnum.OFF, 'OFF'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(ModeMemoryEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
