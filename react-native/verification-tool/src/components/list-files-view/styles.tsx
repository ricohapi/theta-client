import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  itemText: {
    color: 'black',
    fontSize: 16,
    paddingHorizontal: 10,
    paddingVertical: 2,
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
  listItemBaseSelected: {
    width: '100%',
    backgroundColor: 'yellow',
  },
});

export default styles;
