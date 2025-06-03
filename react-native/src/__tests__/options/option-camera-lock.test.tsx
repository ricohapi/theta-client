import { CameraLockEnum } from '../../theta-repository/options/option-camera-lock';

describe('CameraLockEnum', () => {
  const data: string[][] = [
    [CameraLockEnum.UNKNOWN, 'UNKNOWN'],
    [CameraLockEnum.UNLOCK, 'UNLOCK'],
    [CameraLockEnum.BASIC_LOCK, 'BASIC_LOCK'],
    [CameraLockEnum.CUSTOM_LOCK, 'CUSTOM_LOCK'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(CameraLockEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
