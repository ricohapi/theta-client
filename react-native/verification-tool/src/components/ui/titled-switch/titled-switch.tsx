import * as React from 'react';
import {
  StyleProp,
  View,
  ViewStyle,
  Text,
  ViewProps,
  Switch,
} from 'react-native';
import styles from './styles';

interface Props extends Pick<ViewProps, 'testID'> {
  title?: string;
  style?: StyleProp<ViewStyle>;
  value?: boolean;
  onChange?: (value: boolean) => void;
}

export const TitledSwitch: React.FC<Props> = ({
  style,
  title,
  value,
  onChange,
}) => {
  const [editValue, setEditValue] = React.useState(value ?? false);

  React.useEffect(() => {
    if (value != null) {
      setEditValue(value);
    }
  }, [value]);

  const onChangeValue = (newValue: boolean) => {
    setEditValue(newValue);
    onChange?.(newValue);
  };

  return (
    <View style={style}>
      <View style={styles.containerLayout}>
        <View style={styles.titleBack}>
          <Text style={styles.titleText}>{title}</Text>
        </View>
        <View style={styles.itemBack}>
          <Switch value={editValue} onValueChange={onChangeValue} />
        </View>
      </View>
    </View>
  );
};

TitledSwitch.displayName = 'TitledSwitch';

export default TitledSwitch;
