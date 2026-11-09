import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'white',
    },
    takePhotoBack: {
        flex: 1,
        backgroundColor: 'white',
    },
    takePhoto: {
        width: '100%',
        height: '100%',
        backgroundColor: 'white',
        resizeMode: 'contain',
        zIndex: 0,
    },
    contentContainer: {
        flex: 1,
    },
    bottomViewContainer: {
        height: 100,
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
    webview: {
        width: '100%',
        height: '100%',
    },
    itemText: {
        color: 'black',
        fontSize: 16,
        paddingHorizontal: 10,
        paddingVertical: 2,
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
