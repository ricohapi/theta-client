import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
    safeAreaContainer: {
        flex: 1,
        backgroundColor: 'white',
        alignItems: 'center',
    },
    messageArea: {
        width: '90%',
        maxHeight: 120,
        borderColor: 'gray',
        borderWidth: 1,
        margin: 10,
    },
    messageText: {
        marginLeft: 4,
        color: 'black',
    },
    container: {
        width: '80%',
        borderColor: 'gray',
        borderWidth: 1,
        margin: 10,
        padding: 5,
    },
    spacer: {
        height: 8,
    },
    loadingOverlay: {
        flex: 1,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        justifyContent: 'center',
        alignItems: 'center',
    },
    loadingBox: {
        width: '80%',
        backgroundColor: 'white',
        borderRadius: 10,
        padding: 20,
        alignItems: 'center',
    },
    loadingText: {
        marginTop: 12,
        color: 'black',
        textAlign: 'center',
    },
});

export default styles;
