import * as React from 'react';
import type { OptionEditProps } from '..';
import { View } from 'react-native';
import { EnumEdit } from '../enum-edit';
import { PlanEnum, RoamingEnum } from '../../../modules/theta-client';

export const MobileNetworkSettingEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  return (
    <View>
      <EnumEdit
        title={'roaming'}
        option={options?.mobileNetworkSetting?.roaming}
        onChange={(roaming) => {
          const newOptions = {
            ...options,
            mobileNetworkSetting: {
              ...options?.mobileNetworkSetting,
              roaming,
            },
          };
          onChange(newOptions);
        }}
        optionEnum={RoamingEnum}
      />
      <EnumEdit
        title={'plan'}
        option={options?.mobileNetworkSetting?.plan}
        onChange={(plan) => {
          const newOptions = {
            ...options,
            mobileNetworkSetting: {
              ...options?.mobileNetworkSetting,
              plan,
            },
          };
          onChange(newOptions);
        }}
        optionEnum={PlanEnum}
      />
    </View>
  );
};

MobileNetworkSettingEdit.displayName = 'MobileNetworkSettingEdit';

export default MobileNetworkSettingEdit;
