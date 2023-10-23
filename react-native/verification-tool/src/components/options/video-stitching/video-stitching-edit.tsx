import * as React from 'react';
import { VideoStitchingEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const VideoStitchingEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const VideoStitchingList = [
    { name: '[undefined]', value: undefined },
    { name: 'NONE', value: VideoStitchingEnum.NONE },
    { name: 'ONDEVICE', value: VideoStitchingEnum.ONDEVICE },
  ];

  return (
    <EnumEdit
      enumList={VideoStitchingList}
      propName={'videoStitching'}
      onChange={onChange}
      options={options}
    />
  );
};

VideoStitchingEdit.displayName = 'VideoStitchingEdit';

export default VideoStitchingEdit;
