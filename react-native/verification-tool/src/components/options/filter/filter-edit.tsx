import * as React from 'react';
import { FilterEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const FilterEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const filterList = [
    { name: '[undefined]', value: undefined },
    { name: 'OFF', value: FilterEnum.OFF },
    { name: 'DR_COMP', value: FilterEnum.DR_COMP },
    { name: 'NOISE_REDUCTION', value: FilterEnum.NOISE_REDUCTION },
    { name: 'HDR', value: FilterEnum.HDR },
    { name: 'HH_HDR', value: FilterEnum.HH_HDR },
  ];

  return (
    <EnumEdit
      enumList={filterList}
      propName={'filter'}
      onChange={onChange}
      options={options}
    />
  );
};

FilterEdit.displayName = 'FilterEdit';

export default FilterEdit;
