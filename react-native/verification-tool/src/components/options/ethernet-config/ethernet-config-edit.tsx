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
  const ethernetConfig = options?.ethernetConfig ?? { usingDhcp: true };

  return (
    <View>
      <TitledSwitch
        title="usingDhcp"
        value={ethernetConfig.usingDhcp}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            ethernetConfig: {
              ...ethernetConfig,
              usingDhcp: newValue,
              ...(newValue
                ? {
                    ipAddress: undefined,
                    subnetMask: undefined,
                    defaultGateway: undefined,
                    dns1: undefined,
                    dns2: undefined,
                  }
                : {}),
            },
          };
          onChange(newOptions);
        }}
      />
      {!ethernetConfig.usingDhcp && (
        <View>
          <InputString
            title={'ipAddress'}
            onChange={(newValue) => {
              const newOptions = {
                ...options,
                ethernetConfig: {
                  ...ethernetConfig,
                  ipAddress: newValue,
                },
              };
              onChange(newOptions);
            }}
            value={ethernetConfig.ipAddress}
          />
          <InputString
            title={'subnetMask'}
            onChange={(newValue) => {
              const newOptions = {
                ...options,
                ethernetConfig: {
                  ...ethernetConfig,
                  subnetMask: newValue,
                },
              };
              onChange(newOptions);
            }}
            value={ethernetConfig.subnetMask}
          />
          <InputString
            title={'defaultGateway'}
            onChange={(newValue) => {
              const newOptions = {
                ...options,
                ethernetConfig: {
                  ...ethernetConfig,
                  defaultGateway: newValue,
                },
              };
              onChange(newOptions);
            }}
            value={ethernetConfig.defaultGateway}
          />
          <InputString
            title={'dns1'}
            onChange={(newValue) => {
              const newOptions = {
                ...options,
                ethernetConfig: {
                  ...ethernetConfig,
                  dns1: newValue,
                },
              };
              onChange(newOptions);
            }}
            value={ethernetConfig.dns1}
          />
          <InputString
            title={'dns2'}
            onChange={(newValue) => {
              const newOptions = {
                ...options,
                ethernetConfig: {
                  ...ethernetConfig,
                  dns2: newValue,
                },
              };
              onChange(newOptions);
            }}
            value={ethernetConfig.dns2}
          />
        </View>
      )}
      <View style={styles.rowContainerLayout}>
        <Text style={styles.labelText}>proxy</Text>
        <View style={styles.colContainerLayout}>
          <TitledSwitch
            title="use"
            value={ethernetConfig.proxy?.use}
            onChange={(newValue) => {
              const newOptions = {
                ...options,
                ethernetConfig: {
                  ...ethernetConfig,
                  proxy: {
                    ...(newValue ? ethernetConfig.proxy ?? { use: true } : {}),
                    use: newValue,
                  },
                },
              };
              onChange(newOptions);
            }}
          />
          {ethernetConfig.proxy?.use && (
            <View>
              <InputString
                title={'url'}
                onChange={(newValue) => {
                  const use = ethernetConfig.proxy?.use;
                  if (use !== undefined) {
                    const newOptions = {
                      ...options,
                      ethernetConfig: {
                        ...ethernetConfig,
                        proxy: {
                          ...ethernetConfig.proxy,
                          use: use,
                          url: newValue,
                        },
                      },
                    };
                    onChange(newOptions);
                  }
                }}
                value={ethernetConfig.proxy?.url}
              />
              <InputNumber
                title={'port'}
                onChange={(newValue) => {
                  const use = ethernetConfig.proxy?.use;
                  if (use !== undefined) {
                    const newOptions = {
                      ...options,
                      ethernetConfig: {
                        ...ethernetConfig,
                        proxy: {
                          ...ethernetConfig.proxy,
                          use: use,
                          port: newValue,
                        },
                      },
                    };
                    onChange(newOptions);
                  }
                }}
                value={ethernetConfig.proxy?.port}
              />
              <InputString
                title={'userid'}
                onChange={(newValue) => {
                  const use = ethernetConfig.proxy?.use;
                  if (use !== undefined) {
                    const newOptions = {
                      ...options,
                      ethernetConfig: {
                        ...ethernetConfig,
                        proxy: {
                          ...ethernetConfig.proxy,
                          use: use,
                          userid: newValue,
                        },
                      },
                    };
                    onChange(newOptions);
                  }
                }}
                value={ethernetConfig.proxy?.userid}
              />
              <InputString
                title={'password'}
                onChange={(newValue) => {
                  const use = ethernetConfig.proxy?.use;
                  if (use !== undefined) {
                    const newOptions = {
                      ...options,
                      ethernetConfig: {
                        ...ethernetConfig,
                        proxy: {
                          ...ethernetConfig.proxy,
                          use: use,
                          password: newValue,
                        },
                      },
                    };
                    onChange(newOptions);
                  }
                }}
                value={ethernetConfig.proxy?.password}
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
