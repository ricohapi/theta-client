import * as React from 'react';
import type { OptionEditProps } from '..';
import { View } from 'react-native';
import type { TopBottomCorrectionRotation } from 'theta-client-react-native';
import { InputNumber } from '../../ui/input-number';

export const TopBottomCorrectionRotationEdit: React.FC<OptionEditProps> = ({
  onChange,
  options,
}) => {
  const [editRotation, setEditRotation] =
    React.useState<TopBottomCorrectionRotation>();

  const defaultRotation = React.useMemo<TopBottomCorrectionRotation>(() => {
    return {
      pitch: 0.0,
      roll: 0.0,
      yaw: 0.0,
    };
  }, []);

  React.useEffect(() => {
    const rotation = options?.topBottomCorrectionRotation || {
      ...defaultRotation,
    };
    setEditRotation(rotation);
  }, [options, defaultRotation]);

  return (
    <View>
      <InputNumber
        title={'pitch'}
        onChange={(newValue) => {
          if (editRotation != null) {
            const targetValue = newValue ?? 0;
            const newVal: TopBottomCorrectionRotation = {
              ...editRotation,
              pitch: targetValue,
            };
            setEditRotation(newVal);
            onChange({ ...options, topBottomCorrectionRotation: newVal });
          }
        }}
        value={options?.topBottomCorrectionRotation?.pitch}
      />
      <InputNumber
        title={'roll'}
        onChange={(newValue) => {
          if (editRotation != null) {
            const targetValue = newValue ?? 0;
            const newVal: TopBottomCorrectionRotation = {
              ...editRotation,
              roll: targetValue,
            };
            setEditRotation(newVal);
            onChange({ ...options, topBottomCorrectionRotation: newVal });
          }
        }}
        value={options?.topBottomCorrectionRotation?.roll}
      />
      <InputNumber
        title={'yaw'}
        onChange={(newValue) => {
          if (editRotation != null) {
            const targetValue = newValue ?? 0;
            const newVal: TopBottomCorrectionRotation = {
              ...editRotation,
              yaw: targetValue,
            };
            setEditRotation(newVal);
            onChange({ ...options, topBottomCorrectionRotation: newVal });
          }
        }}
        value={options?.topBottomCorrectionRotation?.yaw}
      />
    </View>
  );
};

TopBottomCorrectionRotationEdit.displayName = 'TopBottomCorrectionRotationEdit';

export default TopBottomCorrectionRotationEdit;
