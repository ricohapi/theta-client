import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  safeAreaContainer: {
    flex: 1,
    backgroundColor: 'white',
  },
  topViewContainer: {
    height: 70,
    alignItems: 'center',
  },
  contentContainer: {
    width: '100%',
    height: '85%',
  },
  bottomViewContainer: {
    flex: 1,
    alignItems: 'center',
  },
  rowContainerLayout: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  colContainerLayout: {
    flex: 1,
    flexDirection: 'column',
  },
  button: {
    width: 150,
    paddingHorizontal: 10,
  },
  thumbnail: {
    width: 100,
    height: 50,
  },
  fileItemBase: {
    flexDirection: 'row',
    width: '100%',
    marginTop: 4,
  },
  messageText: {
    flex: 1,
    marginLeft: 4,
    color: 'black',
  },
  largeSpacer: {
    flex: 99,
  },
  messageArea: {
    flex: 1,
    width: '90%',
    height: '100%',
    borderColor: 'gray',
    borderWidth: 1,
    margin: 10,
  },
  bottomViewContainerLayout: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
  },
  labelText: {
    color: 'black',
    fontSize: 16,
    paddingRight: 10,
  },
  itemText: {
    color: 'black',
    fontSize: 16,
    paddingHorizontal: 10,
    paddingVertical: 2,
  },
});

export default styles;
