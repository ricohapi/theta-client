import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  safeAreaContainer: {
    flex: 1,
    backgroundColor: '#000000A0',
    alignItems: 'center',
    justifyContent: 'center',
  },
  modalContainerLayout: {
    backgroundColor: 'white',
    width: '90%',
    height: '90%',
    alignItems: 'center',
  },
  listTitle: {
    color: 'black',
    fontSize: 18,
    textAlign: 'center',
  },
  listTitleBack: {
    alignItems: 'center',
  },
  messageText: {
    flex: 1,
    marginLeft: 4,
    color: 'black',
  },
  messageLayout: {
    width: '100%',
    flex: 1,
    alignItems: 'center',
  },
  messageArea: {
    width: '90%',
    borderColor: 'gray',
    borderWidth: 1,
    margin: 10,
  },
  bottomViewContainerLayout: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 10,
  },
  CloseButton: {
    width: 150,
    marginHorizontal: 10,
  },
});

export default styles;
