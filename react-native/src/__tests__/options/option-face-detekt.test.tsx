import { FaceDetectEnum } from '../../theta-repository/options/option-face-detect';

describe('FaceDetectEnum', () => {
  const data: [FaceDetectEnum, string][] = [
    [FaceDetectEnum.ON, 'ON'],
    [FaceDetectEnum.OFF, 'OFF'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(FaceDetectEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
