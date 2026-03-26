import React from 'react';
import {
  StatusBar,
  Text,
  View,
  TouchableOpacity,
  AppState,
  AppStateStatus,
  ActivityIndicator,
} from 'react-native';
import styles from './Styles';
import { initialize } from './modules/theta-client';
import { Navigation } from './types';

type MainMenuProps = {
  navigation: Navigation;
};

const MainMenu = ({ navigation }: MainMenuProps) => {
  const [isInitializing, setIsInitializing] = React.useState(true);
  const [isInitialized, setIsInitialized] = React.useState(false);

  const goTake = () => {
    navigation.navigate('take');
  };
  const goList = () => {
    navigation.navigate('list');
  };

  const initTheta = async () => {
    try {
      setIsInitializing(true);
      setIsInitialized(false);
      const endpoint = 'http://192.168.1.1';
      const config = {
        // clientMode: { // Client mode authentication settings
        //   username: 'THETAXX12345678',
        //   password: '12345678',
        // }
      };
      await initialize(endpoint, config);
      setIsInitialized(true);
      setIsInitializing(false);
    } catch (error) {
      setIsInitializing(false);
      setIsInitialized(false);
    }
  };

  const handleAppStateChange = (nextAppState: AppStateStatus) => {
    if (nextAppState === 'active') {
      initTheta();
    }
  };

  React.useEffect(() => {
    initTheta();

    const subscription = AppState.addEventListener(
      'change',
      handleAppStateChange,
    );
    return () => {
      subscription.remove();
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <View style={styles.container}>
      <StatusBar barStyle="light-content" />
      <View style={{ alignItems: 'center', marginBottom: 20 }}>
        {isInitializing ? (
          <View style={{ alignItems: 'center' }}>
            <ActivityIndicator size="large" color="#6200ee" />
            <Text style={{ marginTop: 10, color: '#666' }}>
              Connecting to THETA...
            </Text>
          </View>
        ) : isInitialized ? (
          <View style={{ alignItems: 'center' }}>
            <Text style={{ fontSize: 48, color: '#4CAF50' }}>✓</Text>
            <Text style={{ marginTop: 10, color: '#4CAF50', fontWeight: 'bold' }}>
              Connected
            </Text>
          </View>
        ) : (
          <View style={{ alignItems: 'center' }}>
            <Text style={{ fontSize: 48, color: '#F44336' }}>✗</Text>
            <Text style={{ marginTop: 10, color: '#F44336' }}>
              Connection Failed
            </Text>
          </View>
        )}
      </View>
      <TouchableOpacity
        style={[
          styles.buttonBack,
          (!isInitialized || isInitializing) && { opacity: 0.5 }
        ]}
        onPress={goTake}
        disabled={!isInitialized || isInitializing}
      >
        <Text style={styles.button}>Take a Photo</Text>
      </TouchableOpacity>
      <View style={styles.spacer} />
      <TouchableOpacity
        style={[
          styles.buttonBack,
          (!isInitialized || isInitializing) && { opacity: 0.5 }
        ]}
        onPress={goList}
        disabled={!isInitialized || isInitializing}
      >
        <Text style={styles.button}>List Photos</Text>
      </TouchableOpacity>
    </View>
  );
};

export default MainMenu;
