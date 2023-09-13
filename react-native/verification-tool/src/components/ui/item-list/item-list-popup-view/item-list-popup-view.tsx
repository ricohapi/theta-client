import * as React from 'react';
import { View, Text, Modal, SafeAreaView, ModalProps } from 'react-native';
import styles from './styles';
import ItemListView from '../item-list-view/item-list-view';
import type { Item } from '..';

interface Props extends Pick<ModalProps, 'visible'> {
  title: string;
  itemList: Item[];
  selectedItem?: Item;
  onSelected?: (item: Item) => void;
}

export const ItemListPopupView: React.FC<Props> = ({
  visible,
  title,
  itemList,
  selectedItem,
  onSelected,
}) => {
  return (
    <Modal transparent visible={visible}>
      <SafeAreaView style={styles.safeAreaContainer}>
        <View style={styles.modalContainerLayout}>
          <View style={styles.listTitleBack}>
            <Text style={styles.listTitle}>{title}</Text>
          </View>
          <ItemListView
            itemList={itemList}
            selectedItem={selectedItem}
            onSelected={(item) => {
              onSelected?.(item);
            }}
          />
        </View>
      </SafeAreaView>
    </Modal>
  );
};

ItemListPopupView.displayName = 'ItemSelectorView';

export default ItemListPopupView;
