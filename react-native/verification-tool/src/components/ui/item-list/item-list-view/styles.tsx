import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  itemText: {
    color: 'black',
    fontSize: 18,
    paddingHorizontal: 10,
    paddingVertical: 1,
  },
  container: {
    flex: 1,
  },
  listContentContainer: {
    flex: 1,
  },
  listItemBase: {
    width: '100%',
  },
  selectedListItemBase: {
    width: '100%',
    backgroundColor: 'lightgray',
  },
});

export default styles;
