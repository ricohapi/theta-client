import { StyleSheet, Platform, StatusBar } from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  // App.tsx styles
  appContainer: {
    flex: 1,
    backgroundColor: '#6200ee',
    marginTop: Platform.select({
      ios: 0,
      android: StatusBar.currentHeight || 0,
    }),
  },
  appHeader: {
    backgroundColor: '#6200ee',
    paddingVertical: 15,
    paddingHorizontal: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  appHeaderRight: {
    width: 24,
  },
  appBackButton: {
    marginRight: 16,
    padding: 4,
  },
  appHeaderTitle: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
    flex: 1,
  },
  appContent: {
    flex: 1,
    backgroundColor: '#fff',
  },
  // Other styles
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
  // MainMenu styles
  statusContainer: {
    alignItems: 'center',
    marginBottom: 20,
  },
  statusCenter: {
    alignItems: 'center',
  },
  statusConnecting: {
    marginTop: 10,
    color: '#666',
  },
  statusSuccess: {
    fontSize: 48,
    color: '#4CAF50',
  },
  statusSuccessText: {
    marginTop: 10,
    color: '#4CAF50',
    fontWeight: 'bold',
  },
  statusError: {
    fontSize: 48,
    color: '#F44336',
  },
  statusErrorText: {
    marginTop: 10,
    color: '#F44336',
  },
  disabledButton: {
    opacity: 0.5,
  },
});

export default styles;
