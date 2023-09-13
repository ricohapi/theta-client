import * as React from 'react';
import { MaxRecordableTimeEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const MaxRecordableTimeEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const MaxRecordableTimeList = [
    { name: '[undefined]', value: undefined },
    {
      name: 'RECORDABLE_TIME_180',
      value: MaxRecordableTimeEnum.RECORDABLE_TIME_180,
    },
    {
      name: 'RECORDABLE_TIME_300',
      value: MaxRecordableTimeEnum.RECORDABLE_TIME_300,
    },
    {
      name: 'RECORDABLE_TIME_1500',
      value: MaxRecordableTimeEnum.RECORDABLE_TIME_1500,
    },
    {
      name: 'RECORDABLE_TIME_7200',
      value: MaxRecordableTimeEnum.RECORDABLE_TIME_7200,
    },
    {
      name: 'DO_NOT_UPDATE_MY_SETTING_CONDITION',
      value: MaxRecordableTimeEnum.DO_NOT_UPDATE_MY_SETTING_CONDITION,
    },
  ];

  return (
    <EnumEdit
      enumList={MaxRecordableTimeList}
      propName={'maxRecordableTime'}
      onChange={onChange}
      options={options}
    />
  );
};

MaxRecordableTimeEdit.displayName = 'MaxRecordableTimeEdit';

export default MaxRecordableTimeEdit;
