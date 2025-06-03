import { AuthModeEnum } from '../../theta-repository';

describe('AuthModeEnum', () => {
  const data: [AuthModeEnum, string][] = [
    [AuthModeEnum.UNKNOWN, 'UNKNOWN'],
    [AuthModeEnum.NONE, 'NONE'],
    [AuthModeEnum.WEP, 'WEP'],
    [AuthModeEnum.WPA, 'WPA'],
    [AuthModeEnum.WPA3, 'WPA3'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(AuthModeEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0]).toBe(item[1]);
    });
  });
});
