import React from 'react';
import {
  type ViewProps,
  type ViewStyle,
  StyleProp,
  TouchableOpacity,
  Text,
  View,
  ScrollView,
} from 'react-native';
import styles from './styles';
import type { Item } from '..';

interface Props extends Pick<ViewProps, 'testID'> {
  style?: StyleProp<ViewStyle>;
  itemList: Item[];
  selectedItem?: Item;
  onSelected?: (item: Item) => void;
}

export const ItemListView: React.FC<Props> = ({
  itemList,
  onSelected,
  selectedItem,
}) => {
  const onPressItem = (item: Item) => {
    onSelected?.(item);
  };

  const items = itemList.map((item) => (
    <TouchableOpacity
      style={
        selectedItem?.name === item.name
          ? styles.selectedListItemBase
          : styles.listItemBase
      }
      key={item.name}
      onPress={() => onPressItem(item)}
    >
      <View>
        <Text style={styles.itemText}>{item.name}</Text>
      </View>
    </TouchableOpacity>
  ));

  return (
    <View style={styles.container}>
      <ScrollView style={styles.listContentContainer}>{items}</ScrollView>
    </View>
  );
};

export default ItemListView;
