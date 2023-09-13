import * as React from 'react';
import { PhotoFileFormatEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const PhotoFileFormatEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const photoFileFormatList = [
    { name: '[undefined]', value: undefined },
    { name: 'IMAGE_2K', value: PhotoFileFormatEnum.IMAGE_2K },
    { name: 'IMAGE_5K', value: PhotoFileFormatEnum.IMAGE_5K },
    { name: 'IMAGE_6_7K', value: PhotoFileFormatEnum.IMAGE_6_7K },
    { name: 'RAW_P_6_7K', value: PhotoFileFormatEnum.RAW_P_6_7K },
    { name: 'IMAGE_5_5K', value: PhotoFileFormatEnum.IMAGE_5_5K },
    { name: 'IMAGE_11K', value: PhotoFileFormatEnum.IMAGE_11K },
  ];

  return (
    <EnumEdit
      enumList={photoFileFormatList}
      propName={'fileFormat'}
      onChange={onChange}
      options={options}
    />
  );
};

PhotoFileFormatEdit.displayName = 'PhotoFileFormatEdit';

export default PhotoFileFormatEdit;
