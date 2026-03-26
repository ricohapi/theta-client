import React, { useEffect, useState, useCallback } from 'react';
import {
  StatusBar,
  Text,
  View,
  Image,
  RefreshControl,
  ScrollView,
  TouchableOpacity,
  Dimensions,
} from 'react-native';
import styles from './Styles';
import {
  listFiles,
  getThetaInfo,
  FileTypeEnum,
  FileInfo,
} from './modules/theta-client';
import { Navigation } from './types';

type ListPhotosProps = {
  navigation: Navigation;
};

const listPhotos = async () => {
  const { fileList } = await listFiles(FileTypeEnum.IMAGE, 0, 1000);
  return fileList;
};

const ListPhotos = ({ navigation }: ListPhotosProps) => {
  const [refreshing, setRefreshing] = useState<boolean>(false);
  const [files, setFiles] = useState<FileInfo[]>([]);

  const onRefresh = useCallback(async () => {
    try {
      setRefreshing(true);
      const fetchedFiles = await listPhotos();
      setFiles(fetchedFiles);
      setRefreshing(false);
    } catch (error) {
      setRefreshing(false);
    }
  }, []);

  useEffect(() => {
    const init = async () => {
      try {
        const info = await getThetaInfo();
        navigation.setOptions({ title: `${info.model}:${info.serialNumber}` });
        await onRefresh();
      } catch (error) {
        try {
          await onRefresh();
        } catch (refreshError) {
          // Error refreshing
        }
      }
    };
    init();
  }, [onRefresh, navigation]);

  const onSelect = (item: FileInfo) => {
    navigation.navigate('sphere', { fileUrl: item.fileUrl });
  };

  const items = files.map(item => (
    <TouchableOpacity
      style={styles.fileItemBase}
      key={item.name}
      onPress={() => onSelect(item)}>
      <Image style={styles.thumbnail} source={{ uri: item.thumbnailUrl }} />
      <View
        style={{
          width: Dimensions.get('window').width - 108,
        }}>
        <View style={styles.largeSpacer} />
        <Text style={styles.fileName}>{item.name}</Text>
        <View style={styles.largeSpacer} />
      </View>
    </TouchableOpacity>
  ));

  return (
    <View style={styles.container}>
      <StatusBar barStyle="light-content" />
      <ScrollView
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }>
        {items}
      </ScrollView>
    </View>
  );
};

export default ListPhotos;
