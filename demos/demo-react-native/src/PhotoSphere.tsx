import React from 'react';
import {View, Image} from 'react-native';
import styles from './Styles';

const PhotoSphere = ({route, navigation}) => {
  React.useEffect(() => {
    navigation.setOptions({title: route.params.item.name});
  }, [navigation, route.params.item.name]);

  return (
    <View style={styles.takePhotoBack}>
      <Image
        style={styles.takePhoto}
        source={{uri: route.params.item.fileUrl}}
      />
    </View>
  );
};

export default PhotoSphere;
