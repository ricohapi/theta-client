import * as React from 'react';
import {
  View,
  Text,
  Modal,
  SafeAreaView,
  ModalProps,
  ScrollView,
} from 'react-native';
import styles from './styles';
import Button from '../ui/button';

interface Props extends Pick<ModalProps, 'visible'> {
  log: string;
  onClose?: () => void;
  onClear?: () => void;
}

export const LogPopupView: React.FC<Props> = ({
  visible,
  log,
  onClose,
  onClear,
}) => {
  return (
    <Modal transparent visible={visible}>
      <SafeAreaView style={styles.safeAreaContainer}>
        <View style={styles.modalContainerLayout}>
          <View style={styles.listTitleBack}>
            <Text style={styles.listTitle}>log</Text>
          </View>
          <View style={styles.messageLayout}>
            <ScrollView style={styles.messageArea}>
              <Text style={styles.messageText}>{log}</Text>
            </ScrollView>
          </View>
          <View style={styles.bottomViewContainerLayout}>
            <Button
              title="Clear"
              style={styles.CloseButton}
              onPress={onClear}
            />
            <Button
              title="Close"
              style={styles.CloseButton}
              onPress={onClose}
            />
          </View>
        </View>
      </SafeAreaView>
    </Modal>
  );
};

LogPopupView.displayName = 'LogPopupView';

export default LogPopupView;
