import { CompassDirectionRefEnum } from '../../theta-repository/options/option-compass-direction-ref';

describe('CompassDirectionRefEnum', () => {
  const data: string[][] = [
    [CompassDirectionRefEnum.UNKNOWN, 'UNKNOWN'],
    [CompassDirectionRefEnum.AUTO, 'AUTO'],
    [CompassDirectionRefEnum.TRUE_NORTH, 'TRUE_NORTH'],
    [CompassDirectionRefEnum.MAGNETIC, 'MAGNETIC'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(CompassDirectionRefEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
