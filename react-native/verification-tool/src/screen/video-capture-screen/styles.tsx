import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  safeAreaContainer: {
    flex: 1,
    backgroundColor: 'white',
  },
  topViewContainer: {
    height: 110,
    alignItems: 'center',
  },
  contentContainer: {
    width: '100%',
    height: '85%',
  },
  button: {
    width: 150,
    paddingHorizontal: 10,
  },
  bottomViewContainerLayout: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
});

export default styles;
