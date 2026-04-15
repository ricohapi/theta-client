import React from 'react';
import { View, Image } from 'react-native';
import styles from './Styles';
import { Navigation } from './types';

type PhotoSphereProps = {
  navigation: Navigation;
  fileUrl?: string;
};

const PhotoSphere = ({ fileUrl }: PhotoSphereProps) => {
  return (
    <View style={styles.takePhotoBack}>
      {fileUrl ? (
        <Image
          style={styles.takePhoto}
          source={{ uri: fileUrl }}
        />
      ) : null}
    </View>
  );
};

export default PhotoSphere;
