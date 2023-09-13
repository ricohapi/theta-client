import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  takePhotoBack: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: 'white',
  },
  takePhoto: {
    width: '100%',
    height: '100%',
    backgroundColor: 'white',
    resizeMode: 'contain',
    zIndex: 0,
    elevation: 0,
  },
});

export default styles;
