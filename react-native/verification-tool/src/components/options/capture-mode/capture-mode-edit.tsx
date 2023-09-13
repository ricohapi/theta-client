import * as React from 'react';
import { CaptureModeEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const CaptureModeEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const captureModeList = [
    { name: 'IMAGE', value: CaptureModeEnum.IMAGE },
    { name: 'VIDEO', value: CaptureModeEnum.VIDEO },
    { name: 'INTERVAL', value: CaptureModeEnum.INTERVAL },
    { name: 'LIVE_STREAMING', value: CaptureModeEnum.LIVE_STREAMING },
    { name: 'PRESET', value: CaptureModeEnum.PRESET },
  ];

  return (
    <EnumEdit
      enumList={captureModeList}
      propName={'captureMode'}
      onChange={onChange}
      options={options}
    />
  );
};

CaptureModeEdit.displayName = 'CaptureModeEdit';

export default CaptureModeEdit;
