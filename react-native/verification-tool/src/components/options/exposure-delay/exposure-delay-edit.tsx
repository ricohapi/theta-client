import * as React from 'react';
import { ExposureDelayEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const ExposureDelayEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const exposureDelayList = [
    { name: '[undefined]', value: undefined },
    { name: 'DELAY_OFF', value: ExposureDelayEnum.DELAY_OFF },
    { name: 'DELAY_1', value: ExposureDelayEnum.DELAY_1 },
    { name: 'DELAY_2', value: ExposureDelayEnum.DELAY_2 },
    { name: 'DELAY_3', value: ExposureDelayEnum.DELAY_3 },
    { name: 'DELAY_4', value: ExposureDelayEnum.DELAY_4 },
    { name: 'DELAY_5', value: ExposureDelayEnum.DELAY_5 },
    { name: 'DELAY_6', value: ExposureDelayEnum.DELAY_6 },
    { name: 'DELAY_7', value: ExposureDelayEnum.DELAY_7 },
    { name: 'DELAY_8', value: ExposureDelayEnum.DELAY_8 },
    { name: 'DELAY_9', value: ExposureDelayEnum.DELAY_9 },
    { name: 'DELAY_10', value: ExposureDelayEnum.DELAY_10 },
  ];

  return (
    <EnumEdit
      enumList={exposureDelayList}
      propName={'exposureDelay'}
      onChange={onChange}
      options={options}
    />
  );
};

ExposureDelayEdit.displayName = 'ExposureDelayEdit';

export default ExposureDelayEdit;
