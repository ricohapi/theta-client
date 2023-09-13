import * as React from 'react';
import { WhiteBalanceEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const WhiteBalanceEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const whiteBalanceList = [
    { name: '[undefined]', value: undefined },
    { name: 'AUTO', value: WhiteBalanceEnum.AUTO },
    { name: 'DAYLIGHT', value: WhiteBalanceEnum.DAYLIGHT },
    { name: 'SHADE', value: WhiteBalanceEnum.SHADE },
    { name: 'CLOUDY_DAYLIGHT', value: WhiteBalanceEnum.CLOUDY_DAYLIGHT },
    { name: 'INCANDESCENT', value: WhiteBalanceEnum.INCANDESCENT },
    {
      name: 'WARM_WHITE_FLUORESCENT',
      value: WhiteBalanceEnum.WARM_WHITE_FLUORESCENT,
    },
    {
      name: 'DAYLIGHT_FLUORESCENT',
      value: WhiteBalanceEnum.DAYLIGHT_FLUORESCENT,
    },
    {
      name: 'DAYWHITE_FLUORESCENT',
      value: WhiteBalanceEnum.DAYWHITE_FLUORESCENT,
    },
    { name: 'FLUORESCENT', value: WhiteBalanceEnum.FLUORESCENT },
    { name: 'BULB_FLUORESCENT', value: WhiteBalanceEnum.BULB_FLUORESCENT },
    { name: 'COLOR_TEMPERATURE', value: WhiteBalanceEnum.COLOR_TEMPERATURE },
    { name: 'UNDERWATER', value: WhiteBalanceEnum.UNDERWATER },
  ];

  return (
    <EnumEdit
      enumList={whiteBalanceList}
      propName={'whiteBalance'}
      onChange={onChange}
      options={options}
    />
  );
};

WhiteBalanceEdit.displayName = 'WhiteBalanceEdit';

export default WhiteBalanceEdit;
