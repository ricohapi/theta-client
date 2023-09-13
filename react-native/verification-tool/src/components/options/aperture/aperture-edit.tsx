import * as React from 'react';
import { ApertureEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const ApertureEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const apertureList = [
    { name: '[undefined]', value: undefined },
    { name: 'APERTURE_AUTO', value: ApertureEnum.APERTURE_AUTO },
    { name: 'APERTURE_2_0', value: ApertureEnum.APERTURE_2_0 },
    { name: 'APERTURE_2_1', value: ApertureEnum.APERTURE_2_1 },
    { name: 'APERTURE_2_4', value: ApertureEnum.APERTURE_2_4 },
    { name: 'APERTURE_3_5', value: ApertureEnum.APERTURE_3_5 },
    { name: 'APERTURE_5_6', value: ApertureEnum.APERTURE_5_6 },
  ];

  return (
    <EnumEdit
      enumList={apertureList}
      propName={'aperture'}
      onChange={onChange}
      options={options}
    />
  );
};

ApertureEdit.displayName = 'ApertureEdit';

export default ApertureEdit;
