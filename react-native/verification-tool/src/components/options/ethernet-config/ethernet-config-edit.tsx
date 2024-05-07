import * as React from 'react';
import type { OptionEditProps } from '..';
import { View, Text } from 'react-native';
import { InputString } from '../../ui/input-string';
import { InputNumber } from '../../ui/input-number';
import { TitledSwitch } from '../../ui/titled-switch';
import styles from './styles';

export const EthernetConfigEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  return (
    <View>
      <TitledSwitch
        title="usingDhcp"
        value={options?.ethernetConfig?.usingDhcp}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            ethernetConfig: {
              ...options?.ethernetConfig,
              usingDhcp: newValue,
            },
          };
          onChange(newOptions);
        }}
      />
      <InputString
        title={'ipAddress'}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            ethernetConfig: {
              ...options?.ethernetConfig,
              ipAddress: newValue,
            },
          };
          onChange(newOptions);
        }}
        value={options?.ethernetConfig?.ipAddress}
      />
      <InputString
        title={'subnetMask'}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            ethernetConfig: {
              ...options?.ethernetConfig,
              subnetMask: newValue,
            },
          };
          onChange(newOptions);
        }}
        value={options?.ethernetConfig?.subnetMask}
      />
      <InputString
        title={'defaultGateway'}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            ethernetConfig: {
              ...options?.ethernetConfig,
              defaultGateway: newValue,
            },
          };
          onChange(newOptions);
        }}
        value={options?.ethernetConfig?.defaultGateway}
      />
      <View style={styles.rowContainerLayout}>
        <Text style={styles.labelText}>proxy</Text>
        <View style={styles.colContainerLayout}>
          <TitledSwitch
            title="use"
            value={options?.ethernetConfig?.proxy?.use}
            onChange={(newValue) => {
              const newOptions = {
                ...options,
                ethernetConfig: {
                  ...options?.ethernetConfig,
                  proxy: {
                    ...options?.ethernetConfig?.proxy,
                    use: newValue,
                  },
                },
              };
              onChange(newOptions);
            }}
          />
          {options?.ethernetConfig?.proxy?.use && (
            <View>
              <InputString
                title={'url'}
                onChange={(newValue) => {
                  const use = options?.ethernetConfig?.proxy?.use;
                  if (use !== undefined) {
                    const newOptions = {
                      ...options,
                      ethernetConfig: {
                        ...options?.ethernetConfig,
                        proxy: {
                          ...options?.ethernetConfig?.proxy,
                          use: use,
                          url: newValue,
                        },
                      },
                    };
                    onChange(newOptions);
                  }
                }}
                value={options?.ethernetConfig?.proxy?.url}
              />
              <InputNumber
                title={'port'}
                onChange={(newValue) => {
                  const use = options?.ethernetConfig?.proxy?.use;
                  if (use !== undefined) {
                    const newOptions = {
                      ...options,
                      ethernetConfig: {
                        ...options?.ethernetConfig,
                        proxy: {
                          ...options?.ethernetConfig?.proxy,
                          use: use,
                          port: newValue,
                        },
                      },
                    };
                    onChange(newOptions);
                  }
                }}
                value={options?.ethernetConfig?.proxy?.port}
              />
              <InputString
                title={'userid'}
                onChange={(newValue) => {
                  const use = options?.ethernetConfig?.proxy?.use;
                  if (use !== undefined) {
                    const newOptions = {
                      ...options,
                      ethernetConfig: {
                        ...options?.ethernetConfig,
                        proxy: {
                          ...options?.ethernetConfig?.proxy,
                          use: use,
                          userid: newValue,
                        },
                      },
                    };
                    onChange(newOptions);
                  }
                }}
                value={options?.ethernetConfig?.proxy?.userid}
              />
              <InputString
                title={'password'}
                onChange={(newValue) => {
                  const use = options?.ethernetConfig?.proxy?.use;
                  if (use !== undefined) {
                    const newOptions = {
                      ...options,
                      ethernetConfig: {
                        ...options?.ethernetConfig,
                        proxy: {
                          ...options?.ethernetConfig?.proxy,
                          use: use,
                          password: newValue,
                        },
                      },
                    };
                    onChange(newOptions);
                  }
                }}
                value={options?.ethernetConfig?.proxy?.password}
              />
            </View>
          )}
        </View>
      </View>
    </View>
  );
};

EthernetConfigEdit.displayName = 'EthernetConfigEdit';

export default EthernetConfigEdit;
