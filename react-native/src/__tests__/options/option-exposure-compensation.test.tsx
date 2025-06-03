import { ExposureCompensationEnum } from '../../theta-repository/options/option-exposure-compensation';

describe('ExposureCompensationEnum', () => {
  const data: string[][] = [
    [ExposureCompensationEnum.UNKNOWN, 'UNKNOWN'],
    [ExposureCompensationEnum.M_4_0, 'M4_0'],
    [ExposureCompensationEnum.M_3_7, 'M3_7'],
    [ExposureCompensationEnum.M_3_3, 'M3_3'],
    [ExposureCompensationEnum.M_3_0, 'M3_0'],
    [ExposureCompensationEnum.M_2_7, 'M2_7'],
    [ExposureCompensationEnum.M_2_3, 'M2_3'],
    [ExposureCompensationEnum.M_2_0, 'M2_0'],
    [ExposureCompensationEnum.M_1_7, 'M1_7'],
    [ExposureCompensationEnum.M_1_3, 'M1_3'],
    [ExposureCompensationEnum.M_1_0, 'M1_0'],
    [ExposureCompensationEnum.M_0_7, 'M0_7'],
    [ExposureCompensationEnum.M_0_3, 'M0_3'],
    [ExposureCompensationEnum.ZERO, 'ZERO'],
    [ExposureCompensationEnum.P_0_3, 'P0_3'],
    [ExposureCompensationEnum.P_0_7, 'P0_7'],
    [ExposureCompensationEnum.P_1_0, 'P1_0'],
    [ExposureCompensationEnum.P_1_3, 'P1_3'],
    [ExposureCompensationEnum.P_1_7, 'P1_7'],
    [ExposureCompensationEnum.P_2_0, 'P2_0'],
    [ExposureCompensationEnum.P_2_3, 'P2_3'],
    [ExposureCompensationEnum.P_2_7, 'P2_7'],
    [ExposureCompensationEnum.P_3_0, 'P3_0'],
    [ExposureCompensationEnum.P_3_3, 'P3_3'],
    [ExposureCompensationEnum.P_3_7, 'P3_7'],
    [ExposureCompensationEnum.P_4_0, 'P4_0'],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(ExposureCompensationEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
