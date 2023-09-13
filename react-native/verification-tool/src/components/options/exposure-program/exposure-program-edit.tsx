import * as React from 'react';
import { ExposureProgramEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const ExposureProgramEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const exposureProgramList = [
    { name: '[undefined]', value: undefined },
    { name: 'MANUAL', value: ExposureProgramEnum.MANUAL },
    { name: 'NORMAL_PROGRAM', value: ExposureProgramEnum.NORMAL_PROGRAM },
    { name: 'APERTURE_PRIORITY', value: ExposureProgramEnum.APERTURE_PRIORITY },
    { name: 'SHUTTER_PRIORITY', value: ExposureProgramEnum.SHUTTER_PRIORITY },
    { name: 'ISO_PRIORITY', value: ExposureProgramEnum.ISO_PRIORITY },
  ];

  return (
    <EnumEdit
      enumList={exposureProgramList}
      propName={'exposureProgram'}
      onChange={onChange}
      options={options}
    />
  );
};

ExposureProgramEdit.displayName = 'ExposureProgramEdit';

export default ExposureProgramEdit;
