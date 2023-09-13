import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  safeAreaContainer: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center',
  },
  messageText: {
    flex: 1,
    marginLeft: 4,
    color: 'black',
  },
  messageLayout: {
    width: '100%',
    height: 100,
    alignItems: 'center',
  },
  messageArea: {
    width: '90%',
    height: 100,
    borderColor: 'gray',
    borderWidth: 1,
    margin: 10,
  },
  container: {
    width: '80%',
    height: 100,
    borderColor: 'gray',
    borderWidth: 1,
    margin: 10,
    padding: 5,
  },
  spacer: {
    height: 8,
  },
});

export default styles;
