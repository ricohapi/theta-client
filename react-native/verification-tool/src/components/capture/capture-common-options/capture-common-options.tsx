import * as React from 'react';
import type { OptionEditProps } from '../../options';
import { View } from 'react-native';
import {
  ApertureEdit,
  ExposureCompensationEdit,
  ExposureDelayEdit,
  ExposureProgramEdit,
  GpsInfoEdit,
  GpsTagRecordingEdit,
  IsoEdit,
  IsoAutoHighLimitEdit,
  WhiteBalanceEdit,
} from '../../../components/options';
import { NumberEdit } from '../../../components/options/number-edit';

export const CaptureCommonOptionsEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  return (
    <View>
      <ApertureEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            aperture: option.aperture,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
      <NumberEdit
        propName={'colorTemperature'}
        onChange={(option) => {
          const newOptions = {
            ...options,
            colorTemperature: option.colorTemperature,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
        placeHolder="Input value"
      />
      <ExposureCompensationEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            exposureCompensation: option.exposureCompensation,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
      <ExposureDelayEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            exposureDelay: option.exposureDelay,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
      <ExposureProgramEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            exposureProgram: option.exposureProgram,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
      <GpsInfoEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            gpsInfo: option.gpsInfo,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
      <GpsTagRecordingEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            _gpsTagRecording: option._gpsTagRecording,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
      <IsoEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            iso: option.iso,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
      <IsoAutoHighLimitEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            isoAutoHighLimit: option.isoAutoHighLimit,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
      <WhiteBalanceEdit
        onChange={(option) => {
          const newOptions = {
            ...options,
            whiteBalance: option.whiteBalance,
          };
          newOptions && onChange(newOptions);
        }}
        options={options}
      />
    </View>
  );
};

CaptureCommonOptionsEdit.displayName = 'CaptureCommonOptionsEdit';

export default CaptureCommonOptionsEdit;
