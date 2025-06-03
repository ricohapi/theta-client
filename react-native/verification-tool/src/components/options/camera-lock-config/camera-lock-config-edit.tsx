import * as React from 'react';
import type { OptionEditProps } from '..';
import { View } from 'react-native';
import { TitledSwitch } from '../../ui/titled-switch';

export const CameraLockConfigEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const cameraLockConfig = options?.cameraLockConfig;
  return (
    <View>
      <TitledSwitch
        title="isPowerKeyLocked"
        value={cameraLockConfig?.isPowerKeyLocked}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            cameraLockConfig: {
              ...cameraLockConfig,
              isPowerKeyLocked: newValue,
            },
          };
          onChange(newOptions);
        }}
      />
      <TitledSwitch
        title="isShutterKeyLocked"
        value={cameraLockConfig?.isShutterKeyLocked}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            cameraLockConfig: {
              ...cameraLockConfig,
              isShutterKeyLocked: newValue,
            },
          };
          onChange(newOptions);
        }}
      />
      <TitledSwitch
        title="isModeKeyLocked"
        value={cameraLockConfig?.isModeKeyLocked}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            cameraLockConfig: {
              ...cameraLockConfig,
              isModeKeyLocked: newValue,
            },
          };
          onChange(newOptions);
        }}
      />
      <TitledSwitch
        title="isWlanKeyLocked"
        value={cameraLockConfig?.isWlanKeyLocked}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            cameraLockConfig: {
              ...cameraLockConfig,
              isWlanKeyLocked: newValue,
            },
          };
          onChange(newOptions);
        }}
      />
      <TitledSwitch
        title="isFnKeyLocked"
        value={cameraLockConfig?.isFnKeyLocked}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            cameraLockConfig: {
              ...cameraLockConfig,
              isFnKeyLocked: newValue,
            },
          };
          onChange(newOptions);
        }}
      />
      <TitledSwitch
        title="isPanelLocked"
        value={cameraLockConfig?.isPanelLocked}
        onChange={(newValue) => {
          const newOptions = {
            ...options,
            cameraLockConfig: {
              ...cameraLockConfig,
              isPanelLocked: newValue,
            },
          };
          onChange(newOptions);
        }}
      />
    </View>
  );
};

CameraLockConfigEdit.displayName = 'CameraLockConfigEdit';

export default CameraLockConfigEdit;
