import * as React from 'react';
import type { OptionEditProps } from '..';
import { View } from 'react-native';
import { TitledSwitch } from '../../ui/titled-switch';

export const WlanFrequencyClModeEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  return (
    <View>
      <TitledSwitch
        title="enable2.4GHz"
        value={options?.wlanFrequencyClMode?.enable2_4 ?? false}
        onChange={(newValue: boolean) => {
          const newOptions = {
            ...options,
            wlanFrequencyClMode: {
              enable2_4: newValue,
              enable5_2: options?.wlanFrequencyClMode?.enable5_2 ?? false,
              enable5_8: options?.wlanFrequencyClMode?.enable5_8 ?? false,
            },
          };
          onChange(newOptions);
        }}
      />
      <TitledSwitch
        title="enable5.2GHz"
        value={options?.wlanFrequencyClMode?.enable5_2 ?? false}
        onChange={(newValue: boolean) => {
          const newOptions = {
            ...options,
            wlanFrequencyClMode: {
              enable2_4: options?.wlanFrequencyClMode?.enable2_4 ?? false,
              enable5_2: newValue,
              enable5_8: options?.wlanFrequencyClMode?.enable5_8 ?? false,
            },
          };
          onChange(newOptions);
        }}
      />
      <TitledSwitch
        title="enable5.8GHz"
        value={options?.wlanFrequencyClMode?.enable5_8 ?? false}
        onChange={(newValue: boolean) => {
          const newOptions = {
            ...options,
            wlanFrequencyClMode: {
              enable2_4: options?.wlanFrequencyClMode?.enable2_4 ?? false,
              enable5_2: options?.wlanFrequencyClMode?.enable5_2 ?? false,
              enable5_8: newValue,
            },
          };
          onChange(newOptions);
        }}
      />
    </View>
  );
};

WlanFrequencyClModeEdit.displayName = 'WlanFrequencyClModeEdit';

export default WlanFrequencyClModeEdit;
