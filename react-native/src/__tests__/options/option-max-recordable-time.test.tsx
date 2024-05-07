import { MaxRecordableTimeEnum } from '../../theta-repository/options';

describe('MaxRecordableTimeEnum', () => {
  const data: string[][] = [
    [MaxRecordableTimeEnum.UNKNOWN, 'UNKNOWN'],
    [MaxRecordableTimeEnum.RECORDABLE_TIME_180, 'RECORDABLE_TIME_180'],
    [MaxRecordableTimeEnum.RECORDABLE_TIME_300, 'RECORDABLE_TIME_300'],
    [MaxRecordableTimeEnum.RECORDABLE_TIME_1500, 'RECORDABLE_TIME_1500'],
    [MaxRecordableTimeEnum.RECORDABLE_TIME_3000, 'RECORDABLE_TIME_3000'],
    [MaxRecordableTimeEnum.RECORDABLE_TIME_7200, 'RECORDABLE_TIME_7200'],
    [
      MaxRecordableTimeEnum.DO_NOT_UPDATE_MY_SETTING_CONDITION,
      'DO_NOT_UPDATE_MY_SETTING_CONDITION',
    ],
  ];

  test('length', () => {
    expect(data.length).toBe(Object.keys(MaxRecordableTimeEnum).length);
  });

  test('data', () => {
    data.forEach((item) => {
      expect(item[0] ? item[0].toString() : '').toBe(item[1]);
    });
  });
});
