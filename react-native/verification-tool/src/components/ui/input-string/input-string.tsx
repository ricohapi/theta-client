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
  value?: string;
  editable?: boolean;
  onChange?: (value: string) => void;
  placeHolder?: string;
}

export const InputString: React.FC<Props> = ({
  style,
  title,
  value,
  editable,
  placeHolder,
  onChange,
}) => {
  const [editText, setEditText] = React.useState(value ?? '');

  React.useEffect(() => {
    if (value != null) {
      setEditText(value);
    }
  }, [value]);

  const onChangeText = (text: string) => {
    setEditText(text);
    onChange?.(text);
  };

  return (
    <View style={style}>
      <View style={styles.containerLayout}>
        <View style={styles.titleBack}>
          <Text style={styles.titleText}>{title}</Text>
        </View>
        <View style={styles.itemBack}>
          <TextInput
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

InputString.displayName = 'InputString';

export default InputString;
