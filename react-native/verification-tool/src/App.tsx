import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, StatusBar, Platform, SafeAreaView, Button } from 'react-native';
import Svg, { Path } from 'react-native-svg';
import MenuScreen from './screen/menu-screen';
import GetInfoScreen from './screen/get-info-screen';
import PhotoCaptureScreen from './screen/photo-capture-screen';
import OptionsScreen from './screen/options-screen';
import ListFilesScreen from './screen/list-files-screen';
import GetMetadataScreen from './screen/get-metadata-screen';
import DeleteFilesScreen from './screen/delete-files-screen';
import VideoCaptureScreen from './screen/video-capture-screen';
import TimeShiftCaptureScreen from './screen/time-shift-capture-screen';
import TimeShiftManualCaptureScreen from './screen/time-shift-manual-capture-screen';
import LimitlessIntervalCaptureScreen from './screen/limitless-interval-capture-screen';
import CompositeIntervalCaptureScreen from './screen/composite-interval-capture-screen';
import ShotCountSpecifiedIntervalCaptureScreen from './screen/shot-count-specified-interval-capture-screen';
import BurstCaptureScreen from './screen/burst-capture-screen';
import MultiBracketCaptureScreen from './screen/multi-bracket-capture-screen';
import ContinuousCaptureScreen from './screen/continuous-capture-screen';
import LivePreviewScreen from './screen/live-preview-screen';
import VideoConvertScreen from './screen/video-convert-screen';
import CommandsScreen from './screen/commands-screen';
import FilePreviewScreen from './screen/file-preview-screen';
import LogPopupView from './components/log-popup-view/log-popup-view';
import { FileTypeEnum, setApiLogListener } from './modules/theta-client';

const BackIcon = () => (
  <Svg width="24" height="24" viewBox="0 0 24 24" fill="none">
    <Path
      d="M15 18L9 12L15 6"
      stroke="#fff"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </Svg>
);

type ScreenName =
  | 'menu'
  | 'getInfo'
  | 'listFiles'
  | 'deleteFiles'
  | 'getMetadata'
  | 'livePreview'
  | 'videoConvert'
  | 'options'
  | 'commands'
  | 'photoCapture'
  | 'videoCapture'
  | 'limitlessIntervalCapture'
  | 'timeShiftCapture'
  | 'timeShiftManualCapture'
  | 'shotCountSpecifiedIntervalCapture'
  | 'compositeIntervalCapture'
  | 'burstCapture'
  | 'continuousCapture'
  | 'multiBracketCapture'
  | 'filePreview';

const screenTitles: Record<ScreenName, string> = {
  menu: 'Menu',
  getInfo: 'Get Info',
  listFiles: 'List Files',
  deleteFiles: 'Delete Files',
  getMetadata: 'Get Metadata',
  livePreview: 'Live Preview',
  videoConvert: 'Video Convert',
  options: 'Options',
  commands: 'Commands',
  photoCapture: 'Photo Capture',
  videoCapture: 'Video Capture',
  limitlessIntervalCapture: 'Limitless Interval Capture',
  timeShiftCapture: 'Time Shift Capture',
  timeShiftManualCapture: 'Time Shift Manual Capture',
  shotCountSpecifiedIntervalCapture: 'Shot Count Specified Interval Capture',
  compositeIntervalCapture: 'Composite Interval Capture',
  burstCapture: 'Burst Capture',
  continuousCapture: 'Continuous Capture',
  multiBracketCapture: 'Multi Bracket Capture',
  filePreview: 'Preview',
};

