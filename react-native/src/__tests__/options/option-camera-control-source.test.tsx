import { CameraControlSourceEnum } from '../../theta-repository/options/option-camera-control-source';

describe('CameraControlSourceEnum', () => {
  const data: string[][] = [
    [CameraControlSourceEnum.CAMERA, 'CAMERA'],
    [CameraControlSourceEnum.APP, 'APP'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(CameraControlSourceEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
