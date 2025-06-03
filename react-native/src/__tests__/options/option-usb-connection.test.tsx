import { UsbConnectionEnum } from '../../theta-repository/options/option-usb-connection';

describe('UsbConnectionEnum', () => {
  const data: string[][] = [
    [UsbConnectionEnum.UNKNOWN, 'UNKNOWN'],
    [UsbConnectionEnum.MTP, 'MTP'],
    [UsbConnectionEnum.MSC, 'MSC'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(UsbConnectionEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
