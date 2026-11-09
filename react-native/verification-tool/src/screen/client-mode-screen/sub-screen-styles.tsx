import { StyleSheet } from 'react-native';

// Layout of the sub screens of the "Client Mode" menu, same as the Commands screen.
const styles = StyleSheet.create({
    safeAreaContainer: {
        flex: 1,
        backgroundColor: 'white',
    },
    inputArea: {
        maxHeight: 220,
        marginHorizontal: 10,
    },
    input: {
        marginTop: 4,
    },
    bottomViewContainer: {
        height: 70,
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
        alignSelf: 'center',
        borderColor: 'gray',
        borderWidth: 1,
        margin: 10,
    },
});

export default styles;
