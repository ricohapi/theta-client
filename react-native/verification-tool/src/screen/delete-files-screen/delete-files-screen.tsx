import React, { useEffect, useState } from 'react';
import { StatusBar, Text, View, ScrollView, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import {
  FileInfo,
  FileTypeEnum,
  ThetaFiles,
  deleteAllFiles,
  deleteAllImageFiles,
  deleteAllVideoFiles,
  deleteFiles,
} from 'theta-client-react-native';
import Button from '../../components/ui/button';
import { ListFilesView } from '../../components/list-files-view';
import { Item, ItemSelectorView } from '../../components/ui/item-list';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

interface ListFilesProps {
  fileType: FileTypeEnum;
  isMultiselect: boolean;
}

const DeleteFilesScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'deleteFiles'>
> = ({ navigation }) => {
  const [deleteType, setDeleteType] = useState<Item>();
  const [selectedFiles, setSelectedFiles] = React.useState<FileInfo[]>([]);
  const [message, setMessage] = React.useState('');
  const [listFilesProps, setListFilesProps] = React.useState<ListFilesProps>();
  const [refreshCounter, setRefreshCounter] = React.useState(0);
  const [thetaFiles, setThetaFiles] = React.useState<ThetaFiles>();

  const DeleteTypeEnum = {
    All: {
      name: 'deleteAllFiles',
      fileType: FileTypeEnum.ALL,
      isMultiselect: false,
      executeDelete: async (_selected?: FileInfo[]) => {
        await deleteAllFiles();
      },
    },
    Files: {
      name: 'deleteFiles',
      fileType: FileTypeEnum.ALL,
      isMultiselect: true,
      executeDelete: async (_selected?: FileInfo[]) => {
        if ((_selected?.length ?? 0) <= 0) {
          throw 'Not file select.';
        }
        const fileUrls = _selected?.map((element) => element.fileUrl) ?? [];
        await deleteFiles(fileUrls);
      },
    },
    AllImageFile: {
      name: 'deleteAllFiles',
      fileType: FileTypeEnum.IMAGE,
      isMultiselect: false,
      executeDelete: async (_selected?: FileInfo[]) => {
        await deleteAllImageFiles();
      },
    },
    AllVideoFiles: {
      name: 'deleteAllFiles',
      fileType: FileTypeEnum.VIDEO,
      isMultiselect: false,
      executeDelete: async (_selected?: FileInfo[]) => {
        await deleteAllVideoFiles();
      },
    },
  };
  type DeleteTypeEnum = (typeof DeleteTypeEnum)[keyof typeof DeleteTypeEnum];

  const deleteTypeList = [
    { name: 'deleteAll', value: DeleteTypeEnum.All },
    { name: 'deleteFiles', value: DeleteTypeEnum.Files },
    { name: 'deleteAllImageFile', value: DeleteTypeEnum.AllImageFile },
    { name: 'deleteAllVideoFiles', value: DeleteTypeEnum.AllVideoFiles },
  ];

  const execDelete = async (deleteItem: Item) => {
    try {
      deleteItem.value.executeDelete(selectedFiles);
      setMessage('OK.\n' + deleteItem.name);
    } catch (error) {
      setMessage('Error.\n' + JSON.stringify(error));
    } finally {
      showList();
    }
  };

  const onDeletePress = () => {
    if (deleteType == null) {
      return;
    }

    Alert.alert(deleteType.name, `Execute ${deleteType.name} ?`, [
      {
        text: 'No',
      },
      {
        text: 'Yes',
        onPress: () => {
          execDelete(deleteType);
        },
      },
    ]);
  };

  const showList = () => {
    if (deleteType == null) {
      return;
    }
    setListFilesProps({
      fileType: deleteType.value.fileType,
      isMultiselect: deleteType.value.isMultiselect,
    });
    setRefreshCounter(refreshCounter + 1);
  };

  useEffect(() => {
    showList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [deleteType]);

  useEffect(() => {
    navigation.setOptions({ title: 'DeleteFiles' });
  }, [navigation]);

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
        itemList={deleteTypeList}
        title={'delete type'}
        onSelected={(item) => {
          setDeleteType(item);
        }}
        selectedItem={deleteTypeList.find(
          (item) => item.name === deleteType?.name
        )}
      />
      <View style={styles.buttonViewContainerLayout}>
        <Button
          style={styles.button}
          title="DELETE"
          onPress={onDeletePress}
          disabled={
            deleteType == null ||
            (deleteType.value.name === 'deleteFiles' &&
              selectedFiles.length === 0)
          }
        />
      </View>

      {listFilesProps && (
        <View style={styles.listFilesContainerLayout}>
          <Text style={styles.entryText}>{`total: ${
            thetaFiles?.totalEntries
          } entryCount: ${thetaFiles?.fileList.length ?? 0}`}</Text>
          <ListFilesView
            startPosition={0}
            entryCount={100}
            fileType={listFilesProps.fileType}
            onSelected={(files) => {
              setSelectedFiles(files);
              const fileNames = files.map((element) => element.name);
              setMessage('select:\n' + JSON.stringify(fileNames));
            }}
            onRefreshed={(filesInfo) => {
              setSelectedFiles([]);
              setThetaFiles(filesInfo);
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
            multiselect={listFilesProps.isMultiselect}
          />
        </View>
      )}
    </SafeAreaView>
  );
};

export default DeleteFilesScreen;
