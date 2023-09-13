import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  safeAreaContainer: {
    flex: 1,
    backgroundColor: 'white',
  },
  buttonViewContainerLayout: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 70,
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
    width: '90%',
    height: 200,
    borderColor: 'gray',
    borderWidth: 1,
    margin: 10,
  },
  listFilesContainerLayout: {
    flex: 1,
  },
  entryText: {
    marginLeft: 4,
    marginBottom: 5,
    color: 'black',
  },
});

export default styles;
