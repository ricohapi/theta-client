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
import type { Item } from '..';
import { ItemListPopupView } from '../item-list-popup-view';

interface Props extends Pick<ButtonProps, 'disabled' | 'title' | 'testID'> {
  style?: StyleProp<ViewStyle>;
  itemList: Item[];
  selectedItem?: Item;
  onSelected?: (item: Item) => void;
  placeHolder?: string;
}

export const ItemSelectorView: React.FC<Props> = ({
  disabled = false,
  style,
  title,
  itemList,
  selectedItem,
  placeHolder,
  onSelected,
}) => {
  const [isShowList, setShowList] = React.useState<boolean>(false);

  const onPress = () => {
    setShowList(true);
  };
  return (
    <View style={style}>
      <ItemListPopupView
        visible={isShowList}
        title={title}
        itemList={itemList}
        selectedItem={selectedItem}
        onSelected={(item) => {
          setShowList(false);
          onSelected?.(item);
        }}
      />
      <View style={styles.containerLayout}>
        <View style={styles.titleBack}>
          <Text style={styles.titleText}>{title}</Text>
        </View>
        <TouchableOpacity
          style={styles.itemBack}
          onPress={onPress}
          disabled={disabled}
        >
          <Text style={styles.itemText}>
            {selectedItem?.name || placeHolder || 'select value'}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

ItemSelectorView.displayName = 'ItemSelectorView';

export default ItemSelectorView;
