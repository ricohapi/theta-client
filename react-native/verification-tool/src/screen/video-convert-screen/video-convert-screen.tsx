import React, { useEffect, useState } from 'react';
import { StatusBar, Text, View, ScrollView, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import {
  getThetaInfo,
  FileTypeEnum,
  FileInfo,
  convertVideoFormats,
  cancelVideoConvert,
} from '../../modules/theta-client';
import Button from '../../components/ui/button';
import { TitledSwitch } from '../../components/ui/titled-switch';
import { ListFilesView } from '../../components/list-files-view';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

const VideoConvertScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'videoConvert'>
> = ({ navigation }) => {
  const [converting, setConverting] = useState<boolean>(false);
  const [selectedFile, setSelectedFile] = useState<FileInfo>();
  const [message, setMessage] = React.useState('');

  const [toLowResolution, setToLowResolution] = useState<boolean>(false);
  const [applyTopBottomCorrection, setApplyTopBottomCorrection] =
    useState<boolean>(false);

  useEffect(() => {
    const init = async () => {
      try {
        const info = await getThetaInfo();
        navigation.setOptions({ title: `${info.model}:${info.serialNumber}` });
      } catch (_error) {}
    };
    init();
  }, [navigation]);

  const convertStart = () => {
    const fileUrl = selectedFile?.fileUrl;
    if (!fileUrl) {
      return;
    }
    setMessage('start convert.');
    convertVideo(fileUrl);
  };

  const convertVideo = async (fileUrl: string) => {
    setConverting(true);
    try {
      const result = await convertVideoFormats(
        fileUrl,
        toLowResolution,
        applyTopBottomCorrection,
        (completion) => {
          setMessage(`onProgress: ${completion}`);
        }
      );
      setMessage('convertVideoFormats\nEnd convert.\nfileUrl:\n' + result);
    } catch (error) {
      console.log('convertVideoFormats error: ' + JSON.stringify(error));
      setMessage('convertVideoFormats error\n' + JSON.stringify(error));
    } finally {
      setConverting(false);
    }
  };

  const convertCancel = async () => {
    try {
      await cancelVideoConvert();
    } catch (error) {
      console.log('convertVideoFormats error: ' + JSON.stringify(error));
      setMessage('cancelVideoConvert error\n' + JSON.stringify(error));
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
      <TitledSwitch
        title="toLowResolution"
        value={toLowResolution}
        onChange={(newValue) => setToLowResolution(newValue)}
      />
      <TitledSwitch
        title="applyTopBottomCorrection"
        value={applyTopBottomCorrection}
        onChange={(newValue) => setApplyTopBottomCorrection(newValue)}
      />

      <View style={styles.buttonViewContainerLayout}>
        <Button
          style={styles.button}
          title="Start"
          disabled={selectedFile == null || converting}
          onPress={convertStart}
        />
        <Button style={styles.button} title="Cancel" onPress={convertCancel} />
      </View>

      <ListFilesView
        onSelected={(files) => {
          console.log('onSelected:' + files.length);
          const fileInfo = files.length > 0 ? files[0] : undefined;
          setSelectedFile(fileInfo);
          fileInfo && setMessage('select: ' + fileInfo?.fileUrl);
        }}
        fileType={FileTypeEnum.VIDEO}
        onError={() => {
          Alert.alert('listFiles', 'get error', [
            { text: 'OK', onPress: () => navigation.goBack() },
          ]);
        }}
      />
    </SafeAreaView>
  );
};

export default VideoConvertScreen;
