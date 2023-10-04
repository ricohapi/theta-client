import { TopBottomCorrectionOptionEnum } from '../../theta-repository/options/option-top-bottom-correction';

describe('TopBottomCorrectionOptionEnum', () => {
  const data: [TopBottomCorrectionOptionEnum, string][] = [
    [TopBottomCorrectionOptionEnum.APPLY, 'APPLY'],
    [TopBottomCorrectionOptionEnum.APPLY_AUTO, 'APPLY_AUTO'],
    [TopBottomCorrectionOptionEnum.APPLY_SEMIAUTO, 'APPLY_SEMIAUTO'],
    [TopBottomCorrectionOptionEnum.APPLY_SAVE, 'APPLY_SAVE'],
    [TopBottomCorrectionOptionEnum.APPLY_LOAD, 'APPLY_LOAD'],
    [TopBottomCorrectionOptionEnum.DISAPPLY, 'DISAPPLY'],
    [TopBottomCorrectionOptionEnum.MANUAL, 'MANUAL'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(TopBottomCorrectionOptionEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
