import { VisibilityReductionEnum } from '../../theta-repository/options/option-visibility-reduction';

describe('VisibilityReductionEnum', () => {
  const data: [VisibilityReductionEnum, string][] = [
    [VisibilityReductionEnum.ON, 'ON'],
    [VisibilityReductionEnum.OFF, 'OFF'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(VisibilityReductionEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
