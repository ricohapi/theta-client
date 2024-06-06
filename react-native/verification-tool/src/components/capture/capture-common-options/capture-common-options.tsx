import * as React from 'react';
import type { OptionEditProps } from '../../options';
import { View } from 'react-native';
import { GpsInfoEdit } from '../../../components/options';
import { NumberEdit } from '../../../components/options/number-edit';
import { EnumEdit } from '../../options/enum-edit';
import {
  ApertureEnum,
  ExposureCompensationEnum,
  ExposureDelayEnum,
  ExposureProgramEnum,
  GpsTagRecordingEnum,
  IsoAutoHighLimitEnum,
  IsoEnum,
  WhiteBalanceEnum,
} from '../../../modules/theta-client';

export const CaptureCommonOptionsEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  return (
    <View>
      <EnumEdit
        title={'aperture'}
        option={options?.aperture}
        onChange={(aperture) => {
          onChange({ ...options, aperture });
        }}
        optionEnum={ApertureEnum}
      />
      <NumberEdit
        propName={'colorTemperature'}
        onChange={(option) => {
          onChange({
            ...options,
            colorTemperature: option.colorTemperature,
          });
        }}
        options={options}
        placeHolder="Input value"
      />
      <EnumEdit
        title={'exposureCompensation'}
        option={options?.exposureCompensation}
        onChange={(exposureCompensation) => {
          onChange({ ...options, exposureCompensation });
        }}
        optionEnum={ExposureCompensationEnum}
      />
      <EnumEdit
        title={'exposureDelay'}
        option={options?.exposureDelay}
        onChange={(exposureDelay) => {
          onChange({ ...options, exposureDelay });
        }}
        optionEnum={ExposureDelayEnum}
      />
      <EnumEdit
        title={'exposureProgram'}
        option={options?.exposureProgram}
        onChange={(exposureProgram) => {
          onChange({ ...options, exposureProgram });
        }}
        optionEnum={ExposureProgramEnum}
      />
      <GpsInfoEdit
        onChange={(option) => {
          onChange({
            ...options,
            gpsInfo: option.gpsInfo,
          });
        }}
        options={options}
      />
      <EnumEdit
        title={'_gpsTagRecording'}
        option={options?._gpsTagRecording}
        onChange={(_gpsTagRecording) => {
          onChange({ ...options, _gpsTagRecording });
        }}
        optionEnum={GpsTagRecordingEnum}
      />
      <EnumEdit
        title={'iso'}
        option={options?.iso}
        onChange={(iso) => {
          onChange({ ...options, iso });
        }}
        optionEnum={IsoEnum}
      />
      <EnumEdit
        title={'isoAutoHighLimit'}
        option={options?.isoAutoHighLimit}
        onChange={(isoAutoHighLimit) => {
          onChange({ ...options, isoAutoHighLimit });
        }}
        optionEnum={IsoAutoHighLimitEnum}
      />
      <EnumEdit
        title={'whiteBalance'}
        option={options?.whiteBalance}
        onChange={(whiteBalance) => {
          onChange({ ...options, whiteBalance });
        }}
        optionEnum={WhiteBalanceEnum}
      />
    </View>
  );
};

CaptureCommonOptionsEdit.displayName = 'CaptureCommonOptionsEdit';

export default CaptureCommonOptionsEdit;
