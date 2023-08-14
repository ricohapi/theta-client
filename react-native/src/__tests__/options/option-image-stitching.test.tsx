import { ImageStitchingEnum } from '../../theta-repository/options/option-image-stitching';

describe('ImageStitchingEnum', () => {
  const data: string[][] = [
    [ImageStitchingEnum.AUTO, 'AUTO'],
    [ImageStitchingEnum.STATIC, 'STATIC'],
    [ImageStitchingEnum.DYNAMIC, 'DYNAMIC'],
    [ImageStitchingEnum.DYNAMIC_AUTO, 'DYNAMIC_AUTO'],
    [ImageStitchingEnum.DYNAMIC_SEMI_AUTO, 'DYNAMIC_SEMI_AUTO'],
    [ImageStitchingEnum.DYNAMIC_SAVE, 'DYNAMIC_SAVE'],
    [ImageStitchingEnum.DYNAMIC_LOAD, 'DYNAMIC_LOAD'],
    [ImageStitchingEnum.NONE, 'NONE'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(ImageStitchingEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
