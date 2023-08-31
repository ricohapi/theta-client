import React from 'react';
import {
  StatusBar,
  Text,
  View,
  TouchableOpacity,
  AppState,
  AppStateStatus,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import styles from './Styles';
import {initialize} from 'theta-client-react-native';

const MainMenu = ({navigation}) => {
  const goTake = () => {
    navigation.navigate('take');
  };
  const goList = () => {
    navigation.navigate('list');
  };

  const initTheta = async () => {
    const endpoint = 'http://192.168.1.1';
    const config = {
      // clientMode: { // Client mode authentication settings
      //   username: 'THETAXX12345678',
      //   password: '12345678',
      // }
    };
    await initialize(endpoint, config);
    console.log('initialized.');
  };

  const handleAppStateChange = (nextAppState: AppStateStatus) => {
    if (nextAppState === 'active') {
      initTheta();
    }
  };

  React.useEffect(() => {
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
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" />
      <TouchableOpacity style={styles.buttonBack} onPress={goTake}>
        <Text style={styles.button}>Take a Photo</Text>
      </TouchableOpacity>
      <View style={styles.spacer} />
      <TouchableOpacity style={styles.buttonBack} onPress={goList}>
        <Text style={styles.button}>List Photos</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
};

export default MainMenu;
