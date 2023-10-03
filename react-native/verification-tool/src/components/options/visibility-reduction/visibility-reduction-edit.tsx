import * as React from 'react';
import { VisibilityReductionEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const VisibilityReductionEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const VisibilityReductionList = [
    { name: '[undefined]', value: undefined },
    { name: 'OFF', value: VisibilityReductionEnum.OFF },
    { name: 'ON', value: VisibilityReductionEnum.ON },
  ];

  return (
    <EnumEdit
      enumList={VisibilityReductionList}
      propName={'visibilityReduction'}
      onChange={onChange}
      options={options}
    />
  );
};

VisibilityReductionEdit.displayName = 'VisibilityReductionEdit';

export default VisibilityReductionEdit;
