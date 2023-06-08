import { TimeShiftEnum } from '../../theta-repository/options/option-time-shift';

describe('TimeShift', () => {

  

  const data: string[][] = [
    [TimeShiftEnum.=MEMBER=, '=MEMBER='],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(TimeShiftEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
})
