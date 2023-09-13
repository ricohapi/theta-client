import * as React from 'react';
import { PresetEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const PresetEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const presetList = [
    { name: '[undefined]', value: undefined },
    { name: 'FACE', value: PresetEnum.FACE },
    { name: 'NIGHT_VIEW', value: PresetEnum.NIGHT_VIEW },
    { name: 'LENS_BY_LENS_EXPOSURE', value: PresetEnum.LENS_BY_LENS_EXPOSURE },
    { name: 'ROOM', value: PresetEnum.ROOM },
  ];

  return (
    <EnumEdit
      enumList={presetList}
      propName={'preset'}
      onChange={onChange}
      options={options}
    />
  );
};

PresetEdit.displayName = 'PresetEdit';

export default PresetEdit;
