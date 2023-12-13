import { BluetoothRoleEnum } from '../../theta-repository/options/option-bluetooth-role';

describe('BurstCaptureNumEnum', () => {
  const data: string[][] = [
    [BluetoothRoleEnum.CENTRAL, 'CENTRAL'],
    [BluetoothRoleEnum.PERIPHERAL, 'PERIPHERAL'],
    [BluetoothRoleEnum.CENTRAL_PERIPHERAL, 'CENTRAL_PERIPHERAL'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(BluetoothRoleEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
