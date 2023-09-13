import * as React from 'react';
import { ExposureCompensationEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const ExposureCompensationEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const exposureCompensationList = [
    { name: '[undefined]', value: undefined },
    { name: 'M2_0', value: ExposureCompensationEnum.M_2_0 },
    { name: 'M1_7', value: ExposureCompensationEnum.M_1_7 },
    { name: 'M1_3', value: ExposureCompensationEnum.M_1_3 },
    { name: 'M1_0', value: ExposureCompensationEnum.M_1_0 },
    { name: 'M0_7', value: ExposureCompensationEnum.M_0_7 },
    { name: 'M0_3', value: ExposureCompensationEnum.M_0_3 },
    { name: 'ZERO', value: ExposureCompensationEnum.ZERO },
    { name: 'P0_3', value: ExposureCompensationEnum.P_0_3 },
    { name: 'P0_7', value: ExposureCompensationEnum.P_0_7 },
    { name: 'P1_0', value: ExposureCompensationEnum.P_1_0 },
    { name: 'P1_3', value: ExposureCompensationEnum.P_1_3 },
    { name: 'P1_7', value: ExposureCompensationEnum.P_1_7 },
    { name: 'P2_0', value: ExposureCompensationEnum.P_2_0 },
  ];

  return (
    <EnumEdit
      enumList={exposureCompensationList}
      propName={'exposureCompensation'}
      onChange={onChange}
      options={options}
    />
  );
};

ExposureCompensationEdit.displayName = 'ExposureCompensationEdit';

export default ExposureCompensationEdit;
