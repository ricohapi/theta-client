import * as React from 'react';
import {
  StyleProp,
  View,
  ViewStyle,
  Text,
  TextInput,
  ViewProps,
} from 'react-native';
import styles from './styles';

interface Props extends Pick<ViewProps, 'testID'> {
  title?: string;
  style?: StyleProp<ViewStyle>;
  value?: number;
  editable?: boolean;
  onChange?: (value?: number) => void;
  placeHolder?: string;
}

export const InputNumber: React.FC<Props> = ({
  style,
  title,
  value,
  editable,
  placeHolder,
  onChange,
}) => {
  const [editText, setEditText] = React.useState(value?.toString() ?? '');

  React.useEffect(() => {
    if (value != null) {
      setEditText(String(value));
    }
  }, [value]);

  const onChangeText = (text: string) => {
    setEditText(text);
    if (text.length === 0) {
      onChange?.(undefined);
      return;
    }
    const numValue = Number(text);
    if (isNaN(numValue)) {
      return;
    }
    onChange?.(numValue);
  };

  return (
    <View style={style}>
      <View style={styles.containerLayout}>
        <View style={styles.titleBack}>
          <Text style={styles.titleText}>{title}</Text>
        </View>
        <View style={styles.itemBack}>
          <TextInput
            keyboardType="number-pad"
            style={styles.inputText}
            value={editText}
            onChangeText={onChangeText}
            placeholder={placeHolder}
            editable={editable ?? true}
          />
        </View>
      </View>
    </View>
  );
};

InputNumber.displayName = 'InputNumber';

export default InputNumber;
