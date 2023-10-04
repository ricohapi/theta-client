import * as React from 'react';
import { TopBottomCorrectionOptionEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const TopBottomCorrectionEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const captureModeList = [
    { name: 'APPLY', value: TopBottomCorrectionOptionEnum.APPLY },
    { name: 'APPLY_AUTO', value: TopBottomCorrectionOptionEnum.APPLY_AUTO },
    {
      name: 'APPLY_SEMIAUTO',
      value: TopBottomCorrectionOptionEnum.APPLY_SEMIAUTO,
    },
    { name: 'APPLY_SAVE', value: TopBottomCorrectionOptionEnum.APPLY_SAVE },
    { name: 'APPLY_LOAD', value: TopBottomCorrectionOptionEnum.APPLY_LOAD },
    { name: 'DISAPPLY', value: TopBottomCorrectionOptionEnum.DISAPPLY },
    { name: 'MANUAL', value: TopBottomCorrectionOptionEnum.MANUAL },
  ];

  return (
    <EnumEdit
      enumList={captureModeList}
      propName={'topBottomCorrection'}
      onChange={onChange}
      options={options}
    />
  );
};

TopBottomCorrectionEdit.displayName = 'TopBottomCorrectionEdit';

export default TopBottomCorrectionEdit;