const App = () => {
  const [currentScreen, setCurrentScreen] = useState<ScreenName>('menu');
  const [screenHistory, setScreenHistory] = useState<ScreenName[]>([]);
  const [menuMessage, setMenuMessage] = useState('');
  const [screenParams, setScreenParams] = useState<any>(null);
  const [screenTitle, setScreenTitle] = useState<string>('');
  const [log, setLog] = useState<string>('');
  const [isShowLog, setShowLog] = useState<boolean>(false);

  // list-files screen state
  const [listFilesState, setListFilesState] = useState({
    fileType: FileTypeEnum.ALL,
    startPosition: 0,
    entryCount: 100,
    storage: undefined as any,
    message: '',
    selectedFileInfo: null as any,
    listFilesProps: undefined as any,
    refreshCounter: 0,
  });

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

  useEffect(() => {
    setApiLogListener((message) => {
      addLog(message);
    });
    return () => {
      setApiLogListener();
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const navigate = (screen: string, params?: any) => {
    setScreenHistory([...screenHistory, currentScreen]);
    setCurrentScreen(screen as ScreenName);
    setScreenParams(params || null);
    if (params?.fileInfo) {
      setScreenTitle(params.fileInfo.name);
    } else {
      setScreenTitle('');
    }

    if (screen === 'listFiles' && params?.reset) {
      setListFilesState({
        fileType: FileTypeEnum.ALL,
        startPosition: 0,
        entryCount: 100,
        storage: undefined,
        message: '',
        selectedFileInfo: null,
        listFilesProps: undefined,
        refreshCounter: 0,
      });
    }
  };

  const goBack = () => {
    if (screenHistory.length > 0) {
      const previousScreen = screenHistory[screenHistory.length - 1];
      setScreenHistory(screenHistory.slice(0, -1));
      setCurrentScreen(previousScreen);
      setScreenParams(null);
      setScreenTitle('');
    }
  };

  const setOptions = (_options: { title: string }) => {
  };

  const navigation = {
    navigate,
    goBack,
    setOptions,
  };

  const renderScreen = () => {
    switch (currentScreen) {
      case 'menu':
        return (
          <MenuScreen
            navigation={navigation}
            message={menuMessage}
            onChangeMessage={setMenuMessage}
          />
        );
      case 'getInfo':
        return <GetInfoScreen />;
      case 'photoCapture':
        return <PhotoCaptureScreen navigation={navigation} />;
      case 'options':
        return <OptionsScreen navigation={navigation} />;
      case 'listFiles':
        return (
          <ListFilesScreen
            navigation={navigation}
            state={listFilesState}
            setState={setListFilesState}
          />
        );
      case 'getMetadata':
        return <GetMetadataScreen navigation={navigation} />;
      case 'deleteFiles':
        return <DeleteFilesScreen navigation={navigation} />;
      case 'videoCapture':
        return <VideoCaptureScreen navigation={navigation} />;
      case 'timeShiftCapture':
        return <TimeShiftCaptureScreen navigation={navigation} />;
      case 'timeShiftManualCapture':
        return <TimeShiftManualCaptureScreen navigation={navigation} />;
      case 'limitlessIntervalCapture':
        return <LimitlessIntervalCaptureScreen navigation={navigation} />;
      case 'compositeIntervalCapture':
        return <CompositeIntervalCaptureScreen navigation={navigation} />;
      case 'shotCountSpecifiedIntervalCapture':
        return <ShotCountSpecifiedIntervalCaptureScreen navigation={navigation} />;
      case 'burstCapture':
        return <BurstCaptureScreen navigation={navigation} />;
      case 'multiBracketCapture':
        return <MultiBracketCaptureScreen navigation={navigation} />;
      case 'continuousCapture':
        return <ContinuousCaptureScreen navigation={navigation} />;
      case 'livePreview':
        return <LivePreviewScreen navigation={navigation} />;
      case 'videoConvert':
        return <VideoConvertScreen navigation={navigation} />;
      case 'commands':
        return <CommandsScreen navigation={navigation} />;
      case 'filePreview':
        return screenParams?.fileInfo ? (
          <FilePreviewScreen fileInfo={screenParams.fileInfo} />
        ) : null;
      default:
        return (
          <View style={styles.placeholderContainer}>
            <Text style={styles.placeholderText}>
              {screenTitles[currentScreen]} Screen
            </Text>
            <Text style={styles.placeholderSubtext}>Not implemented yet</Text>
          </View>
        );
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" backgroundColor="#6200ee" />
      <View style={styles.header}>
        {screenHistory.length > 0 && (
          <TouchableOpacity onPress={goBack} style={styles.backButton}>
            <BackIcon />
          </TouchableOpacity>
        )}
        <Text style={styles.headerTitle}>
          {screenTitle || screenTitles[currentScreen]}
        </Text>
        <View style={styles.headerRight}>
          <Button onPress={() => setShowLog(true)} title="Api log" />
        </View>
      </View>
      <View style={styles.content}>{renderScreen()}</View>
      <LogPopupView
        visible={isShowLog}
        log={log}
        onClose={() => setShowLog(false)}
        onClear={() => setLog('')}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#6200ee',
    marginTop: Platform.select({
      ios: 0,
      android: (typeof Platform.Version === 'number' && Platform.Version >= 35) ? (StatusBar.currentHeight || 0) : 0,
    }),
  },
  header: {
    backgroundColor: '#6200ee',
    paddingVertical: 15,
    paddingHorizontal: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  headerRight: {
    marginLeft: 'auto',
  },
  backButton: {
    marginRight: 16,
    padding: 4,
  },
  headerTitle: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
  content: {
    flex: 1,
    backgroundColor: '#fff',
  },
  placeholderContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  placeholderText: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  placeholderSubtext: {
    fontSize: 16,
    color: '#666',
  },
});

export default App;

