import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import {
  NativeStackNavigationOptions,
  createNativeStackNavigator,
} from '@react-navigation/native-stack';
import MenuScreen from './screen/menu-screen';
import VideoConvertScreen from './screen/video-convert-screen';
import LivePreviewScreen from './screen/live-preview-screen';
import OptionsScreen from './screen/options-screen/options-screen';
import CommandsScreen from './screen/commands-screen';
import PhotoCaptureScreen from './screen/photo-capture-screen';
import FilePreviewScreen from './screen/file-preview-screen';
import ListFilesScreen from './screen/list-files-screen';
import VideoCaptureScreen from './screen/video-capture-screen/video-capture-screen';
import DeleteFilesScreen from './screen/delete-files-screen/delete-files-screen';
import GetMetadataScreen from './screen/get-metadata-screen/get-metadata-screen';
import GetInfoScreen from './screen/get-info-screen/get-info-screen';
import TimeShiftCaptureScreen from './screen/time-shift-capture-screen/time-shift-capture-screen';
import TimeShiftManualCaptureScreen from './screen/time-shift-manual-capture-screen/time-shift-manual-capture-screen';
import LimitlessIntervalCaptureScreen from './screen/limitless-interval-capture-screen/limitless-interval-capture-screen';
import ShotCountSpecifiedIntervalCaptureScreen from './screen/shot-count-specified-interval-capture-screen/shot-count-specified-interval-capture-screen';
import CompositeIntervalCaptureScreen from './screen/composite-interval-capture-screen/composite-interval-capture-screen';
import BurstCaptureScreen from './screen/burst-capture-screen/burst-capture-screen';
import ContinuousCaptureScreen from './screen/continuous-capture-screen/continuous-capture-screen';
import MultiBracketCaptureScreen from './screen/multi-bracket-capture-screen/multi-bracket-capture-screen';
import { setApiLogListener, type FileInfo } from './modules/theta-client';
import { Button } from 'react-native';
import LogPopupView from './components/log-popup-view/log-popup-view';

export type RootStackParamList = {
  menu: undefined;
  getInfo: undefined;
  listFiles: undefined;
  deleteFiles: undefined;
  getMetadata: undefined;
  livePreview: undefined;
  videoConvert: undefined;
  options: undefined;
  commands: undefined;
  photoCapture: undefined;
  videoCapture: undefined;
  limitlessIntervalCapture: undefined;
  timeShiftCapture: undefined;
  timeShiftManualCapture: undefined;
  shotCountSpecifiedIntervalCapture: undefined;
  compositeIntervalCapture: undefined;
  burstCapture: undefined;
  continuousCapture: undefined;
  multiBracketCapture: undefined;
  filePreview: {
    item: FileInfo;
  };
};

const Stack = createNativeStackNavigator<RootStackParamList>();

const screenOptions = {
  headerStyle: {
    backgroundColor: '#6200ee',
  },
  headerTintColor: '#fff',
  headerTitleStyle: {
    fontWeight: 'bold',
  },
  headerBackTitle: '',
} as NativeStackNavigationOptions;

const App = () => {
  const [log, setLog] = React.useState<string>('');
  const [isShowLog, setShowLog] = React.useState<boolean>(false);
  const headerRight = () => (
    <>
      <Button onPress={() => setShowLog(true)} title="Api log" />
      <LogPopupView
        visible={isShowLog}
        log={log}
        onClose={() => setShowLog(false)}
        onClear={() => setLog('')}
      />
    </>
  );

  const getDateTime = () => {
    const dt = new Date();
    const y = dt.getFullYear();
    const mm = ('00' + (dt.getMonth() + 1)).slice(-2);
    const dd = ('00' + dt.getDate()).slice(-2);

    const HH = ('00' + dt.getHours()).slice(-2);
    const MM = ('00' + dt.getMinutes()).slice(-2);
    const SS = ('00' + dt.getSeconds()).slice(-2);
    const ms = ('000' + dt.getMilliseconds()).slice(-3);

    const result = `${y}-${mm}-${dd} ${HH}:${MM}:${SS}.${ms}`;
    return result;
  };

  const addLog = (message: string) => {
    const newMessage = getDateTime() + '\n' + message;
    setLog((prevItem) => {
      return prevItem + '\n\n' + newMessage;
    });
  };

  React.useEffect(() => {
    setApiLogListener((message) => {
      addLog(message);
    });
    return () => {
      setApiLogListener();
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={screenOptions}>
        <Stack.Screen
          options={{ title: 'Menu', headerRight }}
          name="menu"
          component={MenuScreen}
        />
        <Stack.Screen
          options={{ title: 'Get Info', headerRight }}
          name="getInfo"
          component={GetInfoScreen}
        />
        <Stack.Screen
          options={{ title: 'listFiles', headerRight }}
          name="listFiles"
          component={ListFilesScreen}
        />
        <Stack.Screen
          options={{ title: 'DeleteFiles', headerRight }}
          name="deleteFiles"
          component={DeleteFilesScreen}
        />
        <Stack.Screen
          options={{ title: 'getMetadata', headerRight }}
          name="getMetadata"
          component={GetMetadataScreen}
        />
        <Stack.Screen
          options={{ title: 'Live preview', headerRight }}
          name="livePreview"
          component={LivePreviewScreen}
        />
        <Stack.Screen
          options={{ title: 'VideoConvert', headerRight }}
          name="videoConvert"
          component={VideoConvertScreen}
        />
        <Stack.Screen
          options={{ title: 'Options', headerRight }}
          name="options"
          component={OptionsScreen}
        />
        <Stack.Screen
          options={{ title: 'Commands', headerRight }}
          name="commands"
          component={CommandsScreen}
        />
        <Stack.Screen
          options={{ title: 'Photo Capture', headerRight }}
          name="photoCapture"
          component={PhotoCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'Video Capture', headerRight }}
          name="videoCapture"
          component={VideoCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'Limitless Interval Capture', headerRight }}
          name="limitlessIntervalCapture"
          component={LimitlessIntervalCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'TimeShift Capture', headerRight }}
          name="timeShiftCapture"
          component={TimeShiftCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'TimeShift Manual Capture', headerRight }}
          name="timeShiftManualCapture"
          component={TimeShiftManualCaptureScreen}
        />
        <Stack.Screen
          options={{
            title: 'interval shooting with the shot count specified',
            headerRight,
          }}
          name="shotCountSpecifiedIntervalCapture"
          component={ShotCountSpecifiedIntervalCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'interval composite shooting', headerRight }}
          name="compositeIntervalCapture"
          component={CompositeIntervalCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'burst shooting', headerRight }}
          name="burstCapture"
          component={BurstCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'MultiBracket Capture', headerRight }}
          name="multiBracketCapture"
          component={MultiBracketCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'continuous shooting', headerRight }}
          name="continuousCapture"
          component={ContinuousCaptureScreen}
        />
        <Stack.Screen
          options={{ title: 'Preview', headerRight }}
          name="filePreview"
          component={FilePreviewScreen}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;
