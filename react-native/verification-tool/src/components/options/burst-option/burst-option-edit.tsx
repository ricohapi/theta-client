import * as React from 'react';
import type { OptionEditProps } from '../../options';
import { View } from 'react-native';
import { EnumEdit } from '../../options/enum-edit';
import {
  BurstBracketStepEnum,
  BurstCaptureNumEnum,
  BurstCompensationEnum,
  BurstEnableIsoControlEnum,
  BurstMaxExposureTimeEnum,
  BurstOrderEnum,
} from '../../../modules/theta-client';

export const BurstOptionsEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  return (
    <View>
      <EnumEdit
        title={'burstCaptureNum'}
        option={options?.burstOption?.burstCaptureNum}
        onChange={(burstCaptureNum) => {
          const newOptions = {
            ...options,
            burstOption: {
              ...options?.burstOption,
              burstCaptureNum,
            },
          };
          onChange(newOptions);
        }}
        optionEnum={BurstCaptureNumEnum}
      />
      <EnumEdit
        title={'burstBracketStep'}
        option={options?.burstOption?.burstBracketStep}
        onChange={(burstBracketStep) => {
          const newOptions = {
            ...options,
            burstOption: {
              ...options?.burstOption,
              burstBracketStep,
            },
          };
          onChange(newOptions);
        }}
        optionEnum={BurstBracketStepEnum}
      />
      <EnumEdit
        title={'burstCompensation'}
        option={options?.burstOption?.burstCompensation}
        onChange={(burstCompensation) => {
          const newOptions = {
            ...options,
            burstOption: {
              ...options?.burstOption,
              burstCompensation,
            },
          };
          onChange(newOptions);
        }}
        optionEnum={BurstCompensationEnum}
      />
      <EnumEdit
        title={'burstMaxExposureTime'}
        option={options?.burstOption?.burstMaxExposureTime}
        onChange={(burstMaxExposureTime) => {
          const newOptions = {
            ...options,
            burstOption: {
              ...options?.burstOption,
              burstMaxExposureTime,
            },
          };
          onChange(newOptions);
        }}
        optionEnum={BurstMaxExposureTimeEnum}
      />
      <EnumEdit
        title={'burstEnableIsoControl'}
        option={options?.burstOption?.burstEnableIsoControl}
        onChange={(burstEnableIsoControl) => {
          const newOptions = {
            ...options,
            burstOption: {
              ...options?.burstOption,
              burstEnableIsoControl,
            },
          };
          onChange(newOptions);
        }}
        optionEnum={BurstEnableIsoControlEnum}
      />
      <EnumEdit
        title={'burstOrder'}
        option={options?.burstOption?.burstOrder}
        onChange={(burstOrder) => {
          const newOptions = {
            ...options,
            burstOption: {
              ...options?.burstOption,
              burstOrder,
            },
          };
          onChange(newOptions);
        }}
        optionEnum={BurstOrderEnum}
      />
    </View>
  );
};

BurstOptionsEdit.displayName = 'BurstOptionsEdit';

export default BurstOptionsEdit;
