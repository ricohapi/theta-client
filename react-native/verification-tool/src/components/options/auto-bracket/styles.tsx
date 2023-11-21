import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  scrollViewLayout: {
    height: 300,
  },
  editorContainerLayout: {
    flexDirection: 'column',
    justifyContent: 'center',
  },
  addButtonContainerLayout: {
    flexDirection: 'column',
    alignItems: 'center',
  },
  removeButtonContainerLayout: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  button: {
    margin: 5,
  },
  itemText: {
    color: 'black',
    fontSize: 16,
    paddingHorizontal: 10,
    paddingVertical: 2,
    fontWeight: 'bold',
  },
});

export default styles;
