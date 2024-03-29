import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  safeAreaContainer: {
    flex: 1,
    backgroundColor: 'white',
  },
  bottomViewContainer: {
    height: 110,
    alignItems: 'center',
  },
  bottomViewContainerLayout: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
  button: {
    width: 150,
    paddingHorizontal: 10,
  },
  messageText: {
    flex: 1,
    marginLeft: 4,
    color: 'black',
  },
  messageArea: {
    flex: 1,
    width: '90%',
    height: '100%',
    borderColor: 'gray',
    borderWidth: 1,
    margin: 10,
  },
});

export default styles;
