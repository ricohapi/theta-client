import React, {useEffect, useState, useCallback} from 'react';
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
import {SafeAreaView} from 'react-native-safe-area-context';
import styles from './Styles';
import {
  listFiles,
  getThetaInfo,
  FileTypeEnum,
  FileInfo,
} from 'theta-client-react-native';

const listPhotos = async () => {
  return await listFiles(FileTypeEnum.IMAGE, 0, 1000);
};

const ListPhotos = ({navigation}) => {
  const [refreshing, setRefreshing] = useState<boolean>(false);
  const [files, setFiles] = useState<FileInfo[]>([]);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    setFiles(await listPhotos());
    setRefreshing(false);
  }, []);

  useEffect(() => {
    const init = async () => {
      const info = await getThetaInfo();
      navigation.setOptions({title: `${info.model}:${info.serialNumber}`});
      await onRefresh();
    };
    init();
  }, [onRefresh, navigation]);

  const onSelect = (item: FileInfo) => {
    navigation.navigate('sphere', {item: item});
  };

  const items = files.map(item => (
    <TouchableOpacity
      style={styles.fileItemBase}
      key={item.name}
      onPress={() => onSelect(item)}>
      <Image style={styles.thumbnail} source={{uri: item.thumbnailUrl}} />
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
    <SafeAreaView style={styles.container} edges={['left', 'right', 'bottom']}>
      <StatusBar barStyle="light-content" />
      <ScrollView
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }>
        {items}
      </ScrollView>
    </SafeAreaView>
  );
};

export default ListPhotos;
