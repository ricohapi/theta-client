import {StyleSheet} from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  thumbnail: {
    width: 100,
    height: 50,
  },
  spacer: {
    height: 8,
  },
  fileItemBase: {
    flexDirection: 'row',
    width: '100%',
    marginTop: 4,
  },
  fileName: {
    marginLeft: 4,
    color: 'black',
  },
  largeSpacer: {
    flex: 99,
  },
  button: {
    color: 'white',
    fontSize: 16,
  },
  buttonBack: {
    backgroundColor: '#6200ee',
    borderRadius: 10,
    padding: 10,
  },
  takePhotoBack: {
    flex: 1,
    backgroundColor: 'white',
  },
  livePreviewContainer: {
    flex: 1,
  },
  livePreviewWebview: {
    width: '100%',
    height: '100%',
  },
  livePreviewBottomContainer: {
    position: 'absolute',
    bottom: 0,
    width: '100%',
    alignItems: 'center',
  },
  takePhoto: {
    width: '100%',
    height: '100%',
    backgroundColor: 'white',
    resizeMode: 'contain',
    zIndex: 0,
    elevation: 0,
  },
  shutter: {
    alignSelf: 'center',
    width: 40,
    height: 40,
    marginBottom: 40,
    backgroundColor: '#0ff',
    borderRadius: 20,
    zIndex: 1,
    elevation: 1,
  },
});

export default styles;
