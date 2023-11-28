import React from 'react';
import { View, Image } from 'react-native';
import styles from './styles';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

const FilePreviewScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'filePreview'>
> = ({ route, navigation }) => {
  React.useEffect(() => {
    navigation.setOptions({ title: route.params.item.name });
  }, [navigation, route.params.item.name]);

  return (
    <View style={styles.takePhotoBack}>
      <Image
        style={styles.takePhoto}
        source={{ uri: route.params.item.fileUrl }}
      />
    </View>
  );
};

export default FilePreviewScreen;
