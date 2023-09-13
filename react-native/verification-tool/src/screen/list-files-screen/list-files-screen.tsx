import React, { useEffect, useState } from 'react';
import { StatusBar, Text, View, ScrollView, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import { FileTypeEnum, StorageEnum } from 'theta-client-react-native';
import Button from '../../components/ui/button';
import { ListFilesView } from '../../components/list-files-view';
import { ItemSelectorView } from '../../components/ui/item-list';
import { InputNumber } from '../../components/ui/input-number';

interface ListFilesProps {
  fileType: FileTypeEnum;
  startPosition: number;
  entryCount: number;
  storage?: StorageEnum;
}

export const CommandEnum = {
  /** still image files. */
  IMAGE: 'IMAGE',
  /** video files. */
  VIDEO: 'VIDEO',
  /** all files. */
  ALL: 'ALL',
} as const;

/** type definition of FileTypeEnum */
export type FileTypeEnum = (typeof FileTypeEnum)[keyof typeof FileTypeEnum];

const ListFilesScreen: React.FC = ({ navigation }) => {
  const [fileType, setFileType] = useState(FileTypeEnum.ALL);
  const [startPosition, setStartPosition] = useState(0);
  const [entryCount, setEntryCount] = useState(100);
  const [storage, setStorage] = useState<StorageEnum>();
  const [message, setMessage] = React.useState('');
  const [listFilesProps, setListFilesProps] = React.useState<ListFilesProps>();
  const [refreshCounter, setRefreshCounter] = React.useState(0);

  const fileTypeList = [
    { name: 'ALL', value: FileTypeEnum.ALL },
    { name: 'IMAGE', value: FileTypeEnum.IMAGE },
    { name: 'VIDEO', value: FileTypeEnum.VIDEO },
  ];
  const storageList = [
    { name: '[undefined]', value: undefined },
    { name: 'INTERNAL', value: StorageEnum.INTERNAL },
    { name: 'SD', value: StorageEnum.SD },
    { name: 'CURRENT', value: StorageEnum.CURRENT },
  ];

  useEffect(() => {
    navigation.setOptions({ title: 'listFiles' });
  }, [navigation]);

  const showList = () => {
    setListFilesProps({
      fileType,
      startPosition,
      entryCount,
      storage,
    });
    setRefreshCounter(refreshCounter + 1);
  };

  return (
    <SafeAreaView
      style={styles.safeAreaContainer}
      edges={['left', 'right', 'bottom']}
    >
      <StatusBar barStyle="light-content" />
      <View>
        <ScrollView style={styles.messageArea}>
          <Text style={styles.messageText}>{message}</Text>
        </ScrollView>
      </View>
      <ItemSelectorView
        itemList={fileTypeList}
        title={'fileType'}
        onSelected={(item) => {
          setFileType(item.value);
        }}
        selectedItem={fileTypeList.find((item) => item.value === fileType)}
      />
      <InputNumber
        title={'startPosition'}
        onChange={(newValue) => setStartPosition(newValue ?? 0)}
        value={startPosition}
      />
      <InputNumber
        title={'entryCount'}
        onChange={(newValue) => {
          if (newValue != null) {
            setEntryCount(newValue);
          }
        }}
        value={entryCount}
      />
      <ItemSelectorView
        itemList={storageList}
        title={'storage'}
        onSelected={(item) => {
          setStorage(item.value);
        }}
        selectedItem={storageList.find((item) => item.value === storage)}
      />

      <View style={styles.buttonViewContainerLayout}>
        <Button style={styles.button} title="SHOW LIST" onPress={showList} />
      </View>

      {listFilesProps && (
        <ListFilesView
          startPosition={listFilesProps.startPosition}
          entryCount={listFilesProps.entryCount}
          fileType={listFilesProps.fileType}
          storage={listFilesProps.storage}
          onSelected={(files) => {
            const fileInfo = files.length > 0 ? files[0] : undefined;
            console.log('onSelected:' + fileInfo?.fileUrl);
            if (fileInfo != null) {
              const jsonString = JSON.stringify(
                JSON.parse(JSON.stringify(fileInfo)),
                null,
                2
              );
              setMessage('select:\n' + jsonString);
            }
          }}
          onError={(error) => {
            Alert.alert('listFiles', 'get error\n' + JSON.stringify(error), [
              {
                text: 'OK',
                onPress: () => {
                  setListFilesProps(undefined);
                },
              },
            ]);
          }}
          refreshCounter={refreshCounter}
          onRefreshed={(thetaFiles) => {
            if (thetaFiles != null) {
              const strInfo = `listFiles\n totalEntries: ${thetaFiles.totalEntries} entryCount: ${thetaFiles.fileList.length}`;
              setMessage(strInfo);
            } else {
              setMessage('');
            }
          }}
        />
      )}
    </SafeAreaView>
  );
};

export default ListFilesScreen;
