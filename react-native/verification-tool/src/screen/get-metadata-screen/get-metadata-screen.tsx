import React, { useState } from 'react';
import { StatusBar, Text, View, ScrollView, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import { FileTypeEnum, FileInfo, getMetadata } from 'theta-client-react-native';
import Button from '../../components/ui/button';
import { ListFilesView } from '../../components/list-files-view';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

const GetMetadataScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'getMetadata'>
> = ({ navigation }) => {
  const [selectedFile, setSelectedFile] = useState<FileInfo>();
  const [message, setMessage] = React.useState('');

  React.useEffect(() => {
    navigation.setOptions({ title: 'getMetadata' });
  }, [navigation]);

  const onPress = () => {
    if (selectedFile == null) {
      return;
    }
    execGetMetadata(selectedFile.fileUrl, selectedFile.name);
  };

  const execGetMetadata = async (fileUrl: string, name: string) => {
    try {
      const result = await getMetadata(fileUrl);
      const jsonString = JSON.stringify(
        JSON.parse(JSON.stringify(result)),
        null,
        2
      );
      setMessage(`getMetadata\nname: ${name}\nmetadata:\n${jsonString}`);
    } catch (error) {
      console.log('getMetadata error: ' + JSON.stringify(error));
      setMessage('getMetadata error\n' + JSON.stringify(error));
    }
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

      <View style={styles.buttonViewContainerLayout}>
        <Button
          style={styles.button}
          title="Get metadata"
          disabled={selectedFile == null}
          onPress={onPress}
        />
      </View>

      <ListFilesView
        onSelected={(files) => {
          const fileInfo = files.length > 0 ? files[0] : undefined;
          setSelectedFile(fileInfo);
          fileInfo && setMessage('select: ' + fileInfo?.fileUrl);
        }}
        fileType={FileTypeEnum.ALL}
        onError={() => {
          Alert.alert('listFiles', 'get error', [
            { text: 'OK', onPress: () => navigation.goBack() },
          ]);
        }}
      />
    </SafeAreaView>
  );
};

export default GetMetadataScreen;
