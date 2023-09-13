import * as React from 'react';
import { GpsTagRecordingEnum } from 'theta-client-react-native';
import type { OptionEditProps } from '..';
import { EnumEdit } from '../enum-edit';

export const GpsTagRecordingEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const gpsTagRecordingList = [
    { name: '[undefined]', value: undefined },
    { name: 'ON', value: GpsTagRecordingEnum.ON },
    { name: 'OFF', value: GpsTagRecordingEnum.OFF },
  ];

  return (
    <EnumEdit
      enumList={gpsTagRecordingList}
      propName={'_gpsTagRecording'}
      onChange={onChange}
      options={options}
    />
  );
};

GpsTagRecordingEdit.displayName = 'GpsTagRecordingEdit';

export default GpsTagRecordingEdit;
