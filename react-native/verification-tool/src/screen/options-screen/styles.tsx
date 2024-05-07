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
  },
  contentContainer: {
    width: '100%',
  },
  bottomViewContainer: {
    flex: 1,
    alignItems: 'center',
  },
  rowContainerLayout: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  editorContainerLayout: {
    flexDirection: 'column',
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
  autoBracketEditorLayout: {
    height: 300,
  },
  inputArea: {
    width: '90%',
    height: '10%',
    minHeight: 200,
  },
});

export default styles;
