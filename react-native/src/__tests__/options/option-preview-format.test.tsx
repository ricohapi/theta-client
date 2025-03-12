import { PreviewFormatEnum } from '../../theta-repository/options/option-preview-format';

describe('PreviewFormatEnum', () => {
  const data: string[][] = [
    [PreviewFormatEnum.W1024_H512_F30, 'W1024_H512_F30'],
    [PreviewFormatEnum.W1024_H512_F15, 'W1024_H512_F15'],
    [PreviewFormatEnum.W512_H512_F30, 'W512_H512_F30'],
    [PreviewFormatEnum.W1920_H960_F8, 'W1920_H960_F8'],
    [PreviewFormatEnum.W1920_H960_F30, 'W1920_H960_F30'],
    [PreviewFormatEnum.W1024_H512_F8, 'W1024_H512_F8'],
    [PreviewFormatEnum.W640_H320_F30, 'W640_H320_F30'],
    [PreviewFormatEnum.W640_H320_F8, 'W640_H320_F8'],
    [PreviewFormatEnum.W640_H320_F10, 'W640_H320_F10'],
    [PreviewFormatEnum.W3840_H1920_F30, 'W3840_H1920_F30'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(PreviewFormatEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
