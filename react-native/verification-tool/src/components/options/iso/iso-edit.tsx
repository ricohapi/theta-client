import * as React from 'react';
import { IsoEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const IsoEdit: React.FC<OptionEditProps> = ({ onChange, options }) => {
  const isoList = [
    { name: '[undefined]', value: undefined },
    { name: 'ISO_AUTO', value: IsoEnum.ISO_AUTO },
    { name: 'ISO_50', value: IsoEnum.ISO_50 },
    { name: 'ISO_64', value: IsoEnum.ISO_64 },
    { name: 'ISO_80', value: IsoEnum.ISO_80 },
    { name: 'ISO_100', value: IsoEnum.ISO_100 },
    { name: 'ISO_125', value: IsoEnum.ISO_125 },
    { name: 'ISO_160', value: IsoEnum.ISO_160 },
    { name: 'ISO_200', value: IsoEnum.ISO_200 },
    { name: 'ISO_250', value: IsoEnum.ISO_250 },
    { name: 'ISO_320', value: IsoEnum.ISO_320 },
    { name: 'ISO_400', value: IsoEnum.ISO_400 },
    { name: 'ISO_500', value: IsoEnum.ISO_500 },
    { name: 'ISO_640', value: IsoEnum.ISO_640 },
    { name: 'ISO_800', value: IsoEnum.ISO_800 },
    { name: 'ISO_1000', value: IsoEnum.ISO_1000 },
    { name: 'ISO_1250', value: IsoEnum.ISO_1250 },
    { name: 'ISO_1600', value: IsoEnum.ISO_1600 },
    { name: 'ISO_2000', value: IsoEnum.ISO_2000 },
    { name: 'ISO_2500', value: IsoEnum.ISO_2500 },
    { name: 'ISO_3200', value: IsoEnum.ISO_3200 },
    { name: 'ISO_4000', value: IsoEnum.ISO_4000 },
    { name: 'ISO_5000', value: IsoEnum.ISO_5000 },
    { name: 'ISO_6400', value: IsoEnum.ISO_6400 },
  ];

  return (
    <EnumEdit
      enumList={isoList}
      propName={'iso'}
      onChange={onChange}
      options={options}
    />
  );
};

IsoEdit.displayName = 'IsoEdit';

export default IsoEdit;
