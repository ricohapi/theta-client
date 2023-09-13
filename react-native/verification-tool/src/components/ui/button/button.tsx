import * as React from 'react';
import {
  StyleProp,
  TouchableOpacity,
  View,
  ViewStyle,
  type ButtonProps,
  Text,
} from 'react-native';
import styles from './styles';

interface Props
  extends Pick<ButtonProps, 'disabled' | 'onPress' | 'title' | 'testID'> {
  style?: StyleProp<ViewStyle>;
}

export const Button: React.FC<Props> = ({
  disabled = false,
  onPress,
  style,
  title,
}) => {
  return (
    <View style={style}>
      <TouchableOpacity
        style={disabled ? styles.buttonBackDisabled : styles.buttonBack}
        disabled={disabled}
        onPress={onPress}
      >
        <Text style={styles.button} disabled={disabled}>
          {title}
        </Text>
      </TouchableOpacity>
    </View>
  );
};

Button.displayName = 'Button';

export default Button;
