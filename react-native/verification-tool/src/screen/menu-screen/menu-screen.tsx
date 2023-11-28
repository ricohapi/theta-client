import React from 'react';
import { StatusBar, View, Alert, ScrollView, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { getThetaModel, initialize } from 'theta-client-react-native';
import styles from './styles';
import Button from '../../components/ui/button';
import { ItemListPopupView } from '../../components/ui/item-list/item-list-popup-view';
import type { Item } from '../../components/ui/item-list';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

interface CaptureItem extends Item {
  value: {
    selectedFunction: () => void;
  };
}

interface FunctionItem extends Item {
  value: {
    selectedFunction: () => void;
  };
}

const MenuScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'menu'>
> = ({ navigation }) => {
  const [message, setMessage] = React.useState('');
  const [isShowCaptureList, setShowCaptureList] =
    React.useState<boolean>(false);

  const captureList: CaptureItem[] = [
    {
      name: '・photo capture',
      value: {
        selectedFunction: async () => {
          navigation.navigate('photoCapture');
        },
      },
    },
    {
      name: '・video capture',
      value: {
        selectedFunction: async () => {
          navigation.navigate('videoCapture');
        },
      },
    },
    {
      name: '・time-shift capture',
      value: {
        selectedFunction: async () => {
          navigation.navigate('timeShiftCapture');
        },
      },
    },
    {
      name: '・limitless interval capture',
      value: {
        selectedFunction: async () => {
          navigation.navigate('limitlessIntervalCapture');
        },
      },
    },
    {
      name: '・interval shooting with the shot count specified',
      value: {
        selectedFunction: async () => {
          navigation.navigate('shotCountSpecifiedIntervalCapture');
        },
      },
    },
    {
      name: '・interval composite shooting',
      value: {
        selectedFunction: async () => {
          navigation.navigate('compositeIntervalCapture');
        },
      },
    },
  ];

  const functionList: FunctionItem[] = [
    {
      name: 'Get Info',
      value: {
        selectedFunction: () => navigation.navigate('getInfo'),
      },
    },
    {
      name: 'listFiles',
      value: {
        selectedFunction: () => navigation.navigate('listFiles'),
      },
    },
    {
      name: 'DeleteFiles',
      value: {
        selectedFunction: () => navigation.navigate('deleteFiles'),
      },
    },
    {
      name: 'getMetadata',
      value: {
        selectedFunction: () => navigation.navigate('getMetadata'),
      },
    },
    {
      name: 'Options',
      value: {
        selectedFunction: () => navigation.navigate('options'),
      },
    },
    {
      name: 'Live preview',
      value: {
        selectedFunction: () => navigation.navigate('livePreview'),
      },
    },
    {
      name: 'Video Convert',
      value: {
        selectedFunction: () => navigation.navigate('videoConvert'),
      },
    },
    {
      name: 'Commands',
      value: {
        selectedFunction: () => navigation.navigate('commands'),
      },
    },
    {
      name: 'Capture',
      value: {
        selectedFunction: () => setShowCaptureList(true),
      },
    },
  ];

  const functionItems = functionList.map((item) => (
    <View key={item.name}>
      <View style={styles.spacer} />
      <Button title={item.name} onPress={() => item.value.selectedFunction()} />
    </View>
  ));

  const initTheta = async () => {
    try {
      const endpoint = 'http://192.168.1.1';
      await initialize(endpoint);
      const model = await getThetaModel();
      console.log('Connected.');
      setMessage(`Connected.\nModel: ${model}`);
    } catch (error) {
      console.log('init error: ' + JSON.stringify(error));
      setMessage('Connect error.');
      Alert.alert('initialize', 'error');
    }
  };

  const onPressConnect = () => {
    initTheta();
  };

  const onSelectedCapture = (captureItem: CaptureItem) => {
    setShowCaptureList(false);
    captureItem.value.selectedFunction();
  };

  return (
    <SafeAreaView
      style={styles.safeAreaContainer}
      edges={['left', 'right', 'bottom']}
    >
      <StatusBar barStyle="light-content" />
      <ItemListPopupView
        visible={isShowCaptureList}
        title={'Capture'}
        itemList={captureList}
        onSelected={onSelectedCapture}
      />
      <View style={styles.messageLayout}>
        <ScrollView style={styles.messageArea}>
          <Text style={styles.messageText}>{message}</Text>
        </ScrollView>
      </View>
      <Button title="connect" onPress={onPressConnect} />
      <ScrollView style={styles.container}>{functionItems}</ScrollView>
    </SafeAreaView>
  );
};

export default MenuScreen;
