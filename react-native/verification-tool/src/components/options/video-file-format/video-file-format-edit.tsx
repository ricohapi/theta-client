import * as React from 'react';
import { VideoFileFormatEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const VideoFileFormatEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const VideoFileFormatList = [
    { name: '[undefined]', value: undefined },
    { name: 'VIDEO_HD', value: VideoFileFormatEnum.VIDEO_HD },
    { name: 'VIDEO_FULL_HD', value: VideoFileFormatEnum.VIDEO_FULL_HD },
    { name: 'VIDEO_2K', value: VideoFileFormatEnum.VIDEO_2K },
    { name: 'VIDEO_4K', value: VideoFileFormatEnum.VIDEO_4K },
    { name: 'VIDEO_2K_30F', value: VideoFileFormatEnum.VIDEO_2K_30F },
    { name: 'VIDEO_2K_60F', value: VideoFileFormatEnum.VIDEO_2K_60F },
    { name: 'VIDEO_4K_30F', value: VideoFileFormatEnum.VIDEO_4K_30F },
    { name: 'VIDEO_4K_60F', value: VideoFileFormatEnum.VIDEO_4K_60F },
    { name: 'VIDEO_5_7K_2F', value: VideoFileFormatEnum.VIDEO_5_7K_2F },
    { name: 'VIDEO_5_7K_5F', value: VideoFileFormatEnum.VIDEO_5_7K_5F },
    { name: 'VIDEO_5_7K_30F', value: VideoFileFormatEnum.VIDEO_5_7K_30F },
    { name: 'VIDEO_7K_2F', value: VideoFileFormatEnum.VIDEO_7K_2F },
    { name: 'VIDEO_7K_5F', value: VideoFileFormatEnum.VIDEO_7K_5F },
    { name: 'VIDEO_7K_10F', value: VideoFileFormatEnum.VIDEO_7K_10F },
  ];

  return (
    <EnumEdit
      enumList={VideoFileFormatList}
      propName={'fileFormat'}
      onChange={onChange}
      options={options}
    />
  );
};

VideoFileFormatEdit.displayName = 'VideoFileFormatEdit';

export default VideoFileFormatEdit;
