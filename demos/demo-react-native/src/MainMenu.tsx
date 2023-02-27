import React from 'react';
import {StatusBar, Text, View, TouchableOpacity} from 'react-native';
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
  React.useEffect(() => {
    async function init() {
      await initialize();
    }
    init();
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
