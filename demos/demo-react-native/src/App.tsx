import React, { useState, useMemo, useCallback, useRef, useEffect } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  StatusBar,
  TouchableOpacity,
} from 'react-native';
import Svg, { Path } from 'react-native-svg';
import styles from './Styles';
import MainMenu from './MainMenu';
import TakePhoto from './TakePhoto';
import ListPhotos from './ListPhotos';
import PhotoSphere from './PhotoSphere';

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

type ScreenName = 'main' | 'take' | 'list' | 'sphere';

const screenTitles: Record<ScreenName, string> = {
  main: 'Theta SDK sample app',
  take: 'Take Photo',
  list: 'List Photos',
  sphere: 'Sphere',
};

const App = () => {
  const [currentScreen, setCurrentScreen] = useState<ScreenName>('main');
  const [screenHistory, setScreenHistory] = useState<ScreenName[]>([]);
  const [screenParams, setScreenParams] = useState<any>(null);

  // Use ref to track currentScreen without causing useCallback dependencies to change
  const currentScreenRef = useRef(currentScreen);
  useEffect(() => {
    currentScreenRef.current = currentScreen;
  }, [currentScreen]);

  const navigate = useCallback((screen: string, params?: any) => {
    setScreenHistory(prev => [...prev, currentScreenRef.current]);
    setCurrentScreen(screen as ScreenName);
    setScreenParams(params || null);
  }, []);

  const goBack = useCallback(() => {
    setScreenHistory(prev => {
      if (prev.length > 0) {
        const previousScreen = prev[prev.length - 1];
        setCurrentScreen(previousScreen);
        setScreenParams(null);
        return prev.slice(0, -1);
      }
      return prev;
    });
  }, []);

  const setOptions = useCallback((_options: { title: string }) => {
    // Placeholder for compatibility
  }, []);

  const navigation = useMemo(() => ({
    navigate,
    goBack,
    setOptions,
  }), [navigate, goBack, setOptions]);

  const renderScreen = () => {
    switch (currentScreen) {
      case 'main':
        return <MainMenu navigation={navigation} />;
      case 'take':
        return <TakePhoto navigation={navigation} />;
      case 'list':
        return <ListPhotos navigation={navigation} />;
      case 'sphere':
        return <PhotoSphere navigation={navigation} fileUrl={screenParams?.fileUrl} />;
      default:
        return <MainMenu navigation={navigation} />;
    }
  };

  return (
    <SafeAreaView style={styles.appContainer}>
      <StatusBar barStyle="light-content" backgroundColor="#6200ee" />
      <View style={styles.appHeader}>
        {screenHistory.length > 0 && (
          <TouchableOpacity onPress={goBack} style={styles.appBackButton}>
            <BackIcon />
          </TouchableOpacity>
        )}
        <Text style={styles.appHeaderTitle}>
          {screenTitles[currentScreen]}
        </Text>
        <View style={styles.appHeaderRight} />
      </View>
      <View style={styles.appContent}>{renderScreen()}</View>
    </SafeAreaView>
  );
};

export default App;
