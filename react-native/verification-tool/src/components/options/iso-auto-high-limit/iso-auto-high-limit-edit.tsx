import * as React from 'react';
import { IsoAutoHighLimitEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const IsoAutoHighLimitEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const isoAutoHighLimitList = [
    { name: '[undefined]', value: undefined },
    { name: 'ISO_100', value: IsoAutoHighLimitEnum.ISO_100 },
    { name: 'ISO_125', value: IsoAutoHighLimitEnum.ISO_125 },
    { name: 'ISO_160', value: IsoAutoHighLimitEnum.ISO_160 },
    { name: 'ISO_200', value: IsoAutoHighLimitEnum.ISO_200 },
    { name: 'ISO_250', value: IsoAutoHighLimitEnum.ISO_250 },
    { name: 'ISO_320', value: IsoAutoHighLimitEnum.ISO_320 },
    { name: 'ISO_400', value: IsoAutoHighLimitEnum.ISO_400 },
    { name: 'ISO_500', value: IsoAutoHighLimitEnum.ISO_500 },
    { name: 'ISO_640', value: IsoAutoHighLimitEnum.ISO_640 },
    { name: 'ISO_800', value: IsoAutoHighLimitEnum.ISO_800 },
    { name: 'ISO_1000', value: IsoAutoHighLimitEnum.ISO_1000 },
    { name: 'ISO_1250', value: IsoAutoHighLimitEnum.ISO_1250 },
    { name: 'ISO_1600', value: IsoAutoHighLimitEnum.ISO_1600 },
    { name: 'ISO_2000', value: IsoAutoHighLimitEnum.ISO_2000 },
    { name: 'ISO_2500', value: IsoAutoHighLimitEnum.ISO_2500 },
    { name: 'ISO_3200', value: IsoAutoHighLimitEnum.ISO_3200 },
    { name: 'ISO_4000', value: IsoAutoHighLimitEnum.ISO_4000 },
    { name: 'ISO_5000', value: IsoAutoHighLimitEnum.ISO_5000 },
    { name: 'ISO_6400', value: IsoAutoHighLimitEnum.ISO_6400 },
  ];

  return (
    <EnumEdit
      enumList={isoAutoHighLimitList}
      propName={'isoAutoHighLimit'}
      onChange={onChange}
      options={options}
    />
  );
};

IsoAutoHighLimitEdit.displayName = 'IsoAutoHighLimitEdit';

export default IsoAutoHighLimitEdit;
