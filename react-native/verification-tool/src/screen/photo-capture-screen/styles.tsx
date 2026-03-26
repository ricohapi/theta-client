import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
    safeAreaContainer: {
        flex: 1,
        backgroundColor: 'white',
    },
    topViewContainer: {
        paddingBottom: 16,
        paddingHorizontal: 16,
        borderBottomWidth: 1,
        borderBottomColor: 'gray',
    },
    itemText: {
        fontSize: 14,
        color: 'black',
        marginBottom: 10,
        minHeight: 20,
    },
    bottomViewContainerLayout: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    button: {
        flex: 1,
        marginHorizontal: 4,
    },
    contentContainer: {
        flex: 1,
        padding: 16,
    },
});

export default styles;
